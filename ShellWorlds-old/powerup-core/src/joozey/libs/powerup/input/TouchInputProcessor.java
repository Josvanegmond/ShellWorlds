package joozey.libs.powerup.input;

import java.util.LinkedList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public class TouchInputProcessor implements InputProcessor
{
	//singleton, we only have 1 TouchInputProcessor
	private static TouchInputProcessor _instance;

	public static TouchInputProcessor getInstance()
	{
		if( _instance == null )
		{
			_instance = new TouchInputProcessor();
		}
		
		return _instance;
	}

	
	public static void delete()
	{
		_instance = null;
	}
	
	
	//object code
	private Camera camera;
	private LinkedList<TouchListener> touchListenerList;
	private LinkedList<KeyListener> keyListenerList;
	private boolean disable = false;
	
	private TouchInputProcessor()
	{
		touchListenerList = new LinkedList<TouchListener>();
		keyListenerList = new LinkedList<KeyListener>();
		
		if( this.disable == false )
		{
			Gdx.input.setInputProcessor( this );
		}
	}

	public void setCamera( Camera camera )
	{
		 this.camera = camera;	
	}
	
	public void register( TouchListener touchListener )
	{
		this.touchListenerList.add( touchListener );
	}
	
	public void remove( TouchListener touchListener )
	{
		this.touchListenerList.remove( touchListener );
	}
	
	public void register( KeyListener keyListener )
	{
		this.keyListenerList.add( keyListener );
	}
	
	public void remove( KeyListener keyListener )
	{
		this.keyListenerList.add( keyListener );
	}
	
	@Override
	public boolean keyDown( int keycode )
	{
		LinkedList<KeyListener> list = ( LinkedList<KeyListener> ) keyListenerList.clone();
		for( KeyListener keyListener : list )
		{
			keyListener.keyDown( keycode );
		}
		
		return false;
	}

	@Override
	public boolean keyUp( int keycode )
	{
		LinkedList<KeyListener> list = ( LinkedList<KeyListener> ) keyListenerList.clone();
		for( KeyListener keyListener : list )
		{
			keyListener.keyUp( keycode );
		}
		
		return false;
	}

	@Override
	public boolean keyTyped( char character )
	{
		LinkedList<KeyListener> list = ( LinkedList<KeyListener> ) keyListenerList.clone();
		for( KeyListener keyListener : list )
		{
			keyListener.keyTyped( character );
		}
		
		return false;
	}

	@Override
	public boolean touchDown( int screenX, int screenY, int pointer, int button )
	{
		if( camera != null )
		{
			Vector3 screenVector = new Vector3( screenX, screenY, 0 );
			camera.unproject( screenVector );
			for( TouchListener touchListener : touchListenerList )
			{
				touchListener.touchDown( screenVector, pointer, button );
			}
		}
		
		return false;
	}

	@Override
	public boolean touchUp( int screenX, int screenY, int pointer, int button )
	{
		if( camera != null )
		{
			Vector3 screenVector = new Vector3( screenX, screenY, 0 );
			camera.unproject( screenVector );
			for( TouchListener touchListener : touchListenerList )
			{
				touchListener.touchUp( screenVector, pointer, button );
			}
		}
		
		return false;
	}

	@Override
	public boolean touchDragged( int screenX, int screenY, int pointer )
	{
		if( camera != null )
		{
			Vector3 screenVector = new Vector3( screenX, screenY, 0 );
			camera.unproject( screenVector );
			for( TouchListener touchListener : touchListenerList )
			{
				touchListener.touchDragged( screenVector, pointer );
			}
		}
		
		return false;
	}

	@Override
	public boolean mouseMoved( int screenX, int screenY )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled( int amount )
	{
		// TODO Auto-generated method stub
		return false;
	}


	public void setDisable( boolean disable )
	{
		this.disable = disable;
		
		if( disable == true )
		{
			Gdx.input.setInputProcessor( null );
		}
		else
		{
			Gdx.input.setInputProcessor( this );
		}
	}
}
