package iaas.uni.stuttgart.de.srs.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

public class Configuration {

	private Properties getProperties() {
		Properties prop = new Properties();
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("/config.props");
		try {
			prop.load(inStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop;
	}

	public String getSitOPTAddress() {
		return this.getProperties().getProperty("sitOptURL");
	}

	public String getSitOPTSRSServiceAddress() {

		String srsServiceIp = this.getProperties().getProperty("srsServiceIp");

		if (srsServiceIp.equals("127.0.0.1")) {

			// TODO pretty weak check for the address of this service
			try {

				this.showNetworkInterfaces();
				// taken from http://stackoverflow.com/a/2939223
				URL whatismyip = new URL("http://checkip.amazonaws.com");
				BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

				String ip = in.readLine();
				return "http://" + ip + ":8080/srsTestService";
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			return "http://" + srsServiceIp + ":8080/srsTestService";
		}

	}

	public void showNetworkInterfaces() throws SocketException {
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets))
			displayInterfaceInformation(netint);
	}

	public void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
		System.out.println("Display name: " + netint.getDisplayName() + "\n");
		System.out.println("Name: " + netint.getName() + "\n");
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			System.out.println("InetAddress: " + inetAddress + "\n");
		}
		System.out.println("\n");
	}

}
