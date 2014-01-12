grammar NMEA;

@header{
package bzh.terrevirtuelle. navisu.nmea.controller.parser.impl;


    }
@lexer::header{
package bzh.terrevirtuelle.navisu.nmea.controller.parser.impl;

import bzh.terrevirtuelle.navisu.nmea.model.NMEA;

import bzh.terrevirtuelle.navisu.nmea.model.AAM;
import bzh.terrevirtuelle.navisu.nmea.model.APB;
import bzh.terrevirtuelle.navisu.nmea.model.BEC;
import bzh.terrevirtuelle.navisu.nmea.model.BOD;
import bzh.terrevirtuelle.navisu.nmea.model.BWC;
import bzh.terrevirtuelle.navisu.nmea.model.BWR;
import bzh.terrevirtuelle.navisu.nmea.model.BWW;
import bzh.terrevirtuelle.navisu.nmea.model.DBT;
import bzh.terrevirtuelle.navisu.nmea.model.DBK;
import bzh.terrevirtuelle.navisu.nmea.model.DBS;
import bzh.terrevirtuelle.navisu.nmea.model.DPT;
import bzh.terrevirtuelle.navisu.nmea.model.GGA;
import bzh.terrevirtuelle.navisu.nmea.model.GLL;
import bzh.terrevirtuelle.navisu.nmea.model.GSA;
import bzh.terrevirtuelle.navisu.nmea.model.GSV;
import bzh.terrevirtuelle.navisu.nmea.model.HDG;
import bzh.terrevirtuelle.navisu.nmea.model.HDM;
import bzh.terrevirtuelle.navisu.nmea.model.HDT;
import bzh.terrevirtuelle.navisu.nmea.model.MTA;
import bzh.terrevirtuelle.navisu.nmea.model.MTW;
import bzh.terrevirtuelle.navisu.nmea.model.MWD;
import bzh.terrevirtuelle.navisu.nmea.model.MWV;
import bzh.terrevirtuelle.navisu.nmea.model.MSK;
import bzh.terrevirtuelle.navisu.nmea.model.RMB;
import bzh.terrevirtuelle.navisu.nmea.model.RMC;
import bzh.terrevirtuelle.navisu.nmea.model.RSD;
import bzh.terrevirtuelle.navisu.nmea.model.RTE;
import bzh.terrevirtuelle.navisu.nmea.model.VBW;
import bzh.terrevirtuelle.navisu.nmea.model.VHW;
import bzh.terrevirtuelle.navisu.nmea.model.VLW;
import bzh.terrevirtuelle.navisu.nmea.model.VPW;
import bzh.terrevirtuelle.navisu.nmea.model.VTG;
import bzh.terrevirtuelle.navisu.nmea.model.VWR;
import bzh.terrevirtuelle.navisu.nmea.model.VWT;
import bzh.terrevirtuelle.navisu.nmea.model.XTE;
import bzh.terrevirtuelle.navisu.nmea.model.ZDA;

import bzh.terrevirtuelle.navisu.nmea.controller.parser.handler.Handler;  
import bzh.terrevirtuelle.navisu.nmea.controller.parser.handler.impl.PrintHandler; 
import bzh.terrevirtuelle.navisu.ais.controller.parser.impl.AISParser;
 
import bzh.terrevirtuelle.navisu.nmea.model.GPSSatellite;

 
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
    }
    
@members{ 

}
@lexer::members{
   protected NMEA nmea = null;
   
   protected AAM aam = null;
   protected APB apb = null;
   protected BEC bec = null;
   protected BOD bod = null;
   protected BWC bwc = null;
   protected BWR bwr = null;
   protected BWW bww = null;
   protected DBS dbs = null;
   protected DBT dbt = null;
   protected DBK dbk = null;
   protected DPT dpt = null;
   protected GGA gga = null;
   protected GLL gll = null;
   protected GSA gsa = null;
   protected GSV gsv = null;
   protected HDG hdg = null;
   protected HDM hdm = null;
   protected HDT hdt = null;
   protected MTA mta = null;
   protected MTW mtw = null;
   protected MWD mwd = null;
   protected MWV mwv = null;
   protected MSK msk = null;
   protected RMB rmb = null;
   protected RMC rmc = null;
   protected RSD rsd = null;
   protected RTE rte = null;
   protected VBW vbw = null;
   protected VLW vlw = null;
   protected VHW vhw = null;
   protected VPW vpw = null;
   protected VTG vtg = null;
   protected VWR vwr = null;
   protected VWT vwt = null;
   protected XTE xte = null;
   protected ZDA zda = null;
   
      
   protected int hours;
   protected int minutes;
   protected int seconds;
   protected int hours2;
   protected int minutes2;
   protected int seconds2;
   protected float lat;
   protected float lon;
   protected float deviation;
   protected float variation;
   protected float sog;
   protected String uoaa;
   protected float ga;
   protected String uoga;
   protected String northOrSouth;
   protected String eastOrWest;
   protected float bdt;
   protected float bdm;
   protected float dtwp;
   protected String uodtwp;
   protected String unitsOfDistanceToWayPoint;
   protected String wid;
   protected String sId; 
   protected Calendar date;  
   protected GPSSatellite s1;
   protected GPSSatellite s2;
   protected GPSSatellite s3;
   protected GPSSatellite s4;
   
   /* Default handlers */
   protected Handler handler = new PrintHandler();
   protected Handler aisHandler = new PrintHandler();
   protected AISParser aisParser = new AISParser(aisHandler);
   
   
   public void setHandler(Handler handler){
   this.handler = handler;
   }
   
   public void setAISHandler(Handler handler){
   this.aisHandler = handler;
   aisParser.setHandler(handler);
   }
   
   private float latConvert(float latitude, String ns){
        int latitudeInt = (int) latitude / 100;
        float latitudeMin = latitude - (latitudeInt * 100);
        float latDeg = latitudeInt + (latitudeMin / 60);
        if (ns.contains("S")){
                   latDeg *= -1;
        } 
        return latDeg;
   }
   private float lonConvert(float longitude, String ew){
        int longitudeInt = (int) longitude / 100;
        float longitudeMin = longitude - (longitudeInt * 100);
        float lonDeg = longitudeInt + (longitudeMin / 60);   
        if (ew.contains("W")){
            lonDeg *= -1;
        }
        return lonDeg;
   }	
   private float ewConvert(float var, String ew){
           if (ew.contains("W")){
            var *= -1;
        }
        return var;
   }
   private Calendar dateFactory(String utc){
     int hours = new Integer(utc.substring(0,2)).intValue();
     int minutes = new Integer(utc.substring(2,4)).intValue();
     int seconds = new Integer(utc.substring(4,6)).intValue();
     Calendar  date = new GregorianCalendar();
     date.set(Calendar.HOUR_OF_DAY, hours);
     date.set(Calendar.MINUTE, minutes);
     date.set(Calendar.SECOND, seconds);
     return date;
   }
   private Calendar dateFactory(String yymmdd, String utc){
     
     Calendar  date = dateFactory(utc);
     if(yymmdd.length() != 0){
       int dd = new Integer(yymmdd.substring(0,2)).intValue();
       int mm = new Integer(yymmdd.substring(2,4)).intValue();
       int yy = new Integer(yymmdd.substring(4,6)).intValue();
       date.set(Calendar.YEAR, yy);
       date.set(Calendar.MONTH, mm-1);
       date.set(Calendar.DAY_OF_MONTH, dd);
     }
     return date;
   }
}		
entry 	:    (AAM|APB|BEC|BOD|BWC|BWR|DBS|DBT|DBK|DPT|GGA|GLL|GSA|GSV|HDG|HDM|HDT|
		MSK|MTA|MTW|MWD|MWV|RMB|RMC|RTE|VBW|VLW|VHW|VPW|VTG|VWR|VWT|XTE|ZDA
		//Radar
		|RSD
		//AIS
		|VDM|TXT|ALR
		//PRO
		|PRO)+ ;

