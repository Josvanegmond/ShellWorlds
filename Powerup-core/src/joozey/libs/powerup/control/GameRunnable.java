package joozey.libs.powerup.control;

public interface GameRunnable extends Runnable
{
	public abstract void run();
	public abstract boolean isInitialised();
}
