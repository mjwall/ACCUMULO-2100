package instamo;

import instamo.wrapper.MiniAccumuloClusterWrapper;
import instamo.wrapper.MiniAccumuloClusterWrapper1_5_0;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.hdfs.server.namenode.UnsupportedActionException;

public class MiniAccumuloFactory {
    public static MiniAccumuloClusterWrapper create(File tmpDir, String password) throws IOException {
        String accumuloVersion = System.getProperty("accumulo.version");
        if (null == accumuloVersion) {
            throw new RuntimeException("accumulo.version was not set, try something like -Daccumulo.version=1.5.0");
        }
        if (accumuloVersion.equals("1.5.0")) {
            return new MiniAccumuloClusterWrapper1_5_0(tmpDir, password);
        } else {
            throw new UnsupportedActionException("Accumulo version not supported: " + accumuloVersion);
        }
    }
}
