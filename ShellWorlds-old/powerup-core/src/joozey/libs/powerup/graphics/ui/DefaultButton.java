package joozey.libs.powerup.graphics.ui;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class DefaultButton extends TextButton
{
	public DefaultButton( String text )
	{
		super( text, DefaultSkin.getDefaultSkin() );
	}
}
