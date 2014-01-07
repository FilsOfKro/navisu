/******************************************************************************
 * (c) Copyright 2007, IRENav. All rights reserved.
 * Property of ECOLE NAVALE
 *
 * For Unrestricted Internal Use Only
 * Unauthorized reproduction and/or distribution is strictly prohibited.
 * This product is protected under copyright law and trade secret law as an
 * unpublished Work.
 *
 * Modified in 05/2007.
 *
 * Original Designers : RAY
 *
 ******************************************************************************/
package bzh.terrevirtuelle.navisu.ais.model;

//import objects.gps.WGS84Location;
/**
 * Standard SAR Aircraft position report
 * 
 */
public class AISMessageType9 extends AISMessage {

    private float cog;
    private int altitude; // TODO: check conversion to float
    private float speed;
    private float latitude, longitude;

    /**
     * decodeFrame : decode AIS message of type 9
     *
     */
    @Override
    public void decodeFrame() {
        if (messageAisBinary.BinaryFrame.length() == 167) {

            MMSI = messageAisBinary.binaryToInt(8, 38);
            altitude = messageAisBinary.binaryToInt(38, 50);
            speed = (float) (0.1 * messageAisBinary.binaryToInt(50, 60));
            cog = (float) (0.1 * messageAisBinary.binaryToInt(116, 128));
            longitude = ((float) (0.0001 * messageAisBinary.complementToInt(61, 89))) / 60;
            latitude = ((float) (0.0001 * messageAisBinary.complementToInt(89, 116))) / 60;
        }
    }

    /**
     * displayFrame : print AIS message of type 9
     *
     */
    @Override
    public String toString() {
        return ("(MESSAGEAISTYPE9) ISMM=" + MMSI + ", ALT=" + altitude + ", COG=" + cog + ", SPEED=" + speed + ", LAT=" + latitude + ", LONG=" + longitude);
    }

    /**
     *
     * @return
     */
    public int getAltitude() {
        return altitude;
    }

    /**
     *
     * @param altitude
     */
    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    /**
     *
     * @return
     */
    public float getCog() {
        return cog;
    }

    /**
     *
     * @param cog
     */
    public void setCog(float cog) {
        this.cog = cog;
    }

    /**
     *
     * @return
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     */
    public float getSpeed() {
        return speed;
    }

    /**
     *
     * @param speed
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

}
/** end AISMessageType9 */
