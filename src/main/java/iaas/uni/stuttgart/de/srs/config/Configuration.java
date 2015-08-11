package iaas.uni.stuttgart.de.srs.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
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
			
			// taken from http://stackoverflow.com/a/2939223
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
			                whatismyip.openStream()));

			String ip = in.readLine();
			return ip;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
