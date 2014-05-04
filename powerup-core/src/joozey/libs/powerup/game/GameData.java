package joozey.libs.powerup.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;

import java.util.LinkedList;

import joozey.libs.powerup.object.GameObject2D;

public class GameData
{
	private Camera camera;
	private static float width, height;
    private static float speed;
	
	private boolean gameStarted;
	private boolean gameFinished;
	
	private LinkedList<GameObject2D> gameObjectList;
	
	public GameData( float width, float height )
	{
		GameData.width = width;
		GameData.height = height; 
		
		this.gameObjectList = new LinkedList<GameObject2D>();
	}
	
	public void setSize( float width, float height )
	{
		GameData.width = width;
		GameData.height = height;
	}
	
	public void addGameObject( GameObject2D gameObject )
	{
		this.gameObjectList.add( gameObject );
	}
	
	public LinkedList<GameObject2D> getGameObjects()
	{
		LinkedList<GameObject2D> objectList = new LinkedList<GameObject2D>( gameObjectList );
		return objectList;
	}
	
	public Camera getCamera()
	{
		return this.camera;
	}
	
	public void setCamera( Camera camera )
	{
		this.camera = camera;
	}
	
	public void setGameFinished(boolean b)
	{
		this.gameFinished = b;
	}
	
	public boolean getGameFinished()
	{
		return this.gameFinished;
	}
	
	public void setGameStarted( boolean b )
	{
		this.gameStarted = b;
	}
	
	public boolean getGameStarted()
	{
		return this.gameStarted;
	}
	
	public static float getWidth()
	{
		if( width == 0 ) { return Gdx.graphics.getWidth(); }
		else			 { return width; }
	}
	
	public static float getHeight()
	{
		if( height == 0 ) { return Gdx.graphics.getHeight(); }
		else			  { return height; }
	}


    public static float getSpeed()
    {
        return 10f;//speed/Gdx.graphics.getFramesPerSecond();
    }

    public static void setSpeed( float speed )
    {
        GameData.speed = speed;
    }

}
