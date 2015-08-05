/**
 * 
 */
package iaas.uni.stuttgart.de.srs.data.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.springframework.http.MediaType;

import iaas.uni.stuttgart.de.srs.config.Configuration;
import iaas.uni.stuttgart.de.srs.model.SituationChange;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * @author kepeskn
 *
 */
public class SituationChangeDataSource {

	public List<SituationChange> getSituationChanges() {
		List<SituationChange> sitChanges = new ArrayList<SituationChange>();

		Client client = ClientBuilder.newClient();

		WebTarget thingsResource = client.target(new Configuration().getSitOPTAddress()).path("situations")
				.path("changes");

		String response = thingsResource.request(MediaType.APPLICATION_JSON.toString()).get(String.class);

		JSONArray jsonOutput = (JSONArray) JSONSerializer.toJSON(response);

		for (int i = 0; i < jsonOutput.size(); i++) {
			JSONObject sitChangeJson = jsonOutput.getJSONObject(i);

			SituationChange sitChange = new SituationChange(sitChangeJson.getString("id"),
					sitChangeJson.getString("callbackURL"));
			
			sitChanges.add(sitChange);

		}

		return sitChanges;
	}

}
