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

import joozey.libs.powerup.control.GameRunnable;
import joozey.libs.powerup.control.GameThread;
import joozey.libs.powerup.game.GameData;
import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.object.BatchManager;

public class ShellWorlds extends Game implements GameRunnable, ApplicationListener, InputProcessor, GestureDetector.GestureListener
{
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ArrayList<BodyObject> bodyObjectList;
    private Vector2 prevScreen;
    private int pointers = 0;
    private boolean lockInput = false;
    private BodyObject followBodyObject;
    private BodyObject touchedBodyOrbit;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private DefaultSprite background;

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
        gameThread.register( this );

        bodyObjectList = new ArrayList<BodyObject>();

        BodyObject star = new BodyObject( gameThread, "star", 0.001f, 10f + (float)(Math.random() * 10f), false );
        gameThread.register( star );
        bodyObjectList.add( star );

        String[] planets = { "brown-planet", "blue-planet", "purple-planet" };

        for( int i = 0; i < 10; i++ )
        {
            BodyObject planet = new BodyObject( gameThread, planets[(int)(Math.random()*planets.length)], 2000 + (i+2) * 7000 + (float)Math.random() * 4000, (float)Math.random() * 5f + 5f, ((int)(Math.random()*2)==0)?true:false );
            gameThread.register( planet );
            bodyObjectList.add( planet );
        }

        followBodyObject = bodyObjectList.get(0);

        camera = new OrthographicCamera( GameData.getWidth(), GameData.getHeight() );
        batch = BatchManager.getSpriteBatch();

        shapeRenderer = new ShapeRenderer();

        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 12; // font size 16 pixels
        params.characters = FONT_CHARACTERS;
        params.genMipMaps = true;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ModernDestronic.ttf"));
        font = generator.generateFont( params );
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        background = new DefaultSprite( "background1.png" );
    }

    @Override
    public void dispose()
    {
        batch.dispose();
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if ( followBodyObject != null ) {
            Vector2 position = followBodyObject.getData().getPosition();
            camera.position.set( position.x, position.y, 0 );
        }

        camera.update();

        batch.begin();

        float scale = 0.75f;
        float ratio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        Matrix4 normalProjection = new Matrix4().setToOrtho2D( 512f * (1f-scale), 512f * (1f-scale * ratio), 1024 * scale, 1024 * scale * ratio );
        normalProjection.translate( camera.position.cpy().scl(-0.001f) );
        batch.setProjectionMatrix(normalProjection);

        background.draw(batch);

        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for( BodyObject bodyObject : bodyObjectList )
        {
            BodyData bodyData = bodyObject.getData();
            if( bodyObject != touchedBodyOrbit ) {
                shapeRenderer.setColor(bodyData.getOrbitColor());
            }
            else
            {
                shapeRenderer.setColor(bodyData.getSelectedOrbitColor());
            }
            shapeRenderer.circle(0, 0, bodyData.getDistance());
        }

        shapeRenderer.end();

        batch.begin();

        batch.setProjectionMatrix(camera.combined);

        for( BodyObject bodyObject : bodyObjectList)
        {
            bodyObject.update();
        }

        normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
        batch.setProjectionMatrix(normalProjection);

        for( int i = 0; i < bodyObjectList.size(); i++ )
        {
            BodyObject bodyObject = bodyObjectList.get( i );
            BodyData bodyData = bodyObject.getData();

            Vector3 viewPosition = camera.project( new Vector3( bodyData.getPosition().x, bodyData.getPosition().y, 0 ));
            font.setColor( bodyData.getSelectedOrbitColor() );
            font.draw( batch, bodyData.getName(), viewPosition.x, viewPosition.y );
        }

        batch.end();
    }

    public BodyObject getBodyOnPosition( Vector3 worldPosition )
    {
        for( BodyObject bodyObject : bodyObjectList)
        {
            BodyData bodyData = bodyObject.getData();
            if( new Vector3( bodyData.getPosition().x, bodyData.getPosition().y, 0 ).dst( worldPosition ) < 1200 )
            {
                return bodyObject;
            }
        }

        return null;
    }



    public BodyObject getBodyOnDistance( Vector3 worldPosition )
    {
        for( BodyObject bodyObject : bodyObjectList)
        {
            BodyData bodyData = bodyObject.getData();
            if( Math.abs( bodyData.getPosition().len() - worldPosition.len() ) < 5000 )
            {
                return bodyObject;
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
            followBodyObject = null;
            camera.translate( (prevScreen.x - screenX) * camera.zoom, (prevScreen.y - screenY) * -camera.zoom );
            //camera.rotateAround( new Vector3( followBody.getX(), followBody.getY(), 0 ), new Vector3(1,0,0), (prevScreen.x - screenX));

            touchedBodyOrbit = getBodyOnDistance( camera.unproject( new Vector3( Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0 ) ) );

            prevScreen.x = screenX;
            prevScreen.y = screenY;
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        //touchedBodyOrbit = getBodyOnDistance( camera.unproject( new Vector3( screenX, screenY, 0 ) ) );
        return false;
    }

    @Override
    public boolean scrolled( int scrollAmount )
    {
        camera.zoom += 20 * scrollAmount * camera.zoom / (3 * camera.zoom);
        camera.zoom = Math.max( camera.zoom, 2f );
        return false;
    }


    /* for gestures */
    @Override
    public boolean touchDown(float v, float v2, int i, int i2) {
        return false;
    }

    @Override
    public boolean tap(float screenX, float screenY, int i, int i2)
    {
        //TODO move to a button
        if( touchedBodyOrbit != null )
        {
            followBodyObject = touchedBodyOrbit;
        }
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
        float difference = (initialDistance - distance)/50;
        difference = Math.min( Math.max( difference, -0.1f ), 0.1f );

        camera.zoom += 20 * difference * camera.zoom / (3 * camera.zoom);
        camera.zoom = Math.max( camera.zoom, 2f );

        return false;
    }

    @Override
    public boolean pinch(Vector2 vector2, Vector2 vector22, Vector2 vector23, Vector2 vector24) {
        return false;
    }

    @Override
    public void run(GameThread gameThread)
    {
    }

    @Override
    public boolean isInitialised() {
        return true;
    }
}