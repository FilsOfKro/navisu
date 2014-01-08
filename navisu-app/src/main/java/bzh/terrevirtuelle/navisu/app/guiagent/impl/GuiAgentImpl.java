/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bzh.terrevirtuelle.navisu.app.guiagent.impl;

import bzh.terrevirtuelle.navisu.api.progress.Job;
import bzh.terrevirtuelle.navisu.api.progress.JobsManager;
import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgent;
import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.app.guiagent.geoview.GeoViewServices;
import bzh.terrevirtuelle.navisu.app.guiagent.geoview.impl.GeoViewImpl;
import bzh.terrevirtuelle.navisu.app.guiagent.layertree.LayerTreeServices;
import bzh.terrevirtuelle.navisu.app.guiagent.layertree.impl.LayerCheckTreeImpl;
import bzh.terrevirtuelle.navisu.app.guiagent.menu.DefaultMenuEnum;
import bzh.terrevirtuelle.navisu.app.guiagent.menu.MenuManagerServices;
import bzh.terrevirtuelle.navisu.app.guiagent.menu.impl.MenuManagerImpl;
import bzh.terrevirtuelle.navisu.app.guiagent.options.OptionsManagerServices;
import bzh.terrevirtuelle.navisu.app.guiagent.options.impl.OptionsManagerImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.capcaval.c3.component.annotation.SubComponent;
import org.capcaval.c3.component.annotation.UsedService;
import org.capcaval.c3.componentmanager.ComponentManager;

import java.io.IOException;
import java.util.logging.Logger;

import  bzh.terrevirtuelle.navisu.app.guiagent.utilities.Translator;

/**
 * NaVisu
 *
 * @author tibus
 * @date 02/11/2013 11:54
 */
public class GuiAgentImpl implements GuiAgent, GuiAgentServices {

    private static final Logger LOGGER = Logger.getLogger(GuiAgentImpl.class.getName());

    @SubComponent OptionsManagerImpl optionsManager;
    @UsedService OptionsManagerServices optionsManagerServices;

    @SubComponent MenuManagerImpl menu;
    @UsedService MenuManagerServices menuServices;

    @SubComponent LayerCheckTreeImpl layerTree;
    @UsedService LayerTreeServices layerTreeServices;

    @SubComponent GeoViewImpl geoView;
    @UsedService  GeoViewServices geoViewServices;

    protected JobsManager jobsManager;

    protected int width;
    protected int height;
    
    protected Stage stage;

    @Override
    public void showGui(Stage stage, int width, int height) {

        this.width = width;
        this.height = height;

        this.stage = stage;

        this.jobsManager = JobsManager.create();

        StackPane root = null;
        final FXMLLoader loader = new FXMLLoader();
        GuiAgentController ctrl = null;

        try {
            root = loader.load(GuiAgentImpl.class.getResourceAsStream("GuiAgent.fxml"));
            ctrl = loader.getController();
        } catch (IOException e) {
            LOGGER.severe("Cannot load GuiAgent.fxml !");
            System.exit(0);
        }

        Scene scene = new Scene(root, this.width, this.height, Color.ALICEBLUE);

        // Place scene components
        ctrl.leftBorderPane.setCenter(layerTreeServices.getDisplayService().getDisplayable());
        ctrl.centerBorderPane.setCenter(geoViewServices.getDisplayService().getDisplayable());

        ctrl.statusBorderPane.setRight(jobsManager.getDisplay().getDisplayable());

        // Initialize menu
        this.menuServices.setMenuComponent(ctrl.menuBar);
        this.initializeMenuItems(this.menuServices);

        stage.setTitle("NaVisu");
        stage.setOnCloseRequest(e -> {
            LOGGER.info("Stop Application");
            ComponentManager.componentManager.stopApplication();
            System.exit(0);
        });
        stage.setScene(scene);
        stage.show();
    }

    protected void initializeMenuItems(final MenuManagerServices menuServices) {

        MenuItem fileMenuItem = new MenuItem(Translator.tr("menu.file.exit"));
        fileMenuItem.setOnAction(e -> {

            ComponentManager.componentManager.stopApplication();
            System.exit(0);
        });
        menuServices.addMenuItem(DefaultMenuEnum.FILE, fileMenuItem);

        MenuItem preferenceMenuItem = new MenuItem(Translator.tr("menu.edit.preferences"));
        preferenceMenuItem.setOnAction(e -> {
            optionsManagerServices.show();
        });
        menuServices.addMenuItem(DefaultMenuEnum.EDIT, preferenceMenuItem);;
    }

    @Override
    public JobsManager getJobsManager() {
        return this.jobsManager;
    }

    @Override
    public boolean isFullScreen() {
        return this.stage.isFullScreen();
    }

    @Override
    public void setFullScreen(boolean fullScreen) {
        this.stage.setFullScreen(true);
    }
}
