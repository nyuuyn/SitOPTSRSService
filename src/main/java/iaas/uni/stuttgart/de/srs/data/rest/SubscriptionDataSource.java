package iaas.uni.stuttgart.de.srs.data.rest;

import iaas.uni.stuttgart.de.srs.config.Configuration;
import iaas.uni.stuttgart.de.srs.model.Situation;
import iaas.uni.stuttgart.de.srs.model.SituationChange;
import iaas.uni.stuttgart.de.srs.model.SituationTemplate;
import iaas.uni.stuttgart.de.srs.model.Subscription;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.apache.cxf.helpers.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
				Subscription sub = new Subscription(sitTempId, thingId, null, callbackUrl, null);

				subs.add(sub);
			}

		}

		return subs;
	}

	public void addSubscription(Subscription sub) {

		// This is the address SitOPT should use as callback
		
		// for matching/storing correlation and addressing id's we construct a
		// callbackURL that SitOPT calls in the following form:
		// http://host:port/srsService/rest/callback/{correlationId}/{addressingId}/{sitMeProcessCallbackAddressEncoded}
		String sitOPtCallbackURL = new Configuration().getSitOPTSRSServiceAddress() + "/rest/callback?CorrelationId="
				+ sub.getCorrelation() + "&AddressingId=" + sub.getAddrMsgId() + "&CallbackEndpoint=" + URLEncoder.encode(sub.getEndpoint());

		System.out.println("Constructed CallbackUrl for Subscription:");
		System.out.println(sitOPtCallbackURL);

		JSONObject subJson = new JSONObject();

		subJson.put("SitTempID", sub.getSituationTemplateId());
		subJson.put("ThingID", sub.getThingId());
		subJson.put("CallbackURL", sitOPtCallbackURL);
		subJson.put("once", "false");

		// the address of SitOPT
		String sitOptAddr = new Configuration().getSitOPTAddress() + "/situations/changes?SitTempID="
				+ sub.getSituationTemplateId() + "&ThingID=" + sub.getThingId() + "&CallbackURL="
				+ URLEncoder.encode(sitOPtCallbackURL) + "&once=false";

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost post = new HttpPost(sitOptAddr);
		post.addHeader("Accept", "application/json");
		post.addHeader("Content-Type", "application/json");

		try {
			CloseableHttpResponse response1 = httpclient.execute(post);
			String responseAsString = IOUtils.readStringFromStream(response1.getEntity().getContent());

			System.out.println("Sent POST to " + sitOptAddr);
			System.out.println("ResponseBody: " + responseAsString);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
