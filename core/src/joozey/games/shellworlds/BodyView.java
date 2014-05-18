package joozey.games.shellworlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.graphics.StackedSprite;
import joozey.libs.powerup.object.BatchManager;

/**
 * Created by mint on 4/30/14.
 */
public class BodyView extends StackedSprite
{
    private BodyData bodyData;
    private boolean shell;
    private float flashAlpha = (float)Math.random() * 10f;

    public BodyView( String planetImage )
    {
        super(new DefaultSprite(planetImage + ".png"), 0, 0);
    }

    public void setData( BodyData bodyData )
    {
        this.bodyData = bodyData;
    }

    @Override
    public void drawBatch()
    {
        if( this.bodyData != null ) {
            if (this.bodyData.hasShell() != shell) {
                this.shell = this.bodyData.hasShell();
                if (this.shell == true) {
                    this.addSprite(new DefaultSprite("shell.png"), 0, 0 );
                    float scale = this.bodyData.getBodySize();
                    this.setScale( scale, scale );
                } else {
                    this.removeSprite(0);
                }
            }

            super.drawBatch();
        }
    }


    @Override
    public void drawShape()
    {
        ShapeRenderer shapeRenderer = BatchManager.getShapeRenderer();
        Color color = bodyData.getOrbitColor();
        Color selectedColor = bodyData.getSelectedOrbitColor();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glLineWidth(1.8f);

        flashAlpha = (flashAlpha + 1) % 20;
        shapeRenderer.setColor( 1f, 1f, 1f, (float)Math.random() );
        for( BodyObject bodyObject : this.bodyData.getReachableBodies() ) {
            BodyData bodyData = bodyObject.getData();
            Vector2 displace = new Vector2( (float)Math.random() * 20, (float)Math.random() * 20 );
            shapeRenderer.line( this.bodyData.getPosition().add(displace), bodyData.getPosition().add(displace) );
        }

        if( bodyData.isHighlighted() == false ) {
            shapeRenderer.setColor( color.r, color.g, color.b, 0.3f );
        }
        else
        {
            shapeRenderer.setColor( selectedColor.r, selectedColor.g, selectedColor.b, .7f );
        }

        shapeRenderer.circle(0, 0, bodyData.getDistance());

        shapeRenderer.end();

        //TODO: should make this a responsibility for batchManager
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor( color.r, color.g, color.b, .3f );
        shapeRenderer.arc(
                bodyData.getPosition().x, bodyData.getPosition().y, bodyData.getSize().x + 20, 0, 360f + this.bodyData.getBuildingProgress() * 3.6f );

        this.bodyData.addBuildingProgress( .2f );

        if( bodyData.isHighlighted() == false ) {
            shapeRenderer.setColor( color.r, color.g, color.b, 0.1f );
        }
        else
        {
            shapeRenderer.setColor( selectedColor.r, selectedColor.g, selectedColor.b, 0.3f );
        }

        shapeRenderer.circle(
                bodyData.getPosition().x, bodyData.getPosition().y, bodyData.getReach() );

        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    }
}
