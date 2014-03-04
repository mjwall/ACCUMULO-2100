package instamo;

import static org.junit.Assert.assertEquals;
import instamo.factory.MiniAccumuloFactory;
import instamo.wrapper.cluster.MiniAccumuloClusterWrapper;
import instamo.wrapper.config.MiniAccumuloConfigWrapper;

import java.io.IOException;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ExampleTest {

    private MiniAccumuloClusterWrapper accumulo;
    private String rootPassword;

    @Before
    public void setUp() throws IOException, InterruptedException {
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        rootPassword = "apasswordhere";
        MiniAccumuloConfigWrapper config = MiniAccumuloFactory.createConfig(folder.getRoot(), rootPassword);
        accumulo = MiniAccumuloFactory.createCluster(config);
        accumulo.start();
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        accumulo.stop();
    }

    @Test
    public void testScanData() throws AccumuloException, AccumuloSecurityException, TableExistsException, TableNotFoundException {
        Example example = new Example(accumulo, rootPassword);
        example.insertData();
        assertEquals(16, example.scanData().size());
    }
}
