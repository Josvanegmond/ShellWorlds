package joozey.games.shellworlds.core.control;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import joozey.games.shellworlds.core.objects.BodyData;
import joozey.games.shellworlds.core.objects.BodyObject;
import joozey.games.shellworlds.core.ShellWorldData;
import joozey.games.shellworlds.core.objects.ShellObject;
import joozey.libs.powerup.control.GameThread;
import joozey.libs.powerup.control.ThreadManager;
import joozey.libs.powerup.game.GameData;
import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.object.BatchManager;

/**
 * Created by acer on 31-5-2014.
 */
public class RenderThread extends GameThread implements ApplicationListener, InputProcessor, GestureDetector.GestureListener
{
    private OrthographicCamera camera;
    private int pointers = 0;
    private Batch batch;
    private ShapeRenderer shapeRenderer;
    private boolean showDetailedInfo;
    private Vector2 prevScreen;
    private boolean lockInput = false;

    private ShellWorldData shellWorldData;

    public RenderThread( ShellWorldData shellWorldData )
    {
        this.shellWorldData = shellWorldData;
    }


    @Override
    public void create()
    {
        this.prevScreen = new Vector2();
        camera = new OrthographicCamera( GameData.getWidth(), GameData.getHeight() );
        camera.zoom = 1f;
        batch = BatchManager.getSpriteBatch();

        shapeRenderer = BatchManager.getShapeRenderer();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor( inputMultiplexer );

        inputMultiplexer.addProcessor( new GestureDetector( this ) );
        inputMultiplexer.addProcessor( this );


        ThreadManager.register(new BodyControl(shellWorldData), ThreadManager.ThreadType.LOGIC);



        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 14; // fontsize 16 pixels
        params.characters = ShellWorldData.FONT_CHARACTERS;
        params.genMipMaps = true;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ModernDestronic.ttf"));
        this.shellWorldData.setAlienFont( generator.generateFont( params ) );

        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/SFAtarianSystem.ttf"));
        params.size = 13; // fontsize 16 pixels
        this.shellWorldData.setNormalFont( generator.generateFont( params ) );

        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        DefaultSprite background = new DefaultSprite( "background4.png" );
        DefaultSprite marker = new DefaultSprite( "marker.png" );
        marker.setAlpha( 1f - 1f/camera.zoom );
        NinePatch infoDisplay = new NinePatch( new Texture( "data/dialog-frame.png" ), 7, 7, 7, 7 );

        this.shellWorldData.setBackground( background );
        this.shellWorldData.setMarker( marker );
        this.shellWorldData.setInfoDisplay( infoDisplay );
    }


    @Override
    public void dispose()
    {
        batch.dispose();
    }


    private void updateBeforeRendering()
    {
        BodyObject touchedBodyOrbit = shellWorldData.getTouchedBodyOrbit();
        BodyObject followBodyObject = shellWorldData.getFollowBodyObject();
        ArrayList<BodyObject> bodyObjectList = shellWorldData.getBodyObjectList();

        for (BodyObject bodyObject : bodyObjectList)
        {
            bodyObject.update();

            if (bodyObject == touchedBodyOrbit) {
                bodyObject.getData().setHighlighted(true);
            } else {
                bodyObject.getData().setHighlighted(false);
            }
        }

        if( camera != null ) {
            if (followBodyObject != null) {
                Vector2 position = followBodyObject.getData().getPosition();
                Vector3 position3d = new Vector3( position.x, position.y, 0 );

                camera.translate(
                        (position.x - camera.position.x)*.2f,
                        (position.y - camera.position.y)*.2f, 0f);

                if( camera.position.dst( position3d ) < 1500 )
                {
                    camera.position.set( position3d );
                }
            }
        }
    }

    @Override
    public void run()
    {
        updateBeforeRendering();


        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        ArrayList<BodyObject> bodyObjectList = shellWorldData.getBodyObjectList();
        DefaultSprite background = shellWorldData.getBackground();
        DefaultSprite marker = shellWorldData.getMarker();
        NinePatch infoDisplay = shellWorldData.getInfoDisplay();
        BitmapFont alienFont = shellWorldData.getAlienFont();
        BitmapFont normalFont = shellWorldData.getNormalFont();
        BodyObject touchedBody = shellWorldData.getTouchedBody();

        //drawing the backdrop
        batch.begin();

        float scale = 0.75f;
        float ratio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        Matrix4 normalProjection = new Matrix4().setToOrtho2D( background.getWidth()/2f * (1f-scale), background.getHeight()/2f * (1f-scale * ratio), background.getWidth() * scale, background.getHeight() * scale * ratio );
        normalProjection.translate(camera.position.cpy().scl(-0.002f));

        batch.setProjectionMatrix(normalProjection);

        background.draw(BatchManager.DrawType.BATCH);

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

            Vector3 viewPosition = camera.project( new Vector3( bodyData.getPosition().x - 200f, bodyData.getPosition().y + 200f, 0 ));
            alienFont.setColor(bodyData.getSelectedOrbitColor());
            alienFont.draw(batch, bodyData.getName(), viewPosition.x, viewPosition.y);
        }


