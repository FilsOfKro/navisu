/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.navigation.gps.plotter.impl.controller;

import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.app.guiagent.layers.LayersManagerServices;
import bzh.terrevirtuelle.navisu.charts.vector.s57.catalog.global.S57GlobalCatalogServices;
import bzh.terrevirtuelle.navisu.charts.vector.s57.charts.S57ChartComponentServices;
import bzh.terrevirtuelle.navisu.charts.vector.s57.charts.impl.controller.navigation.S57BasicBehavior;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.S57Chart;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BeaconIsolatedDanger;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BeaconLateral;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BeaconSafeWater;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BeaconSpecialPurpose;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BuoyCardinal;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BuoyInstallation;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BuoyIsolatedDanger;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BuoyLateral;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BuoySafeWater;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BuoySpecialPurpose;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.Landmark;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.MooringWarpingFacility;
import bzh.terrevirtuelle.navisu.domain.gpx.model.Gpx;
import bzh.terrevirtuelle.navisu.domain.gpx.model.Highway;
import bzh.terrevirtuelle.navisu.domain.navigation.NavigationData;
import bzh.terrevirtuelle.navisu.domain.navigation.NavigationDataSet;
import bzh.terrevirtuelle.navisu.domain.navigation.avurnav.Avurnav;
import bzh.terrevirtuelle.navisu.domain.navigation.sailingDirections.SailingDirections;
import bzh.terrevirtuelle.navisu.domain.ship.model.Ship;
import bzh.terrevirtuelle.navisu.instruments.gps.plotter.impl.controller.GpsPlotterController;
import bzh.terrevirtuelle.navisu.instruments.transponder.TransponderServices;
import bzh.terrevirtuelle.navisu.kml.KmlObjectServices;
import bzh.terrevirtuelle.navisu.navigation.controller.AvurnavController;
import bzh.terrevirtuelle.navisu.navigation.controller.HighwayController;
import bzh.terrevirtuelle.navisu.navigation.controller.S57ChartController;
import bzh.terrevirtuelle.navisu.navigation.controller.SailingDirectionsController;
import bzh.terrevirtuelle.navisu.navigation.gps.plotter.impl.GpsPlotterWithRouteImpl;
import bzh.terrevirtuelle.navisu.util.io.IO;
import bzh.terrevirtuelle.navisu.util.xml.ImportExportXML;
import bzh.terrevirtuelle.navisu.widgets.textArea.TextAreaController;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.ogc.collada.ColladaRoot;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.PointPlacemark;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javax.xml.bind.JAXBException;

/**
 * NaVisu
 *
 * @date 7 mai 2015
 * @author Serge Morvan
 */
public class GpsPlotterWithRouteController extends GpsPlotterController {

    private final String NAME2 = "Nautical documents";
    private final String NAME3 = "S57 Buoyage behavior";
    private final String NAME4 = "Nautical documents icons";
    private final List<String> NAVIGATION_OBJECTS = Arrays.asList("Avurnav", "SailingDirections");
    protected RenderableLayer navigationPgonLayer;
    protected RenderableLayer navigationIconsLayer;
    protected RenderableLayer transponderZoneLayer;
    protected List<String> s57ControllerIdList;
    protected GpsPlotterWithRouteImpl component;
    protected NavigationDataSet navigationDataSet = null;
    protected TextAreaController textAreaController;
    protected S57GlobalCatalogServices s57GlobalCatalogServices;
    protected S57ChartComponentServices s57ChartComponentServices;
    protected TransponderServices transponderServices;

    public GpsPlotterWithRouteController(GpsPlotterWithRouteImpl component,
            LayersManagerServices layersManagerServices,
            GuiAgentServices guiAgentServices,
            KmlObjectServices kmlObjectServices,
            S57ChartComponentServices s57ChartComponentServices,
            S57GlobalCatalogServices s57GlobalCatalogServices,
            TransponderServices transponderServices,
            String name) {
        super(layersManagerServices,
                guiAgentServices,
                kmlObjectServices,
                name);
        this.component = component;
        this.s57ChartComponentServices = s57ChartComponentServices;
        this.s57GlobalCatalogServices = s57GlobalCatalogServices;
        this.transponderServices = transponderServices;
        navigationPgonLayer = layersManagerServices.initLayer(GROUP, NAME2);
        navigationPgonLayer.setPickEnabled(false);
        navigationIconsLayer = layersManagerServices.initLayer(GROUP, NAME4);
        transponderZoneLayer = layersManagerServices.initLayer(GROUP, NAME3);
    }

    @Override
    public void init() {
        super.init();
        activateControllers();
        transponderServices.on();
        transponderServices.setShip(ownerShip);
    }

