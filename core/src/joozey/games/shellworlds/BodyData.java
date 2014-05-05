package joozey.games.shellworlds;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

import joozey.libs.powerup.graphics.ColorMath;
import joozey.libs.powerup.object.GameObject2DData;

/**
 * Created by mint on 5-5-14.
 */
public class BodyData extends GameObject2DData
{
    private float angle = 0f;
    private float distance;
    private Color orbitColor;
    private String name;
    private float velocity;
    private float size;

    public BodyData( float distance, float size, float width )
    {
        super( 0, 0, width, width );

        this.angle = (float)(Math.random() * Math.PI*2);
        this.distance = distance;
        this.size = size;
        this.velocity = 3.3f / distance;    //based on an orbitperiod : distance ratio of 1 : 3.3*10^18

        Random r = new Random();
        this.name = "";
        int chars = (int)(Math.random() * 4) + 4;
        for( int i = 0; i < chars; i++ )
        {
            name += (char)(r.nextInt(26) + 'a');
        }

        Color color = new Color();
        ColorMath.xform(color, 0.2f, ColorMath.ColorC.LUMINANCE, true);
        ColorMath.xform( color, 0.6f, ColorMath.ColorC.SATURATION, true );
        ColorMath.xform( color, (float)Math.random(), ColorMath.ColorC.HUE, true );
        color.a = 1f;

        this.setOrbitColor(color);
    }

    public String getName()
    {
        return this.name;
    }

    public void setOrbitColor( Color color )
    {
        this.orbitColor = color;
    }

    public Color getOrbitColor()
    {
        return this.orbitColor;
    }

    public float getDistance()
    {
        return this.distance;
    }

    public float getAngle() { return this.angle; }
    public void addAngle( float amount ) { this.angle += amount; }

    public float getVelocity()
    {
        return this.velocity;
    }

    public float getBodySize()
    {
        return this.size;
    }

}
