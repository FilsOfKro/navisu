/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.widgets.surveyZone.controller;

import bzh.terrevirtuelle.navisu.client.nmea.controller.events.AIS1Event;
import bzh.terrevirtuelle.navisu.client.nmea.controller.events.AIS2Event;
import bzh.terrevirtuelle.navisu.client.nmea.controller.events.AIS3Event;
import bzh.terrevirtuelle.navisu.client.nmea.controller.events.AIS4Event;
import bzh.terrevirtuelle.navisu.domain.nmea.model.AIS1;
import bzh.terrevirtuelle.navisu.domain.nmea.model.AIS2;
import bzh.terrevirtuelle.navisu.domain.nmea.model.AIS3;
import bzh.terrevirtuelle.navisu.domain.nmea.model.NMEA;
import bzh.terrevirtuelle.navisu.widgets.surveyZone.model.SurveyZone;
import bzh.terrevirtuelle.navisu.widgets.Widget;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import org.capcaval.c3.component.ComponentEventSubscribe;
import org.capcaval.c3.componentmanager.ComponentManager;

/**
 *
 * @author Serge Morvan
 * @date 7 oct. 2014 NaVisu project
 */
public class SurveyZoneController extends Widget
        implements Initializable {

    protected static final Logger LOGGER = Logger.getLogger(SurveyZoneController.class.getName());

    ComponentManager cm = ComponentManager.componentManager;
    ComponentEventSubscribe<AIS1Event> ais1ES = cm.getComponentEventSubscribe(AIS1Event.class);
    ComponentEventSubscribe<AIS2Event> ais2ES = cm.getComponentEventSubscribe(AIS2Event.class);
    ComponentEventSubscribe<AIS3Event> ais3ES = cm.getComponentEventSubscribe(AIS3Event.class);
    ComponentEventSubscribe<AIS4Event> ais4ES = cm.getComponentEventSubscribe(AIS4Event.class);

    private final List<SurveyZone> zones;
    @FXML
    public Text mmsi;
    @FXML
    public Text acronym;
    @FXML
    public Text objectname;

    public SurveyZoneController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HUD_MMSI.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        zones = Collections.synchronizedList(new ArrayList());
        subscribe();
    }

    private void subscribe() {
        ais1ES.subscribe(new AIS1Event() {

            @Override
            public <T extends NMEA> void notifyNmeaMessageChanged(T d) {
                AIS1 data = (AIS1) d;
                contains(data.getMMSI(), data.getLatitude(), data.getLongitude());
            }
        });

        ais2ES.subscribe(new AIS2Event() {

            @Override
            public <T extends NMEA> void notifyNmeaMessageChanged(T d) {
                AIS2 data = (AIS2) d;
                contains(data.getMMSI(), data.getLatitude(), data.getLongitude());
            }
        });

        ais3ES.subscribe(new AIS3Event() {

            @Override
            public <T extends NMEA> void notifyNmeaMessageChanged(T d) {
                AIS3 data = (AIS3) d;
                contains(data.getMMSI(), data.getLatitude(), data.getLongitude());
            }
        });
    }

    public void add(SurveyZone surveyZone) {
        zones.add(surveyZone);
    }

    private void contains(Integer _mmsi, double lat, double lon) {

        Platform.runLater(() -> {
            zones.stream().filter((s) -> (s.contains(lat, lon))).forEach((_item) -> {
                SurveyZoneController.this.mmsi.setText(_mmsi.toString());
                SurveyZoneController.this.acronym.setText(_item.getAcronym());
                if (_item.getObjname() != null) {
                    SurveyZoneController.this.objectname.setText(_item.getObjname());
                } else {
                    SurveyZoneController.this.objectname.setText("no name");
                }
            });
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
