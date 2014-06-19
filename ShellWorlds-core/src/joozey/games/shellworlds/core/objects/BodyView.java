package joozey.games.shellworlds.core.objects;

import joozey.games.shellworlds.core.ShellWorldData;
import joozey.libs.powerup.graphics.MatrixSet.MatrixType;
import joozey.libs.powerup.game.GameData;
import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.graphics.RenderMethod;
import joozey.libs.powerup.graphics.RenderMethod.DrawType;
import joozey.libs.powerup.graphics.StackedSprite;
import joozey.libs.powerup.object.Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by mint on 4/30/14.
 */
public class BodyView extends StackedSprite
{
    private BodyData bodyData;

    public BodyView( String planetImage )
    {
        super(new DefaultSprite( planetImage + "_medium.png"), 0, 0);
        this.addSprite( new DefaultSprite( "atmosphere_medium.png" ), 0, 0);
        
		Renderer.add( this, 8, new RenderMethod( DrawType.SPRITE, MatrixType.SCREENMATRIX ) );
		Renderer.add( this, 9, new RenderMethod( DrawType.SPRITE, MatrixType.CAMERAMATRIX ) );
		Renderer.add( this, 10, new RenderMethod( DrawType.SHAPE, MatrixType.CAMERAMATRIX, ShapeType.Line ) );
		Renderer.add( this, 11, new RenderMethod( DrawType.SHAPE, MatrixType.CAMERAMATRIX, ShapeType.Filled ) );
    }

    public BodyView( DefaultSprite image )
    {
        super(image, 0, 0);
        
		Renderer.add( this, 8, new RenderMethod( DrawType.SPRITE, MatrixType.SCREENMATRIX ) );
		Renderer.add( this, 9, new RenderMethod( DrawType.SPRITE, MatrixType.CAMERAMATRIX ) );
		Renderer.add( this, 10, new RenderMethod( DrawType.SHAPE, MatrixType.CAMERAMATRIX, ShapeType.Line ) );
		Renderer.add( this, 11, new RenderMethod( DrawType.SHAPE, MatrixType.CAMERAMATRIX, ShapeType.Filled ) );
    }

    public void setData( BodyData bodyData )
    {
        this.bodyData = bodyData;
        
        this.bodyData.setOffset( 0, 0 );
    }

    @Override
    public void drawSpriteBatch( Batch batch, MatrixType matrixType )
    {
        if( this.bodyData != null )
        {
        	if( matrixType == MatrixType.SCREENMATRIX )
        	{
	            Vector3 viewPosition = GameData.getCamera().project( new Vector3( bodyData.getPosition().x - 200f, bodyData.getPosition().y + 200f, 0 ));
	
	            BitmapFont alienFont = ShellWorldData.getInstance().getAlienFont();
	        	
	            alienFont.setColor(bodyData.getSelectedOrbitColor());
	            alienFont.draw(batch, bodyData.getName(), viewPosition.x, viewPosition.y);
	
		            
	            BodyObject touchedBody = ShellWorldData.getInstance().getTouchedBody();
	            if( touchedBody != null && touchedBody.getSprite() == this )
	            {
	                //batch.draw( this, 210, GameData.getHeight() - 84, 80, 80 );
	                
		            viewPosition = new Vector3( GameData.getWidth() / 2f - 128, GameData.getHeight() / 2f + 96, 0 );
		            
		            BitmapFont normalFont = ShellWorldData.getInstance().getNormalFont();
		            
		            normalFont.setColor(bodyData.getSelectedOrbitColor());
		            normalFont.draw(batch, bodyData.readMassInfo(), viewPosition.x, viewPosition.y);
		            normalFont.draw(batch, bodyData.readDensityInfo(), viewPosition.x, viewPosition.y - 16f);
		            normalFont.draw(batch, bodyData.readAtmosphereInfo(), viewPosition.x, viewPosition.y - 32f);
		            normalFont.draw(batch, bodyData.readDiversityInfo(), viewPosition.x, viewPosition.y - 48f);
		
		           
		            if( ShellWorldData.getInstance().showDetailedInfo == true )
		            {
		                normalFont.draw( batch, bodyData.readBuildingProject(), GameData.getWidth() * 5f/7f, GameData.getHeight() / 2 + 96f );
		                normalFont.draw( batch, bodyData.readBuildingProgress(), GameData.getWidth() * 5f/7f, GameData.getHeight() / 2 + 80f );
		            }
	            }
        	}
        	
        	if( matrixType == MatrixType.CAMERAMATRIX )
        	{
        		//rotate the atmosphere
        		super.getSpriteStack().get(0).setRotation( (float) (this.bodyData.getAngle() * 180/Math.PI) );
        		super.setRotation( (float)(this.bodyData.getAxisRotation() * 180/Math.PI) );
        		
        		//keep scale equal
        		super.drawSpriteBatch( batch, matrixType );
        	}
        }
    }


    @Override
    public void drawShape( ShapeRenderer shapeRenderer )
    {
        Color color = bodyData.getOrbitColor();
        Color selectedColor = bodyData.getSelectedOrbitColor();
        
        if( shapeRenderer.getCurrentType() == ShapeType.Line )
        {
	        shapeRenderer.setColor( 1f, 1f, 1f, (float)Math.random()/3f + .3f );
	        for( BodyObject bodyObject : this.bodyData.getReachableBodies() ) {
	            BodyData bodyData = bodyObject.getData();
	            Vector2 displace = new Vector2( (float)Math.random() * 20, (float)Math.random() * 20 );
	            shapeRenderer.line( this.bodyData.getPosition().cpy().add(displace), bodyData.getPosition().cpy().add(displace) );
	        }
	
	        if( bodyData.isHighlighted() == false ) {
	            Gdx.gl.glLineWidth(1.2f);
	            shapeRenderer.setColor( color.r, color.g, color.b, 0.3f );
	        }
	        else
	        {
	            Gdx.gl.glLineWidth(1.8f);
	            shapeRenderer.setColor( selectedColor.r, selectedColor.g, selectedColor.b, .5f );
	        }
	
	        shapeRenderer.circle(0, 0, (float)bodyData.getDistance());
	    }


        if( shapeRenderer.getCurrentType() == ShapeType.Filled )
        {
	        shapeRenderer.setColor( color.r, color.g, color.b, .3f );
	        shapeRenderer.arc(
	                bodyData.getPosition().x, bodyData.getPosition().y, bodyData.getSize().x + 20, 0, 360f + (float)this.bodyData.getBuildingProgress() * 3.6f );
	
	        this.bodyData.addBuildingProgress( .2f );
	
	        if( bodyData.isHighlighted() == false ) {
	            shapeRenderer.setColor( color.r, color.g, color.b, 0.1f );
	        }
	        else
	        {
	            shapeRenderer.setColor( selectedColor.r, selectedColor.g, selectedColor.b, 0.2f );
	        }
	
	        shapeRenderer.circle(
	                bodyData.getPosition().x, bodyData.getPosition().y, (float)bodyData.getReach() );
        }
    }
}
