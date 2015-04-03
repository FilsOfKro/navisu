package bzh.terrevirtuelle.navisu.app.guiagent.dock.impl;

import bzh.terrevirtuelle.navisu.app.drivers.databasedriver.DatabaseDriverManagerServices;
import bzh.terrevirtuelle.navisu.app.drivers.driver.DriverManagerServices;
import bzh.terrevirtuelle.navisu.app.drivers.instrumentdriver.InstrumentDriverManagerServices;
import bzh.terrevirtuelle.navisu.app.drivers.webdriver.WebDriverManagerServices;
import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.app.guiagent.dock.DockManager;
import bzh.terrevirtuelle.navisu.app.guiagent.dock.DockManagerServices;
import bzh.terrevirtuelle.navisu.app.guiagent.tools.AnimationFactory;
import org.capcaval.c3.component.ComponentState;

import java.util.logging.Logger;

import bzh.terrevirtuelle.navisu.widgets.dock.Dock;
import bzh.terrevirtuelle.navisu.widgets.dock.DockItem;
import bzh.terrevirtuelle.navisu.widgets.dock.DockItemFactory;
import bzh.terrevirtuelle.navisu.widgets.radialmenu.menu.RadialMenu;
import bzh.terrevirtuelle.navisu.widgets.radialmenu.menu.RadialMenuBuilder;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.capcaval.c3.component.annotation.UsedService;

/*
 * NaVisu
 *
 * @date 23 mars 2015
 * @author Serge Morvan
 */
