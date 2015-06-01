/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.instruments.common.view.panel;

import bzh.terrevirtuelle.navisu.widgets.impl.Widget2DController;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 *
 * @author Serge
 */
public class TrackPanel
        extends Widget2DController
        implements Initializable {

    @FXML
    public Group trackPanel;
    @FXML
    TextArea names;
    @FXML
    public Text timeStamp;
    @FXML
    public Text shipsInSight;
    @FXML
    public Text countOfAisShipsReceived;
    @FXML
    public ImageView quit;
    @FXML
    public Slider slider;

    NumberFormat nf = new DecimalFormat("0.###");
    SimpleDateFormat dt = new SimpleDateFormat("hh:mm dd-MM");

    public TrackPanel(KeyCode keyCode, KeyCombination.Modifier keyCombination) {
        super(keyCode, keyCombination);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("aisTracks.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        trackPanel.setOpacity(0.8);
        quit.setOnMouseClicked((MouseEvent event) -> {
            trackPanel.setVisible(false);
        });
        slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            Platform.runLater(() -> {
                trackPanel.setOpacity(slider.getValue());
            });
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO 

    }

    public void updateAisPanelShips(LocalTime time, int ships) {
        timeStamp.setText(time.toString());
        shipsInSight.setText(Integer.toString(ships));
    }
    
    public void updateAisPanelCount(LocalTime time, int ships, int count) {
        timeStamp.setText(time.toString());
        shipsInSight.setText(Integer.toString(ships));
        countOfAisShipsReceived.setText(Integer.toString(count));
    }
    
    public void updateAisPanelName(LocalTime time, int ships, int count, String name) {
        timeStamp.setText(time.toString());
        shipsInSight.setText(Integer.toString(ships));
        countOfAisShipsReceived.setText(Integer.toString(count));
        names.appendText(name+"\n");
    }
}
