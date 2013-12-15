package bzh.terrevirtuelle.navisu.api.progress.impl.view;

import bzh.terrevirtuelle.navisu.api.progress.ProgressHandle;
import bzh.terrevirtuelle.navisu.core.view.display.jfx.impl.JFXAbstractDisplay;
import javafx.scene.Node;

/**
 * NaVisu
 *
 * @author tibus
 * @date 15/12/2013 15:03
 */
public class JobViewCtrl extends JFXAbstractDisplay implements ProgressHandle {

    protected final JobView view;
    protected int finalWorkUnit, currentWorkUnit;
    protected String title, description;
    protected boolean isIndeterminate;


    public JobViewCtrl(final String displayName) {
        this.view = new JobView();
        this.title = displayName;
        this.isIndeterminate = true;
        this.updateView();
    }

    public JobViewCtrl(final String displayName, int workunit) {
        this.view = new JobView();
        this.title = displayName;
        this.isIndeterminate = false;
        this.finalWorkUnit = workunit;
        this.currentWorkUnit = 0;
        this.updateView();
    }

    @Override
    public Node getDisplayable() {
        return this.view;
    }

    //-----------------------------------------------------------------------------------//
    // ProgressHandle implementation
    //
    @Override
    public void start() {
        this.isIndeterminate = true;
        this.updateView();
    }

    @Override
    public void start(int workunits) {
        this.isIndeterminate = false;
        this.finalWorkUnit = workunits;
        this.currentWorkUnit = 0;
        this.updateView();
    }

    @Override
    public void finish() {
        //TODO send an event to kill the job
    }

    @Override
    public void progress(int workunit) {
        this.currentWorkUnit = workunit;
        this.updateView();
    }

    @Override
    public void progress(String message) {
        this.description = message;
        this.updateView();
    }

    @Override
    public void progress(String message, int workunit) {
        this.currentWorkUnit = workunit;
        this.description = message;
        this.updateView();
    }

    @Override
    public void setDisplayName(String newDisplayName) {
        this.title = newDisplayName;
        this.updateView();
    }

    @Override
    public void suspend(String message) {
        this.isIndeterminate = true; //TODO see if it must be indeterminate
        this.description = message;
        this.updateView();
    }

    @Override
    public void switchToDeterminate(int workunits) {
        this.isIndeterminate = false;
        this.finalWorkUnit = workunits;
        this.updateView();
    }

    @Override
    public void switchToIndeterminate() {
        this.isIndeterminate = true;
        this.updateView();
    }
    //
    //-----------------------------------------------------------------------------------//

    protected void updateView() {

        this.view.titleText.setText(this.title);
        this.view.descriptionText.setText(this.description);

        if(this.isIndeterminate) {
            this.view.progressBar.setProgress(-1d);
        }
        else {

            double progress = (double)this.currentWorkUnit / this.finalWorkUnit;
            this.view.progressBar.setProgress(progress);
        }
    }
}
