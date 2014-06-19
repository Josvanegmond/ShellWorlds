package joozey.libs.powerup.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import joozey.libs.powerup.game.GameData;

/**
 * Created by acer on 31-5-2014.
 */
public class ThreadManager
{
    public enum ThreadType
    {
        LOGIC,
        RENDER
    };

    private static GameThread logicThread;
    private static GameThread renderThread;

    public static void init( GameThread renderThread, GameThread logicThread )
    {
        ThreadManager.logicThread = logicThread;
        ThreadManager.renderThread = renderThread;
    }

    public static void register( GameRunnable runnable, ThreadType threadType )
    {
        if( threadType == ThreadType.LOGIC ) { ThreadManager.logicThread.register( runnable ); }
        if( threadType == ThreadType.RENDER ) { ThreadManager.renderThread.register( runnable ); }
    }
}
