package joozey.games.shellworlds.core;

import joozey.games.shellworlds.core.control.BodyControl;
import joozey.games.shellworlds.core.control.LogicThread;
import joozey.games.shellworlds.core.control.RenderThread;
import joozey.libs.powerup.control.ThreadManager;

public class ShellWorlds
{
    private RenderThread renderThread;
    private LogicThread logicThread;
    private ShellWorldData shellWorldData;

    public ShellWorlds()
    {
        ShellWorldData shellWorldData = ShellWorldData.initInstance( 0, 0 );

        renderThread = new RenderThread( shellWorldData );
        logicThread = new LogicThread( shellWorldData );

        ThreadManager.init( renderThread, logicThread );
    }

    public RenderThread getRenderThread()
    {
        return this.renderThread;
    }

    public LogicThread getLogicThread()
    {
        return this.logicThread;
    }
}
