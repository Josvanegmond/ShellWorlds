package joozey.libs.powerup.object;

import com.badlogic.gdx.math.Vector2;


public class GameObject2DData
{
	protected double x, y;
	protected double offsetX, offsetY;
	protected double width, height;
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
	
	public void setPosition( double x, double y )
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
		return new Vector2( (float)this.x, (float)this.y );
	}

	public Vector2 getOffset()
	{
		return new Vector2( (float)this.offsetX, (float)this.offsetY );
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
	
	public double getOffsetX()
	{
		return this.offsetX;
	}
	
	public double getOffsetY()
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
		return new Vector2( (float)this.width, (float)this.height );
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
