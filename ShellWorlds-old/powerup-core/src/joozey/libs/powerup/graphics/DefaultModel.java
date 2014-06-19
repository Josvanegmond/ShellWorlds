package joozey.libs.powerup.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

public class DefaultModel extends ModelInstance
{
    private static AssetManager modelManager = new AssetManager();
    private static AssetManager textureManager = new AssetManager();

    private static Model getLoadedModel( String modelName )
    {
        Model model = modelManager.get( "data/" + modelName + ".g3db", Model.class );
        return model;
    }

    private static Texture getLoadedTexture( String textureName )
    {
        Texture texture = textureManager.get( "data/" + textureName + ".png", Texture.class );
        return texture;
    }

    public static void prepare()
    {
        String[] modelNames = { "controltower", "cylinder", "glider", "hangar", "runway", "ter_corner_in",
                "ter_corner_out", "ter_wall", "ter_flat" };

        for( String modelName : modelNames )
        {
            modelManager.load( "data/" + modelName + ".g3db", Model.class );
        }


        String[] textureNames = { "controltower_light", "runway_light", "glider_light", "hangar_light" };

        for( String textureName : textureNames )
        {
            textureManager.load( "data/" + textureName + ".png", Texture.class );
        }
    }

    public static boolean isPrepared()
    {
        return (modelManager.update() && textureManager.update());
    }


    private Shader shader;
    private Texture lightTexture;
    private Texture texture;

    public DefaultModel( String modelName )
    {
        super(getLoadedModel(modelName));
        if( this.materials.first() != null && this.materials.first().get(TextureAttribute.Diffuse) != null && ((TextureAttribute)this.materials.first().get(TextureAttribute.Diffuse)).textureDescription != null )
        {
            this.texture = ((TextureAttribute)this.materials.first().get(TextureAttribute.Diffuse)).textureDescription.texture;
        }
    }

    public static void dispose()
    {
        modelManager.dispose();
        textureManager.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public Texture getLightTexture()
    {
        return lightTexture;
    }

    public void setLightTexture( String lightMap )
    {
        this.lightTexture = getLoadedTexture( lightMap );
    }

    public Shader getShader()
    {
        return this.shader;
    }
    public void setShader( Shader shader )
    {
        this.shader = shader;
    }
}
