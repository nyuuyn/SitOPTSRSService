package iaas.uni.stuttgart.de.srs.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class SituationTemplate {
	
	private String id;
	
	private String name;

	private List<String> observedSituations = new ArrayList<String>();
	
	public SituationTemplate(String id){
		this.id = id;
	}
	
	public SituationTemplate(String id, String name){
		this.id = id;
		this.name = name;
	}
	
	public List<String> getObservedSituations() {
		return this.observedSituations;
	}

	public void setObservedSituations(List<String> observedSituations) {
		this.observedSituations = observedSituations;
	}

	public String getId() {
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean isTriggered(Thing obsObj){
		
		// makes a simple conjunction
		boolean result = true;
		
		for(String prop : this.observedSituations){
			if(obsObj.getProperties().containsKey(prop)){
				result &= obsObj.getProperties().get(prop);
			}
		}
		
		return result;
	}
}
