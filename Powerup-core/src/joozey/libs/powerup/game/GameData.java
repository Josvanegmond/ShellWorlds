package joozey.libs.powerup.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;

import java.util.LinkedList;

import joozey.libs.powerup.object.GameObject2D;

public class GameData
{
	private static Camera camera;
	private static float width, height;
    private static float speed;
	
	private static boolean gameStarted;
	private static boolean gameFinished;
	
	private static LinkedList<GameObject2D> gameObjectList;
	
	public GameData( float width, float height )
	{
		GameData.width = width;
		GameData.height = height; 
		
		GameData.gameObjectList = new LinkedList<GameObject2D>();
	}
	
	public void setSize( float width, float height )
	{
		GameData.width = width;
		GameData.height = height;
	}
	
	public void addGameObject( GameObject2D gameObject )
	{
		GameData.gameObjectList.add( gameObject );
	}
	
	public LinkedList<GameObject2D> getGameObjects()
	{
		LinkedList<GameObject2D> objectList = new LinkedList<GameObject2D>( gameObjectList );
		return objectList;
	}
	
	public static Camera getCamera()
	{
		return camera;
	}
	
	public static void setCamera( Camera camera )
	{
		GameData.camera = camera;
	}
	
	public void setGameFinished(boolean b)
	{
		GameData.gameFinished = b;
	}
	
	public boolean getGameFinished()
	{
		return GameData.gameFinished;
	}
	
	public void setGameStarted( boolean b )
	{
		GameData.gameStarted = b;
	}
	
	public boolean getGameStarted()
	{
		return GameData.gameStarted;
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


    public static double getSpeed()
    {
        return 1;
    }

    public static void setSpeed( float speed )
    {
        GameData.speed = speed;
    }

}
