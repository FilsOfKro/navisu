/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.navigation.gps.plotter.impl;

import bzh.terrevirtuelle.navisu.app.drivers.instrumentdriver.InstrumentDriver;
import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.app.guiagent.geoview.GeoViewServices;
import bzh.terrevirtuelle.navisu.app.guiagent.layers.LayersManagerServices;
import bzh.terrevirtuelle.navisu.app.guiagent.layertree.LayerTreeServices;
import bzh.terrevirtuelle.navisu.domain.navigation.NavigationDataSet;
import bzh.terrevirtuelle.navisu.instruments.ais.base.AisServices;
import bzh.terrevirtuelle.navisu.instruments.common.controller.GpsEventsController;
import bzh.terrevirtuelle.navisu.instruments.gpstrack.track.GpsTrackServices;
import bzh.terrevirtuelle.navisu.instruments.transponder.TransponderServices;
import bzh.terrevirtuelle.navisu.kml.KmlObjectServices;
import bzh.terrevirtuelle.navisu.navigation.gps.plotter.GpsPlotterWithRoute;
import bzh.terrevirtuelle.navisu.navigation.gps.plotter.GpsPlotterWithRouteServices;
import bzh.terrevirtuelle.navisu.navigation.gps.plotter.impl.controller.GpsPlotterWithRouteController;
import bzh.terrevirtuelle.navisu.navigation.gps.plotter.impl.controller.GpsPlotterWithRouteGpsEventsController;
import gov.nasa.worldwind.layers.RenderableLayer;
import java.util.List;
import org.capcaval.c3.component.ComponentState;
import org.capcaval.c3.component.annotation.ProducedEvent;
import org.capcaval.c3.component.annotation.UsedService;
import bzh.terrevirtuelle.navisu.instruments.gps.plotter.impl.controller.events.TransponderActivateEvent;

/**
 * NaVisu
 *
 * @date 7 mai 2015
 * @author Serge Morvan
 */
public class GpsPlotterWithRouteImpl
        implements GpsPlotterWithRoute, GpsPlotterWithRouteServices, InstrumentDriver, ComponentState {

    @UsedService
    GeoViewServices geoViewServices;
    @UsedService
    GuiAgentServices guiAgentServices;
    @UsedService
    LayerTreeServices layerTreeServices;
    @UsedService
    LayersManagerServices layersManagerServices;
    @UsedService
    KmlObjectServices kmlObjectServices;
    @UsedService
    GpsTrackServices gpsTrackServices;
    @UsedService
    AisServices aisServices;
    @UsedService
    TransponderServices transponderServices;

    @ProducedEvent
    protected TransponderActivateEvent transponderActivateEvent;

    protected boolean on = false;
    private final String NAME0 = "GpsPlotter";
    private final String NAME1 = "GpsPlotterWithRoute";
    private final String NAME2 = "Nautical documents";
    private final String NAME3 = "Transponder";
    protected final String GROUP = "Navigation";
   // private RenderableLayer transponderZoneLayer;
    private GpsPlotterWithRouteController gpsPlotterController;
    private GpsEventsController gpsEventsController;
    private boolean withTarget = true;
    protected List<String> s57Controllers;
    protected NavigationDataSet navigationDataSet = null;

    @Override
    public void componentInitiated() {

    }

    @Override
    public void componentStarted() {
    }

    @Override
    public void on(NavigationDataSet navigationDataSet, boolean withTarget) {
        this.navigationDataSet = navigationDataSet;
        this.withTarget = withTarget;
         
    }

    @Override
    public void on(String... files) {
        if (gpsPlotterController == null) {
            gpsPlotterController = GpsPlotterWithRouteController.getInstance(this,
                    layersManagerServices,
                    guiAgentServices, kmlObjectServices, aisServices,
                    withTarget,
                    navigationDataSet,
                    NAME0, NAME2, NAME3, GROUP);
            if (withTarget) {
                gpsPlotterController.createTarget();
            }
            transponderServices.on();
            gpsEventsController = new GpsPlotterWithRouteGpsEventsController(gpsPlotterController);
            gpsEventsController.subscribe();
            gpsTrackServices.on(files);
        } else {
            gpsPlotterController.activateControllers();
        }
    }

    @Override
    public void off() {
    }

    @Override
    public void componentStopped() {
    }

    @Override
    public void on() {
        this.on("");
    }

    @Override
    public boolean isOn() {
        return on;
    }

    @Override
    public boolean canOpen(String category) {
        return category.equals(NAME1);
    }

    @Override
    public InstrumentDriver getDriver() {
        return this;
    }

    public void notifyTransponderActivateEvent(RenderableLayer layer, List<String> targets) {
        transponderActivateEvent.notifyAisActivateMessageChanged(layer, targets);
    }
}
