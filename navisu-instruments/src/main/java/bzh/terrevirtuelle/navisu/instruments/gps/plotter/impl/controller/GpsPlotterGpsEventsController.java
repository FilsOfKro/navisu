/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.instruments.gps.plotter.impl.controller;

import bzh.terrevirtuelle.navisu.domain.nmea.model.nmea183.GGA;
import bzh.terrevirtuelle.navisu.domain.nmea.model.nmea183.RMC;
import bzh.terrevirtuelle.navisu.domain.nmea.model.nmea183.VTG;
import bzh.terrevirtuelle.navisu.instruments.common.controller.GpsEventsController;

/**
 * NaVisu
 *
 * @date 19 juin 2015
 * @author Serge Morvan
 */
public class GpsPlotterGpsEventsController
        extends GpsEventsController {

    GpsPlotterController controller;

    public GpsPlotterGpsEventsController(GpsPlotterController controller) {
        this.controller = controller;
    }
   @Override
    public void notifyNmeaMessage(GGA data) {
        controller.notifyNmeaMessage(data);
    }

    @Override
    public void notifyNmeaMessage(VTG data) {
        controller.notifyNmeaMessage(data);
    }

    @Override
    public void notifyNmeaMessage(RMC data) {
        controller.notifyNmeaMessage(data);
    } 
}
