/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.database.app.impl;

import bzh.terrevirtuelle.navisu.app.drivers.databasedriver.DatabaseDriver;
import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.database.relational.DatabaseServices;
import bzh.terrevirtuelle.navisu.database.app.TestDB;
import bzh.terrevirtuelle.navisu.database.app.TestDBServices;
import bzh.terrevirtuelle.navisu.database.graph.neo4J.GraphDatabaseComponentServices;
import bzh.terrevirtuelle.navisu.domain.ship.model.Ship;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.capcaval.c3.component.ComponentState;
import org.capcaval.c3.component.annotation.UsedService;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

/**
 * NaVisu
 *
 * @date 21 mai 2015
 * @author Serge Morvan
 */
public class TestDBImpl
        implements TestDB, TestDBServices, DatabaseDriver, ComponentState {

    @UsedService
    DatabaseServices databaseServices;
    @UsedService
    GraphDatabaseComponentServices graphDatabaseComponentServices;
    @UsedService
    GuiAgentServices guiAgentServices;

    private List<Ship> ships;
    private final String SHIP = "SHIP";
    private final String NAME = "TestDB";
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private final static String CREATE_TABLE_SHIP = "CREATE TABLE SHIP("
            + "mmsi        INT NOT NULL PRIMARY KEY,"
            + "shipName    VARCHAR(128),"
            + "latitude    DOUBLE,"
            + "longitude   DOUBLE,"
            + "date        VARCHAR(32),"
            + "time        VARCHAR(32)"
            + ")";
    private final String insertQuery = "INSERT INTO " + SHIP + " (mmsi, shipName,latitude,longitude,date,time)"
            + " VALUES(?,?,?,?,?,?)";
    private final String retrieveQuery = "SELECT * FROM " + SHIP;
    private EntityManager em;
    private Query query;
    private List<String> polygons;

    @Override
    public Connection connect(String dbName, String user, String passwd) {
        connection = databaseServices.connect(dbName, user, passwd);
        return connection;
    }

    @Override
    public Connection connect(String dbName, String hostName, String protocol, String port, String driverName, String userName, String passwd) {
        connection = databaseServices.connect(dbName, hostName, protocol, port, driverName, userName, passwd);
        return connection;
    }

    @Override
    public void close() {
        databaseServices.close();
    }

    /*
     *
     * Recuperation des Ship dans un fichier csv
     *
     */
    public List<Ship> readAllShips() {
        List<Ship> tmp = null;
        Stream<String> lines = null;
        try {
            lines = Files.lines(Paths.get("data/saved", "savedAisShips.csv"));
            tmp = lines.filter(string -> !string.contains("MMSI")).map(Ship::parseCsv).collect(Collectors.toList());
        } catch (IOException ex) {
            Logger.getLogger(TestDBImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (lines != null) {
            lines.close();
        }
        return tmp;
    }

    /*
     *
     SQL-API JDBC Section
     *
     */
    @Override
    public void runJdbcDerby() {
        guiAgentServices.getJobsManager().newJob(null, (progressHandle) -> {
            ships = readAllShips();
            createTable();
            populateTable();
            ships.clear();
            ships.addAll(retrieveAllShips());
            System.out.println(ships);
        });
    }

    /**
     * Execute a query.
     *
     * @param query The query to execute
     * @throws java.sql.SQLException
     */
    public void executeCommand(String query) throws SQLException {
        statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    /**
     * Create a table into the database
     */
    public void createTable() {
        try {
            System.out.println("Creating table : " + SHIP);
            executeCommand(CREATE_TABLE_SHIP);
        } catch (SQLException e) {
            try {
                System.out.println(SHIP + " already exists : " + e);
                System.out.println("Drop table : " + SHIP);
                executeCommand("DROP TABLE " + SHIP);
                System.out.println("Creating table : " + SHIP);
                executeCommand(CREATE_TABLE_SHIP);
            } catch (SQLException f) {
                System.out.println("Error DROP TABLE :" + f);
            }
        }
    }

    /**
     * Populate a table into the database
     */
    public void populateTable() {
        try {
            preparedStatement = connection.prepareStatement(insertQuery);
        } catch (SQLException ex) {
            Logger.getLogger(TestDBImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        ships.stream().forEach((ship) -> {
            try {
                preparedStatement.setInt(1, ship.getMMSI());
                preparedStatement.setString(2, ship.getName());
                preparedStatement.setDouble(3, ship.getLatitude());
                preparedStatement.setDouble(4, ship.getLongitude());
                preparedStatement.setString(5, ship.getLocalDate().toString());
                preparedStatement.setString(6, ship.getLocalTime().toString());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(TestDBImpl.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        });
    }

    /**
     * Retrieve all data in a table into the database
     *
     * @return
     */
    public List<Ship> retrieveAllShips() {
        List<Ship> tmp = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery(retrieveQuery);
            while (rs.next()) {
                tmp.add(new Ship(rs.getInt("mmsi"),
                        rs.getString("shipName"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        LocalDate.parse(rs.getString("date")),
                        LocalTime.parse(rs.getString("time"))));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TestDBImpl.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        return tmp;
    }

    @Override
    public void runJdbcMySql() {
        guiAgentServices.getJobsManager().newJob(null, (progressHandle) -> {
            polygons = retrieveAllpolygons();
            System.out.println(polygons);
        });
    }

    public List<String> retrieveAllpolygons() {
        List<String> tmp = new ArrayList<>();

        try {
            statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(TestDBImpl.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        try {
            //ResultSet rs = statement.executeQuery("SELECT * FROM inpolygons;");
            ResultSet rs = statement.executeQuery("SELECT polygon_id, name, AsText(polygon) FROM inpolygons;");
            while (rs.next()) {
                tmp.add(rs.getString(1));
                tmp.add(rs.getString(2));
                tmp.add(rs.getString(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TestDBImpl.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        return tmp;
    }

    /*
     *
     JPA Section 
     *
     */
    @Override
    public void runJPA() {
        em = databaseServices.getEntityManager();
        
        guiAgentServices.getJobsManager().newJob(null, (progressHandle) -> {
            ships = readAllShips();
            persistAllShips();
            ships.clear();
            ships.addAll(findAllShips());
            System.out.println(ships);
        });
    }

    public void persistAllShips() {
        em.getTransaction().begin();
        ships.stream().forEach((s) -> {
            em.persist(s);
        });
        em.getTransaction().commit();
    }

    public Collection<Ship> findAllShips() {
        query = em.createQuery("SELECT s FROM Ship s");
        return (Collection<Ship>) query.getResultList();
    }

    /*
     *
     Neo4J Section 
     *
     */
    private static enum RelTypes
            implements RelationshipType {
        KNOWS
    }
    GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Node ownerShipNode;
    Node shipNode;
    Ship ownerShip;
    Relationship relationship;
    List<Relationship> relationshipList;

    @Override
    public Connection connect(String hostName, String protocol, String port, String driverName, String userName, String passwd) {
        connection = databaseServices.connect(hostName, protocol, port, driverName, userName, passwd);
        return connection;
    }

    @Override
    public void runEmbeddedNeo4J(String dbName) {
        graphDb = graphDatabaseComponentServices.newEmbeddedDatabase(dbName);
        ownerShip = new Ship();
        ownerShip.setName("Lithops");
        relationshipList = new ArrayList<>();
        ships = readAllShips();
        try (Transaction tx = graphDb.beginTx()) {

            firstNode = graphDb.createNode();
            firstNode.setProperty("message", "Hello, ");

            secondNode = graphDb.createNode();
            secondNode.setProperty("message", "World!");

            relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
            relationship.setProperty("message", "brave Neo4j ");

            System.out.print(firstNode.getProperty("message"));
            System.out.print(relationship.getProperty("message"));
            System.out.println(secondNode.getProperty("message"));

            // let's remove the data
            firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete();
            firstNode.delete();
            secondNode.delete();

            ownerShipNode = graphDb.createNode();
            ownerShipNode.setProperty("name", ownerShip.getName());
            System.out.println(ownerShipNode.getProperty("name"));

            ships.stream().filter((s) -> (s.getShipName() != null)).map((s) -> {
                shipNode = graphDb.createNode();
                return s;
            }).map((s) -> {
                shipNode.setProperty("name", s.getName());
                return s;
            }).forEach((_item) -> {
                ownerShipNode.createRelationshipTo(shipNode, RelTypes.KNOWS);
            });
            tx.success();
            /*
            String rows = null;
            
            try (Transaction ignored = graphDb.beginTx();
                    
                    Result result = graphDb.execute("match (n {name: 'shipNode'}) return n, n.name")) {
                while (result.hasNext()) {
                    Map<String, Object> row = result.next();
                    for (Entry<String, Object> column : row.entrySet()) {
                        rows += column.getKey() + ": " + column.getValue() + "; ";
                    }
                    rows += "\n";
                }
                System.out.println(rows);
                ignored.success();
            }
             */
        }
    }

    @Override
    public void componentInitiated() {
        ships = new ArrayList<>();
    }

    @Override
    public void componentStarted() {
    }

    @Override
    public void componentStopped() {
    }

    @Override
    public boolean canOpen(String dbName) {
        return dbName.equalsIgnoreCase(NAME);
    }

    @Override
    public DatabaseDriver getDriver() {
        return this;
    }
}
