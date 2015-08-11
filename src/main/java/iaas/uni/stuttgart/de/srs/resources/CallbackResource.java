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

/**
 * @author kepeskn
 *
 */
public class CallbackResource extends RESTResource {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response callback(@QueryParam("CorrelationId") String correlationId,
			@PathParam("AddressingId") String addressingId,
			@PathParam("CallbackEndpoint") String callbackEndpointEncoded, @Context HttpServletRequest httpRequest) {

		// http://host:port/srsService/rest/callback/{correlationId}/{addressingId}/{sitMeProcessCallbackAddressEncoded}
		// TODO Callback to SitME Process

		System.out.println("CallbackResource#callback called with: ");
		System.out.println("CorrelationId: " + correlationId);
		System.out.println("AddressingId: " + addressingId);
		System.out.println("CallbackEndpoint: " + callbackEndpointEncoded);

		try {
			String body = IOUtils.readStringFromStream(httpRequest.getInputStream());
			System.out.println("Body: \n" + body);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("HTTPServletRequest: ");

		System.out.println("Params:");
		for (Object paramName : Collections.list(httpRequest.getParameterNames())) {
			System.out.println("ParamKey: " + paramName);
			for (int i = 0; i < httpRequest.getParameterValues((String) paramName).length; i++) {
				System.out.println("ParamValue: " + httpRequest.getParameterValues((String) paramName)[i]);
			}

		}

		System.out.println("QueryString: " + httpRequest.getQueryString());

		// this.getSub(situation, object, correlationId, endpoint)

		return Response.accepted().build();
	}

}
