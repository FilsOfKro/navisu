/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.locators.view.impl;

import bzh.terrevirtuelle.navisu.geodesy.Location;
import bzh.terrevirtuelle.navisu.locators.gps.GpsLocator;
import bzh.terrevirtuelle.navisu.locators.model.TShip;
import bzh.terrevirtuelle.navisu.locators.view.Shape;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.ogc.collada.ColladaRoot;
import gov.nasa.worldwind.ogc.collada.impl.ColladaController;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.ShapeAttributes;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author Serge
 */
public class Shape3D_0
        implements Shape {

    TShip tShip;
    ColladaRoot colladaRoot = null;
    ColladaController colladaController = null;
    String fileName;//"data/collada/lithops.dae"

    public Shape3D_0(TShip tShip, String fileName, LatLon latlon, double d) {
        this.tShip = tShip;
        this.fileName = fileName;
        try {
            colladaRoot = ColladaRoot.createAndParse(new File(fileName));
            colladaRoot.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            colladaRoot.setHeading(Angle.fromDegrees(30.0));
           // colladaRoot.setPosition(Position.fromDegrees(48.3649, -4.490, 0));
           // latlon.
            colladaRoot.setPosition(new Position(latlon.getLatitude(), latlon.getLongitude(),0));
            colladaRoot.setPitch(Angle.fromDegrees(0.0));
            colladaRoot.setRoll(Angle.fromDegrees(0.0));
            colladaRoot.setModelScale(new Vec4(20.0));
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(GpsLocator.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Create a KMLController to adapt the KMLRoot to the World Wind renderable interface.
        colladaController = new ColladaController(colladaRoot);
    }

    @Override
    public void setLocation(Location location) {

    }

    @Override
    public Renderable[] getRenderables() {
        return new Renderable[]{colladaController};
    }

    @Override
    public void setRotation(double cog) {
        // super.setRotation(-cog);
    }

    @Override
    public String toString() {
        return "Shape_0{" + super.toString() + '}';
    }

    @Override
    public TShip getShip() {
        return tShip;
    }

    public ShapeAttributes getAttributes() {
        return null;
    }
}
