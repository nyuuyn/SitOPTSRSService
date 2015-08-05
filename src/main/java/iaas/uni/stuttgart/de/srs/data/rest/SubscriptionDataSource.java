package iaas.uni.stuttgart.de.srs.data.rest;

import iaas.uni.stuttgart.de.srs.config.Configuration;
import iaas.uni.stuttgart.de.srs.model.Situation;
import iaas.uni.stuttgart.de.srs.model.SituationChange;
import iaas.uni.stuttgart.de.srs.model.SituationTemplate;
import iaas.uni.stuttgart.de.srs.model.Subscription;
import net.sf.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientResponse;
import org.springframework.http.MediaType;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class SubscriptionDataSource {

	public List<Subscription> getSubscriptions() {
		List<Subscription> subs = new ArrayList<Subscription>();

		SituationTemplateDataSource sitTempDataSource = new SituationTemplateDataSource();
		List<SituationTemplate> sitTemplates = sitTempDataSource.getSituationTemplates();

		SituationDataSource sitDataSource = new SituationDataSource();
		List<Situation> sits = sitDataSource.getSituations();

		SituationChangeDataSource sitChangeDataSource = new SituationChangeDataSource();
		List<SituationChange> sitChanges = sitChangeDataSource.getSituationChanges();

		for (Situation sit : sits) {
			String sitId = sit.getId();
			String thingId = sit.getThing();
			String sitTempId = sit.getSituationTemplate();
			String callbackUrl = null;
			for (SituationChange sitChange : sitChanges) {
				if (sitChange.getId().equals(sitId)) {
					callbackUrl = sitChange.getCallbackUrl();
				}
				// TODO find a way to handle correlation and addressingId inside
				// this service, so there is no need to change SitOPT
				Subscription sub = new Subscription(sitTempId, thingId, null, callbackUrl, null);

				subs.add(sub);
			}

		}

		return subs;
	}

	public void addSubscription(Subscription sub) {

		// for matching/storing correlation and addressing id's we construct a
		// callbackURL that SitOPT calls in the following form:
		// http://host:port/srsService/rest/callback/{correlationId}/{addressingId}/{sitMeProcessCallbackAddressEncoded}
		String sitOPtCallbackURL = new Configuration().getSitOPTSRSServiceAddress() + "/rest/callback/"
				+ sub.getCorrelation() + "/" + sub.getAddrMsgId() + "/" + URLEncoder.encode(sub.getEndpoint());
		
		System.out.println("Constructed CallbackUrl for Subscription:");
		System.out.println(sitOPtCallbackURL);

		JSONObject subJson = new JSONObject();

		subJson.put("SitTempID", sub.getSituationTemplateId());
		subJson.put("ThingID", sub.getThingId());
		subJson.put("CallbackURL", sitOPtCallbackURL);
		subJson.put("once", "false");

		// TODO implement sending subscription to SitOPT system

		Client client = ClientBuilder.newClient();

		WebTarget sitOptSitChangesResource = client.target(new Configuration().getSitOPTAddress()).path("situations")
				.path("changes");

		ClientResponse response = sitOptSitChangesResource.request(MediaType.APPLICATION_JSON.toString()).post(Entity.json(subJson.toString()), ClientResponse.class);
		
//		ClientResponse response = sitOptSitChangesResource.type("application/json").post(ClientResponse.class, subJson.toString());
		
//		String response = sitOptSitChangesResource.request(MediaType.APPLICATION_JSON.toString())
//				.post(Entity.entity(subJson.toString(), javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);

		System.out.println("Received following response from SitOPT:");
		System.out.println(response);
	}

}
