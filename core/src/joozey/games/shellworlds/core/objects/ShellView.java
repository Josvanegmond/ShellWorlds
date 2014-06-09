package joozey.games.shellworlds.core.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;

import java.awt.Font;

import joozey.games.shellworlds.core.ShellWorldData;
import joozey.libs.powerup.game.GameData;
import joozey.libs.powerup.graphics.DefaultSprite;
import joozey.libs.powerup.object.BatchManager;

/**
 * Created by acer on 29-5-2014.
 */
public class ShellView extends DefaultSprite
{
    private ShellObject shellObject;
    public ShellView(ShellObject shellObject, String bitmapName) {
        super(bitmapName); this.shellObject = shellObject;
    }

    @Override
    public void drawBatch()
    {
        super.drawBatch();
    }

    public void drawInfo( BitmapFont font )
    {
        BodyData data = (BodyData) shellObject.getData();

        //TODO draw shellworld data
        Vector3 viewPosition = new Vector3( 60, 60, 0 );//camera.project( new Vector3( bodyData.getPosition().x + 250f, bodyData.getPosition().y, 0 ));

        int dxPop = shellObject.getPopulation() - shellObject.getPrevPopulation();
        float dxHap = shellObject.getHappiness() - shellObject.getPrevHappiness();
        int dxWth = shellObject.getWealthLevel() - shellObject.getPrevWealthLevel();
        float dxPrt = shellObject.getProductionRate() - shellObject.getPrevProductionRate();

        BitmapFont normalFont = ShellWorldData.getInstance().getNormalFont();
        normalFont.setColor( Color.WHITE );
        normalFont.draw(BatchManager.getSpriteBatch(), "Population: " + shellObject.getPopulation() + " (" + dxPop + ")", viewPosition.x, viewPosition.y);
        normalFont.draw(BatchManager.getSpriteBatch(), "Happiness:  " + (int)(shellObject.getHappiness() * 100f) + "% (" + dxHap + ")", viewPosition.x, viewPosition.y + 20);
        normalFont.draw(BatchManager.getSpriteBatch(), "Wealth lvl: " + shellObject.getWealthLevel() + " (" + dxWth + ")", viewPosition.x, viewPosition.y + 40 );

        ImportExportData producedData = shellObject.getProducedData();
        normalFont.draw(BatchManager.getSpriteBatch(), "Light elements:  " + producedData.getLightElements(), viewPosition.x, viewPosition.y + 80);
        normalFont.draw(BatchManager.getSpriteBatch(), "Heavy elements: " + producedData.getHeavyElements(), viewPosition.x, viewPosition.y + 100 );
        normalFont.draw(BatchManager.getSpriteBatch(), "Services:  " + producedData.getServices(), viewPosition.x, viewPosition.y + 120);
        normalFont.draw(BatchManager.getSpriteBatch(), "Recreation: " + producedData.getSupplies(), viewPosition.x, viewPosition.y + 140 );
        
        normalFont.draw(BatchManager.getSpriteBatch(), "Production rate: " + (int)(shellObject.getProductionRate()*100) + " (" + dxPrt + ")", viewPosition.x, viewPosition.y + 180 );
        normalFont.draw(BatchManager.getSpriteBatch(), "Type: " + shellObject.getType().toString(), viewPosition.x, viewPosition.y + 200 );
    }

    @Override
    public void drawShape()
    {
        super.drawShape();
    }
}
