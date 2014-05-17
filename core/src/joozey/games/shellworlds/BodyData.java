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
    private Color selectedOrbitColor;
    private String name;
    private float velocity;
    private float size;
    private boolean shell;
    private boolean highlighted;
    private float reach;

    private float mass;
    private float density;
    private float atmosphere;
    private float diversity;

    public BodyData( float distance, float size, float width, float reach )
    {
        super( 0, 0, width, width );

        this.reach = reach;
        this.angle = (float)(Math.random() * Math.PI*2f);
        this.distance = distance;
        this.size = size;
        this.velocity = 33f / distance;    //based on an orbitperiod : distance ratio of 1 : 3.3*10^18

        this.mass = (float)Math.random() * 0.8f + 0.2f;
        this.density = (float)Math.random() * 0.9f + 0.1f;
        this.atmosphere = (float)Math.random() * 1f;
        this.diversity = (float)Math.random() * 1f;

        Random r = new Random();
        this.name = "";
        int chars = (int)(Math.random() * 4) + 4;
        for( int i = 0; i < chars; i++ )
        {
            name += (char)(r.nextInt(26) + 'a');
        }

        Color color = new Color();
        ColorMath.xform(color, 0.2f, ColorMath.ColorC.LUMINANCE, true);
        ColorMath.xform( color, 1f, ColorMath.ColorC.SATURATION, true );
        ColorMath.xform( color, (float)Math.random(), ColorMath.ColorC.HUE, true );
        color.a = 1f;

        this.orbitColor = color;
        this.selectedOrbitColor = ColorMath.xform( color, 0.5f, ColorMath.ColorC.LUMINANCE, false );
    }

    public boolean hasShell()
    {
        return this.shell;
    }
    public void setShell( boolean on )
    {
        this.shell = on;
    }

    public boolean isHighlighted() { return this.highlighted; }
    public void setHighlighted( boolean on ) { this.highlighted = on; }

    public String getName()
    {
        return this.name;
    }

    public Color getOrbitColor()
    {
        return this.orbitColor;
    }

    public Color getSelectedOrbitColor()
    {
        return this.selectedOrbitColor;
    }

    public float getDistance()
    {
        return this.distance;
    }

    public float getAngle() { return this.angle; }
    public void addAngle( float amount ) { this.angle += amount/(Math.PI*2); }

    public float getVelocity()
    {
        return this.velocity;
    }

    public float getBodySize()
    {
        return this.size;
    }

    public float getMass() {
        return mass;
    }

    public float getDensity() {
        return density;
    }

    public float getAtmosphere() {
        return atmosphere;
    }

    public float getDiversity() {
        return diversity;
    }

    public String readMassInfo() {
        return "Mass scalar: " + (this.mass * 10);
    }

    public String readDensityInfo() {
        return "Surface density: " + this.density;
    }

    public String readAtmosphereInfo() {
        return "Atmosphere density: " + this.atmosphere;
    }

    public String readDiversityInfo() {
        return "Diversity variable: " + (this.diversity + 10);
    }

    public float getReach() {
        return reach;
    }

}