package joozey.games.shellworlds.core.objects;

import joozey.libs.powerup.game.GameData;
import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.object.BatchManager;
import joozey.libs.powerup.object.GameObject2D;

/**
 * Created by mint on 5-5-14.
 */
public class BodyObject extends GameObject2D
{
    private ShellObject shellObject;
    private BodyData bodyData;
    private BodyView bodyView;

    public BodyObject( BodyData.BodyType type, String planetImage, float distance, float size, float reach )
    {
        bodyView = new BodyView( planetImage );
        bodyData = new BodyData( type, distance, size * bodyView.getWidth(), reach );
        bodyView.setData( bodyData );

        this.shellObject = null;

        super.init( bodyData, bodyView );
    }

    @Override
    public BodyData getData()
    {
        return (BodyData) super.getData();
    }

    @Override
    public void run()
    {
        bodyData.addAngle( bodyData.getVelocity() * GameData.getSpeed() );

        if( this.shellObject != null ) { this.shellObject.run(); }

        super.run();
    }

    @Override
    public void update()
    {
        bodyData.setPosition(
                bodyData.getDistance() * (float)Math.cos( bodyData.getAngle() ),
                bodyData.getDistance() * (float)Math.sin( bodyData.getAngle() )
        );

        if( this.shellObject != null ) { this.shellObject.update(); }

        super.update();
    }

    public ShellObject getShell()
    {
        return this.shellObject;
    }
    public void setShell( ShellObject shellObject)
    {
        this.shellObject = shellObject;
    }

    @Override
    public void draw( BatchManager.DrawType drawType )
    {
        super.draw( drawType );
        if( this.shellObject != null ) { this.shellObject.draw( drawType ); }
    }


    public DefaultSprite getSprite() {
        return bodyView;
    }
}
