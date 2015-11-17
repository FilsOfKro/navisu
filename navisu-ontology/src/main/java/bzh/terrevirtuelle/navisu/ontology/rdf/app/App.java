/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.ontology.rdf.app;

import bzh.terrevirtuelle.navisu.domain.navigation.NavigationData;
import bzh.terrevirtuelle.navisu.domain.navigation.avurnav.rss.AvurnavRSS;
import bzh.terrevirtuelle.navisu.domain.navigation.avurnav.rss.Rss;
import bzh.terrevirtuelle.navisu.domain.navigation.NavigationDataSet;
import bzh.terrevirtuelle.navisu.domain.navigation.controller.RdfParser;
import bzh.terrevirtuelle.navisu.domain.ship.model.Ship;
import bzh.terrevirtuelle.navisu.domain.ship.model.ShipBuilder;
import bzh.terrevirtuelle.navisu.util.xml.ImportExportXML;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;

/**
 * NaVisu
 *
 * @date 26 oct. 2015
 * @author Serge Morvan
 */
public class App {

    String SHIP_NAME = "Lithops";
    // String NAME_RDF = "requeteArea";
    // String NAME_RDF = "requeteAvurnav";
    String NAME_RDF = "requete";
    String RESULT = "result";
    String DIR_SRC = "data/";
    String DIR_TARGET = "data/";

    public App() {
        NavigationDataSet navigationDataSet = new NavigationDataSet(); // Ne pas commenter

        // Les avurnav du shom sont des requetes Sparql, 
        // Instanciation d'un AvurnavSet
        // sauvegarde dans un  NavigationDataSet
        RdfParser rdfParser = new RdfParser();
        List<NavigationData> navigationDatas = rdfParser.parse(DIR_SRC, NAME_RDF, "rdf");
        navigationDatas.stream().forEach((n) -> {
            navigationDataSet.add(n);
        });
        // Ne pas commenter
        // sauvegarde du NavigationDataSet dans un fic
        try {
            ImportExportXML.exports(navigationDataSet, DIR_SRC + RESULT + ".xml");
        } catch (JAXBException | FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Lecture du fic et instanciation des entitees
        NavigationDataSet nds = null;
        try {
            nds = ImportExportXML.imports(navigationDataSet, DIR_SRC + RESULT + ".xml");
        } catch (JAXBException | FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(nds);
    }

    public static void main(String[] args) {
        new App();

    }
}
