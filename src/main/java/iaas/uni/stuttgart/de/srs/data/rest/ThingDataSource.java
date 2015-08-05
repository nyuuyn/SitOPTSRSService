package iaas.uni.stuttgart.de.srs.data.rest;

import iaas.uni.stuttgart.de.srs.config.Configuration;
import iaas.uni.stuttgart.de.srs.model.Thing;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.springframework.http.MediaType;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class ThingDataSource {
	
	public List<Thing> getThings(){
		Client client = ClientBuilder.newClient();
		List<Thing> objs = new ArrayList<Thing>();
		
		WebTarget thingsResource = client.target(new Configuration().getSitOPTAddress()).path("things");
		
		String response = thingsResource.request(MediaType.APPLICATION_JSON.toString()).get(String.class);
		
		JSONArray jsonOutput = (JSONArray) JSONSerializer.toJSON(response);
		
		for(int i = 0; i < jsonOutput.size(); i++){
			JSONObject thingJson = jsonOutput.getJSONObject(i);
			
			Thing obj = new Thing(thingJson.getString("_id"), thingJson.getString("name"));
			objs.add(obj);
			
		}
		
		System.out.println("Fetched Objects from SitOPT Service");
		System.out.println(jsonOutput);
		
		return objs;
	}
}
