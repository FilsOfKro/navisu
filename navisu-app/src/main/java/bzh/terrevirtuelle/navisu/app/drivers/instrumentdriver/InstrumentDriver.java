package bzh.terrevirtuelle.navisu.app.drivers.instrumentdriver;

/**
 * NaVisu
 *
 * @date 28 mars 2015
 * @author Serge Morvan
 */
public interface InstrumentDriver {

    default boolean canOpen(String category) {
        return false;
    }

    void on(String ... files);

    void off();
    
    default void newSector() {return;};
    
    default void drawerOn() {return;};
    
    default void savePolygon() {return;};
    
    default void saveAllPolygons() {return;};
    
    default void loadPolygons() {return;};
    
    default void polyShapeOn() {return;};
    
    default void ellipseShapeOn() {return;};
    
    default void circleShapeOn() {return;};
    
    default void quadShapeOn() {return;};
    
    default void freeHandOn() {return;};
    
    default void createCpaZone500() {return;};
    
    default void createCpaZone1000() {return;};
    
    default void createCpaZone() {return;};
    
    default void activateCpaZone() {return;};
    
    default void createPath() {return;};
    
    default void activatePath() {return;};
    
    default void savePath() {return;};
    
    default void loadPath() {return;};
}
