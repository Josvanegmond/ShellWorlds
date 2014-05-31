package joozey.games.shellworlds.core;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import joozey.games.shellworlds.core.objects.BodyData;
import joozey.games.shellworlds.core.objects.BodyObject;
import joozey.libs.powerup.game.GameData;
import joozey.libs.powerup.graphics.DefaultSprite;

/**
 * Created by acer on 31-5-2014.
 */
public class ShellWorldData extends GameData
{
    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";

    private BodyObject followBodyObject;
    private BodyObject touchedBodyOrbit, touchedBody;
    private BitmapFont alienFont, normalFont;
    private DefaultSprite background;
    private DefaultSprite marker;
    private NinePatch infoDisplay;
    private ArrayList<BodyObject> bodyObjectList;

    public ShellWorldData( float width, float height ) {
        super(width, height);
        this.bodyObjectList = new ArrayList<BodyObject>();
    }


    public BodyObject getFollowBodyObject() {
        return followBodyObject;
    }

    public void setFollowBodyObject(BodyObject followBodyObject) {
        this.followBodyObject = followBodyObject;
    }

    public BodyObject getTouchedBodyOrbit() {
        return touchedBodyOrbit;
    }

    public void setTouchedBodyOrbit(BodyObject touchedBodyOrbit) {
        this.touchedBodyOrbit = touchedBodyOrbit;
    }

    public BodyObject getTouchedBody() {
        return touchedBody;
    }

    public void setTouchedBody(BodyObject touchedBody) {
        this.touchedBody = touchedBody;
    }

    public BitmapFont getAlienFont() {
        return alienFont;
    }

    public void setAlienFont(BitmapFont alienFont) {
        this.alienFont = alienFont;
    }

    public BitmapFont getNormalFont() {
        return normalFont;
    }

    public void setNormalFont(BitmapFont normalFont) {
        this.normalFont = normalFont;
    }

    public DefaultSprite getBackground() {
        return background;
    }

    public void setBackground(DefaultSprite background) {
        this.background = background;
    }

    public DefaultSprite getMarker() {
        return marker;
    }

    public void setMarker(DefaultSprite marker) {
        this.marker = marker;
    }

    public NinePatch getInfoDisplay() {
        return infoDisplay;
    }

    public void setInfoDisplay(NinePatch infoDisplay) {
        this.infoDisplay = infoDisplay;
    }

    public ArrayList<BodyObject> getBodyObjectList() {
        return bodyObjectList;
    }





    public BodyObject getBodyOnPosition( Vector3 worldPosition, int range )
    {
        ArrayList<BodyObject> bodyObjectList = getBodyObjectList();
        for( BodyObject bodyObject : bodyObjectList)
        {
            if( isBodyOnPosition( bodyObject, worldPosition, range ) == true )
            {
                return bodyObject;
            }
        }

        return null;
    }

    public boolean isBodyOnPosition( BodyObject bodyObject, Vector3 worldPosition, int range )
    {
        BodyData bodyData = bodyObject.getData();
        if( new Vector3( bodyData.getPosition().x, bodyData.getPosition().y, 0 ).dst( worldPosition ) < range )
        {
            return true;
        }

        return false;
    }

    public boolean isBodyOnPosition( BodyObject bodyObject, BodyObject bodyObject2, int range )
    {
        BodyData bodyData = bodyObject.getData();
        if( bodyData.getPosition().dst( bodyObject2.getData().getPosition() ) < range )
        {
            return true;
        }

        return false;
    }


    public BodyObject getBodyOnDistance( Vector3 worldPosition, int range )
    {
        ArrayList<BodyObject> bodyObjectList = getBodyObjectList();
        for( BodyObject bodyObject : bodyObjectList)
        {
            BodyData bodyData = bodyObject.getData();
            if( Math.abs( bodyData.getPosition().len() - worldPosition.len() ) < range )
            {
                return bodyObject;
            }
        }

        return null;
    }

    public void addBody(BodyObject body) {
        this.bodyObjectList.add( body );
    }
}
