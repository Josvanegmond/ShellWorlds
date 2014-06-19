package joozey.libs.powerup.graphics;

import joozey.libs.powerup.graphics.MatrixSet.MatrixType;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Renderable
{
	public void drawSpriteBatch( Batch batch, MatrixType matrixType );
	public void drawModelBatch( ModelBatch batch );
	public void drawShape( ShapeRenderer shapeRenderer );
}
