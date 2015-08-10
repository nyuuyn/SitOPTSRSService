/**
 * 
 */
package iaas.uni.stuttgart.de.srs.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author kepeskn
 *
 */
public class CallbackResource {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response callback(@QueryParam("CorrelationId") String correlationId,
			@PathParam("AddressingId") String addressingId,
			@PathParam("CallbackEndpoint") String callbackEndpointEncoded) {
		
		// http://host:port/srsService/rest/callback/{correlationId}/{addressingId}/{sitMeProcessCallbackAddressEncoded}
		// TODO Callback to SitME Process

		System.out.println("CallbackResource#callback called");
		
		return Response.accepted().build();
	}

}
