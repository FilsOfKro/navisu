package bzh.terrevirtuelle.navisu.world.gps.locator.view;

import bzh.terrevirtuelle.navisu.app.guiagent.geoview.gobject.GObject;
import bzh.terrevirtuelle.navisu.geodesy.Location;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.ShapeAttributes;

import java.util.List;

/**
 * NaVisu
 *
 * @author tibus
 * @date 19/02/2014 18:49
 */
public class GShip implements GObject {

    protected final int id;

    protected Polygon polygon;
    protected Renderable[] renderables;

    public GShip(int id, Polygon polygon) {
        this.id = id;
        this.polygon = polygon;
    }

    public GShip(int id, List<Position> positions, Position location, double cog) {
        this.id = id;
        this.polygon = new Polygon(positions);
        this.polygon.setEnableBatchPicking(true);
        this.polygon.moveTo(location);
        this.setCog(cog);
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void setLocation(Location location) {
        this.polygon.moveTo(Position.fromDegrees(location.getLatitudeDegree(), location.getLongitudeDegree(), 100));
    }

    @Override
    public Renderable[] getRenderables() {
        return  new Renderable[] {
                this.polygon,
        };
    }

    public void setCog(double cog) {
        this.polygon.setRotation(cog - 90);
    }

    public void setPathAttrs(ShapeAttributes pathAttrs) {
        this.polygon.setAttributes(pathAttrs);
    }

    @Override
    public Object getClone() {

        GShip clone = new GShip(this.id, this.polygon);

        return clone;
    }
}
