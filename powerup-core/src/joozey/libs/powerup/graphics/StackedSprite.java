package joozey.libs.powerup.graphics;

import java.util.Stack;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class StackedSprite extends DefaultSprite
{
	private float offsetX = 0, offsetY = 0;
	
	private Stack<DefaultSprite> spriteStack;
	
	public StackedSprite( int width, int height )
	{
		super( new Sprite( new Texture( width, height, Format.RGBA8888 ) ) );
		this.spriteStack = new Stack<DefaultSprite>();
	}
	
	public StackedSprite( DefaultSprite sprite, int offsetX, int offsetY )
	{
		super( sprite );
		this.spriteStack = new Stack<DefaultSprite>();

		this.setOffset( offsetX, offsetY );
	}
	
	public Stack<DefaultSprite> getSpriteStack()
	{
		@SuppressWarnings( "unchecked" )
		Stack<DefaultSprite> clone = ( Stack<DefaultSprite> ) spriteStack.clone();
		clone.add(  this  );
		return clone;
	}
	
	public int getSpriteStackSize()
	{
		return this.spriteStack.size();
	}
	
	public void removeSprite( int index )
	{
			this.spriteStack.remove( index );
	}
	
	@Override
	public void draw()
	{
		super.draw();
		
		for( DefaultSprite sprite : this.spriteStack )
		{
			if( sprite != this )
			{
				Vector2 origOffset = sprite.getOffset();
				sprite.setOffset( origOffset.x + this.offsetX, origOffset.y + this.offsetY );
				sprite.draw();
				sprite.setOffset( origOffset.x, origOffset.y );
			}
		}
	}
	
	@Override
	public void setColor( Color color )
	{
		super.setColor( color );
		for( DefaultSprite sprite : this.spriteStack )
		{
			sprite.setColor( color );
		}
	}
	
	@Override
	public void flip( boolean x, boolean y )
	{
		super.flip( x, y );
		for( DefaultSprite sprite : this.spriteStack )
		{
			sprite.flip( x, y );
		}
	}
	
	@Override
	public void setScale( float x, float y )
	{
		super.setScale( x, y );
		if( this.spriteStack != null )
		{
			for( DefaultSprite sprite : this.spriteStack )
			{
				sprite.setScale( x, y );
			}
		}
	}

	
	@Override
	public void setPosition( float x, float y )
	{
		super.setPosition( x,  y );
		if( this.spriteStack != null )
		{
			for( DefaultSprite sprite : this.spriteStack )
			{
				sprite.setPosition( x, y );
			}
		}
	}
	
	@Override
	public void setOffset( float x, float y )
	{
		this.offsetX = x;
		this.offsetY = y;
	}
	
	public void addSprite( DefaultSprite sprite, int offsetX, int offsetY )
	{
		sprite.setOffset( offsetX, offsetY );
		this.spriteStack.push( sprite );
	}
}
