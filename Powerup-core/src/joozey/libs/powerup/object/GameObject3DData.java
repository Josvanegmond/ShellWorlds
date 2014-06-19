package joozey.libs.powerup.object;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class GameObject3DData
{
	protected Vector3 position;
	protected Vector3 bounds;
    protected float rotation;
	protected Vector3 rotationAxis;

    private ArrayList<GameObject3D> listeners;
	
	public GameObject3DData( Vector3 position, Vector3 bounds, float rotation, Vector3 rotationAxis )
	{
		this.position = new Vector3( position );
		this.bounds = new Vector3( bounds );
        this.rotation = rotation;
		this.rotationAxis = new Vector3( rotationAxis );

        initialise();
	}
	
	public GameObject3DData( Vector3 position )
	{
		this.position = new Vector3( position );
		this.bounds = new Vector3();
        this.rotation = 0;
		this.rotationAxis = new Vector3();

        initialise();
	}
	
	public GameObject3DData()
	{
		this.position = new Vector3();
		this.bounds = new Vector3();
        this.rotation = 0;
        this.rotationAxis = new Vector3();

        initialise();
	}

    private void initialise()
    {
        this.listeners = new ArrayList<GameObject3D>();
    }

    public void register( GameObject3D object )
    {
        this.listeners.add( object );
    }

    public void remove( GameObject3D object )
    {
        this.listeners.remove( object );
    }

    private void update()
    {
        for( GameObject3D object : this.listeners )
        {
            object.update();
        }
    }

	
	public Vector3 getPosition()
	{
		return this.position;
	}
	
	public void setPosition( Vector3 position )
	{
		this.position.set( position );
        this.update();
	}

    public float getRotation() { return this.rotation; }
    public Vector3 getRotationAxis() { return this.rotationAxis; }

    public void setRotation(Vector3 rotationAxis, float rotation )
    {
        this.rotation = rotation;
        this.rotationAxis.set( rotationAxis );
        this.update();
    }
}
