package instamo.wrapper;

import java.io.File;

import org.apache.accumulo.minicluster.MiniAccumuloConfig;

//Stuff implemented here is same code across different versions of accumulo
public abstract class MiniAccumuloConfigAbstractBase extends MiniAccumuloConfig implements MiniAccumuloConfigWrapper {

    public MiniAccumuloConfigAbstractBase(File dir, String rootPassword) {
        super(dir, rootPassword);
    }

    protected String inputFilePath = null;
    boolean startMonitor = false;

    @Override
    public MiniAccumuloConfigWrapper setInputFilePath(String path) {
        this.inputFilePath = path;
        return this;
    }

    @Override
    public String getInputFilePath() {
        return this.inputFilePath;
    }

    @Override
    public MiniAccumuloConfigWrapper setStartMonitor(boolean start) {
        this.startMonitor = start;
        return this;
    }

    @Override
    public boolean getStartMonitor() {
        return this.startMonitor;
    }
}
