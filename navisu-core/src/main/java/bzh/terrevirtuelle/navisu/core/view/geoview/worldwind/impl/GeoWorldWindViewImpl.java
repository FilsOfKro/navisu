package bzh.terrevirtuelle.navisu.core.view.geoview.worldwind.impl;

import bzh.terrevirtuelle.navisu.core.model.geom.location.Location;
import bzh.terrevirtuelle.navisu.core.model.geom.point.Point;
import bzh.terrevirtuelle.navisu.core.view.display.jfx.impl.JFXAbstractDisplay;
import bzh.terrevirtuelle.navisu.core.view.geoview.Projection;
import bzh.terrevirtuelle.navisu.core.view.geoview.worldwind.GeoWorldWindView;
import bzh.terrevirtuelle.navisu.core.view.geoview.layer.worldwind.WorldWindLayerManager;
import bzh.terrevirtuelle.navisu.core.view.geoview.layer.worldwind.impl.WorldWindLayerManagerImpl;
import bzh.terrevirtuelle.navisu.core.utility.Checker;
import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.globes.EarthFlat;
import javafx.embed.swing.SwingNode;
import javafx.scene.Node;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Thibault Pensec <thibault.pensec at gmail.com>
 * @author Jordan Mens <jordan.mens at gmail.com>
 */
public class GeoWorldWindViewImpl extends JFXAbstractDisplay implements GeoWorldWindView {

    protected final WorldWindLayerManager layerManager;
    protected final WorldWindow wwd;

    protected EarthFlat globe;

    protected Projection currentProjection = Projection.mercator;

    protected final SwingNode swingNode;

    public GeoWorldWindViewImpl() {

        // Create the WorldWindow
        this.wwd = new WorldWindowGLJPanel();

        // Create the Globe
        this.globe = new EarthFlat();
        this.globe.setProjection(this.projectionToWorldWindProjection(this.currentProjection));

        // Create a new empty Model
        final Model model = new BasicModel(this.globe, null);
        this.wwd.setModel(model);

        // Create the LayerManager
        this.layerManager = new WorldWindLayerManagerImpl(model);

        // Create the JavaFX display node
        this.swingNode = this.createSwingDisplayNode(this.wwd);
    }

    protected SwingNode createSwingDisplayNode(WorldWindow wwd) {

        SwingNode swingNode = new SwingNode();

        if(wwd instanceof WorldWindowGLCanvas) {
            JPanel container = new JPanel(new BorderLayout());
            container.add((WorldWindowGLCanvas)wwd, BorderLayout.CENTER);
            swingNode.setContent(container);
        }
        else {
            swingNode.setContent((WorldWindowGLJPanel)wwd);
        }

        return swingNode;
    }

    @Override
    public WorldWindow getWorldWindow() {
        return this.wwd;
    }

    @Override
    public WorldWindLayerManager getLayerManager() {
        return this.layerManager;
    }

    @Override
    public Node getDisplayable() {
        return this.swingNode;
    }

    @Override
    public Location transformPixelToLatLon(Point pixel) {

        Checker.notNull(pixel, "Point is null.");

        Position pos = this.wwd.getView().computePositionFromScreenPoint(pixel.getX(), pixel.getY());

        if(pos == null) return null;

        return Location.factory.newLocation(pos.latitude.degrees, pos.longitude.degrees);
    }

    @Override
    public Point transformLatLonToPixel(Location latLon) {

        Checker.notNull(latLon, "Location is null.");

        Vec4 pt = this.wwd.getModel().getGlobe().computePointFromLocation(
                LatLon.fromDegrees(latLon.getLatitudeDegree(), latLon.getLongitudeDegree()));

        if(pt == null) return null;

        return Point.factory.newPoint((int)pt.x, (int)pt.y);
    }

    @Override
    public void setProjection(Projection projection) {

        Checker.notNull(projection, "Projection is null.");

        this.currentProjection = projection;

        String wwProjection = this.projectionToWorldWindProjection(this.currentProjection);

        this.globe.setProjection(wwProjection);
    }

    protected String projectionToWorldWindProjection(Projection projection) {
        String wwProjection;

        switch (projection) {
            case latlon:
                wwProjection = EarthFlat.PROJECTION_LAT_LON;
                break;
            case mercator:
                wwProjection = EarthFlat.PROJECTION_MERCATOR;
                break;
            case sinusoidal:
                wwProjection = EarthFlat.PROJECTION_SINUSOIDAL;
                break;

            default:
                wwProjection = EarthFlat.PROJECTION_MERCATOR;
        }
        return wwProjection;
    }

    @Override
    public Projection getProjection() {
        return this.currentProjection;
    }
}
