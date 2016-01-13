/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.navigation.server.impl.vertx;

import bzh.terrevirtuelle.navisu.navigation.server.NavigationServer;
import bzh.terrevirtuelle.navisu.navigation.server.NavigationServerServices;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.xml.bind.Marshaller;
import org.capcaval.c3.component.ComponentState;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.ServerWebSocket;

/**
 *
 * @author Serge
 */
public class NavigationServerImpl
        implements NavigationServer, NavigationServerServices, ComponentState {

    private Properties properties;
    private Vertx vertx;
    int i = 0;
    private Marshaller marshaller;
    private int port;
    private String hostName;

    private StringWriter response;
    private StringWriter stringWriter = null;
    protected static final Logger LOGGER = Logger.getLogger(NavigationServerImpl.class.getName());

    @Override
    public void componentInitiated() {
        properties = new Properties();
        /*
        try {
            properties.load(new FileInputStream("properties/navigation.properties"));
            marshaller = JAXBContext.newInstance(Sentences.class).createMarshaller();
        } catch (IOException | JAXBException ex) {
           LOGGER.log(Level.SEVERE, null, ex);
        }
        */
    }

    @Override
    public void init() {
        this.hostName = properties.getProperty("hostName").trim();
        this.port = new Integer(properties.getProperty("port").trim());
        initVertx();
    }

    @Override
    public void init(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        initVertx();
    }

    private void initVertx() {
        vertx = VertxFactory.newVertx();
        try {
            vertx.createHttpServer().websocketHandler((final ServerWebSocket ws) -> {
                if (ws.path().equals("/myapp")) {
                    ws.dataHandler((Buffer data) -> {
                        ws.writeTextFrame(data.toString() + i);
                        i++;
                    });
                } else {
                    ws.reject();
                }
            }).requestHandler((HttpServerRequest req) -> {
                if (req.path().equals("/")) {
                    req.response().sendFile("data/html/response.html");
                }
            }).listen(8787);
        } catch (Exception e) {
            System.out.println("e " + e);
        }
    
    }

    @Override
    public void componentStarted() {
    }

    @Override
    public void componentStopped() {
       
    }

}

class MyFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        return record.getMessage() + "\n";
    }

    
}