AAM	 : 	'$' device=DEVICE 'AAM' SEP
	    (arrivalCircleEntered = LETTERS)* SEP
            (perpendicularPassed = LETTERS)* SEP
            (circleRadius = NUMBER)* SEP
            (LETTERS)* SEP
            (wayPointID = LETTERS | wayPointID = NUMBER)*
            checksum=CHECKSUM
	{
	aam = new AAM(device.getText(), getText(),
	arrivalCircleEntered != null ? arrivalCircleEntered.getText() : "", 
	perpendicularPassed != null ? perpendicularPassed.getText() : "",
	circleRadius != null ?  new Float(circleRadius.getText()).floatValue(): 0.0f,
	wayPointID != null ? wayPointID.getText() : "");
	
	handler.doIt(aam);
	}
	;
/*
  public APB(String device, String sentence, 
            String status0, String status1, 
            float crossTrackErrorMagnitude, String directionToSteer, String crossTrackUnits, 
            String status2, String status3, 
            int bearingOriginToDestination, String bearingOriginToDestinationType, 
            String destinationWaypointID,
            int bearingPresentPositionToDestination, String bearingPresentPositionToDestinationType, 
            int headingToSteerToDestination, String headingToSteerToDestinationType)
            */
APB	:	'$' device=DEVICE 'APB' SEP
              (status0 = LETTERS)* SEP
              (status1 = LETTERS)* SEP
              (crossTrackErrorMagnitude = NUMBER)* SEP
              (directionToSteer = LETTERS)* SEP
              (crossTrackUnits = LETTERS)* SEP
              (status2 = LETTERS)* SEP
              (status3 = LETTERS)* SEP
              (bearingOriginToDestination = NUMBER)* SEP
              (bearingOriginToDestinationType=LETTERS)* SEP
              (destinationWaypointID = LETTERS | destinationWaypointID = NUMBER)* SEP
              (bearingPresentPositionToDestination = NUMBER)* SEP
              (bearingPresentPositionToDestinationType=LETTERS)* SEP
              (headingToSteerToDestination = NUMBER)* SEP
              (headingToSteerToDestinationType = LETTERS)*
              checksum=CHECKSUM
	{
	apb = new APB(device.getText(), getText(),
	status0 != null ? status0.getText() : "",
	status1 != null ? status1.getText() : "",
	crossTrackErrorMagnitude != null ? new Float(crossTrackErrorMagnitude.getText()).floatValue(): 0.0f,
	directionToSteer != null ? directionToSteer.getText() : "",
	crossTrackUnits != null ? crossTrackUnits.getText() : "",
	status2 != null ? status2.getText() : "",
	status3 != null ? status3.getText() : "",
	bearingOriginToDestination != null ? (new Integer(bearingOriginToDestination.getText())).intValue() : 0,
	bearingOriginToDestinationType != null ? bearingOriginToDestinationType.getText() : "",
	destinationWaypointID != null ? destinationWaypointID.getText() : "",
	bearingPresentPositionToDestination != null ? (new Integer(bearingPresentPositionToDestination.getText())).intValue() : 0,
	bearingPresentPositionToDestinationType != null ? bearingPresentPositionToDestinationType.getText() : "",
	headingToSteerToDestination != null ? (new Integer(headingToSteerToDestination.getText())).intValue() : 0,
	headingToSteerToDestinationType != null ? headingToSteerToDestinationType.getText() : "");
	
	handler.doIt(apb);
	}
	;
/**
 *BEC Bearing & Distance to Waypoint � Dead Reckoning */
 BEC 	:	'$' device=DEVICE 'BEC' SEP
  		  ' '*
             utc=NUMBER SEP+
            (latitude=NUMBER SEP ns = LETTERS SEP longitude=NUMBER SEP ew=LETTERS SEP bearingDegreesTrue=NUMBER SEP)*
             LETTERS SEP+
            (bearingDegreesMagnetic=NUMBER SEP)*
             LETTERS SEP+ 
            (distanceToWayPoint=NUMBER SEP)*
             unitsOfDistanceToWayPoint=LETTERS SEP+
            (waypointID=LETTERS | waypointID=NUMBER)* 
            checksum=CHECKSUM
        {
        date = dateFactory(utc.getText()); 
        
        lat = latConvert(latitude != null ? (new Float(latitude.getText())).floatValue() : 0.0f, ns != null ? ns.getText() : "");
        lon = lonConvert(longitude != null ? (new Float(longitude.getText())).floatValue() : 0.0f, ew != null ? ew.getText() : "");
            
        bdt =  bearingDegreesTrue != null ? new Float(bearingDegreesTrue.getText()).floatValue() : 0.0f;
        bdm = bearingDegreesMagnetic != null ?  new Float(bearingDegreesMagnetic.getText()).floatValue() : 0.0f;
        dtwp = distanceToWayPoint != null ? new Float(distanceToWayPoint.getText()).floatValue() : 0.0f;
        wid= waypointID != null ? waypointID.getText() : "";
        uodtwp = unitsOfDistanceToWayPoint.getText() ;
        bec = new BEC(device.getText(), getText(), 
        date, 
        lat, lon,
        bdt, bdm, 
        dtwp, uodtwp, 
        wid);
        
        handler.doIt(bwc);
       }
 		;
/**Bearing Origin to Destination */
 BOD 	:	'$' device=DEVICE 'BOD' SEP
  		 (bearingDegreesTrue=NUMBER)* SEP+
  		 LETTERS SEP
  		 (bearingDegreesMagnetic=NUMBER)* SEP+
  		 LETTERS SEP
  		 (destinationWaypointID=LETTERS | destinationWaypointID=NUMBER)* SEP 
  		 (originWaypointID=LETTERS | originWaypointID=NUMBER)*
  		 checksum=CHECKSUM
 		{
 		bod = new BOD(device.getText(), getText(),
 		bearingDegreesTrue != null ? new Float(bearingDegreesTrue.getText()).floatValue() : 0.0f,
 		bearingDegreesMagnetic != null ? new Float(bearingDegreesMagnetic.getText()).floatValue() : 0.0f,
 		destinationWaypointID != null ? destinationWaypointID.getText() : "",
 		originWaypointID != null ? originWaypointID.getText() : "");
 
 		 handler.doIt(bod);
 }
 ;
