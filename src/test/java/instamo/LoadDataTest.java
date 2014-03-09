package instamo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import instamo.factory.MiniAccumuloFactory;
import instamo.wrapper.cluster.MiniAccumuloClusterWrapper;
import instamo.wrapper.config.MiniAccumuloConfigWrapper;

import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class LoadDataTest {

    @Test
    public void testLoadFromFile() throws IOException, InterruptedException {
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        String rootPassword = "apasswordhere";
        MiniAccumuloConfigWrapper config = MiniAccumuloFactory.createConfig(folder.getRoot(), rootPassword);
        String inputFileName = "sample.txt";
        String inputFilePath = ClassLoader.getSystemClassLoader().getResource(inputFileName).getPath();
        config.setInputFilePath(inputFilePath);

        MiniAccumuloClusterWrapper accumulo = MiniAccumuloFactory.createCluster(config);
        accumulo.start();

        Example example = new Example(accumulo, rootPassword);
        String table = "foo"; // from sample.txt
        example.overrideTable(table);
        try {
            assertEquals("Should have 2 results", 2, example.scanRow("a").size());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
