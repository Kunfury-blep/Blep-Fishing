package Objects;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;

public class MarketObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8938941631189446175L;
	private double locX;
	private double locY;
	private double locZ;
	private String worldStr;
	
	
	public MarketObject(Sign sign, World world) {

		locX = sign.getLocation().getX();
		locY = sign.getLocation().getY();
		locZ = sign.getLocation().getZ();
		worldStr = world.getName();
	}
	
	public Sign GetSign(){
		Sign sign;
		
		try {
			Location loc = new Location(Bukkit.getServer().getWorld(worldStr), locX, locY, locZ);
			sign = (Sign)loc.getBlock().getState();	
			sign.getLines().toString(); //This is just for checking if it's a sign
			return sign;
		}catch(Exception e){
			return null;
		}

	}
	
	public boolean CheckBool(Sign sign) {
		Location signLoc = sign.getLocation();
		Location markLoc = new Location(Bukkit.getServer().getWorld(worldStr), locX, locY, locZ);
		
		if(signLoc.equals(markLoc))
			return true;
		
		return false;
	}
}
