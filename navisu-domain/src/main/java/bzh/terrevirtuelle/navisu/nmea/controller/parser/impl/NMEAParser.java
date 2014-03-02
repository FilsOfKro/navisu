// $ANTLR 3.4 I:\\developpement\\projetNaVisu\\navisu\\trunk\\navisu\\navisu-domain\\src\\main\\java\\bzh\\terrevirtuelle\\navisu\\nmea\\controller\\parser\\impl\\NMEA.g 2014-03-02 16:47:15

package bzh.terrevirtuelle. navisu.nmea.controller.parser.impl;


    

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class NMEAParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AAM", "ALR", "APB", "BEC", "BOD", "BWC", "BWR", "BWW", "CHECKSUM", "DBK", "DBS", "DBT", "DEV", "DEVICE", "DPT", "EXPONENT", "GGA", "GLL", "GPSD_AIS", "GPSD_DEVICE", "GPSD_DEVICES", "GPSD_VERSION", "GPSD_WATCH", "GSA", "GSV", "HDG", "HDM", "HDT", "LETTERS", "MSK", "MTA", "MTW", "MWD", "MWV", "NAME", "NUMBER", "PGN", "PRO", "RMB", "RMC", "RSD", "RTE", "SEP", "SIGN", "SIGNED", "TIME_STAMP", "TXT", "VBW", "VDM", "VHW", "VLW", "VPW", "VTG", "VWR", "VWT", "WS", "XTE", "ZDA"
    };

    public static final int EOF=-1;
    public static final int AAM=4;
    public static final int ALR=5;
    public static final int APB=6;
    public static final int BEC=7;
    public static final int BOD=8;
    public static final int BWC=9;
    public static final int BWR=10;
    public static final int BWW=11;
    public static final int CHECKSUM=12;
    public static final int DBK=13;
    public static final int DBS=14;
    public static final int DBT=15;
    public static final int DEV=16;
    public static final int DEVICE=17;
    public static final int DPT=18;
    public static final int EXPONENT=19;
    public static final int GGA=20;
    public static final int GLL=21;
    public static final int GPSD_AIS=22;
    public static final int GPSD_DEVICE=23;
    public static final int GPSD_DEVICES=24;
    public static final int GPSD_VERSION=25;
    public static final int GPSD_WATCH=26;
    public static final int GSA=27;
    public static final int GSV=28;
    public static final int HDG=29;
    public static final int HDM=30;
    public static final int HDT=31;
    public static final int LETTERS=32;
    public static final int MSK=33;
    public static final int MTA=34;
    public static final int MTW=35;
    public static final int MWD=36;
    public static final int MWV=37;
    public static final int NAME=38;
    public static final int NUMBER=39;
    public static final int PGN=40;
    public static final int PRO=41;
    public static final int RMB=42;
    public static final int RMC=43;
    public static final int RSD=44;
    public static final int RTE=45;
    public static final int SEP=46;
    public static final int SIGN=47;
    public static final int SIGNED=48;
    public static final int TIME_STAMP=49;
    public static final int TXT=50;
    public static final int VBW=51;
    public static final int VDM=52;
    public static final int VHW=53;
    public static final int VLW=54;
    public static final int VPW=55;
    public static final int VTG=56;
    public static final int VWR=57;
    public static final int VWT=58;
    public static final int WS=59;
    public static final int XTE=60;
    public static final int ZDA=61;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public NMEAParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public NMEAParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return NMEAParser.tokenNames; }
    public String getGrammarFileName() { return "I:\\developpement\\projetNaVisu\\navisu\\trunk\\navisu\\navisu-domain\\src\\main\\java\\bzh\\terrevirtuelle\\navisu\\nmea\\controller\\parser\\impl\\NMEA.g"; }



    // $ANTLR start "entry"
    // I:\\developpement\\projetNaVisu\\navisu\\trunk\\navisu\\navisu-domain\\src\\main\\java\\bzh\\terrevirtuelle\\navisu\\nmea\\controller\\parser\\impl\\NMEA.g:240:1: entry : ( AAM | APB | BEC | BOD | BWC | BWR | DBS | DBT | DBK | DPT | GGA | GLL | GSA | GSV | HDG | HDM | HDT | MSK | MTA | MTW | MWD | MWV | RMB | RMC | RTE | VBW | VLW | VHW | VPW | VTG | VWR | VWT | XTE | ZDA | RSD | VDM | TXT | ALR | GPSD_AIS | GPSD_DEVICE | GPSD_DEVICES | GPSD_VERSION | GPSD_WATCH | PRO )+ ;
    public final void entry() throws RecognitionException {
        try {
            // I:\\developpement\\projetNaVisu\\navisu\\trunk\\navisu\\navisu-domain\\src\\main\\java\\bzh\\terrevirtuelle\\navisu\\nmea\\controller\\parser\\impl\\NMEA.g:240:8: ( ( AAM | APB | BEC | BOD | BWC | BWR | DBS | DBT | DBK | DPT | GGA | GLL | GSA | GSV | HDG | HDM | HDT | MSK | MTA | MTW | MWD | MWV | RMB | RMC | RTE | VBW | VLW | VHW | VPW | VTG | VWR | VWT | XTE | ZDA | RSD | VDM | TXT | ALR | GPSD_AIS | GPSD_DEVICE | GPSD_DEVICES | GPSD_VERSION | GPSD_WATCH | PRO )+ )
            // I:\\developpement\\projetNaVisu\\navisu\\trunk\\navisu\\navisu-domain\\src\\main\\java\\bzh\\terrevirtuelle\\navisu\\nmea\\controller\\parser\\impl\\NMEA.g:240:13: ( AAM | APB | BEC | BOD | BWC | BWR | DBS | DBT | DBK | DPT | GGA | GLL | GSA | GSV | HDG | HDM | HDT | MSK | MTA | MTW | MWD | MWV | RMB | RMC | RTE | VBW | VLW | VHW | VPW | VTG | VWR | VWT | XTE | ZDA | RSD | VDM | TXT | ALR | GPSD_AIS | GPSD_DEVICE | GPSD_DEVICES | GPSD_VERSION | GPSD_WATCH | PRO )+
            {
            // I:\\developpement\\projetNaVisu\\navisu\\trunk\\navisu\\navisu-domain\\src\\main\\java\\bzh\\terrevirtuelle\\navisu\\nmea\\controller\\parser\\impl\\NMEA.g:240:13: ( AAM | APB | BEC | BOD | BWC | BWR | DBS | DBT | DBK | DPT | GGA | GLL | GSA | GSV | HDG | HDM | HDT | MSK | MTA | MTW | MWD | MWV | RMB | RMC | RTE | VBW | VLW | VHW | VPW | VTG | VWR | VWT | XTE | ZDA | RSD | VDM | TXT | ALR | GPSD_AIS | GPSD_DEVICE | GPSD_DEVICES | GPSD_VERSION | GPSD_WATCH | PRO )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= AAM && LA1_0 <= BWR)||(LA1_0 >= DBK && LA1_0 <= DBT)||LA1_0==DPT||(LA1_0 >= GGA && LA1_0 <= HDT)||(LA1_0 >= MSK && LA1_0 <= MWV)||(LA1_0 >= PRO && LA1_0 <= RTE)||(LA1_0 >= TXT && LA1_0 <= VWT)||(LA1_0 >= XTE && LA1_0 <= ZDA)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // I:\\developpement\\projetNaVisu\\navisu\\trunk\\navisu\\navisu-domain\\src\\main\\java\\bzh\\terrevirtuelle\\navisu\\nmea\\controller\\parser\\impl\\NMEA.g:
            	    {
            	    if ( (input.LA(1) >= AAM && input.LA(1) <= BWR)||(input.LA(1) >= DBK && input.LA(1) <= DBT)||input.LA(1)==DPT||(input.LA(1) >= GGA && input.LA(1) <= HDT)||(input.LA(1) >= MSK && input.LA(1) <= MWV)||(input.LA(1) >= PRO && input.LA(1) <= RTE)||(input.LA(1) >= TXT && input.LA(1) <= VWT)||(input.LA(1) >= XTE && input.LA(1) <= ZDA) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

        }

          catch (RecognitionException e) {
            throw e;
           }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "entry"

    // Delegated rules


 

}