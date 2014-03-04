package instamo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.accumulo.core.Constants;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.BatchWriterConfig;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Value;
import instamo.wrapper.cluster.MiniAccumuloClusterWrapper;

public class Example {
    private MiniAccumuloClusterWrapper accumulo;
    private String rootPassword;

    public Example(MiniAccumuloClusterWrapper accumulo, String rootPassword) {
        this.accumulo = accumulo;
        this.rootPassword = rootPassword;
    }

    public Connector getConnector() throws AccumuloException, AccumuloSecurityException {
        Instance instance = new ZooKeeperInstance(accumulo.getInstanceName(), accumulo.getZooKeepers());
        Connector conn = instance.getConnector("root", new PasswordToken(rootPassword));
        return conn;
    }

    public void insertData() throws AccumuloException, AccumuloSecurityException, TableExistsException, TableNotFoundException {

        Connector conn = getConnector();
        conn.tableOperations().create("foo");

        BatchWriter bw = conn.createBatchWriter("foo", new BatchWriterConfig());
        Mutation m = new Mutation("1234");
        m.put("name", "first", "Alice");
        m.put("friend", "5678", "");
        m.put("enemy", "5555", "");
        m.put("enemy", "9999", "");
        bw.addMutation(m);

        m = new Mutation("5678");
        m.put("name", "first", "Bob");
        m.put("friend", "1234", "");
        m.put("enemy", "5555", "");
        m.put("enemy", "9999", "");
        bw.addMutation(m);

        m = new Mutation("9999");
        m.put("name", "first", "Eve");
        m.put("friend", "5555", "");
        m.put("enemy", "1234", "");
        m.put("enemy", "5678", "");
        bw.addMutation(m);

        m = new Mutation("5555");
        m.put("name", "first", "Mallory");
        m.put("friend", "9999", "");
        m.put("enemy", "1234", "");
        m.put("enemy", "5678", "");
        bw.addMutation(m);

        bw.close();
    }

    public HashMap<Key,Value> scanData() throws AccumuloException, AccumuloSecurityException, TableNotFoundException {
        HashMap<Key, Value> result = new HashMap<Key, Value>();
        Connector conn = getConnector();
        Scanner scanner = conn.createScanner("foo", Constants.NO_AUTHS);
        for (Entry<Key,Value> entry : scanner) {
          result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
