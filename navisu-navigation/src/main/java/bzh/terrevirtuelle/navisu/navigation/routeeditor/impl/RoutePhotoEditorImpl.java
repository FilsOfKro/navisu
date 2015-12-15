/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.navigation.routeeditor.impl;

import bzh.terrevirtuelle.navisu.app.drivers.instrumentdriver.InstrumentDriver;
import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.app.guiagent.geoview.GeoViewServices;
import bzh.terrevirtuelle.navisu.app.guiagent.layers.LayersManagerServices;
import bzh.terrevirtuelle.navisu.instruments.ais.base.AisServices;
import bzh.terrevirtuelle.navisu.instruments.ais.base.impl.controller.events.AisUpdateTargetEvent;
import bzh.terrevirtuelle.navisu.instruments.gps.plotter.GpsPlotterServices;
import bzh.terrevirtuelle.navisu.navigation.routeeditor.RoutePhotoEditor;
import bzh.terrevirtuelle.navisu.navigation.routeeditor.RoutePhotoEditorServices;
import bzh.terrevirtuelle.navisu.navigation.routeeditor.RoutePhotoViewerServices;
import bzh.terrevirtuelle.navisu.navigation.routeeditor.impl.controller.RoutePhotoEditorController;
import bzh.terrevirtuelle.navisu.photos.exif.ExifComponentServices;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import org.capcaval.c3.component.ComponentState;
import org.capcaval.c3.component.annotation.ProducedEvent;
import org.capcaval.c3.component.annotation.UsedService;
import bzh.terrevirtuelle.navisu.charts.vector.s57.charts.S57ChartComponentServices;

/**
 * NaVisu
 *
 * @date 26 août 2015
 * @author Serge Morvan
 */
public class RoutePhotoEditorImpl
        implements RoutePhotoEditor, RoutePhotoEditorServices, InstrumentDriver, ComponentState {

    @UsedService
    GuiAgentServices guiAgentServices;
    @UsedService
    S57ChartComponentServices s57ChartServices;
    @UsedService
    GeoViewServices geoViewServices;
    @UsedService
    private LayersManagerServices layersManagerServices;   
    @UsedService
    private GpsPlotterServices gpsPlotterServices;
    @UsedService
    ExifComponentServices exifComponentServices;
    @UsedService
    RoutePhotoViewerServices routePhotoViewerServices;
    @ProducedEvent
    protected AisUpdateTargetEvent aisUpdateTargetEvent;
    @UsedService
    AisServices aisServices;
   
    private final String COMPONENT_KEY_NAME = "RoutePhotoEditor";
    private final String LAYER_NAME = "Highway";
    private final String GROUP_NAME = "Navigation";
    private RoutePhotoEditorController routePhotoEditorController;

    @Override
    public void componentInitiated() {
    }

    @Override
    public void on(String... files) {
        routePhotoEditorController = new RoutePhotoEditorController(this,
                layersManagerServices,
                guiAgentServices,
                s57ChartServices,
                routePhotoViewerServices, exifComponentServices,
                gpsPlotterServices,
                aisServices,
                GROUP_NAME, LAYER_NAME,
                KeyCode.M, KeyCombination.CONTROL_DOWN);
        guiAgentServices.getScene().addEventFilter(KeyEvent.KEY_RELEASED, routePhotoEditorController);
        guiAgentServices.getRoot().getChildren().add(routePhotoEditorController);
        routePhotoEditorController.setVisible(true);
    }

    @Override
    public boolean canOpen(String category) {
        return category.equals(COMPONENT_KEY_NAME);
    }

    @Override
    public InstrumentDriver getDriver() {
        return this;
    }

    @Override
    public void off() {
        guiAgentServices.getScene().removeEventFilter(KeyEvent.KEY_RELEASED, routePhotoEditorController);
        guiAgentServices.getRoot().getChildren().remove(routePhotoEditorController);
        routePhotoEditorController.setVisible(false);
    }

    @Override
    public void componentStarted() {
    }

    @Override
    public void componentStopped() {
    }

    public GuiAgentServices getGuiAgentServices() {
        return guiAgentServices;
    }

    public S57ChartComponentServices getS57ChartServices() {
        return s57ChartServices;
    }

    public ExifComponentServices getExifComponentServices() {
        return exifComponentServices;
    }

    public RoutePhotoViewerServices getRoutePhotoViewerServices() {
        return routePhotoViewerServices;
    }
}