/** Bearing and distance to waypoint - great circle*/
BWC 	:  '$' device=DEVICE 'BWC' SEP
             ' '*
             utc=NUMBER SEP+
            (latitude=NUMBER SEP ns = LETTERS SEP longitude=NUMBER SEP ew=LETTERS SEP bearingDegreesTrue=NUMBER SEP)*
             LETTERS SEP+
            (bearingDegreesMagnetic=NUMBER SEP)*
             LETTERS SEP+ 
            (distanceToWayPoint=NUMBER SEP)*
             unitsOfDistanceToWayPoint=LETTERS SEP+
            (waypointID=LETTERS | waypointID=NUMBER)* 
            checksum=CHECKSUM
        {
        date = dateFactory(utc.getText()); 
        
        lat = latConvert(latitude != null ? (new Float(latitude.getText())).floatValue() : 0.0f, ns != null ? ns.getText() : "");
        lon = lonConvert(longitude != null ? (new Float(longitude.getText())).floatValue() : 0.0f, ew != null ? ew.getText() : "");
            
        bdt =  bearingDegreesTrue != null ? new Float(bearingDegreesTrue.getText()).floatValue() : 0.0f;
        bdm = bearingDegreesMagnetic != null ?  new Float(bearingDegreesMagnetic.getText()).floatValue() : 0.0f;
        dtwp = distanceToWayPoint != null ? new Float(distanceToWayPoint.getText()).floatValue() : 0.0f;
        wid= waypointID != null ? waypointID.getText() : "";
        uodtwp = unitsOfDistanceToWayPoint.getText() ;
        bwc = new BWC(device.getText(), getText(), 
        date, 
        lat, lon, 
        bdt, bdm, 
        dtwp, uodtwp, 
        wid);
        
        handler.doIt(bwc);
       }
       ;
       
/** Bearing and Distance to Waypoint � Rhumb Line */
BWR 	:   '$' device=DEVICE 'BWR' SEP	
 	    ' '*
             utc=NUMBER SEP+
            (latitude=NUMBER SEP ns = LETTERS SEP longitude=NUMBER SEP ew=LETTERS SEP bearingDegreesTrue=NUMBER SEP)*
             LETTERS SEP+
            (bearingDegreesMagnetic=NUMBER SEP)*
             LETTERS SEP+ 
            (distanceToWayPoint=NUMBER SEP)*
             unitsOfDistanceToWayPoint=LETTERS SEP+
            (waypointID=LETTERS | waypointID=NUMBER)* 
            checksum=CHECKSUM
        {
        date = dateFactory(utc.getText()); 
        
        lat = latConvert(latitude != null ? (new Float(latitude.getText())).floatValue() : 0.0f, ns != null ? ns.getText() : "");
        lon = lonConvert(longitude != null ? (new Float(longitude.getText())).floatValue() : 0.0f, ew != null ? ew.getText() : "");
            
        bdt =  bearingDegreesTrue != null ? new Float(bearingDegreesTrue.getText()).floatValue() : 0.0f;
        bdm = bearingDegreesMagnetic != null ?  new Float(bearingDegreesMagnetic.getText()).floatValue() : 0.0f;
        dtwp = distanceToWayPoint != null ? new Float(distanceToWayPoint.getText()).floatValue() : 0.0f;
        wid= waypointID != null ? waypointID.getText() : "";
        uodtwp = unitsOfDistanceToWayPoint.getText() ;
        bwr = new BWR(device.getText(), getText(),
        date, 
        lat, lon, 
        bdt, bdm, 
        dtwp, uodtwp, 
        wid);
        
        handler.doIt(bwr);
       }
       ;

/** BWW Bearing � Waypoint to Waypoint
       1   2 3   4 5    6   7
       |   | |   | |    |   |
$--BWW,x.x,T,x.x,M,c--c,c--c*hh
1) Bearing Degrees, TRUE
2) T = True
3) Bearing Degrees, Magnetic
4) M = Magnetic
5) TO Waypoint
6) FROM Waypoint
7) Checksum
*/    
BWW 	:	'$' device=DEVICE 'BWW' SEP 
	  bearingDegreesTrue = NUMBER SEP
	  LETTERS SEP
	  bearingDegreesMagnetic = NUMBER SEP
	  LETTERS SEP
	  (toWaypointID=LETTERS | toWaypointID=NUMBER) SEP
	  (fromWaypointID=LETTERS | fromWaypointID=NUMBER) SEP
	  checksum=CHECKSUM
	{
	bww = new BWW(device.getText(), getText(),
	bearingDegreesTrue != null ? (new Float(bearingDegreesTrue.getText())).floatValue() : 0.0f,
	bearingDegreesMagnetic != null ? (new Float(bearingDegreesMagnetic.getText())).floatValue() : 0.0f,
	toWaypointID != null ? toWaypointID.getText() : "",
	fromWaypointID != null ? fromWaypointID.getText() : "");
	
	
	handler.doIt(bww);
	}
	;   
/** Depth below Transducer Depth value expressed in feet, metres and fathoms.*/
DBT	:	'$' device=DEVICE 'DBT' SEP
	(SEP | (depthInFeet=NUMBER SEP LETTERS SEP))
	(SEP | (depthInMeters=NUMBER SEP LETTERS SEP))
	(SEP | (depthInFathoms=NUMBER SEP LETTERS))
	 checksum=CHECKSUM*
	{
	dbt = new DBT(device.getText(), getText(),
	depthInFeet != null ? (new Float(depthInFeet.getText())).floatValue() : 0.0f,
	depthInMeters !=null ? (new Float(depthInMeters.getText())).floatValue() : 0.0f,
	depthInFathoms !=null ? (new Float(depthInFathoms.getText())).floatValue() : 0.0f);
	
	handler.doIt(dbt);
	}
	; 
	
/** Depth Below Keel value expressed in feet, metres and fathoms. */
DBK	:'$' device=DEVICE 'DBK' SEP
	(SEP | (depthInFeet=NUMBER SEP LETTERS SEP))
	(SEP | (depthInMeters=NUMBER SEP LETTERS SEP))
	(SEP | (depthInFathoms=NUMBER SEP LETTERS))
	 checksum=CHECKSUM*
	{
	dbk = new DBK(device.getText(), getText(),
	depthInFeet != null ? (new Float(depthInFeet.getText())).floatValue() : 0.0f,
	depthInMeters !=null ? (new Float(depthInMeters.getText())).floatValue() : 0.0f,
	depthInFathoms !=null ? (new Float(depthInFathoms.getText())).floatValue() : 0.0f);
	
	handler.doIt(dbk);
	}
	; 
	
/** Depth Below Surface value expressed in feet, metres and fathoms. */
DBS	:'$' device=DEVICE 'DBS' SEP
	(SEP | (depthInFeet=NUMBER SEP LETTERS SEP))
	(SEP | (depthInMeters=NUMBER SEP LETTERS SEP))
	(SEP | (depthInFathoms=NUMBER SEP LETTERS))
	 checksum=CHECKSUM*
	{
	dbs = new DBS(device.getText(), getText(),
	depthInFeet != null ? (new Float(depthInFeet.getText())).floatValue() : 0.0f,
	depthInMeters !=null ? (new Float(depthInMeters.getText())).floatValue() : 0.0f,
	depthInFathoms !=null ? (new Float(depthInFathoms.getText())).floatValue() : 0.0f);
	
	handler.doIt(dbs);
	}
	;  
	
