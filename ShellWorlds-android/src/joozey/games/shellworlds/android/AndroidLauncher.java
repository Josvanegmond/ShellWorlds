package joozey.games.shellworlds.android;

import joozey.games.shellworlds.core.ShellWorlds;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        //config.numSamples = 1;
        ShellWorlds shellWorlds = new ShellWorlds();
        initialize( shellWorlds.getRenderThread(), config);
	}
}
