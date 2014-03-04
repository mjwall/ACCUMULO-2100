package instamo.wrapper.config;

import java.io.File;

import org.apache.accumulo.minicluster.MiniAccumuloConfig;

public class MiniAccumuloConfig1_5 extends MiniAccumuloConfig implements MiniAccumuloConfigWrapper {

    public MiniAccumuloConfig1_5(File dir, String rootPassword) {
        super(dir, rootPassword);
    }
}
