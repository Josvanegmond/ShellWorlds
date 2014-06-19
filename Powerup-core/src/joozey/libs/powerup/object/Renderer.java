package joozey.libs.powerup.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import joozey.libs.powerup.graphics.MatrixSet;
import joozey.libs.powerup.graphics.MatrixSet.MatrixType;
import joozey.libs.powerup.graphics.RenderMethod;
import joozey.libs.powerup.graphics.RenderMethod.DrawType;
import joozey.libs.powerup.graphics.Renderable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

public class Renderer
{
	private static class RenderCall
	{
		public final RenderMethod renderMethod;
		public final Renderable renderable;
		public final int priority;
		
		public RenderCall( RenderMethod renderMethod, Renderable renderable, int priority )
		{
			this.renderMethod = renderMethod;
			this.renderable = renderable;
			this.priority = priority;
		}
	}
	
	private static ArrayList<RenderCall> renderCallList = new ArrayList<RenderCall>();
	
	private static final SpriteBatch spriteBatch = new SpriteBatch();
	private static final ModelBatch modelBatch = new ModelBatch();
	private static final ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	//private static ListedHashMap<Integer,RenderCall> renderMethodMap = new ListedHashMap<Integer,RenderCall>();
	
    public static void add( Renderable renderable, int priority, RenderMethod renderMethod )
    {
        renderCallList.add(new RenderCall(renderMethod, renderable, priority));
    	Collections.sort( renderCallList, new RenderCallComparator() );
    }
    
    
    private static class RenderCallComparator implements Comparator<RenderCall>
    {
		@Override
		public int compare(RenderCall r1, RenderCall r2)
		{
			int result = 0;
			int lowSortId1 = 0;
			int lowSortId2 = 0;

			if( r1.renderMethod.drawType == DrawType.SHAPE ) { lowSortId1 = r1.renderMethod.shapeType.ordinal(); }
			if( r2.renderMethod.drawType == DrawType.SHAPE ) { lowSortId2 = r2.renderMethod.shapeType.ordinal(); }
			
			result = (lowSortId2 + (r2.renderMethod.drawType.ordinal()+1)*10 + r2.priority*100) -
                     (lowSortId1 + (r1.renderMethod.drawType.ordinal()+1)*10 + r1.priority*100);
			return result;
		}
    }

    
    private static boolean rendering = false;
    public static void render()
    {
        RenderCall prevRenderCall = null;
        RenderCallComparator comparator = new RenderCallComparator();

        int renderChangeCount = 0;
        
    	for( RenderCall renderCall : renderCallList )
    	{
            boolean renderChange = true;
            if( prevRenderCall != null ) { renderChange = comparator.compare( renderCall, prevRenderCall ) != 0; }

            if( renderChange == true )
            {
            	renderChangeCount++;
            	if( rendering == true )
            	{
            		endRenderer(renderCall.renderMethod);
            	}
            	
                beginRenderer(renderCall.renderMethod);
                rendering = true;
            }

    		callRender( renderCall.renderable, renderCall.renderMethod.drawType, renderCall.renderMethod.matrixType );

            prevRenderCall = renderCall;
    	}
    	
    	System.out.println( "Render changes: " + renderChangeCount );
    }
    
    
    private static void callRender( Renderable renderable, DrawType drawType,  MatrixType matrixType )
    {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glLineWidth(1f);

    	Matrix4 matrix = MatrixSet.getMatrix(matrixType);
    	if( drawType == DrawType.SPRITE )
    	{
    		spriteBatch.setProjectionMatrix( matrix );
    		renderable.drawSpriteBatch( spriteBatch, matrixType );
    	}

    	if( drawType == DrawType.MODEL )
    	{
    		renderable.drawModelBatch( modelBatch );
    	}

    	if( drawType == DrawType.SHAPE )
    	{
    		shapeRenderer.setProjectionMatrix( matrix );
    		renderable.drawShape( shapeRenderer );
    	}
    }
    
    
    private static void beginRenderer( RenderMethod renderMethod )
    {
    	if( renderMethod.drawType == DrawType.SHAPE )
    	{	
    		shapeRenderer.begin( renderMethod.shapeType );
    	}

    	if( renderMethod.drawType == DrawType.SPRITE )
    	{
    		spriteBatch.begin();
    	}

    	if( renderMethod.drawType == DrawType.MODEL )
    	{
    		modelBatch.begin( renderMethod.camera );
    	}
    }
    
    private static void endRenderer( RenderMethod renderMethod )
    {
    	if( renderMethod.drawType == DrawType.SHAPE )
    	{
    		shapeRenderer.end();
    	}

    	if( renderMethod.drawType == DrawType.SPRITE )
    	{
    		spriteBatch.end();
    	}

    	if( renderMethod.drawType == DrawType.MODEL )
    	{
    		modelBatch.end();
    	}
    }
    
    public static void dispose()
    {
    	spriteBatch.dispose();
    	modelBatch.dispose();
    	shapeRenderer.dispose();
    }
}



