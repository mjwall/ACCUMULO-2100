package instamo.wrapper.cluster;

import instamo.wrapper.config.MiniAccumuloConfig1_6;
import instamo.wrapper.config.MiniAccumuloConfigWrapper;

import java.io.File;
import java.io.IOException;

import org.apache.accumulo.minicluster.MiniAccumuloCluster;
import org.apache.accumulo.minicluster.MiniAccumuloConfig;
import org.apache.log4j.Logger;

public class MiniAccumuloCluster1_6 extends MiniAccumuloCluster implements MiniAccumuloClusterWrapper {

    private static final Logger log = Logger.getLogger(MiniAccumuloCluster1_6.class);

    private MiniAccumuloConfigWrapper config;

    public MiniAccumuloCluster1_6(File tmpDir, String password) throws IOException {
        super(tmpDir, password);
        this.config = new MiniAccumuloConfig1_6(tmpDir, password);
        showConfigDir();
    }

    public MiniAccumuloCluster1_6(MiniAccumuloConfigWrapper cfg) throws IOException {
        super((MiniAccumuloConfig) cfg);
        this.config = cfg;
        showConfigDir();
    }

    private void showConfigDir() {
        log.debug("Using " + config.getDir()) ;
    }

    @Override
    /**
     * Override stop and cleanup the directory, so you don't have to remember to do that.
     */
    public void stop() throws IOException, InterruptedException  {
        super.stop();
        /**
        if (cleanupConfigDir) {
            log.debug("Attempting to remove " + config.getDir());
            FileUtils.deleteQuietly(config.getDir());
        } else {
            log.debug("Not removing " + config.getDir());
        }
        **/
    }

}
