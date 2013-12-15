package bzh.terrevirtuelle.navisu.api.progress;

/**
 * NaVisu
 *
 * @author tibus
 * @date 02/12/2013 21:18
 */
@FunctionalInterface
public interface Job {

    public static final JobManager manager = JobManager.instance;

    void run(ProgressHandle progressHandle);
}
