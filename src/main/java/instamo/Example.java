package instamo;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.hadoop.io.Text;

import instamo.wrapper.MiniAccumuloClusterWrapper;

public class Example {
    private MiniAccumuloClusterWrapper accumulo;
    private String rootPassword;

    public String TABLE = "table1";

    public Example(MiniAccumuloClusterWrapper accumulo, String rootPassword) {
        this.accumulo = accumulo;
        this.rootPassword = rootPassword;
    }

    public void overrideTable(String tableName) {
        TABLE=tableName;
    }

    public Connector getConnector() throws AccumuloException, AccumuloSecurityException {
        Instance instance = new ZooKeeperInstance(accumulo.getInstanceName(), accumulo.getZooKeepers());
        Connector conn = instance.getConnector("root", new PasswordToken(rootPassword));
        return conn;
    }

    public void ensureRootAuths(List<String> auths) throws AccumuloException, AccumuloSecurityException {
        Connector connector = getConnector();
        connector.securityOperations().changeUserAuthorizations("root", new Authorizations((String[]) auths.toArray()));
    }

    public void ensureTableCreated() throws AccumuloException, AccumuloSecurityException, TableExistsException {
        Connector conn = getConnector();
        if (!conn.tableOperations().exists(TABLE)) {
            conn.tableOperations().create(TABLE);
        };
    }

    public void insertRow(String rowid, String cf, String cq, String val, String auths) throws AccumuloException, AccumuloSecurityException, TableNotFoundException {
        Connector conn = getConnector();
        BatchWriter bw = conn.createBatchWriter(TABLE, new BatchWriterConfig());

        Mutation m = new Mutation(new Text(rowid));
        if (null == auths) {
            m.put(new Text(cf), new Text(cq), new Value(val.getBytes()));
        } else {
            m.put(new Text(cf), new Text(cq), new ColumnVisibility(auths), new Value(val.getBytes()));
        }
        bw.addMutation(m);

        bw.close();
    }

    public void insertRow(String rowid, String cf, String cq, String val) throws AccumuloException, AccumuloSecurityException, TableNotFoundException {
        insertRow(rowid, cf, cq, val, null);
    }

    public HashMap<Key,Value> scanRow(String rowid, List<String> auths) throws AccumuloException, AccumuloSecurityException, TableNotFoundException {
        HashMap<Key, Value> result = new HashMap<Key, Value>();
        Connector conn = getConnector();
        Authorizations scanAuths = new Authorizations();
        if (null != auths) {
            scanAuths = new Authorizations((String[]) auths.toArray());
        }
        Scanner scanner = conn.createScanner(TABLE, scanAuths);
        scanner.setRange(new Range(rowid));
        for (Entry<Key,Value> entry : scanner) {
          result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public HashMap<Key, Value> scanRow(String rowid) throws AccumuloException, AccumuloSecurityException, TableNotFoundException {
        return scanRow(rowid, null);
    }
}
