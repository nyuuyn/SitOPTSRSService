package iaas.uni.stuttgart.de.srs.resources;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import iaas.uni.stuttgart.de.srs.data.MainResourceDAO;
import iaas.uni.stuttgart.de.srs.data.ObservedObjectDataSource;
import iaas.uni.stuttgart.de.srs.data.SubscriptionsSingleton;
import iaas.uni.stuttgart.de.srs.model.ObservedObject;
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
import javax.xml.ws.BindingProvider;

import org.apache.cxf.Bus;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.RelatesToType;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.glassfish.jersey.server.mvc.Viewable;

import de.uni_stuttgart.iaas.srsservice.NotifyRequest;
import de.uni_stuttgart.iaas.srsservice.SrsService;
import de.uni_stuttgart.iaas.srsservice.SrsServiceCallback;
import de.uni_stuttgart.iaas.srsservice.SrsServiceNotifciation;
import de.uni_stuttgart.iaas.srsservice.SrsServiceNotifciation_SrsCallbackServiceSOAP_Client;
import de.uni_stuttgart.iaas.srsservice.SrsService_Service;
import de.uni_stuttgart.iaas.srsservice.SubscribeRequest;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 */
@Path("/rest")
public class MainResource {
	
	public MainResource() {
		LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
		loggingInInterceptor.setPrettyLogging(true);
		LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
		loggingOutInterceptor.setPrettyLogging(true);
		
		Bus factory =org.apache.cxf.BusFactory.newInstance().getDefaultBus();
		factory.getInInterceptors().add(loggingInInterceptor);
		factory.getOutInterceptors().add(loggingOutInterceptor);

	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response root() {
		Viewable view = new Viewable("index", new MainResourceDAO());
		return Response.ok(view).build();
	}

	@POST
	@Path("/object")
	@Consumes("application/x-www-form-urlencoded")
	public Response updateObject(@Context HttpServletRequest httpRequest) {
		// pretty hacky method, but works

		try {
			String body = IOUtils.readStringFromStream(httpRequest
					.getInputStream());
			System.out.println("updateObject called with Body: \n" + body);

			String[] params = body.split("&");

			if (!params[0].contains("objectId")) {
				return Response.status(Status.BAD_REQUEST)
						.entity("Request misses objectId param").build();
			}

			String objectId = params[0].split("=")[1];

			for (ObservedObject obj : ObservedObjectDataSource.getInstance().objects) {
				if (obj.getId().equals(objectId)) {
					System.out.println("Found object: " + objectId);
					for (int i = 1; i < params.length; i++) {
						String propKey = params[i].split("=")[0];
						String propVal = params[i].split("=")[1];

						obj.getProperties().put(propKey,
								Boolean.valueOf(propVal));
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
	public Response notify(@FormParam("Situation") String situation,
			@FormParam("Object") String object,
			@FormParam("Correlation") String correlation,
			@FormParam("Endpoint") String endpoint) {

		System.out.println("Called Notify (" + situation + "," + object + ","
				+ correlation + "," + endpoint + ")");

		URL serviceUrl = null;
		try {
			serviceUrl = new URL((endpoint.endsWith("/") ? endpoint.substring(
					0, endpoint.lastIndexOf("/")) : endpoint) + "?wsdl");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// find subscription
		Subscription sub = this
				.getSub(situation, object, correlation, endpoint);

		// TODO set addressing

		// AddressingProperties maps = new AddressingProperties();
		// EndpointReferenceType ref = new EndpointReferenceType();
		// AttributedURIType add = new AttributedURIType();
		// add.setValue("http://localhost:9090/decoupled_endpoint");
		// RelatesToType relatesTo = new RelatesToType();
		// relatesTo.setValue(value);
		// maps.setRelatesTo(rel);
		// maps.setReplyTo(ref);
		// maps.setFaultTo(ref);
		//
		// ((BindingProvider)port).getRequestContext()
		// .put("javax.xml.ws.addressing.context", maps);

		
		
		SrsServiceCallback service = new SrsServiceCallback(serviceUrl, new WSAddressingFeature());
//		SrsServiceCallback service = new SrsServiceCallback(serviceUrl);
		
		AddressingProperties maps = new AddressingProperties();
		
		maps.setMessageID(null);
		maps.setTo(new EndpointReferenceType());
		maps.setReplyTo(null);
		

//		EndpointReferenceType epr = new EndpointReferenceType();
//		AttributedURIType uri = new AttributedURIType();
//		uri.setValue(serviceUrl.toString());
//		epr.setAddress(uri);
//		maps.setTo(epr);
		
		RelatesToType relatesTo = new RelatesToType();
		
		relatesTo.setValue(sub.getAddrMsgId());
		maps.setRelatesTo(relatesTo);
		
		SrsServiceNotifciation notifyService = service
				.getSrsCallbackServiceSOAP();		
		
		((BindingProvider) notifyService).getRequestContext().put(
				"javax.xml.ws.addressing.context", maps);

		NotifyRequest notifyReq = new NotifyRequest();
		
		

		notifyReq.setSituation(situation);
		notifyReq.setObject(object);
		notifyReq.setCorrelation(correlation);

		notifyService.notify(notifyReq);

		Viewable view = new Viewable("index", new MainResourceDAO());
		return Response.ok(view).build();
	}

	private Subscription getSub(String situation, String object,
			String correlation, String endpoint) {
		for (Subscription sub : SubscriptionsSingleton.getInstance().subscriptions) {
			if (sub.getSituationId().equals(situation)
					&& sub.getObjectId().equals(object)
					&& sub.getCorrelation().equals(correlation)
					&& sub.getEndpoint().equals(endpoint)) {
				return sub;
			}
		}
		return null;
	}
}
