/**
 * 
 */
package iaas.uni.stuttgart.de.srs.data.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.springframework.http.MediaType;

import iaas.uni.stuttgart.de.srs.config.Configuration;
import iaas.uni.stuttgart.de.srs.model.Situation;
import iaas.uni.stuttgart.de.srs.service.impl.SrsServiceSOAPImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * @author kepeskn
 *
 */
public class SituationDataSource {
	private static final Logger LOG = Logger.getLogger(SituationDataSource.class
			.getName());

	public List<Situation> getSituations() {
		List<Situation> sits = new ArrayList<Situation>();

		Client client = ClientBuilder.newClient();

		WebTarget thingsResource = client.target(new Configuration().getSitOPTAddress()).path("situations");

		String response = thingsResource.request(MediaType.APPLICATION_JSON.toString()).get(String.class);

		JSONArray jsonOutput = (JSONArray) JSONSerializer.toJSON(response);

		for (int i = 0; i < jsonOutput.size(); i++) {
			JSONObject situationJson = jsonOutput.getJSONObject(i);

			Situation sit = new Situation(situationJson.getString("_id"), situationJson.getString("thing"),
					situationJson.getString("situationtemplate"),situationJson.getBoolean("occured"));
			
			sits.add(sit);

		}
		
		LOG.log(Level.FINEST,"Fetched Situations from SitOPT Service");
		LOG.log(Level.FINEST,jsonOutput.toString());

		return sits;
	}
}
