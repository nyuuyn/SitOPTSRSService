package iaas.uni.stuttgart.de.srs.resources;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */

public class RootResource extends HttpServlet {

	/**
	 * MainHook for the REST API. This method will redirect to the JAX-RS resources
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set response content type
		response.setContentType("text/html");

		String uri = request.getRequestURI() + "rest";

		response.setStatus(response.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", uri);
	}

}
