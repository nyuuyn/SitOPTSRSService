package iaas.uni.stuttgart.de.srs.data.rest;

import iaas.uni.stuttgart.de.srs.config.Configuration;
import iaas.uni.stuttgart.de.srs.model.Situation;
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
public class SituationDataSource {
	
	public List<Situation> situations = new ArrayList<Situation>();

	public SituationDataSource() {
		Situation testSituation1 = new Situation("sit.faults.MachineFaultX");
		Situation testSituation2 = new Situation("sit.faults.MachineRunning");
		testSituation1.observedProperties.add("testBoolean");
		testSituation2.observedProperties.add("testBoolean");
		
		this.situations.add(testSituation1);
		this.situations.add(testSituation2);
	}
	
	public List<Situation> getSituations(){
Client client = ClientBuilder.newClient();
		
		WebTarget thingsResource = client.target(new Configuration().getSitOPTAddress()).path("situations");
		
		String response = thingsResource.request(MediaType.APPLICATION_JSON.toString()).get(String.class);
		
		 JSONObject jsonOutput = (JSONObject) JSONSerializer.toJSON(response); 
		 
		return this.situations;
	}
}
