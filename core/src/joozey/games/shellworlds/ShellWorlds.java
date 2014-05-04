package joozey.games.shellworlds;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import joozey.libs.powerup.control.GameThread;
import joozey.libs.powerup.game.GameData;

public class ShellWorlds extends Game implements ApplicationListener, InputProcessor, GestureDetector.GestureListener
{
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ArrayList<Body> bodyList;
    private Vector2 prevScreen;
    private int pointers = 0;
    private boolean lockInput = false;
    private Body followBody;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";

    @Override
    public void create()
    {
        //Texture.setEnforcePotImages(false);

        prevScreen = new Vector2();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor( inputMultiplexer );

        inputMultiplexer.addProcessor( new GestureDetector( this ) );
        inputMultiplexer.addProcessor( this );


        GameData gameData = new GameData( 0, 0 );
        GameThread gameThread = new GameThread( gameData );

        bodyList = new ArrayList<Body>();

        Body star = new Body( "star", 0.001f, 2f + (float)(Math.random() * 3f) );
        gameThread.register( star );
        bodyList.add( star );

        String[] planets = { "brown-planet", "blue-planet", "purple-planet" };

        for( int i = 0; i < 10; i++ )
        {
            Body planet = new Body( planets[(int)(Math.random()*planets.length)], 200 + (i+2) * 700 + (float)Math.random() * 400, (float)Math.random() * 0.8f + 0.2f );
            gameThread.register( planet );
            bodyList.add( planet );
        }

        followBody = bodyList.get(0);

        camera = new OrthographicCamera( GameData.getWidth(), GameData.getHeight() );
        batch = new SpriteBatch();

        shapeRenderer = new ShapeRenderer();

        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 12; // font size 16 pixels
        params.characters = FONT_CHARACTERS;
        params.genMipMaps = true;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ModernDestronic.ttf"));
        font = generator.generateFont( params );
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
    }

    @Override
    public void dispose() {
        batch.dispose();

        for( Body body : bodyList )
        {
            body.dispose();
        }
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set( followBody.getX(), followBody.getY(), 0 );
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for( Body body : bodyList )
        {
            shapeRenderer.setColor( body.getOrbitColor() );
            shapeRenderer.circle( 0, 0, body.getDistance() );
        }

        shapeRenderer.end();

        batch.begin();

        Matrix4 normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
        batch.setProjectionMatrix(normalProjection);

        for( Body body : bodyList )
        {
            Vector3 viewPosition = camera.project(new Vector3(body.getX(), body.getY(), 0));
            font.setColor( body.getOrbitColor() );
            font.draw( batch, body.getName(), viewPosition.x, viewPosition.y );
        }

        batch.setProjectionMatrix(camera.combined);

        for( Body body : bodyList )
        {
            body.draw(batch);
        }

        batch.end();


    }

    public Body getBodyOnPosition( Vector3 worldPosition )
    {
        for( Body body : bodyList )
        {
            if( new Vector3( body.getX(), body.getY(), 0 ).dst( worldPosition ) < 650 )
            {
                return body;
            }
        }

        return null;
    }

    @Override
    public void resize(int width, int height) {
        this.camera.viewportWidth = width;
        this.camera.viewportHeight = height;
        this.camera.update();
    }



    /* for basic input */
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        pointers++;

        if( pointers == 1 )
        {
            prevScreen.x = screenX;
            prevScreen.y = screenY;

            Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
            camera.unproject(worldCoordinates);
            Body foundBody = getBodyOnPosition( worldCoordinates );
            if( foundBody != null )
            {
                followBody = foundBody;
            }
        }

        if( pointers == 2 )
        {
            lockInput = true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        pointers--;

        if( pointers == 0 )
        {
            lockInput = false;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        if( lockInput == false )
        {
            camera.translate( (prevScreen.x - screenX) * camera.zoom, (prevScreen.y - screenY) * -camera.zoom );

            prevScreen.x = screenX;
            prevScreen.y = screenY;
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled( int scrollAmount )
    {
        camera.zoom += scrollAmount * camera.zoom / (2 * camera.zoom);
        camera.zoom = Math.max( camera.zoom, 0.1f );
        return false;
    }


    /* for gestures */
    @Override
    public boolean touchDown(float v, float v2, int i, int i2) {
        return false;
    }

    @Override
    public boolean tap(float v, float v2, int i, int i2) {
        return false;
    }

    @Override
    public boolean longPress(float v, float v2) {
        return false;
    }

    @Override
    public boolean fling(float v, float v2, int i) {
        return false;
    }

    @Override
    public boolean pan(float v, float v2, float v3, float v4) {
        return false;
    }

    @Override
    public boolean panStop(float v, float v2, int i, int i2) {
        return false;
    }


    @Override
    public boolean zoom(float initialDistance, float distance)
    {
        //Calculate pinch to zoom
        float difference = (initialDistance - distance)/500;
        difference = Math.min( Math.max( difference, -0.1f ), 0.1f );

        camera.zoom += difference * camera.zoom / (2 * camera.zoom);
        camera.zoom = Math.max( camera.zoom, 0.1f );

        return false;
    }

    @Override
    public boolean pinch(Vector2 vector2, Vector2 vector22, Vector2 vector23, Vector2 vector24) {
        return false;
    }
}