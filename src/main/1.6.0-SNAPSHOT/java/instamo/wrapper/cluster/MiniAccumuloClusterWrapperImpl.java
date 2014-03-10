package instamo.wrapper.cluster;

import instamo.wrapper.config.MiniAccumuloConfigWrapper;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class MiniAccumuloClusterWrapperImpl extends MiniAccumuloClusterAbstractBase {

    private static final Logger log = Logger.getLogger(MiniAccumuloClusterWrapperImpl.class);

    public MiniAccumuloClusterWrapperImpl(File tmpDir, String password)
            throws IOException {
        super(tmpDir, password);
    }

    public MiniAccumuloClusterWrapperImpl(MiniAccumuloConfigWrapper config) throws IOException {
        super(config);
    }

    @Override
    public void start() throws IOException, InterruptedException {
        super.start();
        if (configWrapper.getStartMonitor() == true) {
            log.info("Starting monitor");
            log.error("Monitor not implemented yet for 1.6.0-SNAPSHOT");
        }
    }


 }
