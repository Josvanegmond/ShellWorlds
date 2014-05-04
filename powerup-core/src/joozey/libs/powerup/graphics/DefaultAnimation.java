package joozey.libs.powerup.graphics;

import joozey.libs.powerup.control.GameRunnable;
import joozey.libs.powerup.control.GameThread;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DefaultAnimation extends DefaultSprite implements GameRunnable
{
	public static enum Frame
	{
		FIRST_FRAME,
		LAST_FRAME,
		NONE
	};
	
	private GameThread gameThread;
	private Animation spriteAnimation;
	private TextureRegion[] keyFrames;
	private float stateTime = 0.0f;
	private float animSpeed = 0.0025f;
	private int animateLoops = 0;
	private float startDelay = 0f;
	private Frame endFrame = Frame.LAST_FRAME;

	public static TextureRegion[] cutToFrames( Sprite spriteSheet,
			int frameCols, int frameRows )
	{
		Texture texture = spriteSheet.getTexture();
		TextureRegion[][] tmp = TextureRegion.split( texture,
				( int ) ( texture.getWidth() / frameCols ),
				( int ) ( texture.getHeight() / frameRows ) );
		TextureRegion[] spriteFrames = new TextureRegion[frameCols * frameRows];

		int index = 0;
		for( int i = 0; i < frameRows; i++ )
		{
			for( int j = 0; j < frameCols; j++ )
			{
				spriteFrames[index] = tmp[i][j];
				index++;
			}
		}

		return spriteFrames;
	}

	public void setAnimateLoops( int loops, Frame endFrame )
	{
		this.animateLoops = loops;
		this.endFrame = endFrame;
	}

	public DefaultAnimation( GameThread gameThread, DefaultSprite spriteSheet,
			int frameCols, int frameRows, float animSpeed )
	{
		super( spriteSheet );

		this.gameThread = gameThread;
		this.gameThread.register( this );

		this.keyFrames = cutToFrames( spriteSheet, frameCols, frameRows );
		this.setSize( spriteSheet.getWidth() / frameCols,
				spriteSheet.getHeight() / frameRows );

		this.animSpeed = animSpeed;
		this.spriteAnimation = new Animation( this.animSpeed, keyFrames );
	}

	@Override
	public void flip( boolean x, boolean y )
	{
		for( int i = 0; i < this.keyFrames.length; i++ )
		{
			TextureRegion region = this.keyFrames[i];
			region.flip( x, y );
		}
	}

	public boolean isAnimationFinished()
	{
		return this.spriteAnimation.isAnimationFinished( this.stateTime );
	}

	@Override
	public void draw()
	{
		if( this.stateTime - this.startDelay > 0 && this.animateLoops != 0 )
		{
			TextureRegion region = this.spriteAnimation.getKeyFrame(
					this.stateTime - this.startDelay, true );

			this.setRegion( region );
			this.setTexture( region.getTexture() );
			// this.setSize( region.getRegionWidth(), region.getRegionHeight()
			// );

			super.draw();
		}

		else if( this.endFrame != Frame.NONE )
		{
			TextureRegion region = null;
			if( this.endFrame == Frame.FIRST_FRAME )
			{
				region = this.spriteAnimation.getKeyFrame( 0, true );
			}
			
			else if( this.endFrame == Frame.LAST_FRAME )
			{
				region = this.spriteAnimation.getKeyFrame( this.keyFrames.length-1, true );
			}
			
			this.setRegion( region );
			this.setTexture( region.getTexture() );
			// this.setSize( region.getRegionWidth(), region.getRegionHeight()
			// );

			super.draw();
		}
	}

	@Override
	public void run( GameThread gameThread )
	{
		if( startDelay <= 0 )
		{
			startDelay = 0;

			if( this.spriteAnimation != null )
			{
				if( animateLoops > 0 || animateLoops == -1 )
				{
					stateTime += Gdx.graphics.getDeltaTime();
				}

				if( this.spriteAnimation.isAnimationFinished( stateTime ) == true )
				{
					stateTime = 0;

					if( animateLoops > 0 )
					{
						animateLoops--;
					}
				}
			}
		}

		else
		{
			startDelay -= Gdx.graphics.getDeltaTime();
		}
	}

	public int getRemainingLoops()
	{
		return animateLoops;
	}

	public void remove()
	{
		gameThread.remove( this );
	}

	@Override
	public boolean isInitialised()
	{
		return true;
	}

	public void setStartDelay( float startDelay )
	{
		this.startDelay = startDelay;
	}

	@Override
	public void setSize( float width, float height )
	{
		super.setSize( width, height );

		// if( this.keyFrames != null )
		// {
		// for( int i = 0; i < this.keyFrames.length; i++ )
		// {
		// this.keyFrames[i].setSize( width, height );
		// }
		// }
	}
}
