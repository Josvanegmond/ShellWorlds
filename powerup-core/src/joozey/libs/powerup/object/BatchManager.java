package joozey.libs.powerup.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class BatchManager
{
	private static final SpriteBatch spriteBatch = new SpriteBatch();
	private static final ModelBatch modelBatch = new ModelBatch();
	
	public static SpriteBatch getSpriteBatch()
	{
		return spriteBatch;
	}
	
	public static ModelBatch getModelBatch()
	{
		return modelBatch;
	}
}
