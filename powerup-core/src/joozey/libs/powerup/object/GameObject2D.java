package joozey.libs.powerup.object;

import java.util.ArrayList;
import joozey.libs.powerup.control.GameDrawable;
import joozey.libs.powerup.control.GameThread;
import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.modifier.Modifier;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject2D extends GameObject implements GameDrawable
{
	private ArrayList<Modifier> modifierList;
	
	private DefaultSprite sprite;
	private GameObject2DData data;

	protected GameThread gameThread;

	private boolean initialised = false;

	public GameObject2D( GameThread gameThread )
	{
		this.gameThread = gameThread;
		this.modifierList = new ArrayList<Modifier>();
	}

	public GameObject2DData getData()
	{
		return this.data;
	}

	@Override
	public void run( GameThread gameThread )
	{
		if( sprite == null || data == null ) { throw new GameObjectNotInitializedError(); }
	}

	public void init( GameObject2DData data, DefaultSprite sprite )
	{
		this.data = data;
		this.sprite = sprite;
		this.initialised = true;

		this.gameThread.register( this );
	}

	@Override
	public boolean isInitialised()
	{
		return this.initialised;
	}

	public void remove()
	{
		this.gameThread.remove( this );
		this.sprite.dispose();
		this.data.setDestroyed();
	}

	@Override
	public void update()
	{
		if( sprite == null || data == null ) { throw new GameObjectNotInitializedError(); }

		for( Modifier modifier : modifierList )
		{
			modifier.modify( data, sprite, Gdx.graphics.getDeltaTime() );
		}
		
		Vector2 position = data.getPosition();
		Vector2 offset = data.getOffset();
		Vector2 size = data.getSize();
		
		this.sprite.setPosition( position.x - offset.x, position.y - offset.y );
		this.sprite.setSize( size.x, size.y );
		this.sprite.draw();
	}

	public void update( Vector2 position )
	{
		this.data.setPosition( position.x, position.y );
		this.update();
	}

	public void addModifier( Modifier... modifiers )
	{
		for( Modifier modifier : modifiers )
		{
			this.modifierList.add( modifier );
		}
	}
}
