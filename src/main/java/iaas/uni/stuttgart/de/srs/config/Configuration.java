package iaas.uni.stuttgart.de.srs.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

public class Configuration {
	
	private String sitOptAddress = "http://192.168.209.200:10010";
	
	public String getSitOPTAddress(){
		return this.sitOptAddress;
	}

	public String getSitOPTSRSServiceAddress(){
		// TODO pretty weak check for the address of this service
		try {
			return InetAddress.getLocalHost().getHostAddress() + ":8080/srsTestService";
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
