package instamo.wrapper.cluster;

import java.io.IOException;


/**
 * Interface for the MiniAccumuloClusterWrapper
 */
public interface MiniAccumuloClusterWrapper {

    public void start() throws IOException, InterruptedException;
    public void stop() throws IOException, InterruptedException;
    public String getInstanceName();
    public String getZooKeepers();

}
