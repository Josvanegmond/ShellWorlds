package joozey.libs.powerup.graphics;

import joozey.libs.powerup.graphics.MatrixSet.MatrixType;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class RenderMethod
{
    public static enum DrawType
    {
        SHAPE,
        SPRITE,
        MODEL
    };

	public final DrawType drawType;
	public final MatrixType matrixType;
	public final Camera camera;
	public final ShapeType shapeType;
	
	public RenderMethod( DrawType drawType, MatrixType matrixType )
	{
		if( drawType != DrawType.SPRITE ) { throw new Error("Rendering " + drawType.toString() + " with wrong arguments"); }
		
		this.drawType = drawType;
		this.matrixType = matrixType;
		this.camera = null;
		this.shapeType = null;
	}
	
	public RenderMethod( DrawType drawType,  MatrixType matrixType, Camera camera )
	{
		if( drawType != DrawType.MODEL ) { throw new Error("Rendering " + drawType.toString() + " with wrong arguments"); }
		
		this.drawType = drawType;
		this.matrixType = matrixType;
		this.camera = camera;
		this.shapeType = null;
	}
	
	public RenderMethod( DrawType drawType,  MatrixType matrixType, ShapeType shapeType )
	{
		if( drawType != DrawType.SHAPE ) { throw new Error("Rendering " + drawType.toString() + " with wrong arguments"); }
		
		this.drawType = drawType;
		this.matrixType = matrixType;
		this.camera = null;
		this.shapeType = shapeType;
	}
}
