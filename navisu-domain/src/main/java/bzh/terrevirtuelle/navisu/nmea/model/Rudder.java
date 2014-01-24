/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.nmea.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Serge
 */
@XmlRootElement(name = "Rudder")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rudder
        extends N2K {

    private int rudderInstance;
    private int directionOrder;
    private int angleOrder;
    private int position;

    public Rudder() {
    }

    public Rudder(int pgn, String source) {
        super(pgn, source);
    }

    public Rudder(int rudderInstance, int directionOrder, int angleOrder, int position, int pgn, String source) {
        super(pgn, source);
        this.rudderInstance = rudderInstance;
        this.directionOrder = directionOrder;
        this.angleOrder = angleOrder;
        this.position = position;
    }

    /**
     * Get the value of position
     *
     * @return the value of position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Set the value of position
     *
     * @param position new value of position
     */
    public void setPosition(int position) {
        this.position = position;
    }


    /**
     * Get the value of angleOrder
     *
     * @return the value of angleOrder
     */
    public int getAngleOrder() {
        return angleOrder;
    }

    /**
     * Set the value of angleOrder
     *
     * @param angleOrder new value of angleOrder
     */
    public void setAngleOrder(int angleOrder) {
        this.angleOrder = angleOrder;
    }


    /**
     * Get the value of directionOrder
     *
     * @return the value of directionOrder
     */
    public int getDirectionOrder() {
        return directionOrder;
    }

    /**
     * Set the value of directionOrder
     *
     * @param directionOrder new value of directionOrder
     */
    public void setDirectionOrder(int directionOrder) {
        this.directionOrder = directionOrder;
    }


    /**
     * Get the value of rudderInstance
     *
     * @return the value of rudderInstance
     */
    public int getRudderInstance() {
        return rudderInstance;
    }

    /**
     * Set the value of rudderInstance
     *
     * @param rudderInstance new value of rudderInstance
     */
    public void setRudderInstance(int rudderInstance) {
        this.rudderInstance = rudderInstance;
    }

    @Override
    public String toString() {
        return "Rudder{" + "rudderInstance=" + rudderInstance + ", directionOrder=" + directionOrder + ", angleOrder=" + angleOrder + ", position=" + position + '}';
    }

}
