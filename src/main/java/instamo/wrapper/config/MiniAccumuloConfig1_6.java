package instamo.wrapper.config;

import java.io.File;

import org.apache.accumulo.minicluster.MiniAccumuloConfig;

public class MiniAccumuloConfig1_6 extends MiniAccumuloConfigAbstractBase {

    public MiniAccumuloConfig1_6(File dir, String rootPassword) {
        super(dir, rootPassword);
    }

}
