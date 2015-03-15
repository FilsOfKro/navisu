/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.bathymetry.db.impl;

import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.app.guiagent.geoview.GeoViewServices;
import bzh.terrevirtuelle.navisu.app.guiagent.layertree.LayerTreeServices;
import bzh.terrevirtuelle.navisu.bathymetry.db.BathymetryDB;
import bzh.terrevirtuelle.navisu.bathymetry.db.BathymetryDBServices;
import bzh.terrevirtuelle.navisu.bathymetry.db.impl.controller.BathymetryDBController;
import bzh.terrevirtuelle.navisu.core.view.geoview.layer.GeoLayer;
import bzh.terrevirtuelle.navisu.core.view.geoview.worldwind.impl.GeoWorldWindViewImpl;
import bzh.terrevirtuelle.navisu.database.DatabaseServices;
import bzh.terrevirtuelle.navisu.domain.currents.model.CurrentBuilder;
import bzh.terrevirtuelle.navisu.domain.geometry.Point2D;
import bzh.terrevirtuelle.navisu.domain.geometry.Point2Df;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.capcaval.c3.component.ComponentState;
import org.capcaval.c3.component.annotation.UsedService;
import org.postgis.PGgeometry;

/**
 * @date 13 mars 2015
 * @author Serge Morvan
 */
public class BathymetryDBImpl
        implements BathymetryDB, BathymetryDBServices, ComponentState {

    protected static final Logger LOGGER = Logger.getLogger(BathymetryDBImpl.class.getName());
    @UsedService
    GuiAgentServices guiAgentServices;
    @UsedService
    GeoViewServices geoViewServices;
    @UsedService
    LayerTreeServices layerTreeServices;
    @UsedService
    DatabaseServices databaseServices;

    private String dataFileName;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private List<Point2Df> points2df;
    private List<Point2D> points2d;
    protected WorldWindow wwd;
    protected RenderableLayer layer;
    protected static final String GROUP = "Bathymetry";
    double longitude;
    double latitude;
    NumberFormat nf4 = new DecimalFormat("0.0000");
    NumberFormat nf1 = new DecimalFormat("0.0");
    int i = 0;
    BathymetryDBController bathymetryDBController;

    @Override
    public void componentInitiated() {
        points2df = new ArrayList<>();
        points2d = new ArrayList<>();
        bathymetryDBController = BathymetryDBController.getInstance();
        bathymetryDBController.setBathymetryDB(this);
        wwd = GeoWorldWindViewImpl.getWW();
        layer = new RenderableLayer();
        layer.setName("BATHY SHOM");
        geoViewServices.getLayerManager().insertGeoLayer(GeoLayer.factory.newWorldWindGeoLayer(layer));
        layerTreeServices.addGeoLayer(GROUP, GeoLayer.factory.newWorldWindGeoLayer(layer));

    }

    @Override
    public void componentStarted() {
    }

    @Override
    public void componentStopped() {
    }

    @Override
    public Connection connect(String dbName, String hostName, String protocol, String port,
            String driverName, String userName, String passwd,
            String dataFileName) {
        this.dataFileName = dataFileName;
        this.connection = databaseServices.connect(dbName, hostName, protocol, port, driverName, userName, passwd);
        bathymetryDBController.setConnection(connection);
        return connection;
    }

    @Override
    public void create() {
    }

    public final void read() {
        try {
            points2df = Files.lines(new File(dataFileName).toPath())
                    .map(line -> line.trim())
                    .map(line -> line.split(" "))
                    .map(tab -> new Point2Df(Float.parseFloat(tab[1]),
                                    Float.parseFloat(tab[0]),
                                    Float.parseFloat(tab[2])))
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public final void insert() {
        points2df.stream().forEach((p) -> {
            try {
                preparedStatement.setDouble(1, p.getLon());
                preparedStatement.setDouble(2, p.getLat());
                preparedStatement.setDouble(3, p.getElevation());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    public final void createIndex() {
        try {
            connection.createStatement().executeQuery("CREATE INDEX bathyIndex ON bathy USING GIST (coord)");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public final List<Point2D> retrieve(double lat, double lon) {
        PGgeometry geom;
        ResultSet r0;
        ResultSet r1;
        points2d.clear();
        layer.removeAllRenderables();
        try {
            r0 = connection.createStatement().executeQuery(
                    "SELECT coord,elevation "
                    + "FROM bathy "
                    + "ORDER BY coord <-> ST_SetSRID("
                    + "ST_MakePoint(" + Double.toString(lon) + ", " + Double.toString(lat) + "), 4326) "
                    + "LIMIT 100");
            while (r0.next()) {

                geom = (PGgeometry) r0.getObject(1);
                longitude = geom.getGeometry().getFirstPoint().getX();
                latitude = geom.getGeometry().getFirstPoint().getY();
                r1 = connection.createStatement().executeQuery(
                        "SELECT ST_DISTANCE("
                        + "ST_SetSRID(ST_MakePoint(" + longitude
                        + ", " + latitude + "), 4326)::geography,"
                        + "ST_SetSRID(ST_MakePoint(" + Double.toString(lon)
                        + ", " + Double.toString(lat) + "), 4326)::geography"
                        + ");");
                while (r1.next()) {
                    if ((Double) r1.getObject(1) < 900.0) {
                        points2d.add(new Point2D(latitude, longitude, r0.getDouble(2)));
                        PointPlacemark pointPlacemark = createSounding(latitude, longitude, r0.getDouble(2));
                        layer.addRenderable(pointPlacemark);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("ex " + ex);
        }
        return points2d;
    }

    public final void retrieveAll() {
        guiAgentServices.getJobsManager().newJob("retrieveAll", (progressHandle) -> {
            PGgeometry geom;
            try {
                ResultSet r = connection.createStatement().executeQuery("SELECT ST_AsText(coord) AS gid, coord, elevation FROM bathy");
                while (r.next()) {
                    geom = (PGgeometry) r.getObject(2);
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void close() {
        databaseServices.close();
    }

    public void view(List<Point2Df> points) {

    }

    public PointPlacemark createSounding(double lat, double lon, double depth) {
        PointPlacemarkAttributes attributes = new PointPlacemarkAttributes();
        attributes.setUsePointAsDefaultImage(true);

        PointPlacemark placemark = new PointPlacemark(Position.fromDegrees(lat, lon, 100));
        placemark.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
        placemark.setEnableBatchPicking(true);
        placemark.setAttributes(attributes);
        String label = "Lat : " + nf4.format(lat) + " °\n"
                + "Lon : " + nf4.format(lon) + " °\n"
                + "Depth : " + nf1.format(depth) + " m";
        placemark.setValue(AVKey.DISPLAY_NAME, label);

        return placemark;
    }

    public Connection getConnection() {
        return connection;
    }

}