        //draw planet info at cursor

        infoDisplay.draw( BatchManager.getSpriteBatch(), 10, GameData.getHeight() - 90, GameData.getWidth() / 2 - 20, 80 );

        if( touchedBody != null )
        {
            BodyData bodyData = touchedBody.getData();

            Vector3 viewPosition = new Vector3( 20, GameData.getHeight() - 20, 0 );//camera.project( new Vector3( bodyData.getPosition().x + 250f, bodyData.getPosition().y, 0 ));
            normalFont.setColor(bodyData.getSelectedOrbitColor());
            normalFont.draw(batch, bodyData.readMassInfo(), viewPosition.x, viewPosition.y);
            normalFont.draw(batch, bodyData.readDensityInfo(), viewPosition.x, viewPosition.y - 16f);
            normalFont.draw(batch, bodyData.readAtmosphereInfo(), viewPosition.x, viewPosition.y - 32f);
            normalFont.draw(batch, bodyData.readDiversityInfo(), viewPosition.x, viewPosition.y - 48f);

            if( showDetailedInfo == true )
            {
                normalFont.draw( batch, bodyData.readBuildingProject(), GameData.getWidth() * 5f/7f, GameData.getHeight() / 2 + 96f );
                normalFont.draw( batch, bodyData.readBuildingProgress(), GameData.getWidth() * 5f/7f, GameData.getHeight() / 2 + 80f );
            }

            ShellObject shell = touchedBody.getShell();
            if( shell != null )
            {
                shell.getView().drawInfo( normalFont );
            }

            batch.draw( touchedBody.getSprite(), 210, GameData.getHeight() - 84, 80, 80 );
        }

        //draw marker
        marker.setAlpha( Math.min( camera.zoom/100f, 1f ) );
        marker.setPosition( Gdx.graphics.getWidth()/2f - marker.getWidth()/2f, Gdx.graphics.getHeight()/2f - marker.getHeight()/2f );
        marker.draw( BatchManager.DrawType.BATCH );

        batch.end();
    }


    @Override
    public void render()
    {
        this.run();
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
    public boolean keyDown(int i)
    {
        if( i == Input.Keys.PAGE_UP ) {
            this.doScroll(-20f * this.camera.zoom / (3f * this.camera.zoom));
        }

        if( i == Input.Keys.PAGE_DOWN )
        {
            this.doScroll( 20f * this.camera.zoom / (3f * this.camera.zoom) );
        }

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
        BodyObject followBodyObject = shellWorldData.getFollowBodyObject();
        BodyObject touchedBodyOrbit = null;
        BodyObject touchedBody = null;

        if( lockInput == false )
        {
            followBodyObject = null;
            camera.translate( (prevScreen.x - screenX) * camera.zoom, (prevScreen.y - screenY) * -camera.zoom );
            //camera.rotateAround( new Vector3( followBody.getX(), followBody.getY(), 0 ), new Vector3(1,0,0), (prevScreen.x - screenX));

            Vector3 screenCenter = new Vector3( Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0 );
            touchedBodyOrbit = shellWorldData.getBodyOnDistance(camera.unproject(screenCenter), 2000);

            if( touchedBodyOrbit != null ) {
                if (shellWorldData.isBodyOnPosition(touchedBodyOrbit, screenCenter, 5000) == true) {
                    touchedBody = touchedBodyOrbit;
                }
            }

            this.prevScreen.x = screenX;
            this.prevScreen.y = screenY;
        }

        shellWorldData.setFollowBodyObject( followBodyObject );
        shellWorldData.setTouchedBody( touchedBody );
        shellWorldData.setTouchedBodyOrbit( touchedBodyOrbit );

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
        this.doScroll( 20f * scrollAmount * this.camera.zoom / (3f * this.camera.zoom) );
        return false;
    }

    public void doScroll( float amount )
    {
        this.camera.zoom += amount;
        this.camera.zoom = Math.max( this.camera.zoom, 2f );

        showDetailedInfo = ( this.camera.zoom == 2f ) ? true : false;
    }


    /* for gestures */
    @Override
    public boolean touchDown(float v, float v2, int i, int i2) {
        return false;
    }

    @Override
    public boolean tap(float screenX, float screenY, int i, int i2)
    {
        BodyObject touchedBodyOrbit = shellWorldData.getTouchedBodyOrbit();

        //TODO move to a button
        if( touchedBodyOrbit != null )
        {
            shellWorldData.setFollowBodyObject( touchedBodyOrbit );
            shellWorldData.setTouchedBody( touchedBodyOrbit );
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
        float difference = (initialDistance - distance)/30f;
        difference = Math.min( Math.max( difference, -1f ), 1f );

        doScroll( difference );
        return false;
    }

    @Override
    public boolean pinch(Vector2 vector2, Vector2 vector22, Vector2 vector23, Vector2 vector24) {
        return false;
    }

}
