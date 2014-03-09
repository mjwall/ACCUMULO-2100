package instamo.wrapper.config;

import java.io.File;

import org.apache.accumulo.minicluster.MiniAccumuloConfig;

public abstract class MiniAccumuloConfigAbstractBase extends MiniAccumuloConfig implements MiniAccumuloConfigWrapper {

    public MiniAccumuloConfigAbstractBase(File dir, String rootPassword) {
        super(dir, rootPassword);
    }

    protected String inputFilePath = null;

    @Override
    public MiniAccumuloConfigWrapper setInputFilePath(String path) {
        this.inputFilePath = path;
        return this;
    }

    @Override
    public String getInputFilePath() {
        return this.inputFilePath;
    }
}
