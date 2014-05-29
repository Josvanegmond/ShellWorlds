package joozey.games.shellworlds.core;

import joozey.libs.powerup.control.GameThread;
import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.object.GameObject2D;

import java.util.ArrayList;

/**
 * Created by acer on 29-5-2014.
 */
public class ShellObject extends GameObject2D
{
    public static enum Type
    {
        RESIDENTIAL,
        INDUSTRIAL,
        COMMERCIAL
    };

    private ArrayList<ShellObject> requestingShellObjects;
    private Type type;

    private float wealthLevel;
    private float productionRate;
    private float productionQuality;
    private float socialHappiness;

    private float consumeLightElements, lightElements;
    private float consumeHeavyElements, heavyElements;
    private float maintenanceCost, deterioration;

    private BodyData bodyData;

    public ShellObject(GameThread gameThread, BodyData bodyData, Type type)
    {
        super( gameThread );

        this.type = type;
        this.bodyData = bodyData;
        this.requestingShellObjects = new ArrayList<ShellObject>();

        super.init( bodyData, new ShellView("shell.png"));
    }

    public float getProfit()
    {
        return (1f+productionRate) * productionQuality;
    }

    public void requestExport( ShellObject requestingShellObject)
    {
        this.requestingShellObjects.add(requestingShellObject);
    }
}
