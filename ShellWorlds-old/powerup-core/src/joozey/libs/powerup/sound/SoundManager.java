package joozey.libs.powerup.sound;

import com.badlogic.gdx.audio.Sound;

public class SoundManager
{
	private static SoundManager _instance = null;
	
	public static SoundManager getInstance()
	{
		if( _instance == null )
		{
			_instance = new SoundManager();
		}
		
		return _instance;
	}
	
	
	private float masterVolume = 1f;
	
	private SoundManager()
	{
		this.masterVolume = 1f;
	}
	
	public float getMasterVolume()
	{
		return masterVolume;
	}
	
	public void setMasterVolume( float volume )
	{
		this.masterVolume = volume;
	}

	public long play( Sound sound, float volumeFactor, float pitch, float pan )
	{
		long id = sound.play();
		sound.setPitch( id, pitch );
		sound.setVolume( id, this.masterVolume * volumeFactor );
		
		return id;
	}
}
