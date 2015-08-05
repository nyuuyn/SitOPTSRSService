/**
 * 
 */
package iaas.uni.stuttgart.de.srs.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author kepeskn
 *
 */
public class CallbackResource {

	@POST
	@Path("/{correlationId}/{addressingId}/{sitMeProcessCallbackAddressEncoded}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response callback(@PathParam("correlationId") String correlationId,
			@PathParam("addressingId") String addressingId,
			@PathParam("sitMeProcessCallbackAddressEncoded") String callbackAddress) {
		
		// http://host:port/srsService/rest/callback/{correlationId}/{addressingId}/{sitMeProcessCallbackAddressEncoded}
		// TODO Callback to SitME Process

		return Response.accepted().build();
	}

}
