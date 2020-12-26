package Objects;

public class RarityObject implements Comparable<RarityObject>{
	public String Name;
	public Integer Weight;
	public String Prefix;
	public double PriceMod = 1;
	
	
	public RarityObject(String name, int weight, String prefix, double priceMod) {
		Prefix = prefix;
		Name = "&" + prefix + "&l&o" + name;
		Weight = weight;
		PriceMod = priceMod;
	}
	
	
    @Override
    public int compareTo(RarityObject r) {
        return this.Weight.compareTo(r.Weight);
    }
	
}
