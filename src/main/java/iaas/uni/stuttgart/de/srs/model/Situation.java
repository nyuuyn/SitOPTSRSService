package iaas.uni.stuttgart.de.srs.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class Situation {
	
	private String id;

	public List<String> observedProperties = new ArrayList<String>();
	
	public Situation(String id){
		this.id = id;
	}
	
	public List<String> getObservedProperties() {
		return observedProperties;
	}

	public void setObservedProperties(List<String> observedProperties) {
		this.observedProperties = observedProperties;
	}

	public String getId() {
		return id;
	}
	
	public boolean isTriggered(ObservedObject obsObj){
		
		// makes a simple conjunction
		boolean result = true;
		
		for(String prop : this.observedProperties){
			if(obsObj.getProperties().containsKey(prop)){
				result &= obsObj.getProperties().get(prop);
			}
		}
		
		return result;
	}
}
