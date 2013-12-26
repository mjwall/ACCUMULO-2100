package instamo;

import org.apache.accumulo.minicluster.MiniAccumuloCluster;
import org.apache.accumulo.minicluster.MiniAccumuloConfig;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Just a wrapper for the MiniAccumuloCluster
 */
public class MiniAccumuloClusterWrapper extends MiniAccumuloCluster{
    private static final Logger log = Logger.getLogger(MiniAccumuloClusterWrapper.class);

    private MiniAccumuloConfig config;
    private boolean cleanupConfigDir = true;

    public MiniAccumuloClusterWrapper(File tmpDir, String password) throws IOException {
        super(tmpDir, password);
        this.config = new MiniAccumuloConfig(tmpDir, password);
        showConfigDir();
    }

    public MiniAccumuloClusterWrapper(MiniAccumuloConfig cfg) throws IOException {
        super(cfg);
        this.config = cfg;
        showConfigDir();
    }

    public MiniAccumuloClusterWrapper(File tmpDir, String password, boolean cleanup) throws IOException {
        super(tmpDir, password);
        this.config = new MiniAccumuloConfig(tmpDir, password);
        this.cleanupConfigDir = cleanup;
        showConfigDir();
    }

    public MiniAccumuloClusterWrapper(MiniAccumuloConfig cfg, boolean cleanup) throws IOException {
        super(cfg);
        this.config = cfg;
        this.cleanupConfigDir = cleanup;
        showConfigDir();
    }

    private void showConfigDir() {
        log.debug("Using " + config.getDir()) ;
    }

    @Override
    /**
     * Override stop and cleanup the directory, so you don't have to remember to do that.
     */
    public void stop() throws IOException, InterruptedException {
        super.stop();
        if (cleanupConfigDir) {
            log.debug("Attempting to remove " + config.getDir());
            FileUtils.deleteQuietly(config.getDir());
        } else {
            log.debug("Not removing " + config.getDir());
        }
    }

}
