package instamo.wrapper;

import java.io.File;
import java.io.IOException;

public class MiniAccumuloClusterWrapper1_5_0 extends MiniAccumuloClusterWrapper {

    public MiniAccumuloClusterWrapper1_5_0(File tmpDir, String password)
            throws IOException {
        super(tmpDir, password);
    }

}
