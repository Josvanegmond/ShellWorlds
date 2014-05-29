package joozey.games.shellworlds.core;

import joozey.libs.powerup.control.GameThread;
import joozey.libs.powerup.game.GameData;
import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.object.GameObject2D;

/**
 * Created by mint on 5-5-14.
 */
public class BodyObject extends GameObject2D
{
    private ShellObject shellObject;
    private BodyData bodyData;
    private BodyView bodyView;

    public BodyObject( GameThread gameThread, BodyData.BodyType type, String planetImage, float distance, float size, float reach )
    {
        super( gameThread );

        bodyView = new BodyView( planetImage );
        bodyData = new BodyData( type, distance, size * bodyView.getWidth(), reach );
        bodyView.setData( bodyData );

        this.shellObject = new ShellObject( gameThread,  bodyData, ShellObject.Type.RESIDENTIAL );

        super.init( bodyData, bodyView );
    }

    @Override
    public BodyData getData()
    {
        return (BodyData) super.getData();
    }

    @Override
    public void run( GameThread gameThread )
    {
        bodyData.addAngle( bodyData.getVelocity() * GameData.getSpeed() );
        super.run( gameThread );
    }

    @Override
    public void update()
    {
        bodyData.setPosition(
                bodyData.getDistance() * (float)Math.cos( bodyData.getAngle() ),
                bodyData.getDistance() * (float)Math.sin( bodyData.getAngle() )
        );

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


    public DefaultSprite getSprite() {
        return bodyView;
    }
}
