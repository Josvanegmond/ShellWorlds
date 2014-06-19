package joozey.libs.powerup.modifier;

import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.object.GameObject2DData;
import com.badlogic.gdx.graphics.Color;

public class FadeModifier extends Modifier
{
	private float[] fades;
	
	public FadeModifier( float speed, float... fades )
	{
		super( speed );
		this.fades = fades;
	}

	@Override
	public void modify( GameObject2DData data, DefaultSprite sprite, float delta )
	{
		float progress = this.progress( delta );
		float fade = this.fades[0];
		
		if( progress+1 < this.fades.length )
		{
			float fade1 = this.fades[ (int)(progress) ];	
			float fade2 = this.fades[ (int)(progress+1) ];
			
			float relativeProgress = progress-(int)(progress);
			fade = fade1 + (fade2-fade1)*relativeProgress;
		}
		
		else
		{
			fade = this.fades[ this.fades.length-1 ];
		}
		
		sprite.setAlpha( fade );
	}
}
