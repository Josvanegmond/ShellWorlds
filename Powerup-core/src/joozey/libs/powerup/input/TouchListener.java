package joozey.libs.powerup.input;

import com.badlogic.gdx.math.Vector3;


public interface TouchListener
{
	public void touchDown( Vector3 screenVector, int pointer, int button );

	public void touchUp( Vector3 screenVector, int pointer, int button );

	public void touchDragged( Vector3 screenVector, int pointer );
}
