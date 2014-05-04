package joozey.libs.powerup.modifier;

import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.object.GameObject2DData;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class LightnessModifier extends Modifier
{
	private float[] lightness;
	private Color originalColor = null;
	private Pixmap whiteMap;
	
	public LightnessModifier( float speed, float... lightness )
	{
		super( speed );
		this.lightness = lightness;
		throw new UnsupportedOperationException( "This modifier is not functional yet" );
	}

	@Override
	public void modify( GameObject2DData data, DefaultSprite sprite, float delta )
	{
		float lightValue = this.lightness[0];
		float progress = this.progress( delta );
		
		if( progress+1 < this.lightness.length )
		{
			float fade1 = this.lightness[ (int)(progress) ];	
			float fade2 = this.lightness[ (int)(progress+1) ];
			
			float relativeProgress = progress-(int)(progress);
			lightValue = fade1 + (fade2-fade1)*relativeProgress;
		}
		
		else
		{
			lightValue = this.lightness[ this.lightness.length-1 ];
		}
		
		sprite.setLightValue( lightValue );
	}

}
