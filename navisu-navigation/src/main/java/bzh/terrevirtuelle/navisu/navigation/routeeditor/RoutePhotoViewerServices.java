/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.navigation.routeeditor;

import bzh.terrevirtuelle.navisu.app.drivers.instrumentdriver.InstrumentDriver;
import javafx.scene.image.Image;
import org.capcaval.c3.component.ComponentService;

/**
 * NaVisu
 *
 * @date 26 août 2015
 * @author Serge Morvan
 */
public interface RoutePhotoViewerServices
        extends ComponentService {

    void on(String... files);

    boolean canOpen(String category);

    InstrumentDriver getDriver();

    void load(Image image);
}
