package iaas.uni.stuttgart.de.srs.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class Thing {
	
	private String id;
	private String name;

	private Map<String,Boolean> properties = new HashMap<String,Boolean>();
	
	public Thing(String id){
		this.id = id;
	}
	
	public Thing(String id, String name){
		this.id = id;
		this.name = name;
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Map<String,Boolean> getProperties(){
		return this.properties;
	}
}
