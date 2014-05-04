package joozey.games.shellworlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

import joozey.libs.powerup.control.GameRunnable;
import joozey.libs.powerup.control.GameThread;
import joozey.libs.powerup.game.GameData;
import joozey.libs.powerup.graphics.ColorMath;

/**
 * Created by mint on 4/30/14.
 */
public class Body extends Sprite implements GameRunnable
{
    private Texture texture;
    private float angle = 0f;
    private float distance;
    private float size;
    private Color orbitColor;
    private String name;
    private float velocity;

    private static TextureRegion createTextureRegion( String planetName, float size )
    {
        Texture texture = new Texture(Gdx.files.internal("data/" + planetName + ".png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return new TextureRegion(texture, 0, 0, 160, 160);
    }

    public Body( String planetName, float distance, float size )
    {
        super( createTextureRegion( planetName, size ) );

        this.setScale(size);
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        this.setPosition(-this.getWidth() / 2 - distance, -this.getHeight() / 2);

        this.angle = (float)(Math.random() * Math.PI*2);
        this.distance = distance;
        this.size = size;
        this.texture = super.getTexture();
        this.velocity = 3.3f / distance;    //based on a orbitperiod : distance of 1 : 3.3*10^18


        Random r = new Random();
        this.name = "";
        int chars = (int)(Math.random() * 4) + 4;
        for( int i = 0; i < chars; i++ )
        {
            name += (char)(r.nextInt(26) + 'a');
        }


        Color color = new Color();
        ColorMath.xform( color, 0.4f, ColorMath.ColorC.LUMINANCE, true );
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

    public float getSize()
    {
        return this.size;
    }


    @Override
    public void run( GameThread gameThread )
    {
        angle += velocity * GameData.getSpeed();
        this.setPosition(
            -this.getWidth() / 2 - distance * (float)Math.cos( angle ),
            -this.getHeight() / 2 - distance * (float)Math.sin( angle )
        );
    }

    public float getDistance()
    {
        return this.distance;
    }

    @Override
    public boolean isInitialised()
    {
        return true;
    }


    public void dispose()
    {
        this.texture.dispose();
    }
}
