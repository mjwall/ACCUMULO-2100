package instamo.wrapper.cluster;

import instamo.wrapper.config.MiniAccumuloConfigWrapper;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class MiniAccumuloCluster1_6 extends MiniAccumuloClusterAbstractBase {

    private static final Logger log = Logger.getLogger(MiniAccumuloCluster1_6.class);

    public MiniAccumuloCluster1_6(File tmpDir, String password)
            throws IOException {
        super(tmpDir, password);
    }

    public MiniAccumuloCluster1_6(MiniAccumuloConfigWrapper config) throws IOException {
        super(config);
    }
}
