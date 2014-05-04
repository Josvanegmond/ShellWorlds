package joozey.libs.powerup.object;

import com.badlogic.gdx.math.Vector2;


public class GameObject2DData
{
	protected float x, y;
	protected float offsetX, offsetY;
	protected float width, height;
	private boolean destroyed;
	
	public GameObject2DData()
	{
		this.x = 0;
		this.y = 0;
	}

	public GameObject2DData( float x, float y, float w, float h )
	{
		this.setPosition( x, y );
		this.setSize( w, h );
	}
	
	public void setPosition( float x, float y )
	{
		this.x = x;
		this.y = y;
	}
	
	public void setSize( float w, float h )
	{
		this.width = w;
		this.height = h;
	}
	
	public Vector2 getPosition()
	{
		return new Vector2( this.x, this.y );
	}

	public Vector2 getOffset()
	{
		return new Vector2( this.offsetX, this.offsetY );
	}

	public void setX( float x )
	{
		this.x = x;
	}
	
	public void setY( float y )
	{
		this.y = y;
	}

	public void setOffset( float x, float y )
	{
		this.offsetX = x;
		this.offsetY = y;
	}
	
	public float getOffsetX()
	{
		return this.offsetX;
	}
	
	public float getOffsetY()
	{
		return this.offsetY;
	}
	
	public void translateOffset( float x, float y )
	{
		this.offsetX += x;
		this.offsetY += y;
	}

	public Vector2 getSize()
	{
		return new Vector2( this.width, this.height );
	}

	
	public boolean isDestroyed()
	{
		return this.destroyed;
	}
	
	public void setDestroyed()
	{
		this.destroyed = true;
	}

}
