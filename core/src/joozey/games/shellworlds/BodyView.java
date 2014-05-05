package joozey.games.shellworlds;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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
                    this.addSprite(new DefaultSprite("shell.png"), 0, 0);
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

        if( bodyData.isHighlighted() == false ) {
            shapeRenderer.setColor(bodyData.getOrbitColor());
        }
        else
        {
            shapeRenderer.setColor(bodyData.getSelectedOrbitColor());
        }

        shapeRenderer.circle(0, 0, bodyData.getDistance());
    }
}