public class DockManagerImpl
        implements DockManager, DockManagerServices, ComponentState {

    @UsedService
    GuiAgentServices guiAgentServices;
    @UsedService
    DriverManagerServices driverManagerServices;
    @UsedService
    WebDriverManagerServices webDriverManagerServices;
    @UsedService
    InstrumentDriverManagerServices instrumentDriverManagerServices;
    @UsedService
    DatabaseDriverManagerServices databaseDriverManagerServices;

    protected static final Logger LOGGER = Logger.getLogger(DockManagerImpl.class.getName());
    private final String HOST_NAME = "localhost";
    private final String PORT = "5432";
    private final String DRIVER_NAME = "org.postgresql.Driver";
    private final String JDBC_PROTOCOL = "jdbc:postgresql://";
    private final String DB_NAME = "Bathy";
    private final String USER_NAME = "Serge";
    private final String PASSWD = "lithops";
    protected static final String ICON_PATH = "bzh/terrevirtuelle/navisu/app/guiagent/impl/";
    protected final String EMODNET = "http://ows.emodnet-bathymetry.eu/wms";
    protected final String GEBCO = "http://www.gebco.net/data_and_products/gebco_web_services/web_map_service/mapserv?";
    protected RadialMenu booksRadialMenu;
    protected RadialMenu instrumentsRadialMenu;
    protected RadialMenu meteoRadialMenu;
    protected RadialMenu tidesRadialMenu;
    protected RadialMenu chartsRadialMenu;
    protected RadialMenu toolsRadialMenu;
    protected RadialMenu navigationRadialMenu;
    protected ImageView centerImg;
    protected int width;
    protected int height;
    private StackPane root = null;
    private Scene scene = null;
    private List<RadialMenu> radialMenus;

    public final DockItem[] ICONS = new DockItem[]{
        DockItemFactory.newImageItem("user tools", ICON_PATH + "dock_icons/tools.png",
        (e) -> {
            toolsRadialMenu.setVisible(!toolsRadialMenu.isVisible());
        }),
        DockItemFactory.newImageItem("charts", ICON_PATH + "dock_icons/charts.png",
        (e) -> {
            chartsRadialMenu.setVisible(!chartsRadialMenu.isVisible());
        }),
        DockItemFactory.newImageItem("tides", ICON_PATH + "dock_icons/tides.png",
        (e) -> {
            tidesRadialMenu.setVisible(!tidesRadialMenu.isVisible());
        }),
        DockItemFactory.newImageItem("meteo", ICON_PATH + "dock_icons/meteo.png",
        (e) -> {
            meteoRadialMenu.setVisible(!meteoRadialMenu.isVisible());
        }),
        DockItemFactory.newImageItem("instrum.", ICON_PATH + "dock_icons/instruments.png",
        (e) -> {
            instrumentsRadialMenu.setVisible(!instrumentsRadialMenu.isVisible());
        }),
        DockItemFactory.newImageItem("navigation", ICON_PATH + "dock_icons/navigation_150.png",
        (e) -> {
            navigationRadialMenu.setVisible(!navigationRadialMenu.isVisible());
        }),
        DockItemFactory.newImageItem("logbook", ICON_PATH + "dock_icons/book.png",
        (e) -> {
            booksRadialMenu.setVisible(!booksRadialMenu.isVisible());
        })
    };
    final Dock dock = new Dock(ICONS);

    @Override
    public void componentInitiated() {
        radialMenus = new ArrayList<>();
    }

    @Override
    public void init(StackPane root, Scene scene, int height, int width) {
        this.scene = scene;
        this.root = root;
        this.height = height;
        this.width = width;
    }

    @Override
    public void makeDock() {
        createDockWidget(scene);
        createBooksRadialWidget();
        createChartsRadialWidget();
        createInstrumentsRadialWidget();
        createMeteoRadialWidget();
        createTidesRadialWidget();
        createToolsRadialWidget();
        createNavigationRadialWidget();
    }

    private void createDockWidget(Scene scene) {

        Group groupDock = new Group();
        groupDock.getChildren().add(dock);
        root.getChildren().add(groupDock);
        dock.setLayoutX(475.0);
        dock.setLayoutY(40.0);
        dock.setOrientation(Orientation.HORIZONTAL);
        StackPane.setAlignment(groupDock, Pos.BOTTOM_CENTER);
        Animation downAnimation = AnimationFactory.newTranslateAnimation(groupDock, 200, 300, true);
        Animation upAnimation = AnimationFactory.newTranslateAnimation(groupDock, 200, 0, true);
        scene.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.DOWN) {
                downAnimation.play();
            }
            if (ke.getCode() == KeyCode.UP) {
                upAnimation.play();
            }
        });
    }

    //--------------BOOKS------------------
    private void createBooksRadialWidget() {
        booksRadialMenu = RadialMenuBuilder.create()
                .centralImage("booksradialmenu150.png")
                .createNode(0, "logbook.png", 0, "vide.png", 0, "vide.png", (e) -> open())
                .createNode(0, "logbook.png", 1, "vide.png", 0, "vide.png", (e) -> open())
                .createNode(1, "lightsbook.png", 0, "images.png", 0, "emodnet.png", (e) -> open())
                .createNode(1, "lightsbook.png", 1, "vide.png", 1, "vide.png", (e) -> open())
                .createNode(2, "sailingbook.png", 0, "worldwide_sailing_directions.png", 0, "vide.png", (e) -> open())
                .createNode(2, "sailingbook.png", 1, "IrelandSouth_sailing_directions.png", 0, "vide.png", (e) -> open())
                .createNode(2, "sailingbook.png", 2, "UK_Welsh harbours_sailing_directions.png", 0, "vide.png", (e) -> open())
                .build();

        booksRadialMenu.setLayoutX((width / 2) - 10);
        booksRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(booksRadialMenu);
        radialMenus.add(booksRadialMenu);
    }

    //--------------CHARTS------------------
    private void createChartsRadialWidget() {
        chartsRadialMenu = RadialMenuBuilder.create()
                .centralImage("chartsradialmenu150.png")
                .createNode(0, "nav.png", 0, "vector.png", 0, "s57.png", (e) -> open("charts/vector/S57", "S57", ".000"))
                //  .createNode(0, "nav.png", 1, "raster.png", 0, "bsbkap.png", (e) -> open("BSB/KAP", ".KAP"))
                .createNode(0, "nav.png", 1, "raster.png", 1, "geotiff.png", (e) -> open("charts/raster/geotiff", "GeoTiff", ".tif", ".TIF", ".tiff"))
                .createNode(1, "bathy.png", 0, "images.png", 0, "emodnet.png", (e) -> openWMS("WMS", EMODNET))
                .createNode(1, "bathy.png", 0, "images.png", 1, "gebco.png", (e) -> openWMS("WMS", GEBCO))
                .createNode(1, "bathy.png", 1, "data.png", 1, "dbshomon.png", (e) -> openDB(DB_NAME, HOST_NAME, JDBC_PROTOCOL, PORT, DRIVER_NAME, USER_NAME, PASSWD))
                .createNode(1, "bathy.png", 1, "data.png", 2, "dbshomoff.png", (e) -> closeDB(DB_NAME))
                .createNode(2, "sediment.png", 0, "data.png", 0, "shom.png", (e) -> open("sedimentology", "Sediments SHOM", ".shp"))
                .build();

        chartsRadialMenu.setLayoutX((width / 2) - 10);
        chartsRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(chartsRadialMenu);
        radialMenus.add(chartsRadialMenu);
    }

    //--------------INSTRUMENTS------------------
    private void createInstrumentsRadialWidget() {
        instrumentsRadialMenu = RadialMenuBuilder.create()
                .centralImage("instrumentsradialmenu150.png")
                .createNode(0, "navigation.png", 0, "ais.png", 0, "aisradar.png", (e) -> open("AisRadar"))
                .createNode(0, "navigation.png", 1, "ais.png", 1, "template.png", (e) -> open("InstrumentTemplate"))
                .build();

        instrumentsRadialMenu.setLayoutX((width / 2) - 40);
        instrumentsRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(instrumentsRadialMenu);
        radialMenus.add(instrumentsRadialMenu);
    }

    //--------------METEO------------------
    private void createMeteoRadialWidget() {
        meteoRadialMenu = RadialMenuBuilder.create()
                .centralImage("meteoradialmenu150.png")
                .build();

        meteoRadialMenu.setLayoutX((width / 2) - 30);
        meteoRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(meteoRadialMenu);
        radialMenus.add(meteoRadialMenu);
    }
