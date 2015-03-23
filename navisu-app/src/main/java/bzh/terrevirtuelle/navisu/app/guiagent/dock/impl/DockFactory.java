/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.app.guiagent.dock.impl;

import bzh.terrevirtuelle.navisu.app.guiagent.tools.AnimationFactory;
import bzh.terrevirtuelle.navisu.widgets.dock.Dock;
import bzh.terrevirtuelle.navisu.widgets.dock.DockItem;
import bzh.terrevirtuelle.navisu.widgets.dock.DockItemFactory;
import bzh.terrevirtuelle.navisu.widgets.mob.Mob;
import bzh.terrevirtuelle.navisu.widgets.radialmenu.menu.RadialMenu;
import bzh.terrevirtuelle.navisu.widgets.radialmenu.menu.RadialMenuBuilder;
import javafx.animation.Animation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

/**
 * NaVisu
 *
 * @date 23 mars 2015
 * @author Serge Morvan
 */
public class DockFactory {

    protected static final String ICON_PATH = "bzh/terrevirtuelle/navisu/app/guiagent/impl/";
    protected RadialMenu booksRadialMenu;
    protected RadialMenu instrumentsRadialMenu;
    protected RadialMenu meteoRadialMenu;
    protected RadialMenu tidesRadialMenu;
    protected RadialMenu chartsRadialMenu;
    protected RadialMenu toolsRadialMenu;
    boolean firstBooksRadialMenu = false;
    boolean firstInstrumentsRadialMenu = false;
    boolean firstMeteoRadialMenu = false;
    boolean firstToolsRadialMenu = false;
    boolean firstChartsRadialMenu = false;
    boolean firstTidesRadialMenu = false;
    protected ImageView centerImg;
    protected int width;
    protected int height;
    private final StackPane root;
    private final Scene scene;
    public final DockItem[] ICONS = new DockItem[]{
        DockItemFactory.newImageItem("user tools", ICON_PATH + "dock_icons/tools.png", (e) -> showToolsMenu()),
        DockItemFactory.newImageItem("charts", ICON_PATH + "dock_icons/charts.png", (e) -> showChartsMenu()),
        DockItemFactory.newImageItem("tides", ICON_PATH + "dock_icons/tides.png", (e) -> showTidesMenu()),
        DockItemFactory.newImageItem("meteo", ICON_PATH + "dock_icons/meteo.png", (e) -> showMeteoMenu()),
        DockItemFactory.newImageItem("instrum.", ICON_PATH + "dock_icons/instruments.png", (e) -> showInstrumentsMenu()),
        DockItemFactory.newImageItem("logbook", ICON_PATH + "dock_icons/book.png", (e) -> showBooksMenu())
    };
    final Dock dock = new Dock(ICONS);

    public DockFactory(StackPane root, Scene scene, int height, int width) {
        this.scene = scene;
        this.root = root;
        this.height = height;
        this.width = width;
    }

    public void makeDock() {
        createDockWidget(scene);
     //   createMOBWidget(scene);
        createBooksRadialWidget();
        createInstrumentsRadialWidget();
        createMeteoRadialWidget();
        createToolsRadialWidget();
        createChartsRadialWidget();
        createTidesRadialWidget();
    }

    private void testMenuItem() {
        System.out.println("testMenuItem");
    }

