package joozey.libs.powerup.control;

import java.util.LinkedList;
import java.util.Queue;
import joozey.libs.powerup.game.GameData;

public class GameThread extends Thread
{
	private boolean keepRunning;
	private Queue<GameRunnable> runnableQueue;
	private GameData gameData;

	public GameThread( GameData gameData )
	{
		this.runnableQueue = new LinkedList<GameRunnable>();
		this.keepRunning = true;
		this.gameData = gameData;

		this.start();
	}

	public GameData getGameData()
	{
		return this.gameData;
	}

	public void register( GameRunnable runnable )
	{
		this.runnableQueue.add( runnable );
	}

	public void remove( GameRunnable runnable )
	{
		this.runnableQueue.remove( runnable );
	}

	public void requestStop()
	{
		this.keepRunning = false;
	}

	@Override
	public void run()
	{
		while( this.keepRunning == true )
		{
            try
			{
				Queue<GameRunnable> runnableQueueClone = new LinkedList<GameRunnable>(
						this.runnableQueue );

                for( GameRunnable runnable : runnableQueueClone )
				{
					if( runnable.isInitialised() )
					{
						runnable.run( this );
					}
				}

                Thread.sleep( 10 );
			}

			catch( InterruptedException e )
			{
				e.printStackTrace();
			}

            catch( Exception e )
            {
                e.printStackTrace();
            }
		}

	}

}
