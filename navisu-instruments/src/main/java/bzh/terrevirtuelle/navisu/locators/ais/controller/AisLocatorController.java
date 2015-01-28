/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.locators.ais.controller;

import static bzh.terrevirtuelle.navisu.domain.ship.parameters.ShipType.TYPE;
import bzh.terrevirtuelle.navisu.locators.model.TShip;
import bzh.terrevirtuelle.navisu.widgets.Widget2DController;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Text;

/**
 *
 * @author Serge
 */
public class AisLocatorController
        extends Widget2DController
        implements Initializable {

    @FXML
    public Group ais;
    @FXML
    public Text shipname;
    @FXML
    public Text ageReport;
    @FXML
    public Text type;
    @FXML
    public Text AgeReport;
    @FXML
    public Text callSign;
    @FXML
    public Text mmsi;
    @FXML
    public Text imo;
    @FXML
    public Text length;
    @FXML
    public Text width;
    @FXML
    public Text draught;
    @FXML
    public Text status;
    @FXML
    public Text sog;
    @FXML
    public Text cog;
    @FXML
    public Text destination;
    @FXML
    public Text latitude;
    @FXML
    public Text longitude;
    @FXML
    public Text country;
    @FXML
    public Text eta;
  //  @FXML
  //  public Button photo;
    NumberFormat nf = new DecimalFormat("0.###");
    SimpleDateFormat dt = new SimpleDateFormat("hh:mm dd-MM");

    public AisLocatorController(KeyCode keyCode, KeyCombination.Modifier keyCombination) {
        super(keyCode, keyCombination);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ais.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
/*
        photo.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Platform.runLater(() -> {
                  //  WebView webView = new WebView("http://www.shipspotting.com/gallery/photo.php?lid=2137261");
                    // guiAgentServices.getRoot().getChildren().add(webView); 
                });
            }
        });
*/
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO    
    }

    public void updateAisPanel(TShip ship, Map<Integer, Calendar> timestamps, Map<Integer, String> midMap) {
        setVisible(true);
        if (ship.getName() != null) {
            shipname.setText(ship.getName());
            System.out.println(ship.getName());    
        } else {
            shipname.setText("Unknown");
            System.out.println("Unknown");
        }
        // Le type est publié tel quel, le dictionnaire n'est pas bien defini dans la spec AIS
        // A revoir
        if (ship.getType() != 0) {
            type.setText(TYPE.get(ship.getType()));
        } else {
            type.setText("-----");
        }
        if (ship.getCallSign() != null) {
            callSign.setText(ship.getCallSign());
        } else {
            callSign.setText("-----");
        }
        if (ship.getMmsi() != 0) {
            mmsi.setText(Integer.toString(ship.getMmsi()));
            long seconds = Calendar.getInstance().getTimeInMillis()
                    - timestamps.get(ship.getMmsi()).getTimeInMillis();
            ageReport.setText(Long.toString(seconds / 1000) + " s");
        } else {
            mmsi.setText("-----");
        }
        if (ship.getImo() != 0) {
            imo.setText(Integer.toString(ship.getImo()));
        } else {
            imo.setText("-----");
        }
        if (ship.getLength() != 0) {
            length.setText(Float.toString(ship.getLength()) + " m");
        } else {
            length.setText("-----");
        }
        if (ship.getWidth() != 0) {
            width.setText(Float.toString(ship.getWidth()) + " m");
        } else {
            width.setText("-----");
        }
        if (ship.getDraught() != 0) {
            draught.setText(Float.toString(ship.getDraught()) + " m");
        } else {
            draught.setText("-----");
        }
        if (ship.getNavigationalStatus() != 0) {
            status.setText(Integer.toString(ship.getNavigationalStatus()));
        } else {
            status.setText("-----");
        }
        if (ship.getSog() != 0) {
            sog.setText(nf.format(ship.getSog()) + " Kn");
        } else {
            sog.setText("-----");
        }
        if (ship.getCog() != 0 && ship.getCog() != 511) {
            cog.setText((int) ship.getCog() + " °");
        } else {
            cog.setText("-----");
        }
        if (ship.getDestination() != null) {
            destination.setText(ship.getDestination());
        } else {
            destination.setText("-----");
        }
        if (ship.getETA() != null) {
            eta.setText(dt.format(ship.getETA().getTime()));
        } else {
            eta.setText("-----");
        }
        if (ship.getLatitude() != 0) {
            latitude.setText(nf.format(ship.getLatitude()));
        } else {
            latitude.setText("-----");
        }
        if (ship.getLongitude() != 0) {
            longitude.setText(nf.format(ship.getLongitude()));
        } else {
            longitude.setText("-----");
        }
        if (ship.getMmsi() != 0) {
            String mmsiStr = Integer.toString(ship.getMmsi());
            String mid = mmsiStr.substring(0, 3);
            country.setText(midMap.get(new Integer(mid)));
        } else {
            country.setText("-----");
        }
    }
}
