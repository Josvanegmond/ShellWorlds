package joozey.libs.powerup.object;

import joozey.libs.powerup.control.GameThread;
import joozey.libs.powerup.graphics.DefaultModel;

public class GameObject3D extends GameObject
{
	private DefaultModel model;
	private GameObject3DData data;
	
	private boolean initialised = false;
	
	public GameObject3D( DefaultModel model, GameObject3DData data )
	{
		this.model = model;
		this.data = data;

        this.data.register( this );
        this.update();
	}
	
	@Override
	public void run( GameThread gameThread )
	{
		this.initialised = true;
	}

	@Override
	public boolean isInitialised()
	{
		return initialised;
	}

	@Override
	public void update()
	{
        this.model.transform.setToRotation( this.data.getRotationAxis(), this.data.getRotation() );
        this.model.transform.setTranslation( this.data.getPosition() );
	}
	
	public DefaultModel getModel()
	{
		return this.model;
	}


    public GameObject3DData getData()
    {
        return this.data;
    }
}
