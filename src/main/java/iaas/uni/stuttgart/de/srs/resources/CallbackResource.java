/**
 * 
 */
package iaas.uni.stuttgart.de.srs.resources;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.IOUtils;

import iaas.uni.stuttgart.de.srs.model.Subscription;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * @author kepeskn
 *
 */
public class CallbackResource extends RESTResource {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response callback(@Context HttpServletRequest httpRequest) {

		// http://host:port/srsService/rest/callback/{correlationId}/{addressingId}/{sitMeProcessCallbackAddressEncoded}
		String callbackEndpoint = null;
		String addressingId = null;
		String correlationId = null;
		String situationId = null;
		String thingId = null;

		System.out.println("CallbackResource#callback called with: ");
		String body = null;
		try {
			body = IOUtils.readStringFromStream(httpRequest.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSON jsonBody = JSONSerializer.toJSON(body);

		situationId = ((JSONObject) jsonBody).getString("situationtemplate");
		thingId = ((JSONObject) jsonBody).getString("thing");

		System.out.println("Parsed JSON body: ");
		System.out.println(jsonBody);


		for (Object paramName : Collections.list(httpRequest.getParameterNames())) {
			if (httpRequest.getParameterValues((String) paramName).length != 1) {
				continue;
			}

			switch (((String) paramName)) {
			case "CorrelationId":
				correlationId = httpRequest.getParameterValues(((String) paramName))[0];
				break;
			case "AddressingId":
				addressingId = httpRequest.getParameterValues(((String) paramName))[0];
				break;
			case "CallbackEndpoint":
				callbackEndpoint = httpRequest.getParameterValues(((String) paramName))[0];
				break;
			default:
				break;
			}

		}

		System.out.println("Found following query values: ");
		System.out.println("CorrelationId: " + correlationId);
		System.out.println("AddressingId: " + addressingId);
		System.out.println("CallbackEndpoint: " + callbackEndpoint);

		Subscription sub = this.getSub(situationId, thingId, correlationId, callbackEndpoint);

		this.notifyService(sub);
		
		return Response.accepted().build();
	}

}
