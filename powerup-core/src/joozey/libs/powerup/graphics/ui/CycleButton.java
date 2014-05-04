package joozey.libs.powerup.graphics.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CycleButton extends DefaultButton
{
	private int name = 0;
	private String[] names;
	
	public CycleButton( String[] names )
	{
		super( names[0] );
		this.names = names;
		this.addListener( getCycleListener() );
	}
	
	public ClickListener getCycleListener()
	{
		ClickListener clickListener = new ClickListener()
		{
			@Override
			public void clicked( InputEvent event, float x, float y )
			{
				name++;
				name %= names.length;
				
				CycleButton.this.setText( names[name] );
			}
		};
		
		return clickListener;
	}

	public int getSelection()
	{
		return name;
	}
}
