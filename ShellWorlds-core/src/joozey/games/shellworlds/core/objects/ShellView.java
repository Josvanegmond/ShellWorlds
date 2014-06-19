package joozey.games.shellworlds.core.objects;

import java.util.HashMap;
import java.util.LinkedList;

import joozey.games.shellworlds.core.ShellWorldData;
import joozey.libs.powerup.game.GameData;
import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.graphics.MatrixSet.MatrixType;
import joozey.libs.powerup.graphics.RenderMethod;
import joozey.libs.powerup.graphics.RenderMethod.DrawType;
import joozey.libs.powerup.object.Renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by acer on 29-5-2014.
 */
public class ShellView extends DefaultSprite
{
    private ShellObject shellObject;
    public ShellView(ShellObject shellObject, String bitmapName)
    {
        super( bitmapName );
        this.shellObject = shellObject;

		Renderer.add( this, 8, new RenderMethod( DrawType.SPRITE, MatrixType.CAMERAMATRIX ) );
        Renderer.add( this, 9, new RenderMethod( DrawType.SPRITE, MatrixType.SCREENMATRIX ) );
        //Renderer.add( this, 10, new RenderMethod( DrawType.SHAPE, MatrixType.SCREENMATRIX, ShapeType.Line ) );
    }

    private void drawInfo( Batch batch )
    {
        BodyData data = (BodyData) shellObject.getData();

        //TODO draw shellworld data
        //Vector3 viewPosition = new Vector3( 20, 40, 0 );//camera.project( new Vector3( bodyData.getPosition().x + 250f, bodyData.getPosition().y, 0 ));

        Vector3 viewPosition = new Vector3( GameData.getWidth()/3, GameData.getHeight()/4, 0 );//camera.project( new Vector3( bodyData.getPosition().x + 250f, bodyData.getPosition().y, 0 ));

        float pop = (int)shellObject.getImportExportData().getPopulation();
        float le = (int)shellObject.getImportExportData().getLightElements();
        float he = (int)shellObject.getImportExportData().getHeavyElements();
        float se = (int)shellObject.getImportExportData().getServices();
        float su = (int)shellObject.getImportExportData().getSupplies();
        
        BitmapFont normalFont = ShellWorldData.getInstance().getNormalFont();
        normalFont.setColor( Color.WHITE );
        
        normalFont.draw(batch, "Population: " + pop, viewPosition.x, viewPosition.y);
        normalFont.draw(batch, "Light elements:  " + le, viewPosition.x, viewPosition.y + 30);
        normalFont.draw(batch, "Heavy elements: " + he, viewPosition.x, viewPosition.y + 50 );
        normalFont.draw(batch, "Services:  " + se, viewPosition.x, viewPosition.y + 70);
        normalFont.draw(batch, "Supplies: " + su, viewPosition.x, viewPosition.y + 90 );
        
        normalFont.draw(batch, "Type: " + shellObject.getType(), viewPosition.x, viewPosition.y + 110 );
    }
    
    
    private HashMap<String, LinkedList<Float>> graphMap = new HashMap<String, LinkedList<Float>>();
    
    public void setGraphValue( String name, float value )
    {
        if( graphMap.containsKey( name ) == false ) { graphMap.put( name, new LinkedList<Float>() ); }
        LinkedList<Float> graph = graphMap.get( name );
        graph.add( value );
    }
    
    private void drawGraph( ShapeRenderer shapeRenderer, String name, float x, float y )
    {
        LinkedList<Float> graph = graphMap.get( name );
    	while( graph.size() > 50 ) { graph.removeFirst(); }
    	
		System.out.println("draw " + name + " with " + graph.size() + " data" );

        shapeRenderer.setColor( 1f, 1f, 1f, .5f );
		
		if( graph.size() > 0 )
		{
			float top = graph.get(0)-graph.get(1), bottom = graph.get(0)-graph.get(1);
	    	for( int i = 1; i < graph.size()-1; i++ )
	    	{
	    		float var = graph.get(i)-graph.get(i+1);
	    		top = Math.max( top, var );
	    		bottom = Math.min( bottom, var );
	    	}
	    	
	    	float last = graph.getLast() - graph.get( graph.size()-2);
			
	    	for( int i = 0; i < graph.size()-2; i++ )
	    	{
	    		float value = graph.get(i);
	    		float nextValue = graph.get(i+1);
	    		float nextNextValue = graph.get(i+2);
	    		
	    		float dy = value-nextValue;
	    		float nextDy = nextValue - nextNextValue;
	    		
	    		float scale = 20f/top;
	    		
	    		shapeRenderer.line( x+i*3, y+dy, x+(i+1)*3, y+nextDy);
	    	}
		}
    }
    
    @Override
    public void drawSpriteBatch( Batch batch, MatrixType matrixType )
    {
    	if( matrixType == MatrixType.SCREENMATRIX )
    	{
	    	BodyObject touchedBody = ShellWorldData.getInstance().getTouchedBody();
	    	if( touchedBody != null && touchedBody.getData() == this.shellObject.getData() )
	    	{
	    		this.drawInfo(batch );
	    	}
    	}
    	
    	if( matrixType == MatrixType.CAMERAMATRIX )
    	{
    		super.drawSpriteBatch( batch, matrixType );
    	}
    }

    @Override
    public void drawShape( ShapeRenderer shapeRenderer )
    {
    	if( shapeRenderer.getCurrentType() == ShapeType.Line )
    	{
	    	BodyObject touchedBody = ShellWorldData.getInstance().getTouchedBody();
	    	if( touchedBody != null && touchedBody.getData() == this.shellObject.getData() )
	    	{
		        Vector3 viewPosition = new Vector3( GameData.getWidth()/3, GameData.getHeight()/4, 0 );//camera.project( new Vector3( bodyData.getPosition().x + 250f, bodyData.getPosition().y, 0 ));
		
		        drawGraph( shapeRenderer, "pop", viewPosition.x + 140, viewPosition.y );
		        drawGraph( shapeRenderer, "le", viewPosition.x + 140, viewPosition.y + 30 );
		        drawGraph( shapeRenderer, "he", viewPosition.x + 140, viewPosition.y + 50 );
		        drawGraph( shapeRenderer, "se", viewPosition.x + 140, viewPosition.y + 70 );
		        drawGraph( shapeRenderer, "su", viewPosition.x + 140, viewPosition.y + 90 );
	    	}
		}
	        
        super.drawShape( shapeRenderer );
    }
}
