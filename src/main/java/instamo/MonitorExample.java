package instamo;

import instamo.factory.MiniAccumuloFactory;
import instamo.wrapper.cluster.MiniAccumuloClusterWrapper;
import instamo.wrapper.config.MiniAccumuloConfigWrapper;

import java.io.File;
import java.io.IOException;

import org.apache.accumulo.core.util.shell.Shell;

import com.google.common.io.Files;

public class MonitorExample implements Runnable {

    @Override
    public void run() {
      File tempDir = null;
      MiniAccumuloClusterWrapper mac = null;

      try {
        tempDir = Files.createTempDir();

        final String PASSWORD = "pass1234";

        MiniAccumuloConfigWrapper config = MiniAccumuloFactory.createConfig(tempDir, PASSWORD);
        config.setStartMonitor(true);

        mac = MiniAccumuloFactory.createCluster(config);

        mac.start();

        String[] args = new String[] {"-u", "root", "-p", PASSWORD, "-z",
          mac.getInstanceName(), mac.getZooKeepers()};

        Shell.main(args);

      } catch (InterruptedException e) {
        System.err.println("Error starting MiniAccumuloCluster: " + e.getMessage());
        System.exit(1);
      } catch (IOException e) {
        System.err.println("Error starting MiniAccumuloCluster: " + e.getMessage());
        System.exit(1);
      } finally {
        if (null != mac) {
          try {
            mac.stop();
          } catch (InterruptedException e) {
            System.err.println("Error stopping MiniAccumuloCluster: " + e.getMessage());
            System.exit(1);
          } catch (IOException e) {
            System.err.println("Error stopping MiniAccumuloCluster: " + e.getMessage());
            System.exit(1);
          }
        }
      }
    }

    public static void main(String[] args) {
      System.out.println("\n   ---- Initializing Accumulo Shell with the monitor\n");

      MonitorExample shell = new MonitorExample();
      shell.run();

      System.exit(0);
    }

}
