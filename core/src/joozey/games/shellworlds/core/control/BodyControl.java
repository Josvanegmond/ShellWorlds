package joozey.games.shellworlds.core.control;

import java.util.ArrayList;

import joozey.games.shellworlds.core.ShellWorldData;
import joozey.games.shellworlds.core.objects.BodyData;
import joozey.games.shellworlds.core.objects.BodyObject;
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
