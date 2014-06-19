package joozey.games.shellworlds.core.objects;

import joozey.games.shellworlds.core.ShellWorldData;
import joozey.libs.powerup.game.GameData;
import joozey.libs.powerup.object.GameObject2D;

/**
 * Created by acer on 29-5-2014.
 */
public class ShellObject extends GameObject2D
{
	public static final float speed = 10f;
	
	public static final int NORMAL = 0;
	public static final int RESIDENTIAL = 1;
	public static final int COMMERCIAL = 2;
	public static final int INDUSTRIAL = 3;
	public static final int SHELLTYPES = 4;
	
	public static final int POPULATION = 0;
	public static final int LIGHTELEMENT = 1;
	public static final int HEAVYELEMENT = 2;
	public static final int SERVICE = 3;
	public static final int SUPPLY = 4;
	public static final int RESOURCES = 5;
	
	public static final int MASS = 0;
	public static final int DENSITY = 1;
	public static final int DIVERSITY = 2;
	public static final int ATMOSPHERE = 3;
	public static final int PLANETPROPERTIES = 4;

    private int shellType;
    private int operationTime = 0;

    private ImportExportData importExportData;

    private BodyData bodyData;
    private ShellView shellView;

    public ShellObject(BodyData bodyData, int type)
    {
        this.shellType = type;
        this.bodyData = bodyData;
        this.importExportData = new ImportExportData( 1000, 10000, 10, 100, 10000 );

        this.shellView = new ShellView( this, "shell.png" );

        super.init( bodyData, this.shellView );
    }

    @Override
    public void run()
    {
        //produce etc.
        produce();

        for( BodyObject body : this.bodyData.getReachableBodies() )
        {
            ShellObject reachableShell = body.getShell();
            if( reachableShell != null )
            {
                //TODO exchange resources and services

            }
        }

        operationTime++;
        operationTime %= (speed*10);
    }
    
    public ImportExportData getImportExportData()
    {
    	return this.importExportData;
    }

    private float getEstimateProduction( int propType )
    {
		ImportExportData productionData = getProductionDataForShell( this.shellType );
		ImportExportData demandData = getDemandDataForShell( this.shellType );
		
   		float production = (productionData.get(propType) - demandData.get(propType)) * 
						 	this.importExportData.get( POPULATION );
   		
		production += getBoost( propType ) * this.importExportData.get( POPULATION );

		float invSpeed = (float) (speed*GameData.getSpeed()/1000f);
		production *= invSpeed;//speed*GameData.getSpeed()/1000f;
   		
		production += this.importExportData.get( propType );
   		
   		return production;
    }

    //producing goods
    private void produce()
    {
        if( operationTime % speed == 0 )
        {
        	float newPop = getEstimateProduction( POPULATION );
        	float newLE = getEstimateProduction( LIGHTELEMENT );
        	float newHE = getEstimateProduction( HEAVYELEMENT );
        	float newSe = getEstimateProduction( SERVICE );
        	float newSu = getEstimateProduction( SUPPLY );
        	
        	this.importExportData.setPopulation( newPop );
        	this.importExportData.setLightElements( newLE );
        	this.importExportData.setHeavyElements( newHE );
        	this.importExportData.setServices( newSe );
        	this.importExportData.setSupplies( newSu );

        	this.shellView.setGraphValue( "pop", newPop );
        	this.shellView.setGraphValue( "le", newLE );
        	this.shellView.setGraphValue( "he", newHE );
        	this.shellView.setGraphValue( "se", newSe );
        	this.shellView.setGraphValue( "su", newSu );
        }
    }

    public int getType() {
        return shellType;
    }
    
    public ShellView getView()
    {
        return this.shellView;
    }



	private final static ImportExportData[] allProductionData = {
		new ImportExportData( 0, 0, 0, 0, 0 ),
		new ImportExportData( .3f, 0, 0, 0, .6f ),
    	new ImportExportData( 0, .2f, 0, .5f, 0 ),
    	new ImportExportData( 0, .3f, .8f, 0, 0 )
	};
	
	private final static ImportExportData[] allDemandData = {
		new ImportExportData( 0, .1f, 0, .1f, 0 ),
		new ImportExportData( 0, .5f, 0, .3f, 0f ),
        new ImportExportData( .1f, .1f, .1f, .2f, .3f ),
        new ImportExportData( .2f, .2f, 0, .3f, .4f )
	};

	private final static ImportExportData getProductionDataForShell( int shellType )
    {
    	return allProductionData[shellType];
    }

	private final static ImportExportData getDemandDataForShell( int shellType )
    {
    	return allDemandData[shellType];
    }
    
	//ugly way for storing variables
	//[0] = shell property to receive boost or penalty
	//[0][0] = whether to take the boost or the penalty
	//[0][0][0] = if apparent or lacking, this is the value to boost or penalty with
    private final static float propBoosts[][][] ={{ { .1f, .2f, 0f, 0f, .2f },	{ -0f, -.5f, -0f, -.4f, -.8f } },
    									{ { 0f, 0f, 0f, .2f, 0f },		{ -0f, -0f, -0f, -.5f, -0f } },
    									{ { .2f, .4f, 0f, .4f, 0f },	{ -0f, -.6f, -0f, -.4f, -0f } },
    									{ { .4f, .1f, 0f, 0f, .2f },	{ -.5f, -.3f, -0f, -0f, -.4f } },
    									{ { 0f, .2f, 0f, 0f, 0f },		{ -.3f, -.5f, -0f, -0f, -0f } }};

	//[0] = shell property which gets a boost from the planet properties
	//[0][0] = planet property values for optimal shell property production
    private final static float planetBoosts[][] = {{0.5f, 1f, 1f, 0.5f},
    										{1f, 0f, 0.5f, 1f},
    										{1f, 1f, 0f, 0f},
    										{0.3f, 0.8f, 1f, 0.5f},
    										{0.5f, 1f, 0.5f, 0.5f}};
    
    private static float getPropBoost( int propType, boolean boost, int availablePropType )
    {
    	return propBoosts[propType][(boost)?0:1][availablePropType];
    }
    
    private static float getPlanetBoost( int propType, int planetProp )
    {
    	return planetBoosts[propType][planetProp];
    }
    
    private float getPlanetBoost( int propType )
    {
    	float[] planetStats = { this.bodyData.getMass(), this.bodyData.getDensity(), this.bodyData.getDiversity(), this.bodyData.getAtmosphere() };
    	float planetBoost = 0;
    	
    	for( int planetProp = 0; planetProp < PLANETPROPERTIES; planetProp++ )
    	{
    		planetBoost += 1f - ( Math.abs(planetStats[planetProp] - getPlanetBoost(propType, planetProp)) * 4);
    	}
    	
    	return planetBoost / PLANETPROPERTIES;
    }
    
    private float getBoost( int propType )
    {
		float boost = 0;
		ImportExportData productionData = getProductionDataForShell( this.shellType );
		
   		for( int availablePropType = 0; availablePropType < RESOURCES; availablePropType++ )
   		{
   	   		float currentProduction = this.importExportData.get(availablePropType);
   			if( productionData.get( propType ) > 0)
   			{	
   				boost += getPropBoost( propType, currentProduction>0, availablePropType );
   			}
	    }

   		boost += getPlanetBoost( propType ) - (ShellWorldData.getInstance().getDifficulty()/10f);
   		boost *= (productionData.get( propType ) > 0) ? 1 : 0;

    	//float invSpeed = speed/100f;
    	//boost *= invSpeed;
    	
    	return boost;
    }
}
