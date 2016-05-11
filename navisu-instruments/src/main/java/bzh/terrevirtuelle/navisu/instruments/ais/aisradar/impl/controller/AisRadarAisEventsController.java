/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.instruments.ais.aisradar.impl.controller;

import bzh.terrevirtuelle.navisu.domain.devices.model.BaseStation;
import bzh.terrevirtuelle.navisu.domain.ship.model.Ship;
import bzh.terrevirtuelle.navisu.instruments.common.controller.AisEventsController;

/**
 * NaVisu
 *
 * @date 18 juin 2015
 * @author Serge Morvan
 */
public class AisRadarAisEventsController
        extends AisEventsController {

    AisRadarController controller;

    public AisRadarAisEventsController(AisRadarController aisRadarController) {
        this.controller = aisRadarController;
    }

    @Override
    public void createTarget(Ship ship) {
        controller.createTarget(ship);
    }

    @Override
    public void updateTarget(Ship ship) {
        controller.updateTarget(ship);
    }

    @Override
    public void deleteTarget(Ship ship) {
    }

    @Override
    public void createBaseStation(BaseStation baseStation) {
    }

    @Override
    public void updateBaseStation(BaseStation baseStation) {
     }

    @Override
    public void deleteBaseStation(BaseStation baseStation) {
    }

}
