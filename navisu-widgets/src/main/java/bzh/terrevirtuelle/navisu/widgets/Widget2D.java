/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.widgets;

/**
 *
 * @author Serge
 */
public interface Widget2D {

    void initEvt();

    void setScale(double scale);

    default void init() {

    }

    default void start() {

    }

    default void stop() {

    }
}
