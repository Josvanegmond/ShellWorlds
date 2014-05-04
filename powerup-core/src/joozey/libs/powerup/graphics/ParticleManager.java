package joozey.libs.powerup.graphics;

import java.util.LinkedList;
import java.util.Stack;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticleManager
{
	//singleton
	private static ParticleManager _instance;

	public static ParticleManager getInstance()
	{
		if( ParticleManager._instance == null )
		{
			ParticleManager._instance = new ParticleManager();
		}
		
		return ParticleManager._instance;
	}

	
	public static void delete()
	{
		_instance = null;
	}

	private class EffectData
	{
		public float x, y;
		public String name;
		public String image;
		public boolean flipVertical;
		public Color color;
		
		public EffectData( String name, String image, Color color, float x, float y, boolean flipVertical )
		{
			this.image = image;
			this.name = name;
			this.x = x;
			this.y = y;
			this.flipVertical = flipVertical;
			this.color = color;
		}
	}
	
	//constructor etc
	private LinkedList<ParticleEffect> particleEffects;
	private Stack<EffectData> pendingEffects;
	
	private ParticleManager()
	{
		this.particleEffects = new LinkedList<ParticleEffect>();
		this.pendingEffects = new Stack<EffectData>();
	}
	
	public void addEffect( String effect, String image, Color color, float x, float y, boolean flipVertical )
	{
		this.pendingEffects.push( new EffectData( effect, image, color, x, y, flipVertical ) );
	}
	
	public void draw( SpriteBatch batch, float delta )
	{
		while( this.pendingEffects.size() > 0 )
		{
			EffectData effectData = this.pendingEffects.pop();
			String effect = effectData.name;

			DefaultSprite particleSprite = new DefaultSprite( effectData.image, effectData.color );
			particleSprite.setColor( effectData.color );
			ParticleEffect particleEffect = new ParticleEffect();
			particleEffect.load( Gdx.files.internal( "effects/" + effect ), Gdx.files.internal("effects") );
			particleEffect.getEmitters().get( 0 ).setSprite( particleSprite );

			float angle = 90;
			if( effectData.flipVertical == true )
			{
				angle = (angle + 180) % 360;
			}
			
			particleEffect.getEmitters().get(0).getAngle().setLow( angle, angle );
			particleEffect.getEmitters().get(0).getAngle().setHigh( angle-20, angle+20);
			
			particleEffect.setPosition( effectData.x, effectData.y );
			
			this.particleEffects.add( particleEffect );
			particleEffect.start();
		}
		
		for( ParticleEffect effect : this.particleEffects )
		{
			effect.draw( batch, delta );
		}
	}
}
