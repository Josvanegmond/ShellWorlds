package joozey.libs.powerup.util;

import com.badlogic.gdx.graphics.Color;

public class DefaultColors
{
	public static String getName( Color color )
	{
		String colorName = "default";

		if( color == Color.BLUE ) { colorName = "blue"; }
		if( color == Color.RED ) { colorName = "red"; }
		
		return colorName;
	}
}
