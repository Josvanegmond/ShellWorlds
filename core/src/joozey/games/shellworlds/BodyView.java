package joozey.games.shellworlds;

import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.graphics.StackedSprite;

/**
 * Created by mint on 4/30/14.
 */
public class BodyView extends StackedSprite
{
    public BodyView( String planetImage )
    {
        super( new DefaultSprite( planetImage + ".png"), 0, 0 );
        this.addSprite( new DefaultSprite("shell.png"), 0, 0 );
    }
}
