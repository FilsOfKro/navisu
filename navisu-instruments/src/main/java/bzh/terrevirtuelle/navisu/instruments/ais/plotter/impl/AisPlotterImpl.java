/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.instruments.ais.plotter.impl;

import bzh.terrevirtuelle.navisu.instruments.ais.plotter.impl.controller.AisPanelController;
import bzh.terrevirtuelle.navisu.app.dpagent.DpAgentServices;
import bzh.terrevirtuelle.navisu.app.drivers.instrumentdriver.InstrumentDriver;
import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.app.guiagent.geoview.GeoViewServices;
import bzh.terrevirtuelle.navisu.app.guiagent.layertree.LayerTreeServices;
import bzh.terrevirtuelle.navisu.domain.devices.model.BaseStation;
import bzh.terrevirtuelle.navisu.domain.ship.model.Ship;
import bzh.terrevirtuelle.navisu.instruments.ais.base.AisServices;
import bzh.terrevirtuelle.navisu.instruments.ais.base.impl.controller.events.AisCreateStationEvent;
import bzh.terrevirtuelle.navisu.instruments.ais.base.impl.controller.events.AisCreateTargetEvent;
import bzh.terrevirtuelle.navisu.instruments.ais.base.impl.controller.events.AisDeleteStationEvent;
import bzh.terrevirtuelle.navisu.instruments.ais.base.impl.controller.events.AisDeleteTargetEvent;
import bzh.terrevirtuelle.navisu.instruments.ais.base.impl.controller.events.AisUpdateStationEvent;
import bzh.terrevirtuelle.navisu.instruments.ais.base.impl.controller.events.AisUpdateTargetEvent;
import bzh.terrevirtuelle.navisu.instruments.ais.plotter.AisPlotter;
import bzh.terrevirtuelle.navisu.instruments.ais.plotter.AisPlotterServices;
import bzh.terrevirtuelle.navisu.instruments.ais.plotter.impl.controller.AisPlotterController;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import org.capcaval.c3.component.ComponentEventSubscribe;
import org.capcaval.c3.component.ComponentState;
import org.capcaval.c3.component.annotation.UsedService;
import org.capcaval.c3.componentmanager.ComponentManager;

/**
 * @date 3 mars 2015
 * @author Serge Morvan
 */
public class AisPlotterImpl
        implements AisPlotter, AisPlotterServices, InstrumentDriver, ComponentState {

    @UsedService
    GeoViewServices geoViewServices;

    @UsedService
    DpAgentServices dpAgentServices;

    @UsedService
    GuiAgentServices guiAgentServices;

    @UsedService
    LayerTreeServices layerTreeServices;

    @UsedService
    AisServices aisServices;

    ComponentManager cm;
    ComponentEventSubscribe<AisCreateStationEvent> aisCSEvent;
    ComponentEventSubscribe<AisCreateTargetEvent> aisCTEvent;
    ComponentEventSubscribe<AisDeleteStationEvent> aisDSEvent;
    ComponentEventSubscribe<AisDeleteTargetEvent> aisDTEvent;
    ComponentEventSubscribe<AisUpdateStationEvent> aisUSEvent;
    ComponentEventSubscribe<AisUpdateTargetEvent> aisUTEvent;

    protected final String NAME = "AisPlotter";
    protected final String GROUP = "Devices";
    protected AisPanelController aisPanelController;
    protected Ship ship;
    protected BaseStation station;
    protected Map<Integer, Ship> ships;
    protected Map<Integer, BaseStation> stations;
    protected boolean on = false;
    protected AisPlotterController controller;
    protected Map<Integer, Calendar> timestamps;
    protected Map<Integer, String> midMap;

    @Override
    public void componentInitiated() {

        cm = ComponentManager.componentManager;
        aisCSEvent = cm.getComponentEventSubscribe(AisCreateStationEvent.class);
        aisCTEvent = cm.getComponentEventSubscribe(AisCreateTargetEvent.class);
        aisDSEvent = cm.getComponentEventSubscribe(AisDeleteStationEvent.class);
        aisDTEvent = cm.getComponentEventSubscribe(AisDeleteTargetEvent.class);
        aisUSEvent = cm.getComponentEventSubscribe(AisUpdateStationEvent.class);
        aisUTEvent = cm.getComponentEventSubscribe(AisUpdateTargetEvent.class);
    }

    @Override
    public void componentStarted() {
    }

    @Override
    public void componentStopped() {
    }

    @Override
    public void on(String... files) {
        controller = new AisPlotterController(geoViewServices,
                layerTreeServices,
                guiAgentServices,
                NAME, GROUP);
        if (!aisServices.isOn()) {
            aisServices.on();
        }
        if (on == false) {
            on = true;
            ships = aisServices.getShips();
            Set<Integer> shipSet = ships.keySet();
            shipSet.stream().forEach((i) -> {
                controller.createTarget(ships.get(i));
            });
            stations = aisServices.getStations();
            Set<Integer> stationSet = stations.keySet();
            timestamps = aisServices.getTimestamps();
            controller.setTimestamps(timestamps);
            midMap = aisServices.getMidMap();
            controller.setMidMap(midMap);

            aisCTEvent.subscribe((AisCreateTargetEvent) (Ship updatedData) -> {
                controller.createTarget(updatedData);
            });
            aisUTEvent.subscribe((AisUpdateTargetEvent) (Ship updatedData) -> {
                controller.updateTarget(updatedData);
            });
            aisDTEvent.subscribe((AisDeleteTargetEvent) (Ship updatedData) -> {
                System.out.println(updatedData);
            });
            aisCSEvent.subscribe((AisCreateStationEvent) (BaseStation updatedData) -> {
            });
            aisUSEvent.subscribe((AisUpdateStationEvent) (BaseStation updatedData) -> {
                //  System.out.println(updatedData);
            });
            aisDSEvent.subscribe((AisDeleteStationEvent) (BaseStation updatedData) -> {
                System.out.println(updatedData);
            });
        }
    }

    @Override
    public void off() {
        if (on == true) {
            on = false;
            aisCTEvent.unsubscribe((AisCreateTargetEvent) (Ship updatedDate) -> {
                System.out.println(updatedDate);
            });
            aisUTEvent.unsubscribe((AisUpdateTargetEvent) (Ship updatedData) -> {
                System.out.println(updatedData);
            });
            aisDTEvent.unsubscribe((AisDeleteTargetEvent) (Ship updatedDate) -> {
                System.out.println(updatedDate);
            });

            aisCSEvent.unsubscribe((AisCreateStationEvent) (BaseStation updatedData) -> {
                System.out.println(updatedData);
            });
            aisUSEvent.unsubscribe((AisUpdateStationEvent) (BaseStation updatedData) -> {
                System.out.println(updatedData);
            });
            aisDSEvent.unsubscribe((AisDeleteStationEvent) (BaseStation updatedDate) -> {
                System.out.println(updatedDate);
            });
        }
    }

    @Override
    public void on() {
        this.on("");
    }

    @Override
    public InstrumentDriver getDriver() {
        return this;
    }

    @Override
    public boolean canOpen(String category) {
        return category.equals(NAME);
    }

    @Override
    public boolean isOn() {
        return on;
    }
}
