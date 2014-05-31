package joozey.libs.powerup.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import joozey.libs.powerup.control.GameDrawable;
import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.modifier.Modifier;

public abstract class GameObject2D extends GameObject
{
	private ArrayList<Modifier> modifierList;
	
	private DefaultSprite sprite;
	private GameObject2DData data;

	private boolean initialised = false;

	public GameObject2D()
	{
		this.modifierList = new ArrayList<Modifier>();
	}

	public GameObject2DData getData()
	{
		return this.data;
	}

	@Override
	public void run()
	{
		if( sprite == null || data == null ) { throw new GameObjectNotInitializedError(); }
	}

	public void init( GameObject2DData data, DefaultSprite sprite )
	{
		this.data = data;
		this.sprite = sprite;
		this.initialised = true;
	}

	@Override
	public boolean isInitialised()
	{
		return this.initialised;
	}

	public void remove()
	{
		this.sprite.dispose();
		this.data.setDestroyed();
	}


    public final void draw( BatchManager.DrawType drawType )
    {
        this.sprite.draw(drawType);
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
		
		this.sprite.setPosition( position.x - size.x/2, position.y - size.y/2 );
		this.sprite.setSize(size.x, size.y);
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
