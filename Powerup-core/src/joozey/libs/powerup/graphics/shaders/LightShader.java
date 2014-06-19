package joozey.libs.powerup.graphics.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

import joozey.libs.powerup.graphics.DefaultModel;

/**
 * Created by joozey on 3/16/14.
 */
public class LightShader implements Shader
{
    private static final String vert = Gdx.files.internal("data/light.vertex.glsl").readString();
    private static final String frag = Gdx.files.internal("data/light.fragment.glsl").readString();

    private static ShaderProgram program;
    private Camera camera;
    private RenderContext context;
    private TextureDescriptor lightmapDescription;
    private TextureDescriptor textureDescription;
    private Environment environment;

    private DirectionalLight sunlight;
    private DirectionalLight moonlight;

    public LightShader(Environment environment, DefaultModel model ) //Texture lightMap, Texture texture )
    {
        this.environment = environment;
        this.sunlight = environment.directionalLights.first();
        this.moonlight = environment.directionalLights.get(1);

        Texture lightMap = model.getLightTexture();
        Texture texture = model.getTexture();

        model.setShader(this);
        lightmapDescription = TextureAttribute.createDiffuse(lightMap).textureDescription;
        textureDescription = TextureAttribute.createDiffuse( texture ).textureDescription;

        this.init();
    }


    @Override
    public void init(){
        if( program == null )
        {
            program = new ShaderProgram(vert, frag);
            if (!program.isCompiled())
                throw new GdxRuntimeException(program.getLog());
        }
    }

    @Override
    public void begin(Camera camera, RenderContext renderContext) {
        this.camera = camera;
        this.context = renderContext;

        program.begin();

        program.setUniformMatrix("u_projViewTrans", camera.combined);
        program.setUniformi("u_lightmap", context.textureBinder.bind( lightmapDescription ));
        program.setUniformi("u_texture", context.textureBinder.bind(textureDescription));

        program.setUniformf("u_dirSunlight", new Vector3(sunlight.direction).rotate(-90.0f, 0.0f, 1.0f, 0.0f) );
        program.setUniformf("u_dirMoonlight", new Vector3(moonlight.direction).rotate(-90.0f, 0.0f, 1.0f, 0.0f));
        program.setUniformf( "u_diffuseSunlight", sunlight.color );
        program.setUniformf( "u_diffuseMoonlight", moonlight.color );
        //program.setUniformf( "u_dirCam", camera.direction );

        context.setDepthTest( GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);
    }

    @Override
    public void render(Renderable renderable) {
        program.setUniformMatrix("u_worldTrans", renderable.worldTransform);

        renderable.mesh.render(program,
                renderable.primitiveType,
                renderable.meshPartOffset,
                renderable.meshPartSize);
    }

    @Override
    public void end() {
        program.end();
    }

    @Override
    public void dispose() {
        program.dispose();
    }
    @Override
    public int compareTo(Shader shader) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable renderable) {
        return true;
    }
}
