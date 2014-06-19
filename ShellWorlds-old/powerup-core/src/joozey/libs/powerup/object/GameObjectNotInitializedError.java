package joozey.libs.powerup.object;

public class GameObjectNotInitializedError extends Error
{
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage()
	{
		return "GameObject has uninitialized fields, check if init( GameObjectData, Sprite ) is called correctly.";
	}
}
