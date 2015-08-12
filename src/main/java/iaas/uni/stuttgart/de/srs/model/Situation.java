package iaas.uni.stuttgart.de.srs.model;

public class Situation {
	private String id;
	private String thing;
	private String situationTemplate;
	private boolean occured;

	public Situation(String id, String thing, String situationTemplate, boolean occured){
		this.id = id;
		this.thing = thing;
		this.situationTemplate = situationTemplate;
	}
	
	public String getId() {
		return this.id;
	}

	public String getThing() {
		return this.thing;
	}

	public String getSituationTemplate() {
		return this.situationTemplate;
	}
	
	public boolean getOccured(){
		return this.occured;
	}
}