/** Depth and offset */ 
DPT	:'$' device=DEVICE 'DPT' SEP
	depth=NUMBER SEP
        (offset=NUMBER SEP | offset=NUMBER )
         checksum=CHECKSUM*
	{
	dpt = new DPT(device.getText(), getText(),
	depth != null ? (new Float(depth.getText())).floatValue() : 0.0f,
	offset != null ? (new Float(offset.getText())).floatValue() : 0.0f);
	
	handler.doIt(dpt);
	}	
	;
	/** $GPGGA,234112,3926.5031,N,11946.1982,W,2,12,0.8,1380.2,M,-22.0,M,,*4A 
	    $GPGGA,094550,4729.2477,N,01904.7650,E,6,00,,112.2,M,41.0,M,,*65
	    $GPGGA,094551,4729.2477,N,01904.7650,E,0,00,,,M,,M,,*57
	    $GPGGA,161229.487,3723.2475,N,12158.3416,W,  1,07,1.0,9.0,M,,,,0000*18 
	    $GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47
	    $GPGGA,131550,6003.9425,N,01940.2853,E,1,10,0.9,-1.6,M,21.7,M,,*68
	    $ECGGA,194330,3748.824,N,12226.873,W,1,06,2.0,-17.0,M,0.0,M,0.0,*43
	    $ECGGA,194310,3748.788,N,12226.836,W,1,06,2.0,-18.0,M,0.0,M,0.0,*4
	    $GPGGA,,,,,,0,00,,,M,,M,,*66
	    $GPGGA,,,,,,0,00,,,M,,M,,*66
	    */
GGA :	     '$' device=DEVICE 'GGA' SEP
             utc=NUMBER* SEP
             latitude=NUMBER* SEP
             ns=LETTERS* SEP
             longitude=NUMBER* SEP
             ew=LETTERS* SEP
             
             ' '*
             gpsQualityIndicator=NUMBER* SEP
             numberOfSatellitesInView=NUMBER* SEP
             horizontalDilutionOfPrecision=NUMBER* SEP
             
             SIGN*
             (antennaAltitude=NUMBER)* SEP
             unitsOfAntennaAltitude=LETTERS SEP
             SIGN*
             (geoidAltitude= NUMBER)* SEP 
             (unitsOfGeoidAltitude=LETTERS)* SEP+
             
             ( NUMBER SEP)*          
             ( LETTERS | NUMBER )* 

             checksum=CHECKSUM
      {
      if(utc != null){
        date = dateFactory(utc.getText());
       }else {
       date = dateFactory("000000");
       }
        uoaa = unitsOfAntennaAltitude !=null ? unitsOfAntennaAltitude.getText() :  " ";
        ga = geoidAltitude != null ? (new Float(geoidAltitude.getText())).floatValue() : 0.0f;
        uoga = unitsOfGeoidAltitude != null ? unitsOfGeoidAltitude.getText() : " ";
        if(latitude != null){
        lat = latConvert((new Float(latitude.getText())).floatValue(), ns.getText());
           }else {
             lat = 0.0f;
           }
        if(longitude != null){
            lon = lonConvert((new Float(longitude.getText())).floatValue(), ew.getText());
        } else {
        lon = 0.0f;
        }
        gga = new GGA(device.getText(), getText(),
        		     date,
                             lat, 
                             lon,
                             gpsQualityIndicator != null ? (new Integer(gpsQualityIndicator.getText())).intValue() : 0,
                             numberOfSatellitesInView != null ? (new Integer(numberOfSatellitesInView.getText())).intValue() : 0,
                             horizontalDilutionOfPrecision != null ? (new Float(horizontalDilutionOfPrecision.getText())).floatValue() : 0.0f,
                             antennaAltitude != null ? (new Float(antennaAltitude.getText())).floatValue() : 0.0f,
                             uoaa,
                             ga,
                             uoga);
             
       handler.doIt(gga);
      }
      ;
 /** Geographic position, latitude and longitude. 
 $GPGLL,4916.45,N,12311.12,W,225444,A,*31
 $GPGLL,3348.794,N,11754.064,W,060002,A*3E
 $IIGLL,3748.4051,N,12226.618,W,,A*1D
 $GPGLL,,,,,,V,N*64
 */     
GLL	:	'$' device=DEVICE 'GLL' SEP
 		latitude=NUMBER* SEP
             	ns=LETTERS* SEP
             	longitude=NUMBER* SEP
             	ew=LETTERS* SEP
             	utc=NUMBER* SEP
             	status=LETTERS SEP*
             	LETTERS* 
             	checksum=CHECKSUM
	{
	//String origin, float latitude, float longitude, Calendar utc, String status
	 if(utc != null)
	   date = dateFactory(utc.getText());
	 else
	   date = dateFactory("000000");
	 lat = latConvert(latitude != null ? (new Float(latitude.getText())).floatValue() : 0.0f, ns != null ? ns.getText() : "");
         lon = lonConvert(longitude != null ? (new Float(longitude.getText())).floatValue() : 0.0f, ew != null ? ew.getText() : "");
	 gll = new GLL(device.getText(), getText(),
	 lat, lon, 
	 date, 
	 status.getText());
	 
	 handler.doIt(gll); 
	}
;
 /**  GSA      Satellite status 
 $GPGSA,A,1,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,   ,   ,*1E
 $GPGSA,A,3,04,07,08,09,11,24,26,27,28,29,35,47,1.5,0.8,1.2*3C
 */              
