package instamo.wrapper.cluster;

import instamo.wrapper.config.MiniAccumuloConfigWrapper;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class MiniAccumuloCluster1_5 extends MiniAccumuloClusterAbstractBase {

    private static final Logger log = Logger.getLogger(MiniAccumuloCluster1_5.class);

    public MiniAccumuloCluster1_5(File tmpDir, String password)
            throws IOException {
        super(tmpDir, password);
    }

    public MiniAccumuloCluster1_5(MiniAccumuloConfigWrapper config) throws IOException {
        super(config);
    }
}
