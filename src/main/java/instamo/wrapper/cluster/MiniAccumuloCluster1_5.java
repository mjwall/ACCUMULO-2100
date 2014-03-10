package instamo.wrapper.cluster;

import instamo.wrapper.config.MiniAccumuloConfigWrapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.file.FileUtil;
import org.apache.accumulo.core.util.CachedConfiguration;
import org.apache.accumulo.minicluster.MiniAccumuloCluster;
import org.apache.accumulo.server.Accumulo;
import org.apache.accumulo.server.client.HdfsZooInstance;
import org.apache.accumulo.server.conf.ServerConfiguration;
import org.apache.accumulo.server.monitor.Monitor;
import org.apache.accumulo.server.util.time.SimpleTimer;
import org.apache.accumulo.start.Main;
import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.Logger;

public class MiniAccumuloCluster1_5 extends MiniAccumuloClusterAbstractBase {

    private static final Logger log = Logger.getLogger(MiniAccumuloCluster1_5.class);

    public MiniAccumuloCluster1_5(File tmpDir, String password)
            throws IOException {
        super(tmpDir, password);
    }

    public MiniAccumuloCluster1_5(MiniAccumuloConfigWrapper config) throws IOException {
        super(config);
    }

    @Override
    public void start() throws IOException, InterruptedException {
        super.start();
        if (configWrapper.getStartMonitor() == true) {
            log.info("Starting monitor");
            try {
                Instance instance = HdfsZooInstance.getInstance();
                ServerConfiguration serverConfiguration = new ServerConfiguration(instance);
                FileSystem fs = FileUtil.getFileSystem(CachedConfiguration.getInstance(), ServerConfiguration.getSiteConfiguration());
                Accumulo.init(fs, serverConfiguration, "monitor");
                Monitor monitor = new Monitor();
                monitor.run("localhost");
                //Monitor.main(new String[0]);
                //exec(Monitor.class, new String[]{"localhost"});
                //exec2(Monitor.class, new String[0]);
            } catch (Exception e) {
                log.error("Problem starting monitor", e);
                System.exit(3);
            }
        }
    }

    // the following stuff is copied from the MiniAccumuloCluster to expose exec
    // yeah, I know, yucky
    private static class LogWriter extends Thread {
        private BufferedReader in;
        private BufferedWriter out;

        /**
         * @throws IOException
         */
        public LogWriter(InputStream stream, File logFile) throws IOException {
          this.setDaemon(true);
          this.in = new BufferedReader(new InputStreamReader(stream));
          out = new BufferedWriter(new FileWriter(logFile));

          SimpleTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
              try {
                flush();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }, 1000, 1000);
        }

        public synchronized void flush() throws IOException {
          if (out != null)
            out.flush();
        }

        @Override
        public void run() {
          String line;

          try {
            while ((line = in.readLine()) != null) {
              out.append(line);
              out.append("\n");
            }

            synchronized (this) {
              out.close();
              out = null;
              in.close();
            }

          } catch (IOException e) {}
        }
      }

    private File confDir;
    private File logDir;
    private List<LogWriter> logWriters = new ArrayList<MiniAccumuloCluster1_5.LogWriter>();

    private Process exec(Class<? extends Object> clazz, String... args) throws IOException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");

        confDir = new File(configWrapper.getDir(), "conf");
        logDir = new File(configWrapper.getDir(), "logs");

        classpath = confDir.getAbsolutePath() + File.pathSeparator + classpath;

        String className = clazz.getCanonicalName();

        ArrayList<String> argList = new ArrayList<String>();

        argList.addAll(Arrays.asList(javaBin, "-cp", classpath, "-Xmx128m", "-XX:+UseConcMarkSweepGC", "-XX:CMSInitiatingOccupancyFraction=75",
            Main.class.getName(), className));

        argList.addAll(Arrays.asList(args));

        ProcessBuilder builder = new ProcessBuilder(argList);

        builder.environment().put("ACCUMULO_HOME", configWrapper.getDir().getAbsolutePath());
        builder.environment().put("ACCUMULO_LOG_DIR", logDir.getAbsolutePath());

        // if we're running under accumulo.start, we forward these env vars
        String env = System.getenv("HADOOP_PREFIX");
        if (env != null)
          builder.environment().put("HADOOP_PREFIX", env);
        env = System.getenv("ZOOKEEPER_HOME");
        if (env != null)
          builder.environment().put("ZOOKEEPER_HOME", env);

        Process process = builder.start();

        LogWriter lw;
        lw = new LogWriter(process.getErrorStream(), new File(logDir, clazz.getSimpleName() + "_" + process.hashCode() + ".err"));
        logWriters.add(lw);
        lw.start();
        lw = new LogWriter(process.getInputStream(), new File(logDir, clazz.getSimpleName() + "_" + process.hashCode() + ".out"));
        logWriters.add(lw);
        lw.start();

        return process;
      }


    /*
     * Hack to use reflection to call private exec method in the MiniAccumuloCluster class
     * Doesn't work with the API, can't really get back to the original MAC
     */
    public Process exec2(Class<? extends Object> clazz, String... args) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, ClassNotFoundException, InstantiationException {
        // doesn't work, not sure why
        //Class<?>[] argClasses = { Class.class, String[].class };
        //Method method = Monitor.class.getDeclaredMethod("exec",  argClasses);
        MiniAccumuloCluster mac = new MiniAccumuloCluster(configWrapper.getDir(), configWrapper.getRootPassword());
        Method method = null;
        for (Method m : mac.getClass().getDeclaredMethods()) {
            if ("exec".equals(m.getName())) {
                method = m;
//                System.out.println(m.toString());
//                for (Class<?> param : m.getParameterTypes()) {
//                    System.out.println("\t" + param.getComponentType());
//                }
//                System.out.println("----------");
            }
        }

        method.setAccessible(true);

        try {
            Monitor mon = new Monitor();
            return (Process) method.invoke(mac, new Object[]{clazz, args});
        } finally {
            method.setAccessible(false); // Reset accessibility
        }
    }

 }
