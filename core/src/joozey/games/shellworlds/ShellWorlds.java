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
    private BodyObject touchedBodyOrbit, touchedBody;
    private ShapeRenderer shapeRenderer;
    private BitmapFont alienFont, normalFont;
    private DefaultSprite background;
    private DefaultSprite marker;

    private GameThread gameThread;

    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";

    @Override
    public void create()
    {
        prevScreen = new Vector2();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor( inputMultiplexer );

        inputMultiplexer.addProcessor( new GestureDetector( this ) );
        inputMultiplexer.addProcessor( this );

        GameData gameData = new GameData( 0, 0 );
        gameThread = new GameThread( gameData );
        //gameThread.register( this );
        //TODO: separating planet movement to different thread will cause jittering with camera following objects

        bodyObjectList = new ArrayList<BodyObject>();

        BodyObject star = new BodyObject( gameThread, "star", 0.001f, 20f + (float)(Math.random() * 10f), false );
        bodyObjectList.add( star );

        String[] planets = { "brown-planet", "blue-planet", "purple-planet" };

        float minDistance = 6000;
        for( int i = 0; i < 10; i++ )
        {
            minDistance += 2000f + ((float)Math.random() * 5000f) * ((float) Math.random() * 5f);
            BodyObject planet = new BodyObject( gameThread, planets[(int)(Math.random()*planets.length)], minDistance, (float)Math.random() * 5f + 5f, ((int)(Math.random()*2)==0)?true:false );
            bodyObjectList.add( planet );
        }

        followBodyObject = bodyObjectList.get(0);

        camera = new OrthographicCamera( GameData.getWidth(), GameData.getHeight() );
        batch = BatchManager.getSpriteBatch();

        shapeRenderer = BatchManager.getShapeRenderer();

        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 14; // fontsize 16 pixels
        params.characters = FONT_CHARACTERS;
        params.genMipMaps = true;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ModernDestronic.ttf"));
        alienFont = generator.generateFont( params );

        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/SFAtarianSystem.ttf"));
        params.size = 13; // fontsize 16 pixels
        normalFont = generator.generateFont( params );

        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        background = new DefaultSprite( "background2.png" );
        marker = new DefaultSprite( "marker.png" );
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

        camera.update();


        //drawing the backdrop
        batch.begin();

        float scale = 0.75f;
        float ratio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        Matrix4 normalProjection = new Matrix4().setToOrtho2D( background.getWidth()/2f * (1f-scale), background.getHeight()/2f * (1f-scale * ratio), background.getWidth() * scale, background.getHeight() * scale * ratio );
        normalProjection.translate( camera.position.cpy().scl(-0.001f) );
        batch.setProjectionMatrix(normalProjection);

        background.draw( BatchManager.DrawType.BATCH );

        batch.end();


        //draw shapes of objects
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(camera.combined);

        for( BodyObject bodyObject : bodyObjectList )
        {
            bodyObject.draw( BatchManager.DrawType.SHAPE );
        }

        shapeRenderer.end();


        //draw batch of objects
        batch.begin();

        for( BodyObject bodyObject : bodyObjectList)
        {
            BodyData bodyData = bodyObject.getData();

            batch.setProjectionMatrix(camera.combined);
            bodyObject.draw( BatchManager.DrawType.BATCH );

            normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
            batch.setProjectionMatrix(normalProjection);

            Vector3 viewPosition = camera.project( new Vector3( bodyData.getPosition().x, bodyData.getPosition().y, 0 ));
            alienFont.setColor(bodyData.getSelectedOrbitColor());
            alienFont.draw(batch, bodyData.getName(), viewPosition.x + 50f, viewPosition.y);
        }

        //draw planet info at cursor
        if( this.touchedBody != null )
        {
            BodyData bodyData = this.touchedBody.getData();

            Vector3 viewPosition = camera.project( new Vector3( bodyData.getPosition().x, bodyData.getPosition().y, 0 ));
            normalFont.setColor(bodyData.getSelectedOrbitColor());
            normalFont.draw(batch, bodyData.readMassInfo(), viewPosition.x + 66f, viewPosition.y - 16f);
            normalFont.draw(batch, bodyData.readDensityInfo(), viewPosition.x + 66f, viewPosition.y - 32f);
            normalFont.draw(batch, bodyData.readAtmosphereInfo(), viewPosition.x + 66f, viewPosition.y - 48f);
            normalFont.draw(batch, bodyData.readDiversityInfo(), viewPosition.x + 66f, viewPosition.y - 64f);
        }

        //draw marker
        marker.setPosition( Gdx.graphics.getWidth()/2f - marker.getWidth()/2f, Gdx.graphics.getHeight()/2f - marker.getHeight()/2f );
        marker.draw( BatchManager.DrawType.BATCH );

        batch.end();
    }





    public BodyObject getBodyOnPosition( Vector3 worldPosition )
    {
        for( BodyObject bodyObject : bodyObjectList)
        {
            if( isBodyOnPosition( bodyObject, worldPosition ) == true )
            {
                return bodyObject;
            }
        }

        return null;
    }

    public boolean isBodyOnPosition( BodyObject bodyObject, Vector3 worldPosition )
    {
        BodyData bodyData = bodyObject.getData();
        if( new Vector3( bodyData.getPosition().x, bodyData.getPosition().y, 0 ).dst( worldPosition ) < 5000 )
        {
            return true;
        }

        return false;
    }


    public BodyObject getBodyOnDistance( Vector3 worldPosition )
    {
        for( BodyObject bodyObject : bodyObjectList)
        {
            BodyData bodyData = bodyObject.getData();
            if( Math.abs( bodyData.getPosition().len() - worldPosition.len() ) < 2000 )
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

        this.pointers++;

        if( pointers == 1 )
        {
            this.prevScreen.x = screenX;
            this.prevScreen.y = screenY;
        }

        if( this.pointers == 2 )
        {
            this.lockInput = true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        this.pointers--;

        if( pointers == 0 )
        {
            this.lockInput = false;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        this.touchedBodyOrbit = null;
        this.touchedBody = null;

        if( lockInput == false )
        {
            this.followBodyObject = null;
            this.camera.translate( (prevScreen.x - screenX) * this.camera.zoom, (this.prevScreen.y - screenY) * -this.camera.zoom );
            //camera.rotateAround( new Vector3( followBody.getX(), followBody.getY(), 0 ), new Vector3(1,0,0), (prevScreen.x - screenX));

            Vector3 screenCenter = new Vector3( Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0 );
            this.touchedBodyOrbit = getBodyOnDistance( this.camera.unproject( screenCenter ) );

            if( touchedBodyOrbit != null ) {
                if (isBodyOnPosition( this.touchedBodyOrbit, screenCenter ) == true) {
                    this.touchedBody = this.touchedBodyOrbit;
                }
            }

            this.prevScreen.x = screenX;
            this.prevScreen.y = screenY;
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
        this.camera.zoom += 20f * scrollAmount * this.camera.zoom / (3f * this.camera.zoom);
        this.camera.zoom = Math.max( this.camera.zoom, 2f );
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
        if( this.touchedBodyOrbit != null )
        {
            this.followBodyObject = this.touchedBodyOrbit;
            this.touchedBody = this.touchedBodyOrbit;
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
        float difference = (initialDistance - distance)/50f;
        difference = Math.min( Math.max( difference, -0.1f ), 0.1f );

        camera.zoom += difference;
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
        for (BodyObject bodyObject : bodyObjectList) {
            bodyObject.run( gameThread );

            //TODO: this should go to different thread but causes jittering
            bodyObject.run( gameThread );

            if (bodyObject == touchedBodyOrbit) {
                bodyObject.getData().setHighlighted(true);
            } else {
                bodyObject.getData().setHighlighted(false);
            }
        }

        if( camera != null ) {
            if (followBodyObject != null) {
                Vector2 position = followBodyObject.getData().getPosition();
                camera.position.set(position.x, position.y, 0f);
            }
        }
    }

    @Override
    public boolean isInitialised() {
        return true;
    }
}