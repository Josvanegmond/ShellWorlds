package joozey.libs.powerup.control;

import joozey.libs.powerup.object.GameObject;

public interface GameRunnable extends Runnable
{
	public abstract void run();
	public abstract boolean isInitialised();
}
