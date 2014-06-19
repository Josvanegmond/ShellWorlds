package joozey.libs.powerup.input;

public interface KeyListener
{
	public boolean keyDown( int keycode );
	public boolean keyUp( int keycode );
	public boolean keyTyped( char character );
}
