package iaas.uni.stuttgart.de.srs.data.rest;

import iaas.uni.stuttgart.de.srs.config.Configuration;
import iaas.uni.stuttgart.de.srs.model.Situation;
import iaas.uni.stuttgart.de.srs.model.SituationChange;
import iaas.uni.stuttgart.de.srs.model.SituationTemplate;
import iaas.uni.stuttgart.de.srs.model.Subscription;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
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
import org.apache.http.client.methods.HttpDelete;
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

		SituationDataSource sitDataSource = new SituationDataSource();
		List<Situation> sits = sitDataSource.getSituations();

		SituationChangeDataSource sitChangeDataSource = new SituationChangeDataSource();
		List<SituationChange> sitChanges = sitChangeDataSource.getSituationChanges();

		
		for(SituationChange sitChange : sitChanges){
			String sitOpt2srsCallbackEndpoint = sitChange.getCallbackUrl();
			
			String sitMeCallbackEndpoint = this.fetchEndpointFromSitOPTCallbackEndpoint(sitOpt2srsCallbackEndpoint);
			String correlationId = this.fetchCorrelationFromSitOPTCallbackEndpoint(sitOpt2srsCallbackEndpoint);
			String addressingId = this.fetchAddressingIdFromSitOPTCallbackEndpoint(sitOpt2srsCallbackEndpoint);
			String sitTemplateId = null;
			String thingId= null;
			
			for(Situation sit : sits){
				if(sitChange.getId().equals(sit.getId())){
					sitTemplateId = sit.getSituationTemplate();
					thingId = sit.getThing();
					break;
				}
			}
			
			subs.add(new Subscription(sitTemplateId, thingId, correlationId, sitMeCallbackEndpoint, addressingId));
			
		}

		return subs;
	}
	
	public void removeSubscription(Subscription sub){
		
		// the address of SitOPT
				String sitOptAddr = new Configuration().getSitOPTAddress() + "/situations/changes?SitTempID="
						+ sub.getSituationTemplateId() + "&ThingID=" + sub.getThingId() + "&CallbackURL="
						+ URLEncoder.encode(sub.getEndpoint());
		
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpDelete post = new HttpDelete(sitOptAddr);
		post.addHeader("Accept", "application/json");

		try {
			CloseableHttpResponse response1 = httpclient.execute(post);
			String responseAsString = IOUtils.readStringFromStream(response1.getEntity().getContent());

			System.out.println("Sent DELETE to " + sitOptAddr);
			System.out.println("ResponseBody: " + responseAsString);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	
	// TODO refactor these 3 methods into one method with a String parameter
	private String fetchAddressingIdFromSitOPTCallbackEndpoint(String sitOptCallbackEndpoint){
		// we gonna fetch the CallbackEndpoint part
		// http://192.168.209.224:8080/srsTestService/rest/callback?CorrelationId=someCorrelation123&AddressingId=null&CallbackEndpoint=http%3A%2F%2F192.168.209.224%3A9763%2Fservices%2FsrsServiceCallback
		System.out.println("Fetching SitME Workflow AddressingId from " + sitOptCallbackEndpoint);
		if(!this.hasQuery(sitOptCallbackEndpoint)){
			// this url doesn't seem to be using some query => not SRS callback endpoint
			return null;
		}
		
		String queryString = sitOptCallbackEndpoint.split("\\?")[1];
		
		String addressingIdStringDirty = queryString.split("AddressingId=")[1];

		String addressingIdString = addressingIdStringDirty.substring(0, addressingIdStringDirty.indexOf("&"));
		
		return addressingIdString;
		
	}
	
	private String fetchCorrelationFromSitOPTCallbackEndpoint(String sitOptCallbackEndpoint){
		// we gonna fetch the CallbackEndpoint part
		// http://192.168.209.224:8080/srsTestService/rest/callback?CorrelationId=someCorrelation123&AddressingId=null&CallbackEndpoint=http%3A%2F%2F192.168.209.224%3A9763%2Fservices%2FsrsServiceCallback
		if(!this.hasQuery(sitOptCallbackEndpoint)){
			// this url doesn't seem to be using some query => not SRS callback endpoint
			return null;
		}
		
		System.out.println("Fetching SitME Workflow CorrelationID from " + sitOptCallbackEndpoint);
		
		String queryString = sitOptCallbackEndpoint.split("\\?")[1];
		
		String correlationIdStringDirty = queryString.split("CorrelationId=")[1];
		
		String correlationIdString = correlationIdStringDirty.substring(0, correlationIdStringDirty.indexOf("&"));

		return correlationIdString;
		
	}
	
	public String fetchEndpointFromSitOPTCallbackEndpoint(String sitOptCallbackEndpoint){
		
		if(!this.hasQuery(sitOptCallbackEndpoint)){
			// this url doesn't seem to be using some query => not SRS callback endpoint
			return null;
		}
		
		// we gonna fetch the CallbackEndpoint part
		// http://192.168.209.224:8080/srsTestService/rest/callback?CorrelationId=someCorrelation123&AddressingId=null&CallbackEndpoint=http%3A%2F%2F192.168.209.224%3A9763%2Fservices%2FsrsServiceCallback
		System.out.println("Fetching SitME Workflow callbackEndpoint from " + sitOptCallbackEndpoint);
		
		String queryString = sitOptCallbackEndpoint.split("\\?")[1];
		
		String encodedCallbackEndpointString = queryString.split("CallbackEndpoint=")[1];

		String decodedCallbackEndpointString = URLDecoder.decode(encodedCallbackEndpointString);
	
		System.out.println("Found following Endpoint: " + decodedCallbackEndpointString);
		return decodedCallbackEndpointString;
		
	}
	
	private boolean hasQuery(String url){
		if(url.split("\\?").length == 2){
			return true;
		} else{
			return false;
		}
	}

}
