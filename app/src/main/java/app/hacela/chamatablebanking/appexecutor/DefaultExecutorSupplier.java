package app.hacela.chamatablebanking.appexecutor;

import java.util.concurrent.Executor;

/**
 * Created by Job on Tuesday : 4/24/2018.
 * Singleton class for default executor supplier
 */
public class DefaultExecutorSupplier {

    /*
     * Number of cores to decide the number of threads
     */
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();


    /*
     * thread pool executor for main thread tasks
     */
    private final Executor mMainThreadExecutor;
    /*
     * an instance of DefaultExecutorSupplier
     */
    private static DefaultExecutorSupplier sInstance;

    /*
     * returns the instance of DefaultExecutorSupplier
     */
    public static DefaultExecutorSupplier getInstance() {
        if (sInstance == null) {
            synchronized (DefaultExecutorSupplier.class) {
                sInstance = new DefaultExecutorSupplier();
            }
        }
        return sInstance;
    }

    /*
     * constructor for  DefaultExecutorSupplier
     */
    private DefaultExecutorSupplier() {



        // setting the thread pool executor for mMainThreadExecutor;
        mMainThreadExecutor = new MainThreadExecutor();
    }

    /*
     * returns the thread pool executor for main thread task
     */
    public Executor forMainThreadTasks() {
        return mMainThreadExecutor;
    }
}
