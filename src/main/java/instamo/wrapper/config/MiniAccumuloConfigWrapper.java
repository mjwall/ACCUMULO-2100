package instamo.wrapper.config;

import java.io.File;
import java.util.Map;

import org.apache.accumulo.minicluster.MiniAccumuloConfig;

public interface MiniAccumuloConfigWrapper {

    public File getDir();
    public String getRootPassword();
    public int getNumTservers();
    public MiniAccumuloConfig setNumTservers(int numTservers);
    public Map<String,String> getSiteConfig();
    public MiniAccumuloConfig setSiteConfig(Map<String,String> siteConfig);

    public MiniAccumuloConfigWrapper setInputFilePath(String inputFilePath);
    public String getInputFilePath();
 }
