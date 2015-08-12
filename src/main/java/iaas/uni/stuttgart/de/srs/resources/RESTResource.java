package iaas.uni.stuttgart.de.srs.resources;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import iaas.uni.stuttgart.de.srs.service.impl.SrsServiceSOAPImpl;

/**
 * @author kepeskn
 *
 */
public abstract class RESTResource {

	private static final Logger LOG = Logger.getLogger(RESTResource.class
			.getName());
	
	public void notifyService(Subscription sub) {
		LOG.log(Level.FINEST,"Preparing NotifyRequest");

		URL serviceUrl = null;
		try {
			serviceUrl = new URL((sub.getEndpoint().endsWith("/")
					? sub.getEndpoint().substring(0, sub.getEndpoint().lastIndexOf("/")) : sub.getEndpoint())
					+ "?wsdl");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		LOG.log(Level.FINEST,"NotifyRequest will be sent to " + serviceUrl);

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

		LOG.log(Level.FINEST,"NotifyRequest contains following values: ");
		LOG.log(Level.FINEST,"Situation: " + sub.getSituationTemplateId());
		LOG.log(Level.FINEST,"Thing: " + sub.getThingId());
		LOG.log(Level.FINEST,"Correlation: " + sub.getCorrelation());

		NotifyRequest notifyReq = new NotifyRequest();

		notifyReq.setSituation(sub.getSituationTemplateId());
		notifyReq.setObject(sub.getThingId());
		notifyReq.setCorrelation(sub.getCorrelation());

		notifyService.notify(notifyReq);
	}

	public Subscription getSub(String situation, String object, String correlation, String endpoint) {
		SubscriptionDataSource subData = new SubscriptionDataSource();

		LOG.log(Level.FINEST,"Looking for Subscription with: ");
		LOG.log(Level.FINEST,"Situation: " + situation);
		LOG.log(Level.FINEST,"Thing: " + object);
		LOG.log(Level.FINEST,"Correlation: " + correlation);
		LOG.log(Level.FINEST,"Endpoint: " + endpoint);
		
		LOG.log(Level.FINEST,"Found following Subs");
		for (Subscription sub : subData.getSubscriptions()) {
			LOG.log(Level.FINEST,"Sub: ");
			LOG.log(Level.FINEST,sub.toString());
			
			// TODO SUPER ugly if, just for debugging right now
			if(sub.getSituationTemplateId() != null && sub.getSituationTemplateId().equals(situation)){
				if(sub.getThingId() != null && sub.getThingId().equals(object)){
					if(sub.getCorrelation() != null && sub.getCorrelation().equals(correlation)){
						if(sub.getEndpoint() != null && sub.getEndpoint().equals(endpoint)){
							LOG.log(Level.FINEST,"Found matching subscription");
							return sub;
						}
									
					} else {
						continue;
					}
					
					
				} else {
					continue;
				}
				
				
			} else {
				continue;
			}
			
		}
		return null;
	}

}
