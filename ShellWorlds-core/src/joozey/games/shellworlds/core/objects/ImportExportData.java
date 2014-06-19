package joozey.games.shellworlds.core.objects;

/**
 * Created by acer on 2-6-2014.
 */
public class ImportExportData
{
	private float population = 100;
    private float lightElements = 50;
    private float heavyElements = 50;
    private float services = 50;
    private float supplies = 50;

    public ImportExportData() {}

    public ImportExportData( float pop, float le, float he, float se, float su )
    {
    	this.population = pop;
    	this.lightElements = le;
    	this.heavyElements = he;
    	this.services = se;
    	this.supplies = su;
    }
//
//    public ImportExportData( float pop, float le, float he, float se, float re )
//    {
//    	this( (int)pop, (int)le, (int)he, (int)se, (int)re );
//    }

    public float getPopulation() {
        return population;
    }

    public void setPopulation(float population) {
        this.population = Math.max( Math.min( population, 2000000000f ), 0f );
    }

    public float getLightElements() {
        return lightElements;
    }

    public void setLightElements(float lightElements) {
        this.lightElements = Math.max( Math.min( lightElements, 2000000000f ), -2000000000f );
    }

    public float getHeavyElements() {
        return heavyElements;
    }

    public void setHeavyElements(float heavyElements) {
        this.heavyElements = Math.max( Math.min( heavyElements, 2000000000f ), -2000000000f );
    }

    public float getServices() {
        return services;
    }

    public void setServices(float services) {
        this.services = Math.max( Math.min( services, 2000000000f ), -2000000000f );
    }

    public float getSupplies() {
        return supplies;
    }

    public void setSupplies(float supplies) {
        this.supplies = Math.max( Math.min( supplies, 2000000000f ), -2000000000f );
    }

    public int getProducableCount()
    {
        return 5; //pop, light, heavy elements, services and recreation
    }

    public float getTotalValue()
    {
        return this.population + this.lightElements + this.heavyElements + this.services + this.supplies;
    }

	public float get(int propType)
	{
		float value = 0;
		if( propType == ShellObject.POPULATION) { value = this.getPopulation(); }
		if( propType == ShellObject.LIGHTELEMENT) { value = this.getLightElements(); }
		if( propType == ShellObject.HEAVYELEMENT) { value = this.getHeavyElements(); }
		if( propType == ShellObject.SERVICE) { value = this.getServices(); } 
		if( propType == ShellObject.SUPPLY) { value = this.getSupplies(); }
		
		return value;
	}
}
