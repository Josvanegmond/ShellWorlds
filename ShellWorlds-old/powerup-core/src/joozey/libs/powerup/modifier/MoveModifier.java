package joozey.libs.powerup.modifier;

import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.object.GameObject2DData;
import com.badlogic.gdx.math.Vector2;

public class MoveModifier extends Modifier
{
	private Vector2[] pos;
	
	public MoveModifier( float speed, Vector2... pos )
	{
		super( speed );
		this.pos = pos;
	}
	
	@Override
	public void modify( GameObject2DData data, DefaultSprite sprite, float delta )
	{
		float progress = this.progress( delta );
		
		Vector2 pos = this.pos[0];
		if( progress+1 < this.pos.length )
		{
			Vector2 pos1 = this.pos[ (int)(progress) ];	
			Vector2 pos2 = this.pos[ (int)(progress+1) ];
			
			float relativeProgress = progress-(int)(progress);
			pos = new Vector2(
				pos1.x + (pos2.x-pos1.x)*relativeProgress,
				pos1.y + (pos2.y-pos1.y)*relativeProgress
			);
		}
		
		else
		{
			pos = this.pos[ this.pos.length-1 ];
		}
		
		Vector2 currentPosition = data.getPosition();
		data.setPosition( currentPosition.x + pos.x, currentPosition.y + pos.y );
	}
}
