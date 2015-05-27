package iaas.uni.stuttgart.de.srs.model;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class Subscription {
	
	private String situationId;
	private String objectId;
	private String correlation;
	private String endpoint;

	public Subscription(String situationId, String objectId,
			String correlation, String endpoint) {
		this.situationId = situationId;
		this.objectId = objectId;
		this.correlation = correlation;
		this.endpoint = endpoint;
	}
	
	public String getSituationId() {
		return situationId;
	}

	public String getObjectId() {
		return objectId;
	}

	public String getCorrelation() {
		return correlation;
	}

	public String getEndpoint() {
		return endpoint;
	}


}
