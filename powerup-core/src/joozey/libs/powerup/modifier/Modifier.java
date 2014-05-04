package joozey.libs.powerup.modifier;

import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.object.GameObject2DData;

public abstract class Modifier
{
	private float speed;
	private float progress = 0f;
	
	public Modifier( float speed )
	{
		this.speed = speed;
	}
	
	protected float getProgress()
	{
		return progress;
	}
	
	protected float progress( float delta )
	{
		progress += delta * speed;
		return progress;
	}
	
	public abstract void modify( GameObject2DData data, DefaultSprite sprite, float delta );
}
