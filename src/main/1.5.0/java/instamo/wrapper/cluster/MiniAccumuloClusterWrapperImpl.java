package instamo.wrapper.cluster;

import instamo.wrapper.config.MiniAccumuloConfigWrapper;

import java.io.File;
import java.io.IOException;

import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.file.FileUtil;
import org.apache.accumulo.core.util.CachedConfiguration;
import org.apache.accumulo.server.Accumulo;
import org.apache.accumulo.server.client.HdfsZooInstance;
import org.apache.accumulo.server.conf.ServerConfiguration;
import org.apache.accumulo.server.monitor.Monitor;
import org.apache.hadoop.fs.FileSystem;
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
            try {
                Instance instance = HdfsZooInstance.getInstance();
                ServerConfiguration serverConfiguration = new ServerConfiguration(instance);
                FileSystem fs = FileUtil.getFileSystem(CachedConfiguration.getInstance(), ServerConfiguration.getSiteConfiguration());
                Accumulo.init(fs, serverConfiguration, "monitor");
                Monitor monitor = new Monitor();
                monitor.run("localhost");
            } catch (Exception e) {
                log.error("Problem starting monitor", e);
                System.exit(3);
            }
        }
    }


 }