GSA 	:    '$' device=DEVICE 'GSA' SEP       
	     autoOrManualSelection = LETTERS SEP
             ' '* dimensionFix = NUMBER SEP
             (' '* PRNOfSatellitesUsed1 = NUMBER)* SEP
             (' '* PRNOfSatellitesUsed2 = NUMBER)* SEP
             (' '* PRNOfSatellitesUsed3 = NUMBER)* SEP
             (' '* PRNOfSatellitesUsed4 = NUMBER)* SEP
             (' '* PRNOfSatellitesUsed5 = NUMBER)* SEP
             (' '* PRNOfSatellitesUsed6 = NUMBER)* SEP
             (' '* PRNOfSatellitesUsed7 = NUMBER)* SEP
             (' '* PRNOfSatellitesUsed8 = NUMBER)* SEP
             (' '* PRNOfSatellitesUsed9 = NUMBER)* SEP
             (' '* PRNOfSatellitesUsed10 = NUMBER)* SEP
             (' '* PRNOfSatellitesUsed11 = NUMBER)* SEP
             (' '* PRNOfSatellitesUsed12 = NUMBER)* SEP
             (' '* PDOP = NUMBER)* SEP
             (' '* HDOP = NUMBER)* SEP
             (' '* VDOP = NUMBER)*
             checksum=CHECKSUM
        {
        gsa = new GSA (device.getText(), getText(),
        		     autoOrManualSelection.getText(), 
                             (new Integer(dimensionFix.getText())).intValue(),
                             PDOP != null ? (new Float(PDOP.getText())).floatValue() : 0.0f,
			     HDOP != null ? (new Float(HDOP.getText())).floatValue() : 0.0f,
			     VDOP != null ? (new Float(VDOP.getText())).floatValue() : 0.0f);
			     
        if(PRNOfSatellitesUsed1 != null) { 
           sId =PRNOfSatellitesUsed1.getText();  
           if(!sId.equals("00"))
              gsa.addPRNOfSatelliteUsed(new Integer(sId).intValue());
         }
        if(PRNOfSatellitesUsed2 != null) { 
           sId =PRNOfSatellitesUsed2.getText();  
           if(!sId.equals("00"))
              gsa.addPRNOfSatelliteUsed(new Integer(sId).intValue());
         }
         if(PRNOfSatellitesUsed3 != null) { 
           sId =PRNOfSatellitesUsed3.getText();  
           if(!sId.equals("00"))
              gsa.addPRNOfSatelliteUsed(new Integer(sId).intValue());
         }
         if(PRNOfSatellitesUsed4 != null) { 
           sId =PRNOfSatellitesUsed4.getText();  
           if(!sId.equals("00"))
              gsa.addPRNOfSatelliteUsed(new Integer(sId).intValue());
         }
         if(PRNOfSatellitesUsed5 != null) { 
           sId =PRNOfSatellitesUsed5.getText();  
           if(!sId.equals("00"))
              gsa.addPRNOfSatelliteUsed(new Integer(sId).intValue());
         }
         if(PRNOfSatellitesUsed6 != null) { 
           sId =PRNOfSatellitesUsed6.getText();  
           if(!sId.equals("00"))
              gsa.addPRNOfSatelliteUsed(new Integer(sId).intValue());
         }
         if(PRNOfSatellitesUsed7 != null) { 
           sId =PRNOfSatellitesUsed7.getText();  
           if(!sId.equals("00"))
              gsa.addPRNOfSatelliteUsed(new Integer(sId).intValue());
         }
         if(PRNOfSatellitesUsed8 != null) { 
           sId =PRNOfSatellitesUsed8.getText();  
           if(!sId.equals("00"))
              gsa.addPRNOfSatelliteUsed(new Integer(sId).intValue());
         }
         if(PRNOfSatellitesUsed9 != null) { 
           sId =PRNOfSatellitesUsed9.getText();  
           if(!sId.equals("00"))
              gsa.addPRNOfSatelliteUsed(new Integer(sId).intValue());
         }
         if(PRNOfSatellitesUsed10 != null) { 
           sId =PRNOfSatellitesUsed10.getText();  
           if(!sId.equals("00"))
              gsa.addPRNOfSatelliteUsed(new Integer(sId).intValue());
         }
         if(PRNOfSatellitesUsed11 != null) { 
           sId =PRNOfSatellitesUsed11.getText();  
           if(!sId.equals("00"))
              gsa.addPRNOfSatelliteUsed(new Integer(sId).intValue());
         }
         if(PRNOfSatellitesUsed12 != null) { 
           sId =PRNOfSatellitesUsed12.getText();  
           if(!sId.equals("00"))
              gsa.addPRNOfSatelliteUsed(new Integer(sId).intValue());
         }
        
       handler.doIt(gsa);
        }
	;
/** GSV   Satellites in view 
$GPGSV,3,3,11,29,29,235,41,35,10,105,00,47,13,252,00*4F
$GPGSV,3,2,11,11,11,043,39,24,38,107,46,26,32,247,49,28,46,068,48*7F
$GPGSV,3,2,11,18,57,079,45,19,36,301,39,21,30,068,,22,61,146,45*72
$GPGSV,3,3,11,26,12,039,,27,02,314,,29,06,024,,,,,*4E
$GPGSV,3,3,10,27,69,290,48,31,35,048,43,,,,,,,,*7A
$GPGSV,3,2,11,18,57,079,45,19,36,301,39,21,30,068,,22,61,146,45*72
$GPGSV,3,2,09, 31,39,054,, 32,62,149,46,16,24,132,36,04,18,316,*7E
$GPGSV,3,3,09, 01,18,045,40 *4D
$GPGSV,3,3,12,02,,,26,24,,,00,26,30,047,48,06,,,25*43
$GPGSV,3,3,11,23,04,179,,03,02,153,,22,02,054,*46
*/
 
GSV	:   	'$' device=DEVICE 'GSV' 
		(NUMBER | SEP)+ 
		checksum=CHECKSUM
            {
            String sentence = getText();
            String [] tab = sentence.split("\\*");
            sentence = tab[0];
         
           
            String[] ss = sentence.split(",");
                 
            String numberOfSentences = ss[1];
            String sentenceNumber = ss[2];
            String numberOfSatellitesInView = ss[3];
            
            gsv = new GSV (device.getText(), getText(),
                numberOfSentences != null ? new Integer(numberOfSentences).intValue() : 0,
                sentenceNumber != null ? new Integer(sentenceNumber).intValue() : 0,
                numberOfSatellitesInView != null ? new Integer(numberOfSatellitesInView).intValue() :0
            );
            
            String satellitePRNNumber = null;
            String elevationDegrees = null;
            String azimuthDegrees = null;
            String snr = null;
           
            GPSSatellite s;
            int l = ss.length / 4;  
            l *= 4;
            for(int i = 4; i < l ; i+=4){
               satellitePRNNumber = ss[i];
               elevationDegrees = ss[i+1];
               azimuthDegrees = ss[i+2];
               snr = ss[i+3];
               if(snr != null && !snr.equals("") && !snr.equals(" ")){
                  s = new GPSSatellite(
                  satellitePRNNumber != null ? new Integer(satellitePRNNumber).intValue() : 0,
                  elevationDegrees != null ?  new Integer(elevationDegrees).intValue() : 0,
                  azimuthDegrees != null ? new Integer(azimuthDegrees).intValue() : 0,
                  new Integer(snr).intValue()
                  );
                gsv.addSatellite(s);
               }
            }
           handler.doIt(gsv);
          }
          ;
  /** Magnetic heading, deviation, variation */
  HDG	:	'$' device=DEVICE 'HDG' SEP
  		heading = NUMBER SEP+
  		(dev = NUMBER SEP we = LETTERS SEP)*
  		var = NUMBER SEP
  		ew = LETTERS
  		checksum=CHECKSUM
  {
  deviation = ewConvert(dev != null ? (new Float(dev.getText())).floatValue() : 0.0f, we != null ? we.getText() : "");
  variation = ewConvert(var != null ? (new Float(var.getText())).floatValue() : 0.0f, ew != null ? ew.getText() : "");
  hdg = new HDG(device.getText(), getText(),
 		 heading != null ?  new Float(heading.getText()).floatValue(): 0.0f,
 		 deviation, variation);
  	handler.doIt(hdg);
  }
  ;
  /** Heading � Magnetic */
  HDM 	: 	'$' device=DEVICE 'HDM' SEP
              heading = NUMBER SEP
              ( LETTERS)* 
   	      checksum=CHECKSUM
  {
  hdm = new HDM(device.getText(), getText(),
 		 heading != null ?  new Float(heading.getText()).floatValue(): 0.0f);
 		 
    		handler.doIt(hdm);
  }
  ;
  /** Heading � True
  $IIHDT,155.0,T*23
  */
  HDT	:'$' device=DEVICE 'HDT' SEP
              heading = NUMBER SEP
              (LETTERS)*
              checksum= CHECKSUM
  {
   hdt = new HDT(device.getText(), getText(),
 		 heading != null ?  new Float(heading.getText()).floatValue(): 0.0f);
    		handler.doIt(hdt);
  }
  ;
  /** MSK - Control for a Beacon Receiver */
