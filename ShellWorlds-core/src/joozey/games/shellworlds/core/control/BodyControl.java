package joozey.games.shellworlds.core.control;

import java.util.ArrayList;

import joozey.games.shellworlds.core.ShellWorldData;
import joozey.games.shellworlds.core.objects.BodyData;
import joozey.games.shellworlds.core.objects.BodyObject;
import joozey.games.shellworlds.core.objects.ShellObject;
import joozey.libs.powerup.control.GameRunnable;

/**
 * Created by acer on 31-5-2014.
 */
public class BodyControl implements GameRunnable
{
    private boolean initialised = false;
    private ShellWorldData shellWorldData;

    public BodyControl( ShellWorldData shellWorldData )
    {
        this.shellWorldData = shellWorldData;

        float minDistance = 6000;
        for( int i = 0; i < 10; i++ )
        {
            minDistance += 2000f + (5000f * (int)( Math.random() * 3f ) ) + (float) Math.random() * 500f;
            BodyObject planet = new BodyObject( BodyData.BodyType.PLANET, "planet" + (int)(Math.random()*5+1), minDistance, (float) Math.random() * 1f + 1f, 15000);// ((int)(Math.random()*2)==0)?true:false );
            this.shellWorldData.addBody( planet );

            if( (int)(Math.random()*3) == -1 )
            {
	            ShellObject shell = new ShellObject( planet.getData(), 1 );//(int)(Math.random() * 4) );
	            planet.setShell( shell );
            }
        }

        this.shellWorldData.setFollowBodyObject( this.shellWorldData.getBodyObjectList().get(0) );

        this.initialised = true;
    }

    @Override
    public void run()
    {
        ArrayList<BodyObject> bodyObjectList = shellWorldData.getBodyObjectList();

        for (BodyObject bodyObject : bodyObjectList) {
            bodyObject.run();
        }

        for (BodyObject bodyObject : bodyObjectList) {
            for (BodyObject bodyObject2 : bodyObjectList) {
                if (bodyObject != bodyObject2 &&
                        bodyObject2.getData().getType() != BodyData.BodyType.STAR) {
                    if ( shellWorldData.isBodyOnPosition(bodyObject, bodyObject2, (int) bodyObject.getData().getReach())) {
                        if (bodyObject.getData().getReachableBodies().contains(bodyObject2) == false) {
                            bodyObject.getData().addReachableBody(bodyObject2);
                        }
                    } else {
                        bodyObject.getData().removeReachableBody(bodyObject2);
                    }
                }
            }
        }
    }

    @Override
    public boolean isInitialised() {
        return this.initialised;
    }
}
