package instamo.wrapper.cluster;

import instamo.factory.MiniAccumuloFactory;
import instamo.wrapper.config.MiniAccumuloConfigWrapper;

import java.io.File;
import java.io.IOException;

import org.apache.accumulo.core.util.shell.Shell;
import org.apache.accumulo.minicluster.MiniAccumuloCluster;
import org.apache.accumulo.minicluster.MiniAccumuloConfig;
import org.apache.log4j.Logger;

public class MiniAccumuloClusterAbstractBase extends MiniAccumuloCluster implements MiniAccumuloClusterWrapper {

    private static final Logger log = Logger.getLogger(MiniAccumuloClusterAbstractBase.class);

    protected MiniAccumuloConfigWrapper config;

    public MiniAccumuloClusterAbstractBase(File tmpDir, String password) throws IOException {
        super(tmpDir, password);
        this.config = MiniAccumuloFactory.createConfig(tmpDir, password);
        showConfigDir();
    }

    public MiniAccumuloClusterAbstractBase(MiniAccumuloConfigWrapper cfg) throws IOException {
        super((MiniAccumuloConfig) cfg);
        this.config = cfg;
        showConfigDir();
    }

    private void showConfigDir() {
        log.debug("Using " + config.getDir()) ;
    }

    @Override
    public void start() throws IOException, InterruptedException {
        super.start();
        if (null != config.getInputFilePath()) {
            String inputFilePath = config.getInputFilePath();
            log.info("Loading data from " + inputFilePath);
            String[] args = new String[] {"-u", "root",
                    "-p", config.getRootPassword(),
                    "-f", inputFilePath,
                    "-z", this.getInstanceName(), this.getZooKeepers()};
            // Shell.main calls System.exit, which surefire balks at with a
            // The forked VM terminated without saying properly goodbye. VM crash or System.exit called
            // Make sure the last line of you input file is exit
            //Shell.main(args);
            Shell shell = new Shell();
            shell.config(args);
            shell.start();
        }
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
