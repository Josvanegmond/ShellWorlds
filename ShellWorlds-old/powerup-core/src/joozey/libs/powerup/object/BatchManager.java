package joozey.libs.powerup.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BatchManager
{
    public static enum DrawType
    {
        SHAPE,
        BATCH
    };

	private static final SpriteBatch spriteBatch = new SpriteBatch();
	private static final ModelBatch modelBatch = new ModelBatch();
	private static final ShapeRenderer shapeRenderer = new ShapeRenderer();

	public static SpriteBatch getSpriteBatch()
	{
		return spriteBatch;
	}
	public static ModelBatch getModelBatch()
	{
		return modelBatch;
	}
    public static ShapeRenderer getShapeRenderer() { return shapeRenderer; }
}
