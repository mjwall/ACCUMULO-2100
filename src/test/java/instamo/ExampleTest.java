package instamo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import instamo.factory.MiniAccumuloFactory;
import instamo.wrapper.cluster.MiniAccumuloClusterWrapper;
import instamo.wrapper.config.MiniAccumuloConfigWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.hadoop.io.Text;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ExampleTest {

    private static MiniAccumuloClusterWrapper accumulo;
    private static String rootPassword;
    private static Example example;

    @BeforeClass
    public static void setUp() throws IOException, InterruptedException {
        // start up the MiniAccumuloCluster
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        rootPassword = "apasswordhere";
        MiniAccumuloConfigWrapper config = MiniAccumuloFactory.createConfig(folder.getRoot(), rootPassword);
        accumulo = MiniAccumuloFactory.createCluster(config);
        accumulo.start();

        example = new Example(accumulo, rootPassword);
        try {
            example.ensureTableCreated();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        accumulo.stop();
    }

    @Test
    public void testScanningRow() {
        try {
            // insert some data
            example.insertRow("a", "b","c","d");
            example.insertRow("a", "e","f","g");
            example.insertRow("a", "h","i","j");
            example.insertRow("k", "l","m","n");

            // now scan for it
            HashMap<Key, Value> result = example.scanRow("a");
            assertEquals("Expected 3 results", 3,  result.size());

            System.out.println(result.size());
            long timestampForJ = 0;
            for(Key k : result.keySet()) {
                if (k.getColumnFamily().toString().equals("h")) {
                    timestampForJ = k.getTimestamp();
                }
                System.out.println(k);
                System.out.println(result.get(k));
            }
            Key k = new Key(new Text("a"), new Text("h"), new Text("i"), timestampForJ);
            System.out.println(k);
            System.out.println(result.get(k));

            assertEquals("Key a h:i should have Value j", new Value("j".getBytes()), result.get(k));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testScanningRowWithAuths() {
        try {
            // insert some data
            example.insertRow("a", "b","c","d","A|B");
            example.insertRow("a", "e","f","g", "A&B");
            example.insertRow("a", "h","i","j", "B");

            // now make sure the root user has the auths to scan with
            example.ensureRootAuths(Arrays.asList("A","B"));

            // now scan for it
            assertEquals("Expected 3 results with A and B", 3,  example.scanRow("a", Arrays.asList("A","B")).size());
            assertEquals("Expected 2 results with just B", 2,  example.scanRow("a", Arrays.asList("B")).size());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
