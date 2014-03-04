package instamo.factory;

import instamo.wrapper.cluster.MiniAccumuloClusterWrapper;
import instamo.wrapper.cluster.MiniAccumuloCluster1_5;
import instamo.wrapper.cluster.MiniAccumuloCluster1_6;
import instamo.wrapper.config.MiniAccumuloConfig1_5;
import instamo.wrapper.config.MiniAccumuloConfig1_6;
import instamo.wrapper.config.MiniAccumuloConfigWrapper;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.hdfs.server.namenode.UnsupportedActionException;

public class MiniAccumuloFactory {
    public static MiniAccumuloClusterWrapper createCluster(File tmpDir, String password) throws IOException {
        String accumuloVersion = getAccumuloVersion();
        if (null == accumuloVersion) {
            throw new RuntimeException("accumulo.version was not set, try something like -Daccumulo.version=1.5.0");
        }
        if (accumuloVersion.equals("1.5.0") || accumuloVersion.equals("1.5.1")) {
            return new MiniAccumuloCluster1_5(tmpDir, password);
        } else if (accumuloVersion.equals("1.6.0-SNAPSHOT")) {
            return new MiniAccumuloCluster1_6(tmpDir, password);
        } else {
            throw new UnsupportedActionException("Accumulo version not supported: " + accumuloVersion);
        }
    }

    public static MiniAccumuloClusterWrapper createCluster(MiniAccumuloConfigWrapper config) throws IOException {
        String accumuloVersion = getAccumuloVersion();
        if (null == accumuloVersion) {
            throw new RuntimeException("accumulo.version was not set, try something like -Daccumulo.version=1.5.0");
        }
        if (accumuloVersion.equals("1.5.0") || accumuloVersion.equals("1.5.1")) {
            return new MiniAccumuloCluster1_5(config);
        } else if (accumuloVersion.equals("1.6.0-SNAPSHOT")) {
            return new MiniAccumuloCluster1_6(config);
        } else {
            throw new UnsupportedActionException("Accumulo version not supported: " + accumuloVersion);
        }
    }

    public static MiniAccumuloConfigWrapper createConfig(File dir, String rootPassword) throws UnsupportedActionException {
        String accumuloVersion = getAccumuloVersion();
        if (null == accumuloVersion) {
            throw new RuntimeException("accumulo.version was not set, try something like -Daccumulo.version=1.5.0");
        }
        if (accumuloVersion.equals("1.5.0") || accumuloVersion.equals("1.5.1")) {
            return new MiniAccumuloConfig1_5(dir, rootPassword);
        } else if (accumuloVersion.equals("1.6.0-SNAPSHOT")) {
            return new MiniAccumuloConfig1_6(dir, rootPassword);
        } else {
            throw new UnsupportedActionException("Accumulo version not supported: " + accumuloVersion);
        }

    }

    public static String getAccumuloVersion() {
        return System.getProperty("accumulo.version");
    }
}
