/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.navigation.routeeditor.impl.controller;

import bzh.terrevirtuelle.navisu.charts.vector.s57.charts.S57ChartServices;
import bzh.terrevirtuelle.navisu.charts.vector.s57.controller.S57Controller;
import bzh.terrevirtuelle.navisu.core.view.geoview.worldwind.impl.GeoWorldWindViewImpl;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.Poi;
import bzh.terrevirtuelle.navisu.domain.gpx.model.Gpx;
import bzh.terrevirtuelle.navisu.domain.gpx.model.GpxBuilder;
import bzh.terrevirtuelle.navisu.domain.gpx.model.Point;
import bzh.terrevirtuelle.navisu.domain.gpx.model.Track;
import bzh.terrevirtuelle.navisu.domain.gpx.model.TrackBuilder;
import bzh.terrevirtuelle.navisu.domain.gpx.model.TrackSegment;
import bzh.terrevirtuelle.navisu.domain.gpx.model.Waypoint;
import bzh.terrevirtuelle.navisu.domain.gpx.model.WaypointBuilder;
import bzh.terrevirtuelle.navisu.navigation.routeeditor.impl.RouteEditorImpl;
import bzh.terrevirtuelle.navisu.widgets.impl.Widget2DController;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.operation.buffer.BufferOp;
import com.vividsolutions.jts.operation.buffer.BufferParameters;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.TerrainProfileLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.util.UnitsFormat;
import gov.nasa.worldwind.util.measure.MeasureTool;
import gov.nasa.worldwind.util.measure.MeasureToolController;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * NaVisu
 *
 * @date 26 août 2015
 * @author Serge Morvan
 */
