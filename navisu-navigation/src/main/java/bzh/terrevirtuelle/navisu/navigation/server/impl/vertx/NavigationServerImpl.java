/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.navigation.server.impl.vertx;

import bzh.terrevirtuelle.navisu.domain.camera.model.Camera;
import bzh.terrevirtuelle.navisu.domain.navigation.NavigationData;
import bzh.terrevirtuelle.navisu.domain.navigation.NavigationDataSet;
import bzh.terrevirtuelle.navisu.navigation.server.NavigationServer;
import bzh.terrevirtuelle.navisu.navigation.server.NavigationServerServices;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.capcaval.c3.component.ComponentState;
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
    private NavigationDataSet navigationDataSet;

    // private StringWriter response;
    // private StringWriter stringWriter = null;
    protected static final Logger LOGGER = Logger.getLogger(NavigationServerImpl.class.getName());

    @Override
    public void componentInitiated() {

    }

    @Override
    public void init() {
        navigationDataSet = new NavigationDataSet();
        initProperties();
        this.hostName = properties.getProperty("hostName").trim();
        this.port = new Integer(properties.getProperty("port").trim());
        initVertx();
    }

    @Override
    public void init(String hostName, int port) {
        navigationDataSet = new NavigationDataSet();
        initProperties();
        this.hostName = hostName;
        this.port = port;
        initVertx();
    }

    private void initProperties() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("properties/navigation.properties"));
            marshaller = JAXBContext.newInstance(NavigationDataSet.class).createMarshaller();
        } catch (IOException | JAXBException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private void initVertx() {
        vertx = VertxFactory.newVertx();
        try {
            vertx.createHttpServer().websocketHandler((final ServerWebSocket ws) -> {
                if (ws.path().equals("/navigation")) {
                    ws.dataHandler((Buffer data) -> {
                        StringWriter stringWriter = response(ws, data);
                        if (stringWriter != null) {
                            ws.writeTextFrame(stringWriter.toString());
                        }
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

    private StringWriter response(ServerWebSocket ws, Buffer data) {
        Camera camera = new Camera();
        StringWriter stringWriter = new StringWriter();
        navigationDataSet.add(camera);
        try {
            marshaller.marshal(navigationDataSet, stringWriter);
        } catch (JAXBException ex) {
            System.out.println("ex " + ex);
            Logger.getLogger(NavigationServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stringWriter;
    }

    @Override
    public void componentStarted() {
    }

    @Override
    public void componentStopped() {

    }
}
