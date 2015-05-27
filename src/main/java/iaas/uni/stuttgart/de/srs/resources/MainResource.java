package iaas.uni.stuttgart.de.srs.resources;

import java.net.MalformedURLException;
import java.net.URL;

import iaas.uni.stuttgart.de.srs.data.SubscriptionsSingleton;
import iaas.uni.stuttgart.de.srs.service.impl.SrsServiceSOAPImpl;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;

import de.uni_stuttgart.iaas.srsservice.NotifyRequest;
import de.uni_stuttgart.iaas.srsservice.SrsService;
import de.uni_stuttgart.iaas.srsservice.SrsServiceNotifciation;
import de.uni_stuttgart.iaas.srsservice.SrsServiceNotifciation_SrsCallbackServiceSOAP_Client;
import de.uni_stuttgart.iaas.srsservice.SrsService_Service;
import de.uni_stuttgart.iaas.srsservice.SubscribeRequest;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 */
@Path("/rest")
public class MainResource {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response root() {
		Viewable view = new Viewable("index",
				SubscriptionsSingleton.getInstance().subscriptions);
		return Response.ok(view).build();
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	public Response notify(@FormParam("Situation") String situation,
			@FormParam("Object") String object,
			@FormParam("Correlation") String correlation,
			@FormParam("Endpoint") String endpoint) {

		System.out.println("Called Notify (" + situation + "," + object + ","
				+ correlation + "," + endpoint + ")");
		
		URL serviceUrl = null;
		try {
			serviceUrl = new URL(endpoint + "?wsdl");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		SrsService_Service service = new SrsService_Service(serviceUrl);
		SrsServiceNotifciation notifyService = service.getSrsCallbackServiceSOAP();
		
		NotifyRequest notifyReq = new NotifyRequest();
		
		notifyReq.setSituation(situation);
		notifyReq.setObject(object);
		notifyReq.setCorrelation(correlation);
		
		notifyService.notify(notifyReq);
		
		Viewable view = new Viewable("index",
				SubscriptionsSingleton.getInstance().subscriptions);
		return Response.ok(view).build();
	}

}