public class RouteEditorController
        extends Widget2DController {

    private final RouteEditorImpl instrument;
    private final String FXML = "routeeditor.fxml";
    private MeasureTool measureTool;
    private TerrainProfileLayer profile = new TerrainProfileLayer();
    private Gpx gpx;
    private List<Track> tracks;// 1 seul Track par Gpx/Route
    private Track track;
    private List<List<Waypoint>> waypoints;// 1 Liste de WP par TS
    private List<TrackSegment> trackSegments;
    private List<Point> boundaries;
    private List<Position> positions;
    private List<Position> pathPositions;
    private Set<S57Controller> s57Controllers;
    private Poi poi;
    private int size;
    private String routeName;
    private String author;
    private String version;
    private float speed;
    private final double MIN_DISTANCE = 0.5; // minimal distance between 2 Wp
    private final double BUFFER_DISTANCE = 0.01; //unit is decimal degrees
    private double bufferDistance;
    private Geometry buffer;
    private Polygon offset;
    private final GeodeticCalculator geoCalc;
    private final Ellipsoid reference = Ellipsoid.WGS84;//default
    private final double KM_TO_NAUTICAL = 0.53879310;
    private final double KM_TO_METER = 1000;
    private GlobalCoordinates waypointA;
    private GlobalCoordinates waypointB;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hhmmss");
    private final DateTimeFormatter kmlDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    private final DateTimeFormatter kmlTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");
    private WorldWindow wwd;
    private S57ChartServices s57ChartServices;
    @FXML
    public Group view;
    @FXML
    public ImageView quit;
    @FXML
    public Slider opacitySlider;
    @FXML
    public ComboBox unitsCombo;
    @FXML
    public ComboBox anglesCombo;
    @FXML
    public CheckBox showControlsCheck;
    @FXML
    public CheckBox showAnnotationCheck;
    @FXML
    public Button newButton;
    @FXML
    public Button pauseButton;
    @FXML
    public Button endButton;
    @FXML
    public Button openButton;
    @FXML
    public Button snapshotButton;
    @FXML
    public TextField lengthText;
    @FXML
    public TextField totalLengthText;
    @FXML
    public TextField headingText;
    @FXML
    TextField namesText;
    @FXML
    TextField nameText;
    @FXML
    TextField descText;
    @FXML
    TextField routeNameText;
    @FXML
    TextField versionText;
    @FXML
    TextField authorText;
    @FXML
    TextField speedText;
    @FXML
    TextField distPoText;
    @FXML
    TextField distOffsetText;

    public RouteEditorController(RouteEditorImpl instrument, S57ChartServices s57ChartServices,
            KeyCode keyCode, KeyCombination.Modifier keyCombination) {

        super(keyCode, keyCombination);
        this.instrument = instrument;
        this.s57ChartServices = s57ChartServices;
        wwd = GeoWorldWindViewImpl.getWW();
        geoCalc = new GeodeticCalculator();
        bufferDistance = BUFFER_DISTANCE;

        /*
         // Add terrain profile layer
         profile.setEventSource(GeoWorldWindViewImpl.getWW());
         profile.setFollow(TerrainProfileLayer.FOLLOW_PATH);
         profile.setShowProfileLine(false);
         */
        load(FXML);
        speed = Float.parseFloat(speedText.getText());
        quit.setOnMouseClicked((MouseEvent event) -> {
            this.instrument.off();
            measureTool.clear();
            measureTool.setArmed(false);
            if (offset != null) {
                measureTool.getLayer().removeRenderable(offset);
            }
        });

        initMeasureTool();
        newButton.setOnMouseClicked((MouseEvent event) -> {
            newAction();
        });
        openButton.setOnMouseClicked((MouseEvent event) -> {
            initMeasureTool();
            newAction();
            Label fileLabel = new Label();
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter
                    = new FileChooser.ExtensionFilter("GPX files (*.gpx)", "*.gpx", "*.GPX");
            fileChooser.setInitialDirectory(new File("data/gpx"));
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(instrument.getGuiAgentServices().getStage());
            FileInputStream inputFile = null;
            if (file != null) {
                fileLabel.setText(file.getPath());
                gpx = null;
                try {
                    inputFile = new FileInputStream(new File(file.getPath()));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(RouteEditorController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Unmarshaller unmarshaller;
            JAXBContext jAXBContext;
            try {
                jAXBContext = JAXBContext.newInstance(Gpx.class);
                unmarshaller = jAXBContext.createUnmarshaller();
                gpx = (Gpx) unmarshaller.unmarshal(inputFile);
            } catch (JAXBException ex) {
                Logger.getLogger(RouteEditorController.class.getName()).log(Level.SEVERE, null, ex);
            }
            measureTool.setArmed(!measureTool.isArmed());
            fillMesureTool();
        });
        pauseButton.setOnMouseClicked((MouseEvent event) -> {
            measureTool.setArmed(!measureTool.isArmed());
            Platform.runLater(() -> {
                pauseButton.setText(!measureTool.isArmed() ? "Resume" : "Pause");
                pauseButton.disarm();
            });
            ((Component) wwd).setCursor(!measureTool.isArmed() ? Cursor.getDefaultCursor()
                    : Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        });
        endButton.setOnMouseClicked((MouseEvent event) -> {
            filter();
            measureTool.setArmed(false);
            bufferFromJTS();
            showBuffer();
            fillGpx();
            fillGpxBoundaries();
            exportGpx();
            exportKML();
            exportNmea();
            exportS57Controllers();
        });

        snapshotButton.setOnMouseClicked((MouseEvent event) -> {
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Not yet implemented");
                    //  new ScreenShotAction(GeoWorldWindViewImpl.getWW(), instrument.getGuiAgentServices().getStage());
                }
            });
        });

        routeNameText.textProperty().addListener((ov, oldvalue, newvalue) -> {
            if (!"".equals(newvalue)) {
                routeName = routeNameText.getText();
            }
        });
        versionText.textProperty().addListener((ov, oldvalue, newvalue) -> {
            if (!"".equals(newvalue)) {
                version = versionText.getText();
            }
        });
        authorText.textProperty().addListener((ov, oldvalue, newvalue) -> {
            if (!"".equals(newvalue)) {
                author = authorText.getText();
            }
        });
        nameText.textProperty().addListener((ov, oldvalue, newvalue) -> {
            if (!"".equals(newvalue)) {
                //  track.getTrkseg().get(track.getTrkseg().size() - 1).getTrkpt().get(i)
            }
        });
        descText.textProperty().addListener((ov, oldvalue, newvalue) -> {
            if (!"".equals(newvalue)) {
                //  route.getRtept().get(route.getRtept().size() - 1).setDesc(newvalue);
            }
        });
        speedText.textProperty().addListener((ov, oldvalue, newvalue) -> {
            if (!"".equals(newvalue)) {
                speed = Float.parseFloat(newvalue);
            }
        });
        distOffsetText.textProperty().addListener((ov, oldvalue, newvalue) -> {
            if (!"".equals(newvalue)) {
                bufferDistance = Float.parseFloat(newvalue) / 60.0;
            }
        });
        unitsCombo.setOnAction((event) -> {
            String item = (String) unitsCombo.getSelectionModel().getSelectedItem();
            if (item.trim().equals("M")) {
                measureTool.getUnitsFormat().setLengthUnits(UnitsFormat.METERS);
            } else {
                if (item.trim().equals("Km")) {
                    measureTool.getUnitsFormat().setLengthUnits(UnitsFormat.KILOMETERS);
                } else {
                    if (item.trim().equals("Feet")) {
                        measureTool.getUnitsFormat().setLengthUnits(UnitsFormat.FEET);
                    } else {
                        if (item.trim().equals("Miles")) {
                            measureTool.getUnitsFormat().setLengthUnits(UnitsFormat.MILES);
                        } else {
                            if (item.trim().equals("Nm")) {
                                measureTool.getUnitsFormat().setLengthUnits(UnitsFormat.NAUTICAL_MILES);
                            } else {
                                if (item.trim().equals("Yards")) {
                                    measureTool.getUnitsFormat().setLengthUnits(UnitsFormat.YARDS);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    final void load(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        view.setOpacity(0.8);
        opacitySlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            Platform.runLater(() -> {
                view.setOpacity(opacitySlider.getValue());
            });
        });
    }

    private void initMeasureTool() {
        measureTool = new MeasureTool(wwd);
        measureTool.setController(new MeasureToolController());
        measureTool.setMeasureShapeType(MeasureTool.SHAPE_PATH);
        measureTool.setPathType(AVKey.GREAT_CIRCLE);
        measureTool.setFollowTerrain(true);
        measureTool.setShowAnnotation(true);
        measureTool.setShowControlPoints(true);
        measureTool.setLineColor(Color.RED);
        measureTool.getUnitsFormat().setLengthUnits(UnitsFormat.NAUTICAL_MILES);
        // Handle measure tool events
        measureTool.addPropertyChangeListener((PropertyChangeEvent event) -> {
            switch (event.getPropertyName()) {
                case MeasureTool.EVENT_POSITION_ADD:
                case MeasureTool.EVENT_POSITION_REPLACE:
                    Platform.runLater(() -> {
                        nameText.clear();
                        descText.clear();
                    });
                    fillPointsPanel();    // Update position list when changed
                    break;
                case MeasureTool.EVENT_ARMED:
                    if (measureTool.isArmed()) {
                        Platform.runLater(() -> {
                            newButton.disarm();
                            pauseButton.setText("Pause");
                            pauseButton.arm();
                            endButton.arm();
                            ((Component) wwd).setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                        });
                    } else {
                        Platform.runLater(() -> {
                            newButton.arm();
                            pauseButton.setText("Pause");
                            pauseButton.disarm();
                            endButton.disarm();
                            ((Component) wwd).setCursor(Cursor.getDefaultCursor());
                        });
                    }
                    break;
                case MeasureTool.EVENT_METRIC_CHANGED:
                    break;
            }
        });
    }

    private void newAction() {
        routeName = routeNameText.getText();
        version = versionText.getText();
        author = authorText.getText();
        positions = new CopyOnWriteArrayList();
        waypoints = new ArrayList();
        track = TrackBuilder.create().name(routeName).build();
        tracks = new ArrayList<>();
        tracks.add(track);
        boundaries = new ArrayList();
        gpx = GpxBuilder.create().creator(author).version(version)
                .trk(tracks)
                .build();
        trackSegments = new ArrayList<>();
        measureTool.clear();
        measureTool.setArmed(true);
    }

    private void fillPointsPanel() {
        int length = measureTool.getPositions().size();
        Position pos = measureTool.getPositions().get(length - 1);
        String las = String.format("Lat %7.4f\u00B0", pos.getLatitude().getDegrees());
        String los = String.format("Lon %7.4f\u00B0", pos.getLongitude().getDegrees());
        Platform.runLater(() -> {
            namesText.setText(las + "  " + los + "\n");
            if (length > 1) {
                totalLengthText.setText(String.format("%.2f", measureTool.getLength() * 0.000539956803));
                lengthText.setText(String.format("%.2f", (getDistanceNm(measureTool.getPositions().get(length - 2), measureTool.getPositions().get(length - 1)))));
                headingText.setText(String.format("%.1f", (getAzimuth(measureTool.getPositions().get(length - 2), measureTool.getPositions().get(length - 1)))));
            }
        });
    }

    private void filter() {
        size = measureTool.getPositions().size();
        Position ref;
        int i = 0;
        if (size > 1) {
            measureTool.getPositions().stream().forEach((p) -> {
                positions.add(p);
            });
            for (Position p : positions) {
                if (i < positions.size() - 1) {
                    ref = positions.get(i);
                    for (int j = i + 1; j < positions.size(); j++) {
                        if (getDistanceNm(ref, positions.get(j)) < MIN_DISTANCE) {
                            positions.remove(j);
                        }
                    }
                    i++;
                }
            }
        }
    }

    private void fillGpx() {
        size = positions.size();
        for (int i = 0; i < size; i++) {
            Position pos = positions.get(i);
            Waypoint wp = WaypointBuilder.create()
                    .latitude(pos.getLatitude().getDegrees())
                    .longitude(pos.getLongitude().getDegrees())
                    .name("WP" + i)
                    .speed(speed)
                    .build();
            if (i < size - 1) {
                TrackSegment ts = new TrackSegment();
                trackSegments.add(ts);
                track.getTrkseg().add(ts);
                ts.getTrkpt().add(wp);
            }
            if (i >= 1) {
                track.getTrkseg().get(i - 1).getTrkpt().add(wp);
                track.getTrkseg().get(i - 1).getTrkpt().get(0).setCourse((float) getAzimuth(measureTool.getPositions().get(i), measureTool.getPositions().get(i + 1)));
            }
        }
    }

    private void fillGpxBoundaries() {
        boundaries = gpx.getBoundaries().getBounds();
        if (boundaries != null && pathPositions != null) {
            pathPositions.stream().forEach((p) -> {
                boundaries.add(new Point(p.getLatitude().getDegrees(), p.getLongitude().getDegrees()));
            });
        }
    }

    private void bufferFromJTS() {

        if (measureTool.isArmed() == false) {
            Coordinate[] coordinates = new Coordinate[positions.size()];
            for (int i = 0; i < positions.size(); i++) {
                coordinates[i] = new Coordinate(positions.get(i).getLongitude().getDegrees(),
                        positions.get(i).getLatitude().getDegrees());
            }
            Geometry geom = new GeometryFactory().createLineString(coordinates);
            BufferOp bufferOp = new BufferOp(geom);
            bufferOp.setEndCapStyle(BufferParameters.CAP_ROUND);
            buffer = bufferOp.getResultGeometry(bufferDistance);
            pathPositions = new ArrayList<>();
            for (Coordinate c : buffer.getCoordinates()) {
                pathPositions.add(Position.fromDegrees(c.y, c.x, 100));
            }
            if (!pathPositions.isEmpty()) {
                offset = new Polygon(pathPositions);
            }
            offset.setValue(AVKey.DISPLAY_NAME, routeName);
        }
    }

    public final void showBuffer() {
        if (offset != null) {
            ShapeAttributes normalAttributes = new BasicShapeAttributes();
            normalAttributes.setInteriorMaterial(Material.WHITE);
            normalAttributes.setInteriorOpacity(0.3);
            normalAttributes.setOutlineMaterial(Material.YELLOW);
            normalAttributes.setOutlineWidth(2);
            normalAttributes.setDrawOutline(true);
            normalAttributes.setDrawInterior(true);
            normalAttributes.setEnableLighting(true);
            offset.setAttributes(normalAttributes);

            ShapeAttributes highlightAttributes = new BasicShapeAttributes(normalAttributes);
            highlightAttributes.setOutlineMaterial(Material.WHITE);
            highlightAttributes.setOutlineOpacity(1);
            highlightAttributes.setInteriorOpacity(0.8);
            offset.setHighlightAttributes(highlightAttributes);
            measureTool.getLayer().addRenderable(offset);
        }
    }

    public Geometry getBuffer() {
        return buffer;
    }

    private void fillMesureTool() {
        positions = new CopyOnWriteArrayList();
        Position pos;
        for (TrackSegment ts : gpx.getTrk().get(0).getTrkseg()) {
            for (int i = 0; i < ts.getTrkpt().size(); i++) {
                pos = new Position(
                        Angle.fromDegrees(ts.getTrkpt().get(i).getLatitude()),
                        Angle.fromDegrees(ts.getTrkpt().get(i).getLongitude()), 0.0);
                positions.add(pos);
            }
        }

        size = positions.size();
        Position ref;
        int i = 0;
        for (Position p : positions) {
            if (i < positions.size() - 1) {
                ref = positions.get(i);
                for (int j = i + 1; j < positions.size(); j++) {
                    if (getDistanceNm(ref, positions.get(j)) < MIN_DISTANCE) {
                        positions.remove(j);
                    }
                }
                i++;
            }
        }
        ArrayList<Position> positionsList = new ArrayList<>();
        positionsList.addAll(positions);
        measureTool.setPositions(positionsList);
    }

    private void exportGpx() {
        if (gpx != null) {
            try {
                FileOutputStream outputFile;
                outputFile = new FileOutputStream(new File("data/gpx/" + routeName + ".gpx"));
                JAXBContext jAXBContext;
                Marshaller marshaller;
                jAXBContext = JAXBContext.newInstance(Gpx.class);
                marshaller = jAXBContext.createMarshaller();
                marshaller.marshal(gpx, outputFile);
            } catch (JAXBException | FileNotFoundException ex) {
                Logger.getLogger(RouteEditorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void exportNmea() {
        List<GlobalCoordinates> globalCoordinates = new ArrayList<>();
        List<String> nmeaSentences = new ArrayList<>();
        GlobalCoordinates start;
        GlobalCoordinates end;
        String we;
        String ns;
        String sentence;
        double latitude;
        double longitude;
        String strLatitude;
        String strLongitude;
        double minLatitude;
        double minLongitude;
        int degLatitude;
        int degLongitude;
        int si = positions.size() - 1;
        for (int k = 0; k < si; k++) {
            globalCoordinates.clear();
            Position startPos = positions.get(k);
            start = new GlobalCoordinates(startPos.getLatitude().getDegrees(), startPos.getLongitude().getDegrees());
            Position endPos = positions.get(k + 1);
            end = new GlobalCoordinates(endPos.getLatitude().getDegrees(), endPos.getLongitude().getDegrees());

            GeodeticCurve geoCurve = geoCalc.calculateGeodeticCurve(reference, start, end);
            double ellipseMeters = geoCurve.getEllipsoidalDistance();
            double i = 2 * ellipseMeters / speed;

            i = 1 / i;
            i *= 10000;
            for (double j = 0; j < ellipseMeters; j += i) {
                globalCoordinates.add(geoCalc.calculateEndingGlobalCoordinates(reference, start, geoCurve.getAzimuth(), j));
            }
            for (GlobalCoordinates gc : globalCoordinates) {
                latitude = gc.getLatitude();
                longitude = gc.getLongitude();
                LocalTime localTime = LocalTime.now(Clock.systemUTC());
                LocalDate localDate = LocalDate.now(Clock.systemUTC());

                we = longitude > 0 ? "E" : "W";
                ns = latitude > 0 ? "N" : "S";
                if (we.equals("W")) {
                    longitude = -longitude;
                }
                if (ns.equals("S")) {
                    latitude = -latitude;
                }
                degLatitude = (int) latitude;
                degLongitude = (int) longitude;
                minLatitude = latitude - degLatitude;
                minLongitude = longitude - degLongitude;
                minLatitude *= 60;
                minLongitude *= 60;
                strLatitude = Integer.toString(degLatitude) + String.format(Locale.US, "%.4f", minLatitude);
                strLongitude = Integer.toString(degLongitude) + String.format(Locale.US, "%.4f", minLongitude);
                sentence = "$GPRMC,"
                        + localTime.format(timeFormatter) + ","
                        + "A,"
                        + strLatitude + ","
                        + ns + ","
                        + strLongitude + ","
                        + we + ","
                        + String.format(Locale.US, "%.2f", speed) + ","
                        + String.format(Locale.US, "%.2f", geoCurve.getAzimuth()) + ","
                        + localDate.format(dateFormatter) + ",,"
                        + "*";
                nmeaSentences.add(sentence + getChecksum(sentence));
            }
        }
        Path path = Paths.get("data/nmea/" + routeName + ".nmea");
        try {
            Files.write(path, nmeaSentences, Charset.defaultCharset());
        } catch (IOException ex) {
            Logger.getLogger(RouteEditorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getChecksum(String in) {
        int checksum = 0;
        if (in.startsWith("$")) {
            in = in.substring(1, in.length());
        }

        int end = in.indexOf('*');
        if (end == -1) {
            end = in.length();
        }
        for (int i = 0; i < end; i++) {
            checksum = checksum ^ in.charAt(i);
        }
        String hex = Integer.toHexString(checksum);
        if (hex.length() == 1) {
            hex = "0" + hex;
        }
        return hex.toUpperCase();
    }

    private void exportKML() {
        if (positions != null) {
            List<String> kml = new ArrayList<>();
            String header = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                    + "<kml xmlns=\"http://earth.google.com/kml/2.0\">\n"
                    + "    <Document>";
            kml.add(header);
            String body;
            for (int i = 0; i < positions.size() - 1; i++) {
                body = "<Placemark>"
                        + "<description>"
                        + "<![CDATA["
                        + "WP" + i
                        + "<br>Lat : " + String.format("%.4f", positions.get(i).getLatitude().degrees)
                        + "<br>Lon : " + String.format("%.4f", positions.get(i).getLongitude().degrees)
                        + "<br>Time : " + LocalTime.now(Clock.systemUTC()).format(kmlTimeFormatter)
                        + "<br>Day : " + LocalDate.now(Clock.systemUTC()).format(kmlDateFormatter)
                        + "]]>"
                        + "</description>"
                        + "<Point>"
                        + "<coordinates>" + positions.get(i).getLongitude().degrees + ","
                        + positions.get(i).getLatitude().degrees + ",10" + "</coordinates>"
                        + "</Point>"
                        + "</Placemark>"
                        + "<Placemark>"
                        + "<Style>"
                        + "<LineStyle>"
                        + "<color>501400FF</color>"
                        + "<width>2</width>"
                        + "</LineStyle>"
                        + "</Style>"
                        + "<LineString>"
                        + "<coordinates>"
                        + positions.get(i).getLongitude().degrees + "," + positions.get(i).getLatitude().degrees + ",10\n"
                        + positions.get(i + 1).getLongitude().degrees + "," + positions.get(i + 1).getLatitude().degrees + ",0"
                        + "</coordinates>"
                        + "</LineString>"
                        + "</Placemark>";
                kml.add(body);
            }
            int i = positions.size() - 1;
            body = "<Placemark>"
                    + "<description>"
                    + "<![CDATA["
                    + "WP" + i
                    + "<br>Lat : " + String.format("%.4f", positions.get(i).getLatitude().degrees)
                    + "<br>Lon : " + String.format("%.4f", positions.get(i).getLongitude().degrees)
                    + "<br>Time : " + LocalTime.now(Clock.systemUTC()).format(kmlTimeFormatter)
                    + "<br>Day : " + LocalDate.now(Clock.systemUTC()).format(kmlDateFormatter)
                    + "]]>"
                    + "</description>"
                    + "<Point>"
                    + "<coordinates>"
                    + positions.get(i).getLongitude().degrees + ",\n"
                    + positions.get(i).getLatitude().degrees + ",10"
                    + "</coordinates>"
                    + "</Point>"
                    + "</Placemark>";
            kml.add(body);
            String footer = " </Document>"
                    + "</kml>";
            kml.add(footer);
            Path path = Paths.get("data/kml/" + routeName + ".kml");
            try {
                Files.write(path, kml, Charset.defaultCharset());
            } catch (IOException ex) {
                Logger.getLogger(RouteEditorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void exportS57Controllers() {
        s57Controllers = new HashSet<>();
        s57Controllers = s57ChartServices.getS57Controllers();
        if (s57Controllers != null) {
            poi = new Poi();
            Coordinate buoyagePosition;
            for (S57Controller sc : s57Controllers) {
                buoyagePosition = new Coordinate(sc.getLocation().getLon(), sc.getLocation().getLat());
                if (buffer.contains(new GeometryFactory().createPoint(buoyagePosition))) {
                    poi.add(sc.getLocation());
                }
            }
            try {
                FileOutputStream outputFile;
                outputFile = new FileOutputStream(new File("data/poi/" + routeName + ".xml"));
                JAXBContext jAXBContext;
                Marshaller marshaller;
                jAXBContext = JAXBContext.newInstance(Poi.class);
                marshaller = jAXBContext.createMarshaller();
                marshaller.marshal(poi, outputFile);
            } catch (JAXBException | FileNotFoundException ex) {
                Logger.getLogger(RouteEditorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("Il faut charger au moins une carte S57");
        }
    }

    private double getDistanceNm(Position posA, Position posB) {
        waypointA = new GlobalCoordinates(posA.getLatitude().getDegrees(), posA.getLongitude().getDegrees());
        waypointB = new GlobalCoordinates(posB.getLatitude().getDegrees(), posB.getLongitude().getDegrees());
        return geoCalc.calculateGeodeticCurve(reference, waypointA, waypointB).getEllipsoidalDistance() / KM_TO_METER * KM_TO_NAUTICAL;
    }

    private double getAzimuth(Position posA, Position posB) {
        waypointA = new GlobalCoordinates(posA.getLatitude().getDegrees(), posA.getLongitude().getDegrees());
        waypointB = new GlobalCoordinates(posB.getLatitude().getDegrees(), posB.getLongitude().getDegrees());
        return geoCalc.calculateGeodeticCurve(reference, waypointA, waypointB).getAzimuth();
    }
}
