package iaas.uni.stuttgart.de.srs.resources;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.ws.BindingProvider;

import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.RelatesToType;
import org.apache.cxf.ws.addressing.WSAddressingFeature;

import de.uni_stuttgart.iaas.srsservice.NotifyRequest;
import de.uni_stuttgart.iaas.srsservice.SrsServiceCallback;
import de.uni_stuttgart.iaas.srsservice.SrsServiceNotifciation;
import iaas.uni.stuttgart.de.srs.data.rest.SubscriptionDataSource;
import iaas.uni.stuttgart.de.srs.model.Subscription;

/**
 * @author kepeskn
 *
 */
public abstract class RESTResource {

	public void notifyService(Subscription sub) {
		System.out.println("Preparing NotifyRequest");

		URL serviceUrl = null;
		try {
			serviceUrl = new URL((sub.getEndpoint().endsWith("/")
					? sub.getEndpoint().substring(0, sub.getEndpoint().lastIndexOf("/")) : sub.getEndpoint())
					+ "?wsdl");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		System.out.println("NotifyRequest will be sent to " + serviceUrl);

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
		// SrsServiceCallback service = new SrsServiceCallback(serviceUrl);

		AddressingProperties maps = new AddressingProperties();

		maps.setMessageID(null);
		maps.setTo(new EndpointReferenceType());
		maps.setReplyTo(null);

		// EndpointReferenceType epr = new EndpointReferenceType();
		// AttributedURIType uri = new AttributedURIType();
		// uri.setValue(serviceUrl.toString());
		// epr.setAddress(uri);
		// maps.setTo(epr);

		RelatesToType relatesTo = new RelatesToType();

		relatesTo.setValue(sub.getAddrMsgId());
		maps.setRelatesTo(relatesTo);

		SrsServiceNotifciation notifyService = service.getSrsCallbackServiceSOAP();

		((BindingProvider) notifyService).getRequestContext().put("javax.xml.ws.addressing.context", maps);

		System.out.println("NotifyRequest contains following values: ");
		System.out.println("Situation: " + sub.getSituationTemplateId());
		System.out.println("Thing: " + sub.getThingId());
		System.out.println("Correlation: " + sub.getCorrelation());

		NotifyRequest notifyReq = new NotifyRequest();

		notifyReq.setSituation(sub.getSituationTemplateId());
		notifyReq.setObject(sub.getThingId());
		notifyReq.setCorrelation(sub.getCorrelation());

		notifyService.notify(notifyReq);
	}

	public Subscription getSub(String situation, String object, String correlation, String endpoint) {
		SubscriptionDataSource subData = new SubscriptionDataSource();

		for (Subscription sub : subData.getSubscriptions()) {
			if (sub.getSituationTemplateId().equals(situation) && sub.getThingId().equals(object)
					&& sub.getCorrelation().equals(correlation) && sub.getEndpoint().equals(endpoint)) {
				return sub;
			}
		}
		return null;
	}

}
