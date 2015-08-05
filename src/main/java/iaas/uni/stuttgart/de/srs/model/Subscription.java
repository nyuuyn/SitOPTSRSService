package iaas.uni.stuttgart.de.srs.model;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class Subscription {

	private String situationTemplateId;
	private String thingId;
	private String correlation;
	private String endpoint;
	private String addressingMsgId;

	public Subscription(String situationId, String objectId,
			String correlation, String endpoint, String addressingMsgId) {
		this.situationTemplateId = situationId;
		this.thingId = objectId;
		this.correlation = correlation;
		this.endpoint = endpoint;
		this.addressingMsgId = addressingMsgId;
	}
	
	public String getAddrMsgId(){
		return this.addressingMsgId;
	}

	public String getSituationTemplateId() {
		return situationTemplateId;
	}

	public String getThingId() {
		return thingId;
	}

	public String getCorrelation() {
		return correlation;
	}

	public String getEndpoint() {
		return endpoint;
	}

	@Override
	public String toString() {
		return "Subscription: \n SituationId: " + this.situationTemplateId
				+ "\n ObjectId: " + this.thingId + "\n Correlation: "
				+ this.correlation + "\n Endpoint: " + endpoint + "\n addressingMsgId: " + this.addressingMsgId;
	}

}
