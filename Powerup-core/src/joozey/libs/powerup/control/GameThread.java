package joozey.libs.powerup.control;

import java.util.PriorityQueue;

/**
 * Created by acer on 31-5-2014.
 */
public class GameThread implements Runnable
{
    private PriorityQueue<GameRunnable> queue;

    public GameThread()
    {
        this.queue = new PriorityQueue<GameRunnable>();
    }

    @Override
    public void run()
    {
        for (GameRunnable runnable : queue) {
            if (runnable.isInitialised() == true) {
                runnable.run();
            }
        }
    }

    public void register( GameRunnable runnable )
    {
        this.queue.offer(runnable);
    }

    public void remove( GameRunnable runnable )
    {
        this.queue.remove( runnable );
    }
}