MSK 	: '$' device=DEVICE 'MSK' SEP
	    frequencyToUse = NUMBER SEP
            frequencyMode = LETTERS SEP
            beaconBitRate = NUMBER SEP
            bitRateMode = LETTERS SEP
            frequencyForMSS = NUMBER* SEP*
            checksum=CHECKSUM
	{
            msk = new MSK(device.getText(), getText(),
            frequencyToUse != null ?  new Float(frequencyToUse.getText()).floatValue(): 0.0f,
            frequencyMode != null ? frequencyMode.getText() : "",
            beaconBitRate != null ? (new Integer(beaconBitRate.getText())).intValue() : 0,
            bitRateMode != null ? bitRateMode.getText() : "",
            frequencyForMSS != null ? (new Integer(frequencyForMSS.getText())).intValue() : 0);
            
	handler.doIt(msk);
	}
	;
	/**  Air temperature 
	$IIMTA,39.4,C*0B
	*/
MTA	: '$' device=DEVICE 'MTA' SEP
	  (temperature = NUMBER)* SEP
          unit = LETTERS 
          checksum=CHECKSUM
	{
	 mta = new MTA(device.getText(), getText(),
	 temperature != null ?  new Float(temperature.getText()).floatValue(): -276.00f,
	 unit != null ? unit.getText() : "");
	 
	 handler.doIt(mta);
	}
	;   
	/**  Water temperature 
	$IIMTW,18.6,C*1C*
	$IIMTW,,C*0D
	*/
MTW 	: '$' device=DEVICE 'MTW' SEP
	  (temperature = NUMBER)* SEP
          unit = LETTERS 
          checksum=CHECKSUM
	{
	 mtw = new MTW(device.getText(), getText(),
	 temperature != null ?  new Float(temperature.getText()).floatValue(): -276.00f,
	 unit != null ? unit.getText() : "");
	 
	 handler.doIt(mtw);
	}
	;   
/** NMEA 0183 standard Wind Direction and Speed, with respect to north.
$IIMWD,,T,258,M,24.6,N,12.6,M*7E*/   
 MWD 	:	'$' device=DEVICE 'MWD' SEP
 		windDirectionTrue = NUMBER* SEP
            	LETTERS SEP
            	windDirectionMagnetic = NUMBER* SEP
            	LETTERS SEP
		windSpeed = NUMBER* SEP
		LETTERS SEP
                NUMBER SEP
                LETTERS
                checksum=CHECKSUM
		{
 		mwd = new MWD(device.getText(), getText(),
 		windDirectionTrue != null ?  new Float(windDirectionTrue.getText()).floatValue(): 0.0f,
 		windDirectionMagnetic != null ?  new Float(windDirectionMagnetic.getText()).floatValue(): 0.0f,
 		windSpeed != null ?  new Float(windSpeed.getText()).floatValue(): 0.0f);
 		handler.doIt(mwd);
 		}
		 ; 
		 
/** Wind Speed and Angle */
MWV 	:	'$' device = DEVICE 'MWV' SEP 
		windAngle = NUMBER SEP
            	reference = LETTERS SEP
            	windSpeed = NUMBER SEP
            	unit = LETTERS SEP
            	status = LETTERS
            	checksum=CHECKSUM
	{
	mwv = new MWV(device.getText(), getText(),
 		windAngle != null ?  new Float(windAngle.getText()).floatValue(): 0.0f,
 		reference != null ? reference.getText() : "",
 		windSpeed != null ?  new Float(windSpeed.getText()).floatValue(): 0.0f,
 		unit != null ? unit.getText() : "",
 		status != null ? status.getText() : "");
 		
 	 handler.doIt(mwv);
	}
	;
	

           
/** $GPRMB,A,0.66,L,003,004,4917.24,N,12309.57,W,001.3,052.5,000.5,V*20 
    $IIRMB,A,0.00,L,,MB,4822.794,N,00425.911,W,26.82,243,0.00,A*43*/          
  RMB 	: '$' device = DEVICE 'RMB' SEP 
   	      status = LETTERS SEP  
   	      (crossTrackError =  NUMBER)* SEP
   	      (directionToSteer = LETTERS)* SEP
   	      (fromWaypointId = LETTERS |fromWaypointId = NUMBER)*  SEP
   	      (toWaypointId =  LETTERS | toWaypointId = NUMBER)* SEP
   	      (destinationWaypointLatitude =  NUMBER)* SEP (ns = LETTERS)* SEP
   	      (destinationWaypointLongitude  =  NUMBER)* SEP (ew  = LETTERS)* SEP
   	      (rangeToDestination =  NUMBER)* SEP
   	      (bearingToDestination =  NUMBER)* SEP
   	      (destinationClosingVelocity =  NUMBER)* SEP 	        	    
   	      (LETTERS SEP)*
   	      (arrivalStatus = LETTERS | '\u0000')* 
   	      checksum=CHECKSUM 
   	     
  {
   lat = latConvert(destinationWaypointLatitude != null ? (new Float(destinationWaypointLatitude.getText())).floatValue() : 0.0f, ns != null ? ns.getText() : "");
   lon = lonConvert(destinationWaypointLongitude != null ? (new Float(destinationWaypointLongitude.getText())).floatValue() : 0.0f, ew != null ? ew.getText() : "");
   rmb = new RMB(device.getText(), getText(),
  		 status.getText(),
  		 crossTrackError != null ? new Float(crossTrackError.getText()).floatValue() : 0.0f,
  		 directionToSteer != null ? directionToSteer.getText() : "",
  		 fromWaypointId != null ? fromWaypointId.getText() : "",
  		 toWaypointId != null ? toWaypointId.getText() : "",
  		 lat,
  		 lon,
  		 rangeToDestination != null ? new Float(rangeToDestination.getText()).floatValue() : 0.0f,
  		 bearingToDestination != null ? new Float(bearingToDestination.getText()).floatValue() : 0.0f,
  		 destinationClosingVelocity != null ? new Float(destinationClosingVelocity.getText()).floatValue() : 0.0f,
  		 //arrivalStatus != null ? arrivalStatus.getText() : "");
	         "");
    handler.doIt(rmb);
  }
  ;      
                     
