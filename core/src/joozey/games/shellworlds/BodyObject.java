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

    public BodyObject( GameThread gameThread, String planetImage, float distance, float size )
    {
        super( gameThread );

        bodyView = new BodyView( planetImage );
        bodyData = new BodyData( distance, size, bodyView.getWidth() );
        bodyView.setScale( bodyData.getBodySize(), bodyData.getBodySize() );

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
            -bodyData.getSize().x / 2f - bodyData.getDistance() * (float)Math.cos( bodyData.getAngle() ),
            -bodyData.getSize().y / 2f - bodyData.getDistance() * (float)Math.sin( bodyData.getAngle() )
        );
    }

}
