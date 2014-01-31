/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.server.nmea.parser;

import bzh.terrevirtuelle.navisu.nmea.controller.parser.handler.Handler;
import bzh.terrevirtuelle.navisu.nmea.controller.parser.impl.NMEALexer;
import bzh.terrevirtuelle.navisu.nmea.controller.parser.impl.NMEAParser;
import bzh.terrevirtuelle.navisu.nmea.model.Sentences;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

/**
 *
 * @author Serge
 */
public class NmeaStringParser {

    private NMEAParser parser;
    private NMEALexer lexer;
    private ANTLRStringStream input;
    private Handler handler;
    private Handler aisHandler;
    private CommonTokenStream tokens;
    private Sentences sentences;
    protected static final Logger LOGGER = Logger.getLogger(NmeaStringParser.class.getName());
    protected static  FileHandler fileHandler = null;

    static {
        try {
            fileHandler = new FileHandler("parser.log");
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(NmeaStringParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOGGER.addHandler(fileHandler);
    }

    ;
    public NmeaStringParser(Sentences sentences) {
        this.sentences = sentences;
    }

    public void parse(String source) {
        input = new ANTLRStringStream(source);
        handler = new NmeaHandler(sentences);
        aisHandler = new NmeaHandler(sentences);
        lexer = new NMEALexer(input);
        lexer.setHandler(handler);
        lexer.setAISHandler(aisHandler);
        tokens = new CommonTokenStream(lexer);
        parser = new NMEAParser(tokens);
        try {
            parser.entry();
        } catch (RecognitionException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
    }
}