/** $GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A 
    $GPRMC,183408,V,4124.7502,N,08152.2393,W,,,010305,007.9,W*68
    
    $IIRMC,083632.00,A,2746.184 ,N,00104.101 ,E,512.26,149.00,      ,4.00 ,E*76
    $GPRMC,183408   ,V,4124.7502,N,08152.2393,W,      ,      ,010305,007.9,W*68
    $GPRMC,234112   ,A,3926.5031,N,11946.1982,W,0.0   ,151.1 ,250905,15.2 ,E,D*32
    $GPRMC,         ,V,         , ,          , ,      ,      ,070106,15.2 ,E,N*0E
    $GPRMC,231532   ,A,3928.1102,N,11947.0862,W,0.0   ,319.0 ,070106,15.2 ,E,D*31
    $IIRMC,083632.00,A,2746.184 ,N,00104.101 ,E,512.26,149.00,      ,4.00 ,E*76
    $IIRMC,103042.00,A,4835.286 ,N,00350.294 ,W,0.00  ,0.00  ,030606,4.00 ,E*61
    $GPRMC,145412.000,A,4853.6252,N,00223.3765,E,0.07 ,134.43,220885,     ,*0A
    $IIRMC,083632.00 ,A,2746.184 ,N,00104.101 ,E,512.26,149.00,      ,4.00 ,E*76
    $IIRMC,105623.00 ,A,4835.292 ,N,00350.289 ,W,0.00  ,0.00  ,030606,4.00 ,E*6
    $IIRMC,085728.00 ,A,2443.197 ,N,00247.234 ,E,-616.82,156.00,110706,4.00,E*54
    */
RMC 	: '$' device=DEVICE 'RMC' SEP
           utc = NUMBER* SEP
           status = LETTERS SEP
           latitude=NUMBER* SEP ns = LETTERS* SEP longitude=NUMBER* SEP ew=LETTERS* SEP
           SIGN* (sog = NUMBER)* SEP
           (track = NUMBER)* SEP
            (yymmdd = NUMBER)* SEP
            magneticVariation = NUMBER* SEP 
            nsew = LETTERS* 
            (SEP LETTERS)*
           checksum=CHECKSUM
            {
            lat = latConvert(latitude != null ? (new Float(latitude.getText())).floatValue() : 0.0f, ns != null ? ns.getText() : "");
            lon = lonConvert(longitude != null ? (new Float(longitude.getText())).floatValue() : 0.0f, ew != null ? ew.getText() : "");
            if(utc != null)
	      date = dateFactory(utc.getText());
	    else
	      date = dateFactory("000000");
            String sentence = getText();
            
            if(sentence.contains("\n")){
                 String [] tab = sentence.split("\n");
                 sentence = tab[0];
            }
            rmc = new RMC( device.getText(), sentence,
            		  date,
            		  status.getText(),
                          lat, 
                          lon, 
                          sog != null ? new Float(sog.getText()).floatValue() : 0.0f,
                          track != null ? new Float(track.getText()).floatValue() : 0.0f,
                          magneticVariation != null ? new Float(magneticVariation.getText()).floatValue() : 0.0f,
                          //nsew != null ? nsew.getText() : ""
                          ""
                          );
          
           handler.doIt(rmc);
            }
	;
	
RSD 	:	 '$' device=DEVICE 'RSD' SEP  
		('\u0021'..'\u007F' | SEP | ' ')* 
		checksum=CHECKSUM 
	{
	//System.out.println(getText());
	}
	;
 
/** RTE  Waypoints in active route
 * RTE - RTE is sent to indicate the names of the waypoints used in an active route. 
*/
RTE	: '$' device=DEVICE 'RTE' SEP
	totalNumberOfsentence = NUMBER SEP
	sentenceNumber = NUMBER SEP
	type = LETTERS SEP
	(LETTERS | NUMBER | '-' | '_' | SEP)* 
	checksum=CHECKSUM
	{
        String[] route = getText().split(",");
        List<String> waypoints = new ArrayList<>();
        for(int i = 4; i < route.length ; i++){
             waypoints.add(route[i]);
         }
             rte = new RTE(device.getText(), getText(),
             new Integer(route[1]).intValue(),
             new Integer(route[2]).intValue(),
             route[3],
             waypoints);
            // System.out.println(rte); 
	}
	;
/** VBW Dual Ground/Water Speed*/
VBW 	:	'$' device=DEVICE 'VBW' SEP
	' '* SIGN* longitudinalWaterSpeed = NUMBER SEP
	' '* SIGN* transverseWaterSpeed = NUMBER SEP
	wstatus = LETTERS
	(SEP ' '* SIGN* (longitudinalGroundSpeed = NUMBER)* SEP
	' '* SIGN* (transverseGroundSpeed = NUMBER)* SEP
	gstatus = LETTERS
	)* 
	checksum=CHECKSUM
	{
	vbw = new VBW (device.getText(), getText(),
                longitudinalWaterSpeed != null ? (new Float(longitudinalWaterSpeed.getText())).floatValue() : 0.0f,
                transverseWaterSpeed != null ? (new Float(transverseWaterSpeed.getText())).floatValue() : 0.0f,
                wstatus != null ? wstatus.getText() : "",
                longitudinalGroundSpeed != null ? (new Float(longitudinalGroundSpeed.getText())).floatValue() : 0.0f,
                transverseGroundSpeed != null ? (new Float(transverseGroundSpeed.getText())).floatValue() : 0.0f,
                gstatus != null ? gstatus.getText() : "");
            handler.doIt(vbw);
	}
	;
	
/** Dual Ground/Water Distance */
VLW	: '$' device=DEVICE 'VLW' SEP
	    ' '* (dataTotalWaterDistance = NUMBER)* SEP
	    LETTERS SEP
	    ' '* (dataTripWaterDistance = NUMBER)* SEP
	     LETTERS
	    (SEP ' '* (dataTotalGroundDistance = NUMBER)* SEP
	    LETTERS SEP
	    ' '* (dataTripGroundDistance = NUMBER)* SEP
	    LETTERS
	    )* 
	    checksum=CHECKSUM
	    {
	      vlw = new VLW (device.getText(), getText(),
                dataTotalWaterDistance != null ? (new Float(dataTotalWaterDistance.getText())).floatValue() : 0.0f,
                dataTripWaterDistance != null ? (new Float(dataTripWaterDistance.getText())).floatValue() : 0.0f,
                dataTotalGroundDistance != null ? (new Float(dataTotalGroundDistance.getText())).floatValue() : 0.0f,
                dataTripGroundDistance != null ? (new Float(dataTripGroundDistance.getText())).floatValue() : 0.0f);
            handler.doIt(vlw);
	    }
;
	
/** Water Speed and Heading 
$IIVHW,,,347,M,0.00,N,,*64
*/

VHW	:  '$' device=DEVICE 'VHW' SEP
	    ' '* (degreesTrue = NUMBER)* SEP
            LETTERS* SEP
            ' '* (degreesMagnetic = NUMBER)* SEP
            LETTERS* SEP
            ' '* (speedInKnots = NUMBER)* SEP
            LETTERS* SEP
            ' '* (speedInKilometers = NUMBER)* SEP
            LETTERS*
            checksum=CHECKSUM
            {
            vhw = new VHW (device.getText(), getText(),
                degreesTrue != null ? (new Float(degreesTrue.getText())).floatValue() : 0.0f,
                degreesMagnetic != null ? (new Float(degreesMagnetic.getText())).floatValue() : 0.0f,
                speedInKnots != null ? (new Float(speedInKnots.getText())).floatValue() : 0.0f,
                speedInKilometers != null ? (new Float(speedInKilometers.getText())).floatValue() : 0.0f);
            handler.doIt(vhw);
            }
	;
	/*
	 * VPW Speed � Measured Parallel to Wind
       1 2 3 4 5
       | | | | |
$--VPW,x.x,N,x.x,M*hh
1) Speed, "-" means downwind
2) N = Knots
3) Speed, "-" means downwind
4) M = Meters per second
5) Checksum
*/
VPW 	:  '$' device=DEVICE 'VPW' SEP
	' '* (speed = NUMBER | speed = SIGN)* SEP
	 LETTERS SEP
	 ' '* (NUMBER | '-')* SEP
         LETTERS*
         checksum=CHECKSUM
	{
	 vpw = new VPW (device.getText(), getText(),
	  speed != null ? (new Float(speed.getText())).floatValue() : 0.0f);
	  
	   handler.doIt(vpw);
	}
	;
	
