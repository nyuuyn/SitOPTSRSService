package iaas.uni.stuttgart.de.srs.data.rest;

import iaas.uni.stuttgart.de.srs.config.Configuration;
import iaas.uni.stuttgart.de.srs.model.Subscription;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.springframework.http.MediaType;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class SubscriptionsSingleton {

	public List<Subscription> subscriptions = new ArrayList<Subscription>();

	public SubscriptionsSingleton() {
	}

	public List<Subscription> getSubscriptions() {
		Client client = ClientBuilder.newClient();

		WebTarget thingsResource = client.target(new Configuration().getSitOPTAddress()).path("situations")
				.path("changes");

		String response = thingsResource.request(MediaType.APPLICATION_JSON.toString()).get(String.class);

		JSONObject jsonOutput = (JSONObject) JSONSerializer.toJSON(response);

		return this.subscriptions;
	}
	
	public void addSubscription(Subscription sub){
		// TODO implement sending subscription to SitOPT system
	}

}
