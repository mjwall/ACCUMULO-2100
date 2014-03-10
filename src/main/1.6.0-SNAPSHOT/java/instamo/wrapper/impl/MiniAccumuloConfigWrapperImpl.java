package instamo.wrapper.impl;

import instamo.wrapper.MiniAccumuloConfigAbstractBase;

import java.io.File;

public class MiniAccumuloConfigWrapperImpl extends MiniAccumuloConfigAbstractBase {

    public MiniAccumuloConfigWrapperImpl(File dir, String rootPassword) {
        super(dir, rootPassword);
    }
}
