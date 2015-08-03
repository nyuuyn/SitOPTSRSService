package iaas.uni.stuttgart.de.srs.data.rest;

import iaas.uni.stuttgart.de.srs.config.Configuration;
import iaas.uni.stuttgart.de.srs.model.ObservedObject;
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
public class ObservedObjectDataSource {
	
	public List<ObservedObject> objects = new ArrayList<ObservedObject>();

	public ObservedObjectDataSource(){
		ObservedObject testObj = new ObservedObject("X221");
		testObj.getProperties().put("testBoolean", false);
		objects.add(testObj);
	}
	
	public List<ObservedObject> getObjects(){
		Client client = ClientBuilder.newClient();
		
		WebTarget thingsResource = client.target(new Configuration().getSitOPTAddress()).path("things");
		
		String response = thingsResource.request(MediaType.APPLICATION_JSON.toString()).get(String.class);
		
		 JSONObject jsonOutput = (JSONObject) JSONSerializer.toJSON(response); 
		
		return this.objects;
	}
}
