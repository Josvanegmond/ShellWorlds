package joozey.libs.powerup.object;

import joozey.libs.powerup.graphics.DefaultSprite;
import com.badlogic.gdx.math.Vector2;

public class GraphicObject2D extends GameObject2D
{
	public GraphicObject2D( DefaultSprite sprite, Vector2 pos )
	{
		this.init(
			new GameObject2DData( pos.x, pos.y, sprite.getWidth(), sprite.getHeight() ),
			sprite );
	}

}
