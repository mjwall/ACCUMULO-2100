package instamo.wrapper.config;

import java.io.File;

import org.apache.accumulo.minicluster.MiniAccumuloConfig;

public abstract class MiniAccumuloConfigAbstractBase extends MiniAccumuloConfig implements MiniAccumuloConfigWrapper {

    public MiniAccumuloConfigAbstractBase(File dir, String rootPassword) {
        super(dir, rootPassword);
    }

    protected File inputFile = null;

    @Override
    public MiniAccumuloConfigWrapper setInputFile(File file) {
        this.inputFile = file;
        return this;
    }

    @Override
    public File getInputFile() {
        return this.inputFile;
    }
}
