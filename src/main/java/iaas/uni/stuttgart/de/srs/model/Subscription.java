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
	private String addressingMsgId;

	public Subscription(String situationId, String objectId,
			String correlation, String endpoint, String addressingMsgId) {
		this.situationId = situationId;
		this.objectId = objectId;
		this.correlation = correlation;
		this.endpoint = endpoint;
		this.addressingMsgId = addressingMsgId;
	}
	
	public String getAddrMsgId(){
		return this.addressingMsgId;
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

	@Override
	public String toString() {
		return "Subscription: \n SituationId: " + this.situationId
				+ "\n ObjectId: " + this.objectId + "\n Correlation: "
				+ this.correlation + "\n Endpoint: " + endpoint + "\n addressingMsgId: " + this.addressingMsgId;
	}

}
