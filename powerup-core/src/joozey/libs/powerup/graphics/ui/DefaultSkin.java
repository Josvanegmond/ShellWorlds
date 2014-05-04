package joozey.libs.powerup.graphics.ui;

import joozey.libs.powerup.game.GameData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class DefaultSkin
{
	private static Skin skin;
	private static BitmapFont font;

	public static Skin getDefaultSkin()
	{
		if( skin == null )
		{
			skin = new Skin();

			if( font == null )
			{
				//SmartFontGenerator fontGen = new SmartFontGenerator();
				FileHandle exoFile = Gdx.files.internal("data/exo-small.fnt");
				//fontGen.setReferenceScreenWidth( (int)GameData.getWidth() );
				//font = fontGen.createFont(exoFile, "exo-small", 20);

				float gameWidth = GameData.getWidth();
				float gameHeight = GameData.getHeight();
				
				float screenWidth = Gdx.graphics.getWidth();
				float screenHeight = Gdx.graphics.getHeight();

				float factor = 1f;
				if( gameWidth/screenWidth < gameHeight/screenHeight ) { factor = gameHeight / screenHeight; }
				else { factor = gameWidth / screenWidth; }
			
				font = new BitmapFont( exoFile );
				font.setScale(
					gameWidth / 
					(font.getBounds( " Computer vs Computer " ).width * factor)
				);
			}
			
			skin.add( "default", font );
			Pixmap pixmap = new Pixmap( 1, 1, Format.RGBA8888 );
			pixmap.setColor( Color.WHITE );
			pixmap.fill();
			skin.add( "white", new Texture( pixmap ) );

			skin.add( "default", getTextButtonStyle() );
			skin.add( "default", getLabelStyle() );
		}
		
		return skin;
	}
	
	private static TextButtonStyle getTextButtonStyle()
	{
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		//textButtonStyle.up = skin.newDrawable( "white", Color.DARK_GRAY );
		textButtonStyle.down = skin.newDrawable( "white", Color.LIGHT_GRAY );
		//textButtonStyle.checked = skin.newDrawable( "white",
		//		Color.DARK_GRAY );
		//textButtonStyle.over = skin.newDrawable( "white", Color.LIGHT_GRAY );
		textButtonStyle.font = skin.getFont( "default" );
		
		return textButtonStyle;
	}
	
	private static LabelStyle getLabelStyle()
	{
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont( "default" );
		
		return labelStyle;
	}
}