    //--------------BOOKS------------------
    private void createBooksRadialWidget() {
        booksRadialMenu = RadialMenuBuilder.create()
                //  .innerRadius(30).outerRadius(60).length(360).gap(2)
                .centralImage("centreradialmenu60.png")
                .stageItem(0, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(0, 1, "vide.png", (e) -> testMenuItem())
                .stageItem(0, 2, "vide.png", (e) -> testMenuItem())
                .stageItem(1, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(2, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(3, "vide.png", 0, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .build();

        booksRadialMenu.setLayoutX((width / 2) - 50);
        booksRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(booksRadialMenu);
    }

    private void showBooksMenu() {
        firstBooksRadialMenu = firstBooksRadialMenu != true;
        booksRadialMenu.setVisible(firstBooksRadialMenu);
    }

    //--------------INSTRUMENTS------------------
    private void createInstrumentsRadialWidget() {
        instrumentsRadialMenu = RadialMenuBuilder.create()
                .centralImage("instrumentsradialmenu150.png")
                .stageItem(0, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(0, 1, "vide.png", (e) -> testMenuItem())
                .stageItem(0, 2, "vide.png", (e) -> testMenuItem())
                .stageItem(1, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(2, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(3, "vide.png", 0, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .build();

        instrumentsRadialMenu.setLayoutX((width / 2) - 40);
        instrumentsRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(instrumentsRadialMenu);
    }

    private void showInstrumentsMenu() {
        firstInstrumentsRadialMenu = firstInstrumentsRadialMenu != true;
        instrumentsRadialMenu.setVisible(firstInstrumentsRadialMenu);
    }

    //--------------METEO------------------
    private void createMeteoRadialWidget() {
        meteoRadialMenu = RadialMenuBuilder.create()
                .centralImage("meteoradialmenu150.png")
                .stageItem(0, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(0, 1, "vide.png", (e) -> testMenuItem())
                .stageItem(0, 2, "vide.png", (e) -> testMenuItem())
                .stageItem(1, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(2, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(3, "vide.png", 0, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .build();

        meteoRadialMenu.setLayoutX((width / 2) - 30);
        meteoRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(meteoRadialMenu);
    }

    private void showMeteoMenu() {
        firstMeteoRadialMenu = firstMeteoRadialMenu != true;
        meteoRadialMenu.setVisible(firstMeteoRadialMenu);
    }

    //--------------TIDES------------------
    private void createTidesRadialWidget() {
        tidesRadialMenu = RadialMenuBuilder.create()
                .centralImage("tidesradialmenu150.png")
                .stageItem(0, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(0, 1, "vide.png", (e) -> testMenuItem())
                .stageItem(0, 2, "vide.png", (e) -> testMenuItem())
                .stageItem(1, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(2, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(3, "vide.png", 0, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .build();

        tidesRadialMenu.setLayoutX((width / 2) - 10);
        tidesRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(tidesRadialMenu);
    }

    private void showTidesMenu() {
        firstTidesRadialMenu = firstTidesRadialMenu != true;
        tidesRadialMenu.setVisible(firstTidesRadialMenu);
    }

    //--------------CHARTS------------------
    private void createChartsRadialWidget() {
        chartsRadialMenu = RadialMenuBuilder.create()
                .centralImage("chartsradialmenu150.png")
               // .stageItem(0, "nav.png", 0, "raster.png", (e) -> testMenuItem())
              //  .stageItem(1, "nav.png", 1, "vector.png", (e) -> testMenuItem())
                
                .stageItem(0, "nav.png", 0, "vector.png", 1, "s57.png", (e) -> testMenuItem())
                .stageItem(0, "nav.png", 1, "raster.png", 0, "bsbkap.png", (e) -> testMenuItem())
               // .stageItem(0, "nav.png", 2, "raster.png", 1, "geotiff.png", (e) -> testMenuItem())
                /*
                .stageItem(0, "vector.png", 0, "s57.png", (e) -> testMenuItem())
                .stageItem(1, "raster.png", 0, "bsbkap.png", (e) -> testMenuItem())
                .stageItem(1, 1, "geotiff.png", (e) -> testMenuItem())
                */
                //.stageItem(0, 1, "vide.png", (e) -> testMenuItem())
               // .stageItem(0, 2, "vide.png", (e) -> testMenuItem())
               // .stageItem(1, "vide.png", 0, "vide.png", (e) -> testMenuItem())
               // .stageItem(2, "vide.png", 0, "vide.png", (e) -> testMenuItem())
               // .stageItem(3, "vide.png", 0, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .build();

        chartsRadialMenu.setLayoutX((width / 2) - 10);
        chartsRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(chartsRadialMenu);
    }

    private void showChartsMenu() {
        firstChartsRadialMenu = firstChartsRadialMenu != true;
        chartsRadialMenu.setVisible(firstChartsRadialMenu);
    }

    //--------------TOOLS------------------
    private void createToolsRadialWidget() {
        toolsRadialMenu = RadialMenuBuilder.create()
                .centralImage("toolsradialmenu150.png")
                .stageItem(0, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(0, 1, "vide.png", (e) -> testMenuItem())
                .stageItem(0, 2, "vide.png", (e) -> testMenuItem())
                .stageItem(1, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(2, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .stageItem(3, "vide.png", 0, "vide.png", 0, "vide.png", (e) -> testMenuItem())
                .build();

        toolsRadialMenu.setLayoutX((width / 2));
        toolsRadialMenu.setLayoutY(height / 2);
        root.getChildren().add(toolsRadialMenu);
    }

    private void showToolsMenu() {
        firstToolsRadialMenu = firstToolsRadialMenu != true;
        toolsRadialMenu.setVisible(firstToolsRadialMenu);
    }

    /**
     * *******************************************
     * MOB - Man Over Board **************************************************
     */
    private void createMOBWidget(Scene scene) {

        //Group mob = new Group();
        // mob.getChildren().add(mobOffImg);
        //  mob.getChildren().add(mobOnImg);
        Mob mob = new Mob();
        root.getChildren().add(mob);
        mob.setTranslateX(540.0);
        mob.setTranslateY(-70.0);
        //  mob.setVisible(true);
        // mob.getChildren().add(swingNode);
        StackPane.setAlignment(mob, Pos.BOTTOM_CENTER);
    }

    private void createDockWidget(Scene scene) {

        Group groupDock = new Group();
        groupDock.getChildren().add(dock);
        root.getChildren().add(groupDock);
        dock.setLayoutX(475.0);
        dock.setLayoutY(40.0);
        dock.setOrientation(Orientation.HORIZONTAL);
        /* margins if necessity to adjust position of whole group */
        //StackPane.setMargin(groupdock,(new Insets(0, 0, 0, 0)));
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
}
