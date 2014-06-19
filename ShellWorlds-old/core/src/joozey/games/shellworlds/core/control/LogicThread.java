package joozey.games.shellworlds.core.control;

import java.util.ArrayList;

import joozey.games.shellworlds.core.objects.BodyData;
import joozey.games.shellworlds.core.objects.BodyObject;
import joozey.games.shellworlds.core.ShellWorldData;
import joozey.libs.powerup.control.GameRunnable;
import joozey.libs.powerup.control.GameThread;
import joozey.libs.powerup.object.GameObject;

/**
 * Created by acer on 31-5-2014.
 */
public class LogicThread extends GameThread
{
    private boolean keepRunning = false;
    private ShellWorldData shellWorldData;
    private Thread thread;

    public LogicThread( ShellWorldData shellWorldData )
    {
        this.shellWorldData = shellWorldData;
        this.keepRunning = true;
        this.thread = new Thread( this );
        this.thread.start();
    }

    @Override
    public void run()
    {
        while( keepRunning == true )
        {
            super.run();

            try { Thread.sleep( 10 ); }
            catch( InterruptedException e ) {}
        }
    }

    public void requestStop()
    {
        this.keepRunning = false;
    }
}
