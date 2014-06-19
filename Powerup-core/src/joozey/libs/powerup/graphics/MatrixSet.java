package joozey.libs.powerup.graphics;

import joozey.libs.powerup.game.GameData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;

public class MatrixSet {
	
	public static enum MatrixType
	{
		SCREENMATRIX,
		CAMERAMATRIX,
		PARALLAXMATRIX
	};

	public static Matrix4 getMatrix(MatrixType matrixType)
	{
		Matrix4 matrix = null;
		
		switch( matrixType )
		{
		case SCREENMATRIX:
			matrix = getScreenMatrix();
			break;
		
		case CAMERAMATRIX:
			matrix = getCameraMatrix();
			break;
		
		case PARALLAXMATRIX:
			//TODO: make width and height somehow dynamic
			matrix = getParallaxMatrix( );
			break;
		}
		
		return matrix;
	}
	
	private static Matrix4 getScreenMatrix()
	{
		return new Matrix4().setToOrtho2D( 0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight() );
	}
	
	private static Matrix4 getCameraMatrix()
	{
		return GameData.getCamera().combined;
	}
	
	private static Matrix4 getParallaxMatrix(  )
	{
	    Matrix4 normalProjection = new Matrix4().setToOrtho2D( 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
	    normalProjection.translate( GameData.getCamera().position.cpy().scl(-0.0025f));
	    
	    return normalProjection;
	}
}