/** VTG - Velocity made good. */
VTG 	:  '$' device=DEVICE 'VTG' SEP
            ' '* (trueTrackMadeGoodDegrees = NUMBER)* SEP
            (LETTERS) SEP
            ' '* (magneticTrackMadeGood = NUMBER)* SEP
            (LETTERS) SEP
            ' '* (groundSpeedKnots = NUMBER)* SEP
            (LETTERS) SEP
            ' '* (groundSpeedKph = NUMBER)* SEP
            (LETTERS SEP)*
            LETTERS*
            checksum=CHECKSUM
            {
            vtg = new VTG (device.getText(), getText(),
                trueTrackMadeGoodDegrees != null ? (new Float(trueTrackMadeGoodDegrees.getText())).floatValue() : 0.0f,
                magneticTrackMadeGood != null ? (new Float(magneticTrackMadeGood.getText())).floatValue() : 0.0f,
                groundSpeedKnots != null ? (new Float(groundSpeedKnots.getText())).floatValue() : 0.0f);
                
           handler.doIt(vtg);
            }
	;	
	
/** VWR - Relative Wind Speed and Angle 
$IIVWR,066,R,,N,,M,,K*79
*/
VWR 	:	'$' device=DEVICE 'VWR' SEP
		windDirectionMagnitude  = NUMBER* SEP
		windDirectionOfBow  = LETTERS SEP
		speedInKnots = NUMBER* SEP
		LETTERS SEP
		NUMBER* SEP
		LETTERS SEP
		NUMBER* SEP
		LETTERS* 
		checksum=CHECKSUM
		{
		vwr = new VWR(device.getText(), getText(),
			windDirectionMagnitude != null ? (new Integer(windDirectionMagnitude.getText())).intValue() : 0,
			windDirectionOfBow != null ? windDirectionOfBow.getText() : "",
			speedInKnots != null ? (new Float(speedInKnots.getText())).floatValue() : 0.0f);
			 handler.doIt(vwr);
		}
		;
		/**
 * VWT - True Wind Speed and Angle
 * 	 Obsolete
 */
		
VWT	: '$' device=DEVICE 'VWT' SEP
	('\u0021'..'\u007F')+ SEP
 	  NUMBER* 
 	  checksum=CHECKSUM
	{
	}
	;
/**
 * XTE Cross-Track Error � Measured
       1 2 3   4 5 6
       | | |   | | |
$--XTE,A,A,x.x,a,N,*hh
1) Status
V = LORAN-C blink or SNR warning
A = general warning flag or other navigation systems when a reliable
fix is not available
2) Status
V = Loran-C cycle lock warning flag
A = OK or not used
3) Cross track error magnitude
4) Direction to steer, L or R
5) Cross track units. N = Nautical Miles
6) Checksum
* $IIXTE,A,A,5.36,R,N*67
 * @author Serge
 */
XTE 	: '$' device=DEVICE 'XTE' SEP
	generalWarning=LETTERS SEP
	status=LETTERS SEP
	crossTrackError=NUMBER* SEP
	directionToSteer=LETTERS SEP
	LETTERS
	checksum=CHECKSUM
	{
	xte = new XTE(device.getText(), getText(),
	generalWarning.getText(),
	status.getText(),
	crossTrackError!= null ? (new Float(crossTrackError.getText())).floatValue() : 0.0f,
	directionToSteer.getText());
	handler.doIt(xte);
	}
	;
/** Time & Date � UTC, Day, Month, Year and Local Time Zone 
$ECZDA,194310,14,07,2008,,*5F*/
	 
ZDA 	:	 '$' device=DEVICE 'ZDA' SEP
		utc = NUMBER SEP
		dd = NUMBER SEP
		mm = NUMBER SEP
		yy = NUMBER SEP
		NUMBER* SEP
		NUMBER* 
		checksum=CHECKSUM
	{
	 date = dateFactory(utc.getText());
	 date.set(Calendar.YEAR, (new Integer(yy.getText()).intValue()));
         date.set(Calendar.MONTH, (new Integer(mm.getText()).intValue() - 1));
         date.set(Calendar.DAY_OF_MONTH, (new Integer(dd.getText()).intValue()));
         
         zda = new ZDA(device.getText(), getText(),
         date);
         handler.doIt(zda);
	}
	;
/**
* AIS SECTION
*/
ALR	: '$' device=DEVICE 'ALR' SEP
	('\u0021'..'\u007F')+ SEP
 	  NUMBER* 
 	  checksum=CHECKSUM
	{
	}
	;
	
VDM 	: '!' device=DEVICE 'VDM' SEP
	  NUMBER* SEP
	  NUMBER* SEP
	  NUMBER* SEP
	  LETTERS* SEP
 	 ('\u0021'..'\u007F')+ SEP
 	  NUMBER* 
 	  checksum=CHECKSUM
	{
	aisParser.parse(getText());
	}
	;
	
	/** SECTION GPSD */
	CLASS
    	:	
    	'{\"class\":'
    	;
	
/* $AITXT,01,01,91,FREQ,2087,2088*57 */
TXT	: '$' device=DEVICE 'TXT' SEP
	('\u0021'..'\u007F' | SEP | ' ')*  
	checksum=CHECKSUM 
	{
	}
	;
	
/**
*PROPRIETARY SECTION
*/
PRO	:	('$PR'| '$PG' |'$PS') ('\u0021'..'\u007F' | SEP | ' ')*  
		checksum=CHECKSUM 
	{
	//System.out.println("Proprietary sentence : " + getText());
	}
	; 
	 
DEVICE 	:	
  	('GP'|'II'|'AG'|'AI'|'AP'|'CC'|'CD'|'CS'|'CT'|'CV'|'CX'|'DF'|'EC'|'EP'|'ER'|'HC'|'HE'|'HN'|'IN'|'RA'|'SD'|'SM'|'SN'|'SS'|'TI'|'TR'|'VD'|'DM'|'VW'|'WI'|'YX'|'ZA'|'ZC'|'ZQ'|'ZV')
	{
	// System.out.println("Device : " + getText());
	}
	;
NUMBER
    :   
    '0'..'9'+
    |
    ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;
    
SEP :	(',')
    
    ;

SIGN : ('+'|'-')
     ;

CHECKSUM : (('*'('0'..'9')('0'..'9')) |
            ('*'('A'..'F')('0'..'9')) |
            ('*'('A'..'F')('A'..'F')) |
            ('*'('0'..'9')+('A'..'F')))
         ;   
 
 LETTERS : (('A'..'Z')|('a'..'z')|' ')+
         ;// {System.out.println(getText());};        

	         
fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;


   	


