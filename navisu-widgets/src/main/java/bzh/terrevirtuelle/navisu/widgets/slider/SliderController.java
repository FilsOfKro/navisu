 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.widgets.slider;

import bzh.terrevirtuelle.navisu.widgets.impl.Widget2DController;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;

/**
 * @date 16 avril 2015
 * @author Serge Morvan
 */
public class SliderController
        extends Widget2DController {

    @FXML
    public Group sliderPanel;
    @FXML
    public Slider slider;
    @FXML
    public ImageView quit;

    public SliderController() {

        setMouseTransparent(false);
        load();
    }

    public SliderController(KeyCode keyCode, KeyCombination.Modifier keyCombination) {
        super(keyCode, keyCombination);
        setMouseTransparent(false);
        load();
    }

    private void load() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SliderPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        slider.getStylesheets().add(this.getClass().getResource("widgets.css").toExternalForm());
        quit.setOnMouseClicked((MouseEvent event) -> {
            setVisible(false);
        });
    }

    public ImageView getQuit() {
        return quit;
    }

}
