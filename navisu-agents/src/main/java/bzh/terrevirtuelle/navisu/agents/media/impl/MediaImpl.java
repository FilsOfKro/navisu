/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.agents.media.impl;

import bzh.terrevirtuelle.navisu.agents.media.Media;
import bzh.terrevirtuelle.navisu.agents.media.MediaServices;
import bzh.terrevirtuelle.navisu.api.progress.ProgressHandle;
import bzh.terrevirtuelle.navisu.app.drivers.Driver;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.media.MediaPlayer;
import org.capcaval.c3.component.ComponentState;

/**
 *
 * @author Serge Morvan
 * @date 9 oct. 2014 NaVisu project
 */
public class MediaImpl implements Media, MediaServices, Driver, ComponentState {

    private static final String EXTENSION = ".wav";
    javafx.scene.media.Media media;
    MediaPlayer mediaPlayer;

    @Override
    public void play() {
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(10);
        /*
        Timer time = new Timer(); // Instantiate Timer Object
        ScheduledTask st = new ScheduledTask(); // Instantiate SheduledTask class
        time.schedule(st, 0, 1000); // Create Repetitively task for every 1 secs
*/
    }

    @Override
    public Driver getDriver() {
        return this;
    }

    @Override
    public boolean canOpen(String file) {
        boolean canOpen = false;

        if (file.toLowerCase().endsWith(EXTENSION)) {
            canOpen = true;
        }

        return canOpen;
    }

    @Override
    public void open(ProgressHandle pHandle, String... files) {
        for (String file : files) {
            this.handleOpenFile(pHandle, file);
        }
    }

    protected void handleOpenFile(ProgressHandle pHandle, String fileName) {
        String url = fileName.replace("\\", "/");
        media = new javafx.scene.media.Media("file:///" + url);
        mediaPlayer = new MediaPlayer(media);
        play();
    }

    @Override
    public String getName() {
        return "Media";
    }

    @Override
    public String[] getExtensions() {
        return new String[]{"*" + EXTENSION};
    }

    @Override
    public void componentInitiated() {

    }

    @Override
    public void componentStarted() {

    }

    @Override
    public void componentStopped() {

    }

    class ScheduledTask extends TimerTask {

        Date now; // to display current time
        @Override
        public void run() {
            now = new Date(); // initialize date
            System.out.println("Time is :" + now); // Display current time
        }
    }
}
