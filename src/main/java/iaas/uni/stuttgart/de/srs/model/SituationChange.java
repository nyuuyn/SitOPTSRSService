package iaas.uni.stuttgart.de.srs.model;

public class SituationChange {

	private String id;
	private String callbackUrl;
	
	public SituationChange(String id, String callbackUrl){
		this.id = id;
		this.callbackUrl = callbackUrl;
	}

	public String getId() {
		return this.id;
	}

	public String getCallbackUrl() {
		return this.callbackUrl;
	}
}
