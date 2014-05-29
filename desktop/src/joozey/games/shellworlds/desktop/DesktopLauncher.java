package joozey.games.shellworlds.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import joozey.games.shellworlds.core.ShellWorlds;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.samples = 2;
		new LwjglApplication(new ShellWorlds(), config);
	}
}
