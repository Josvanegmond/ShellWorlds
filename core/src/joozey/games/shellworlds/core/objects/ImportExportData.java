package joozey.games.shellworlds.core.objects;

/**
 * Created by acer on 2-6-2014.
 */
public class ImportExportData
{
	private float population = 10;
    private float lightElements = 10;
    private float heavyElements = 10;
    private float services = 10;
    private float supplies = 10;

    public ImportExportData() {}

    public ImportExportData( float pop, float le, float he, float se, float re )
    {
    	this.population = pop;
    	this.lightElements = le;
    	this.heavyElements = he;
    	this.services = se;
    	this.supplies = re;
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
        this.population = population;
    }

    public void addPopulation( float population) {
        this.population += population;
    }

    public float getLightElements() {
        return lightElements;
    }

    public void setLightElements(float lightElements) {
        this.lightElements = lightElements;
    }

    public void addLightElements(float lightElements) {
        this.lightElements += lightElements;
    }

    public float getHeavyElements() {
        return heavyElements;
    }

    public void setHeavyElements(float heavyElements) {
        this.heavyElements = heavyElements;
    }

    public void addHeavyElements(float heavyElements) {
        this.heavyElements += heavyElements;
    }

    public float getServices() {
        return services;
    }

    public void setServices(float services) {
        this.services = services;
    }

    public void addServices(float services) {
        this.services += services;
    }

    public float getSupplies() {
        return supplies;
    }

    public void setSupplies(float supplies) {
        this.supplies = supplies;
    }

    public void addSupplies(float supplies) {
        this.supplies += supplies;
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
