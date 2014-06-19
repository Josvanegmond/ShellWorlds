package joozey.libs.powerup.graphics;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Created by joozey on 3/26/14.
 */
public class TextureFactory {

    private static HashMap<String,Texture> textureHashmap = new HashMap<String,Texture>();

    public static Texture get( String name )
    {
        Texture texture = textureHashmap.get( name );
        if( texture == null )
        {
            texture = new Texture( name );
            textureHashmap.put( name, texture );
        }

        return texture;
    }
}
