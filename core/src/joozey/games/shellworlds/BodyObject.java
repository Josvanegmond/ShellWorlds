package joozey.games.shellworlds;

import joozey.libs.powerup.control.GameThread;
import joozey.libs.powerup.game.GameData;
import joozey.libs.powerup.object.GameObject2D;

/**
 * Created by mint on 5-5-14.
 */
public class BodyObject extends GameObject2D
{
    private BodyData bodyData;
    private BodyView bodyView;

    public BodyObject( GameThread gameThread, String planetImage, float distance, float size, float reach, boolean shell )
    {
        super( gameThread );

        bodyView = new BodyView( planetImage );
        bodyData = new BodyData( distance, size, bodyView.getWidth(), reach );
        bodyData.setShell( shell );
        bodyView.setScale( bodyData.getBodySize(), bodyData.getBodySize() );
        bodyView.setData( bodyData );

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
        bodyData.setPosition(
                (float)(-bodyData.getSize().x / 2f - bodyData.getDistance() * Math.cos( bodyData.getAngle() ) ),
                (float)(-bodyData.getSize().y / 2f - bodyData.getDistance() * Math.sin( bodyData.getAngle() ) )
        );

        super.run( gameThread );
    }

}
