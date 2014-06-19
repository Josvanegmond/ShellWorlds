package joozey.games.shellworlds.core.control;

import java.util.ArrayList;

import joozey.games.shellworlds.core.ShellWorldData;
import joozey.games.shellworlds.core.objects.BodyData;
import joozey.games.shellworlds.core.objects.BodyObject;
import joozey.libs.powerup.control.GameThread;
import joozey.libs.powerup.control.ThreadManager;
import joozey.libs.powerup.game.GameData;
import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.graphics.MatrixSet;
import joozey.libs.powerup.graphics.RenderMethod;
import joozey.libs.powerup.graphics.RenderMethod.DrawType;
import joozey.libs.powerup.object.Renderer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by acer on 31-5-2014.
 */
public class RenderThread extends GameThread implements ApplicationListener, InputProcessor, GestureDetector.GestureListener
{
    private OrthographicCamera camera;
    private int pointers = 0;
    private Vector2 prevScreen;
    private boolean lockInput = false;
    private DefaultSprite marker;

    private ShellWorldData shellWorldData;
    
	private double scrollAmount = 0f;
	private double scrollDecay = 1f;

    public RenderThread( ShellWorldData shellWorldData )
    {
        this.shellWorldData = shellWorldData;
    }


    @Override
    public void create()
    {
        this.prevScreen = new Vector2();
        camera = new OrthographicCamera( GameData.getWidth(), GameData.getHeight() );
        camera.zoom = 100f;
        GameData.setCamera( camera );
        
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor( inputMultiplexer );

        inputMultiplexer.addProcessor( new GestureDetector( this ) );
        inputMultiplexer.addProcessor( this );



        BodyObject star = new BodyObject( BodyData.BodyType.STAR, new DefaultSprite( "star_medium.png" ), 0.001f, 10f + (float)(Math.random() * 10f), 0 );
        this.shellWorldData.addBody(star);
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

        
        //create the background in parallax scrolling
        DefaultSprite background = new DefaultSprite( "background4.png" );
        background.setPosition( Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f );
        Renderer.add( background, 20, new RenderMethod( DrawType.SPRITE, MatrixSet.MatrixType.PARALLAXMATRIX ) );
        Renderer.add( background, 20, new RenderMethod( DrawType.SPRITE, MatrixSet.MatrixType.PARALLAXMATRIX ) );
        
        
        //create the marker in screen matrix
        marker = new DefaultSprite("marker.png" );
        marker.setAlpha( 1f - 1f/camera.zoom );
        Renderer.add( marker, 1, new RenderMethod( DrawType.SPRITE, MatrixSet.MatrixType.SCREENMATRIX ) );
        
        //create the infodisplay in screen matrix as well
        DefaultSprite infoDisplay = new DefaultSprite( new NinePatch( new Texture( "data/dialog-frame.png" ), 7, 7, 7, 7 ) );
        Renderer.add( infoDisplay, 1, new RenderMethod( DrawType.SPRITE, MatrixSet.MatrixType.SCREENMATRIX ) );
    }


    @Override
    public void dispose()
    {
    	Renderer.dispose();
    }


    private void calculateBodySelection()
    {
        BodyObject touchedBodyOrbit = null;
        BodyObject touchedBody = null;

    	if( lockInput == false )
    	{
	        Vector3 screenCenter = new Vector3( Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0 );
	        touchedBodyOrbit = shellWorldData.getBodyOnDistance(camera.unproject(screenCenter), 2000);
	
	        if( touchedBodyOrbit != null ) {
	            if (shellWorldData.isBodyOnPosition(touchedBodyOrbit, screenCenter, 5000) == true) {
	                touchedBody = touchedBodyOrbit;
	            }
	        }
	    }
    	
	    shellWorldData.setTouchedBody( touchedBody );
	    shellWorldData.setTouchedBodyOrbit( touchedBodyOrbit );
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
                
                calculateBodySelection();
            }
        }
    }

    @Override
    public void run()
    {
        updateBeforeRendering();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.doScroll();
        camera.update();
        
        //draw marker
        marker.setAlpha( Math.min( camera.zoom/100f, 1f ) );
        marker.setPosition( Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f );
        
        Renderer.render();
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
    	if( i == Input.Keys.UP )
    	{
    		ArrayList<BodyObject> bodyObjectList = this.shellWorldData.getBodyObjectList();
    		
    		int index = (bodyObjectList.indexOf( this.shellWorldData.getFollowBodyObject() ) + 1) % bodyObjectList.size();
    		this.shellWorldData.setFollowBodyObject( bodyObjectList.get( index ) );
    	}
    	
    	if( i == Input.Keys.DOWN )
    	{
    		ArrayList<BodyObject> bodyObjectList = this.shellWorldData.getBodyObjectList();
    		
    		int index = (bodyObjectList.indexOf( this.shellWorldData.getFollowBodyObject() ) - 1);
    		while( index < 0 ) { index += bodyObjectList.size(); }
    		this.shellWorldData.setFollowBodyObject( bodyObjectList.get( index ) );
    	}
    	
    	if( i == Input.Keys.PAGE_UP )
    	{
	    	this.scrollAmount = Math.log( this.camera.zoom * this.camera.zoom ) / Math.log(5);
	    	this.scrollDecay = 1f;
    	}
    	
    	if( i == Input.Keys.PAGE_DOWN )
    	{
	    	this.scrollAmount = -Math.log( this.camera.zoom * this.camera.zoom ) / Math.log(10);
	    	this.scrollDecay = 1f;
    	}
    	
        return false;
    }

    @Override
    public boolean keyUp(int i) {

    	if( i == Input.Keys.PAGE_UP || i == Input.Keys.PAGE_DOWN )
    	{
    		this.scrollDecay = 0.6f;
    	}
    	
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
        if( lockInput == false )
        {
            followBodyObject = null;
            camera.translate( (prevScreen.x - screenX) * camera.zoom, (prevScreen.y - screenY) * -camera.zoom );
            //camera.rotateAround( new Vector3( followBody.getX(), followBody.getY(), 0 ), new Vector3(1,0,0), (prevScreen.x - screenX));
	
	        this.prevScreen.x = screenX;
	        this.prevScreen.y = screenY;
        }
	
	    shellWorldData.setFollowBodyObject( followBodyObject );
        
        calculateBodySelection();

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
    	this.scrollDecay = 0.6f;
    	this.scrollAmount = scrollAmount * this.camera.zoom/10f;
        return false;
    }

    public void doScroll( )
    {
        this.camera.zoom += scrollAmount;
        this.camera.zoom = Math.max( Math.min( this.camera.zoom, 500f), 0.3f );

    	this.scrollAmount *= this.scrollDecay;
    	
        shellWorldData.setShowDetailedInfo( ( this.camera.zoom == 2f ) ? true : false );
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
        float difference = (initialDistance - distance)/20f;
        difference = Math.min( Math.max( difference, -3f ), 3f );

        this.scrollAmount = difference;
        this.scrollDecay = 0.8f;
        return false;
    }

    @Override
    public boolean pinch(Vector2 vector2, Vector2 vector22, Vector2 vector23, Vector2 vector24) {
        return false;
    }

}
