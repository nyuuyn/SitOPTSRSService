package iaas.uni.stuttgart.de.srs.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class ObservedObject {
	
	private String id;

	private Map<String,Boolean> properties = new HashMap<String,Boolean>();
	
	public ObservedObject(String id){
		this.id = id;
	}
	
	public String getId(){
		return this.id;
	}
	
	public Map<String,Boolean> getProperties(){
		return this.properties;
	}
}