//--------------NAVIGATION------------------

    private void createNavigationRadialWidget() {
        navigationRadialMenu = RadialMenuBuilder.create()
                .centralImage("navigation_150.png")
                .createNode(0, "navigation.png", 0, "tracks.png", 0, "gpx.png", (e) -> open("Gpx", ".gpx", ".GPX"))
                .createNode(0, "navigation.png", 0, "tracks.png", 1, "kml.png", (e) -> open("Kml", ".kml", ".KML", ".kmz", ".KMZ"))
                .build();

        navigationRadialMenu.setLayoutX((width / 2) - 30);
        navigationRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(navigationRadialMenu);
        radialMenus.add(navigationRadialMenu);
    }

    //--------------TIDES------------------
    private void createTidesRadialWidget() {
        tidesRadialMenu = RadialMenuBuilder.create()
                .centralImage("tidesradialmenu150.png")
                .createNode(0, "currents.png", 0, "catalog.png", 0, "shom.png", (e) -> open("Currents", ".shp", ".SHP"))
                .build();

        tidesRadialMenu.setLayoutX((width / 2) - 10);
        tidesRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(tidesRadialMenu);
        radialMenus.add(tidesRadialMenu);
    }

    //--------------TOOLS------------------
    private void createToolsRadialWidget() {
        toolsRadialMenu = RadialMenuBuilder.create()
                .centralImage("toolsradialmenu150.png")
                .createNode(0, "system.png", 0, "system.png", 0, "system.png", (e) -> open())
                .createNode(1, "data.png", 0, "files.png", 0, "shapefile.png", (e) -> open("SHP", ".shp"))
                .createNode(1, "data.png", 0, "files.png", 1, "kml.png", (e) -> open("KML", ".kml", ".kmz", ".KMZ"))
                .build();
        toolsRadialMenu.setLayoutX((width / 2));
        toolsRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(toolsRadialMenu);
        radialMenus.add(toolsRadialMenu);
    }

    private void open() {
        System.out.println("Work in progress");
        clear();
    }

    private void open(String test) {
        instrumentDriverManagerServices.open(test);
        clear();
    }

    private void open(String description, String... des) {
        String[] tab = new String[des.length];
        int i = 0;
        for (String s : des) {
            tab[i] = "*" + s;
            i++;
        }
        driverManagerServices.open(description, tab);
        clear();
    }

    private void openShp(String description, String des) {
        driverManagerServices.open(new FileChooser.ExtensionFilter(description, des));
        clear();
    }

    private void openWMS(String description, String url) {
        webDriverManagerServices.handleOpenFiles(url);
        clear();
    }

    private void openDB(String dbName, String hostName, String protocol, String port,
            String driverName, String userName, String passwd) {
        //  databaseDriverManagerServices.connect(DB_NAME, HOST_NAME, JDBC_PROTOCOL, PORT, DRIVER_NAME, USER_NAME, PASSWD);
        databaseDriverManagerServices.connect(dbName, hostName, protocol, port, driverName, userName, passwd);
        clear();
    }

    private void closeDB(String dbName) {
        databaseDriverManagerServices.close(dbName);
        clear();
    }

    private void clear() {
        radialMenus.stream().forEach((r) -> {
            r.setVisible(false);
        });
    }

    @Override
    public void componentStarted() {
    }

    @Override
    public void componentStopped() {
    }

}
