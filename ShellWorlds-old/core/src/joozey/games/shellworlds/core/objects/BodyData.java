package joozey.games.shellworlds.core.objects;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

import joozey.libs.powerup.graphics.ColorMath;
import joozey.libs.powerup.object.GameObject2DData;

/**
 * Created by mint on 5-5-14.
 */
public class BodyData extends GameObject2DData
{
    public static enum BodyType
    {
        PLANET, STAR
    };

    private double angle = 0.;
    private double distance;
    private Color orbitColor;
    private Color selectedOrbitColor;
    private String name;
    private double velocity;
    private double size;
    private boolean highlighted;
    private double reach;
    private ArrayList<BodyObject> reachableBodyList;
    private BodyType bodyType;
    private double buildProgress;
    private int years;

    private float mass;
    private float density;
    private float atmosphere;
    private float diversity;

    public BodyData( BodyType bodyType, float distance, float size, float reach )
    {
        super( 0, 0, size, size );

        this.reachableBodyList = new ArrayList<BodyObject>();
        this.bodyType = bodyType;
        this.reach = reach;
        this.angle = (float)(Math.random() * Math.PI*2f);
        this.distance = distance;
        this.size = size;
        this.velocity = 4f * 33f / distance;    //based on an orbitperiod : distance ratio of 1 : 3.3*10^18
        this.years = 0;

        this.mass = (float)Math.random() * 0.8f + 0.2f;
        this.density = (float)Math.random() * 0.9f + 0.1f;
        this.atmosphere = (float)Math.random() * 1f;
        this.diversity = (float)Math.random() * 1f;

        Random r = new Random();
        this.name = "";
        int chars = 3;//(int)(Math.random() * 4) + 4;
        for( int i = 0; i < chars; i++ )
        {
            name += (char)(r.nextInt(26) + 'a');
        }

        Color color = new Color();
        ColorMath.xform(color, .2f, ColorMath.ColorC.LUMINANCE, true);
        ColorMath.xform( color, 1f, ColorMath.ColorC.SATURATION, true );
        ColorMath.xform( color, (float)Math.random(), ColorMath.ColorC.HUE, true );
        color.a = 1f;

        this.orbitColor = color;
        this.selectedOrbitColor = ColorMath.xform( color, 0.5f, ColorMath.ColorC.LUMINANCE, false );
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

    public double getDistance()
    {
        return this.distance;
    }

    public double getAngle() { return this.angle; }
    public void addAngle( double amount )
    {
        //made a revolution
        if( (this.angle % 360) + (amount / (Math.PI * 2.)) >= 360 )
        {
            this.years++;
        }

        this.angle += amount/(Math.PI*2.);
    }

    public double getVelocity()
    {
        return this.velocity;
    }

    public double getBodySize()
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
        return "Mass: " + (this.mass * 10);
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

    public int getYears()
    {
        return this.years;
    }

    public double getReach() {
        return reach;
    }

    public void addReachableBody( BodyObject bodyObject ) {
        this.reachableBodyList.add( bodyObject );
    }

    public void removeReachableBody( BodyObject bodyObject ) {
        this.reachableBodyList.remove(bodyObject);
    }

    public ArrayList<BodyObject> getReachableBodies()
    {
        return this.reachableBodyList;
    }

    public BodyType getType() {
        return bodyType;
    }

    public double getBuildingProgress()
    {
        return this.buildProgress;
    }

    public void addBuildingProgress( float progress )
    {
        this.buildProgress += progress;
        this.buildProgress %= 100;
    }

    public CharSequence readBuildingProject() {
        return "Constructing Shellworld";
    }

    public CharSequence readBuildingProgress() {
        return (int)this.buildProgress + "% COMPLETED";
    }
}