package iaas.uni.stuttgart.de.srs.resources;

import java.io.IOException;
import java.util.Map;

import iaas.uni.stuttgart.de.srs.data.rest.MainResourceDAO;
import iaas.uni.stuttgart.de.srs.data.rest.ThingDataSource;
import iaas.uni.stuttgart.de.srs.data.rest.SituationTemplateDataSource;
import iaas.uni.stuttgart.de.srs.data.rest.SubscriptionDataSource;
import iaas.uni.stuttgart.de.srs.model.Thing;
import iaas.uni.stuttgart.de.srs.model.Subscription;
import iaas.uni.stuttgart.de.srs.service.impl.SrsServiceSOAPImpl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.Bus;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.glassfish.jersey.server.mvc.Viewable;

import de.uni_stuttgart.iaas.srsservice.SrsService;
import de.uni_stuttgart.iaas.srsservice.SrsServiceNotifciation_SrsCallbackServiceSOAP_Client;
import de.uni_stuttgart.iaas.srsservice.SrsService_Service;
import de.uni_stuttgart.iaas.srsservice.SubscribeRequest;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 */
@Path("/rest")
public class MainResource extends RESTResource{

	public MainResource() {
		LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
		loggingInInterceptor.setPrettyLogging(true);
		LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
		loggingOutInterceptor.setPrettyLogging(true);

		Bus factory = org.apache.cxf.BusFactory.newInstance().getDefaultBus();
		factory.getInInterceptors().add(loggingInInterceptor);
		factory.getOutInterceptors().add(loggingOutInterceptor);

	}

	@Path("/callback")
	public CallbackResource getCallbackResource() {
		return new CallbackResource();
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response root() {
		Viewable view = new Viewable("index", new MainResourceDAO(new ThingDataSource(),
				new SituationTemplateDataSource(), new SubscriptionDataSource()));
		return Response.ok(view).build();
	}

	@POST
	@Path("/object")
	@Consumes("application/x-www-form-urlencoded")
	public Response updateObject(@Context HttpServletRequest httpRequest) {
		// pretty hacky method, but works

		try {
			String body = IOUtils.readStringFromStream(httpRequest.getInputStream());
			System.out.println("updateObject called with Body: \n" + body);

			String[] params = body.split("&");

			if (!params[0].contains("objectId")) {
				return Response.status(Status.BAD_REQUEST).entity("Request misses objectId param").build();
			}

			String objectId = params[0].split("=")[1];

			ThingDataSource objData = new ThingDataSource();

			for (Thing obj : objData.getThings()) {
				if (obj.getId().equals(objectId)) {
					System.out.println("Found object: " + objectId);
					for (int i = 1; i < params.length; i++) {
						String propKey = params[i].split("=")[0];
						String propVal = params[i].split("=")[1];

						obj.getProperties().put(propKey, Boolean.valueOf(propVal));
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

		return Response.status(Status.NO_CONTENT).build();
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	public Response notify(@FormParam("Situation") String situation, @FormParam("Object") String object,
			@FormParam("Correlation") String correlation, @FormParam("Endpoint") String endpoint) {

		System.out.println("Called Notify (" + situation + "," + object + "," + correlation + "," + endpoint + ")");

		// find subscription
		Subscription sub = this.getSub(situation, object, correlation, endpoint);

		
		this.notifyService(sub);
		
		Viewable view = new Viewable("index", new MainResourceDAO(new ThingDataSource(),
				new SituationTemplateDataSource(), new SubscriptionDataSource()));
		return Response.ok(view).build();
	}

	
}
