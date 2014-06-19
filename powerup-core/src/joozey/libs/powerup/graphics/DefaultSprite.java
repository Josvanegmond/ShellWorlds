package joozey.libs.powerup.graphics;

import joozey.libs.powerup.util.DefaultColors;
import joozey.libs.powerup.graphics.MatrixSet.MatrixType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class DefaultSprite extends Sprite implements Renderable
{
    public static Texture stringToTexture( String bitmapName )
    {
        Texture texture = new Texture(Gdx.files.internal("data/" + bitmapName));

        if( bitmapName.contains(".9.") ) {
            new NinePatch( texture );
        }
        return texture;
    }

	private float offsetX, offsetY;
	private float x, y;
	private String bitmapName;
	private float alpha = 1f;
    private ShapeRenderer.ShapeType shapeType;
    private NinePatch ninePatch;
    
    public DefaultSprite( NinePatch ninePatch )
    {
    	super( ninePatch.getTexture() );
    	this.ninePatch = ninePatch;
		setDefaultValues();
    }

    public DefaultSprite( String bitmapName )
	{
        super( stringToTexture( bitmapName ) );

		this.bitmapName = bitmapName;
		setDefaultValues();
	}

	public DefaultSprite( String bitmapName, Color color )
	{
		super( new Texture( Gdx.files.internal( "data/"
				+ DefaultColors.getName( color ) + "/" + bitmapName ) ) );

		this.bitmapName = bitmapName;
		setDefaultValues();
	}

	public DefaultSprite( Sprite sprite )
	{
		super( sprite );
		setDefaultValues();
	}

	@Override
	public void setColor( Color color )
	{
		if( this.bitmapName != null )
		{
			super.setTexture( new Texture( Gdx.files.internal( "data/"
					+ DefaultColors.getName( color ) + "/" + bitmapName ) ) );
		}
	}

	public void switchSprite( String bitmapName )
	{
		this.bitmapName = bitmapName;
		super.setTexture( new Texture( Gdx.files
				.internal( "data/" + bitmapName ) ) );
	}

	private void setDefaultValues()
	{
		Texture texture = this.getTexture();
		texture.setFilter( TextureFilter.Nearest, TextureFilter.Linear );

		// set size to match aspect ratio of view
		this.setSize( texture.getWidth(), texture.getHeight() );
		this.setOffset( 0, 0 );
		this.setCenter(0, 0);
		this.setOriginCenter();
	}


    @Override
    public void draw( Batch batch )
    {
    	super.setOriginCenter();
    	super.setCenter( this.x + offsetX, this.y + offsetY );
        
        if( this.ninePatch != null )
        {
        	this.ninePatch.draw(batch, offsetX, offsetY, this.getWidth(), this.getHeight() );
        }

        Color color = super.getColor();
        float oldAlpha = color.a;
        color.a *= alpha;

        super.setColor(color);
        super.draw( batch );

        color.a = oldAlpha;
        super.setColor(color);
    }

    @Override
	public void drawSpriteBatch( Batch batch, MatrixType matrixType )
	{
		this.draw( batch );
	}

	@Override
	public void drawShape(ShapeRenderer shapeRenderer) { }

	@Override
    public void drawModelBatch( ModelBatch batch ) { }
	
	
	@Override
	public void setPosition( float x, float y )
	{
		this.x = x;
		this.y = y;
	}

    public Vector2 getPosition()
    {
        return new Vector2( this.x, this.y );
    }

	public void dispose()
	{
		this.getTexture().dispose();
	}

	public void setOffset( float offsetX, float offsetY )
	{
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public Vector2 getOffset()
	{
		return new Vector2( this.offsetX, this.offsetY );
	}
	
	@Override
	public void setSize( float x, float y )
	{
		super.setSize( x, y );
	}

	public void setWidth( float width )
	{
		this.setSize( width, super.getHeight() );
	}

	public void setHeight( float height )
	{
		this.setSize( super.getWidth(), height );
	}

	public void setAlpha( float alpha )
	{
		this.alpha = alpha;
	}

    public ShapeRenderer.ShapeType getShapeType() {
        return shapeType;
    }
}