    @Override
    protected void addListeners() {
        wwd.addSelectListener((SelectEvent event) -> {
            Object o = event.getTopObject();
            if (event.isLeftClick() && o != null) {
                if (o.getClass() == ColladaRoot.class) {
                    Object object = ((ColladaRoot) o).getField("Ship");
                    if (object != null && object.getClass() == Ship.class) {
                        Ship ship = (Ship) object;
                        updateShipPanel(ship);
                    }
                }
            }
        });
        wwd.addSelectListener((SelectEvent event) -> {
            PickedObject po = event.getTopPickedObject();
            if (po != null && po.getObject() instanceof PointPlacemark) {
                if (event.getEventAction().equals(SelectEvent.LEFT_CLICK)) {
                    PointPlacemark placemark = (PointPlacemark) po.getObject();
                    if (placemark.getValue("TYPE") != null) {
                        String type = (String) placemark.getValue("TYPE");
                        if (NAVIGATION_OBJECTS.contains(type)) {
                            Platform.runLater(() -> {
                                textAreaController = new TextAreaController();
                                textAreaController.getDataTextArea().setWrapText(true);
                                textAreaController.getTitleText().setText((String) placemark.getValue("TITLE"));
                                textAreaController.getDataTextArea().setText((String) placemark.getValue("TEXT"));
                                textAreaController.setVisible(true);
                                guiAgentServices.getRoot().getChildren().add(textAreaController);
                            });
                            event.consume();
                        }
                        if (type.equals("S57Chart")) {
                            Path path = s57GlobalCatalogServices.getChartPath((String) placemark.getValue(AVKey.DISPLAY_NAME));
                            s57ChartComponentServices.openChart(path.toString());
                          //  System.out.println("navigationDataSet " + navigationDataSet);
                            activateS57Controllers();
                            event.consume();
                        }
                    }
                }

            }
        });
    }

    public final void activateControllers() {
        File file = IO.fileChooser(guiAgentServices.getStage(),
                "privateData/nds/", "NDS files (*.nds)", "*.nds", "*.NDS");
        navigationDataSet = new NavigationDataSet();
        try {
            navigationDataSet = ImportExportXML.imports(navigationDataSet, file);
        } catch (FileNotFoundException | JAXBException ex) {
            Logger.getLogger(GpsPlotterWithRouteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        activateS57Controllers();
        activateNavigationControllers();
    }

    private void activateS57Controllers() {
        List<Class> s57ConList = Arrays.asList(BeaconIsolatedDanger.class,
                BeaconLateral.class,
                BeaconSafeWater.class,
                BeaconSpecialPurpose.class,
                BuoyCardinal.class,
                BuoyInstallation.class,
                BuoyIsolatedDanger.class,
                BuoyLateral.class,
                BuoySafeWater.class,
                BuoySpecialPurpose.class,
                MooringWarpingFacility.class,
                Landmark.class);

        List<NavigationData> s57NavigationDataList = new ArrayList<>();
        s57ConList.stream().forEach((claz) -> {
            s57NavigationDataList.addAll(navigationDataSet.get(claz));
        });

        s57ControllerIdList = new ArrayList<>();
        s57NavigationDataList.stream().forEach((s) -> {
            s57ControllerIdList.add(Long.toString(s.getId()));
        });
        component.notifyTransponderActivateEvent(transponderZoneLayer, s57NavigationDataList);
    }

    private void activateNavigationControllers() {
        List<S57Chart> chartList = navigationDataSet.get(S57Chart.class);
        chartList.stream().forEach((S57Chart a) -> {
            String displayName = a.getNumber();
            String description = a.getDescription();
            S57ChartController sc = new S57ChartController(new S57BasicBehavior(),
                    guiAgentServices,
                    a,
                    926, displayName, description);
            sc.setLayer(navigationPgonLayer);
            sc.setIconsLayer(navigationIconsLayer);
            sc.activate();
        });

        List<Avurnav> avurnavList = navigationDataSet.get(Avurnav.class);
        avurnavList.stream().forEach((Avurnav a) -> {
            String displayName = "Avurnav N°" + Long.toString(a.getId());
            String description = a.getDescription();
            AvurnavController sc = new AvurnavController(new S57BasicBehavior(),
                    guiAgentServices, a,
                    926, displayName, description);
            sc.setLayer(navigationPgonLayer);
            sc.setIconsLayer(navigationIconsLayer);
            sc.activate();
        });

        List<SailingDirections> sailingDirectionsList = navigationDataSet.get(SailingDirections.class);
        sailingDirectionsList.stream().forEach((SailingDirections a) -> {
            String displayName = "SailingDirections N°" + Long.toString(a.getId());
            String description = a.getDescription();
            SailingDirectionsController sc = new SailingDirectionsController(new S57BasicBehavior(),
                    guiAgentServices, a,
                    926, displayName, description);
            sc.setLayer(navigationPgonLayer);
            sc.setIconsLayer(navigationIconsLayer);
            sc.activate();
        });
        List<Gpx> gpxList = navigationDataSet.get(Gpx.class);
        gpxList.stream().forEach((Gpx a) -> {
            Highway highway = a.getHighway();
            String displayName = "Highway N°" + Long.toString(a.getId()) + "\n"
                    + highway.getDescription();
            HighwayController sc = new HighwayController(new S57BasicBehavior(),
                    guiAgentServices, highway,
                    926, displayName, highway.getDescription());
            sc.setLayer(navigationPgonLayer);
            sc.setIconsLayer(navigationIconsLayer);
            sc.activate();
        });
    }
}
