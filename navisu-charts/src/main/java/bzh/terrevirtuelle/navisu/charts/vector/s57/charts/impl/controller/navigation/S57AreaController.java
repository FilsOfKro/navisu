/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.charts.vector.s57.charts.impl.controller.navigation;

import gov.nasa.worldwind.render.SurfacePolygons;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Serge Morvan
 * @date 18 oct. 2014 NaVisu project
 */
public class S57AreaController implements EventHandler<KeyEvent> {

    private static final S57AreaController instance = new S57AreaController();
    private final List<SurfacePolygons> shapes;
    final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN);

    private S57AreaController() {
        shapes = new ArrayList<>();
    }

    public static S57AreaController getInstance() {
        return instance;
    }

    public void add(SurfacePolygons shape) {
        shapes.add(shape);
    }

    @Override
    public void handle(KeyEvent event) {
        if (keyComb1.match(event)) {
            System.out.println("Ctrl+A pressed");
        }
    }
}
