package iaas.uni.stuttgart.de.srs.data.rest;

import iaas.uni.stuttgart.de.srs.config.Configuration;
import iaas.uni.stuttgart.de.srs.model.SituationTemplate;
import net.sf.json.JSONArray;
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
public class SituationTemplateDataSource {

	public List<SituationTemplate> getSituationTemplates() {
		List<SituationTemplate> situationTemplates = new ArrayList<SituationTemplate>();
		Client client = ClientBuilder.newClient();

		WebTarget thingsResource = client.target(new Configuration().getSitOPTAddress()).path("situationtemplates");

		String response = thingsResource.request(MediaType.APPLICATION_JSON.toString()).get(String.class);

		JSONArray jsonOutput = (JSONArray) JSONSerializer.toJSON(response);

		for (int i = 0; i < jsonOutput.size(); i++) {
			JSONObject situationTemplate = jsonOutput.getJSONObject(i);
			SituationTemplate situation = new SituationTemplate(situationTemplate.getString("_id"), situationTemplate.getString("name"));
			
			situation.getObservedSituations().add(situationTemplate.getString("situation"));
			situationTemplates.add(situation);
		}

		System.out.println("Fetched SituationTemplates from SitOPT Service");
		System.out.println(jsonOutput);

		return situationTemplates;
	}
}
