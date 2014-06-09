package joozey.games.shellworlds.core.objects;

import joozey.libs.powerup.object.GameObject2D;
import joozey.libs.powerup.util.ExtMath;

import java.util.ArrayList;

/**
 * Created by acer on 29-5-2014.
 */
public class ShellObject extends GameObject2D
{
	public static final int RESIDENTIAL = 0;
	public static final int COMMERCIAL = 1;
	public static final int INDUSTRIAL = 2;
	
	public static final int POPULATION = 0;
	public static final int LIGHTELEMENT = 1;
	public static final int HEAVYELEMENT = 2;
	public static final int SERVICE = 3;
	public static final int SUPPLY = 4;
	
	public static final int MASS = 0;
	public static final int DENSITY = 1;
	public static final int DIVERSITY = 2;
	public static final int ATMOSPHERE = 3;

    private int shellType;
    private int operationTime = 0;

    private float population;
    private ImportExportData importExportData;

    private BodyData bodyData;
    private ShellView shellView;

    public ShellObject(BodyData bodyData, int type)
    {
        this.shellType = type;
        this.bodyData = bodyData;
        this.importExportData = new ImportExportData();
        this.population = 10f;

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
        operationTime %= 100000;
    }

    public float getEstimateProduction( int propType )
    {
		ImportExportData productionData = getProductionDataForShell( this.shellType );
		ImportExportData demandData = getDemandDataForShell( this.shellType );
		
   		float production = 0;
   		if( propType == POPULATION ) { production = productionData.get( POPULATION ) + (productionData.get(propType) - demandData.get(propType)) * productionData.get( POPULATION ); }
   		else
   		{
   			production = productionData.get( propType ) + (productionData.get(propType) - demandData.get(propType)) * this.importExportData.get( POPULATION ) / 10;
   		}
   		
   		return production;
    }

    //producing goods
    private void produce()
    {
        if( operationTime % 8 == 0 )
        {
        	this.importExportData.addPopulation( getEstimateProduction( POPULATION ) );
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
		new ImportExportData( 10, 0, 0, 0, 6 ),
    	new ImportExportData( 0, 2, 0, 5, 0 ),
    	new ImportExportData( 0, 3, 8, 0, 0 )
	};
	
	private final static ImportExportData[] allDemandData = {
		new ImportExportData( 0, 1, 0, 1, 0 ),
		new ImportExportData( 0, 5, 0, 3, 2 ),
        new ImportExportData( 10, 1, 1, 2, 3 ),
        new ImportExportData( 10, 2, 0, 3, 4 )
	};

    public final static ImportExportData getProductionDataForShell( int shellType )
    {
    	return allProductionData[shellType];
    }

    public final static ImportExportData getDemandDataForShell( int shellType )
    {
    	return allDemandData[shellType];
    }
    
    private final static float propBoosts[][][] ={{ { .4f, .2f, 0f, 0f, .2f },	{ -0f, -.5f, -0f, -.4f, -.8f } },
    									{ { 0f, 0f, 0f, .2f, 0f },		{ -0f, -0f, -0f, -.5f, -0f } },
    									{ { .2f, .4f, 0f, .4f, 0f },	{ -0f, -.6f, -0f, -.4f, -0f } },
    									{ { .4f, .1f, 0f, 0f, .2f },	{ -.5f, -.3f, -0f, -0f, -.4f } },
    									{ { 0f, .2f, 0f, 0f, 0f },		{ -.3f, -.5f, -0f, -0f, -0f } }};

    private final static float planetBoosts[][] = {{0.5f, 1f, 1f, 0.5f},
    										{1f, 0f, 0.5f, 1f},
    										{1f, 1f, 0f, 0f},
    										{0.3f, 0.8f, 1f, 0.5f},
    										{0.5f, 1f, 0.5f, 0.5f}};
    
    public static float getPropBoost( int propType, boolean boost, int availablePropType )
    {
    	return propBoosts[propType][(boost)?0:1][availablePropType];
    }
    
    public float getPlanetBoost( int propType )
    {
    	float[] planetStats = { this.bodyData.getMass(), this.bodyData.getDensity(), this.bodyData.getDiversity(), this.bodyData.getAtmosphere() };
    	float planetBoost = 0;
    	
    	for( int planetProp = 0; planetProp < 4; planetProp++ )
    	{
    		planetBoost += 1 - ( Math.abs(planetStats[planetProp] - planetBoosts[propType][planetProp]) * 2 );
    	}
    	
    	return planetBoost / 4;
    }
    
    public float getBoost( int propType )
    {
		float boost = 0;
		ImportExportData productionData = getProductionDataForShell( this.shellType );
		ImportExportData demandData = getDemandDataForShell( this.shellType );
		
   		float estimateProduction = getEstimateProduction( propType );
   		
   		for( int availablePropType = 0; availablePropType < 5; availablePropType++ )
   		{
   			boost += getPropBoost( propType, estimateProduction>0, availablePropType );
   		}

   		boost += getPlanetBoost( shellType );   		
   		boost *= (productionData.get( propType ) > 0) ? 1 : 0;
   		
    	return boost;
    }
}
