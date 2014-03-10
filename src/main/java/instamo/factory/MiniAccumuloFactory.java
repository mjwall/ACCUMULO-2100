package instamo.factory;

import instamo.wrapper.cluster.MiniAccumuloClusterWrapper;
import instamo.wrapper.cluster.MiniAccumuloClusterWrapperImpl;
import instamo.wrapper.config.MiniAccumuloConfigWrapper;
import instamo.wrapper.config.MiniAccumuloConfigWrapperImpl;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.hdfs.server.namenode.UnsupportedActionException;
import org.apache.log4j.Logger;

public class MiniAccumuloFactory {
    static Logger logger = Logger.getLogger(MiniAccumuloFactory.class);
    static String DEFAULT_ACCUMULO_VERSION = "1.5.0";

    public static MiniAccumuloClusterWrapper createCluster(File tmpDir, String password) throws IOException {
        String accumuloVersion = ensureAccumuloVersion();
        return new MiniAccumuloClusterWrapperImpl(tmpDir, password);
    }

    public static MiniAccumuloClusterWrapper createCluster(MiniAccumuloConfigWrapper config) throws IOException {
        String accumuloVersion = ensureAccumuloVersion();
        return new MiniAccumuloClusterWrapperImpl(config);
    }

    public static MiniAccumuloConfigWrapper createConfig(File dir, String rootPassword) throws UnsupportedActionException {
        String accumuloVersion = ensureAccumuloVersion();
        return new MiniAccumuloConfigWrapperImpl(dir, rootPassword);
    }

    public static String ensureAccumuloVersion() {
        String version = System.getProperty("accumulo.version");
        if (null == version) {
            logger.info("No accumulo.version defined, using " + DEFAULT_ACCUMULO_VERSION);
            version = DEFAULT_ACCUMULO_VERSION;
        } else {
            logger.info("accumulo.version set to " + version);
        }
        return version;
    }
}
