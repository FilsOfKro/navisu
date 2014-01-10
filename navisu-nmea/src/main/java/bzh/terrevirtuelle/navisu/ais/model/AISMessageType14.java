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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Safety related Broadcast Message
 * 
 */
@XmlRootElement(name="AISMessageType14")
@XmlAccessorType(XmlAccessType.FIELD)
public class AISMessageType14 extends AISMessage {

    private String SecurityMessage;

    public AISMessageType14() {
    }

    /**
     * decodeFrame : decode AIS message of type 14
     *
     */
    @Override
    public void decodeFrame() {

        if (messageAisBinary.BinaryFrame.length() == 1008) {

            MMSI = messageAisBinary.binaryToInt(8, 38);
            SecurityMessage = messageAisBinary.binaryToString(40, 1008);
        }
    }

   
    @Override
    public String toString() {
        return new String("(MESSAGEAISTYPE14) ISMM=" + MMSI + ", MESS=" + SecurityMessage);
    }

    /**
     *
     * @return
     */
    public String getSecurityMessage() {
        return SecurityMessage;
    }

    /**
     *
     * @param SecurityMessage
     */
    public void setSecurityMessage(String SecurityMessage) {
        this.SecurityMessage = SecurityMessage;
    }

}
/** end AISMessageType14 */
