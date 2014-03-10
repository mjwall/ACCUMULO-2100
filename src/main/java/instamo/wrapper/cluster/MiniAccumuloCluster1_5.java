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

    @Override
    public void start() throws IOException, InterruptedException {
        super.start();
        if (configWrapper.getStartMonitor() == true) {
            log.info("Starting monitor");
            try {
                Instance instance = HdfsZooInstance.getInstance();
                ServerConfiguration serverConfiguration = new ServerConfiguration(instance);
                //FileUtil.getFileSystem(conf, acuconf)
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
