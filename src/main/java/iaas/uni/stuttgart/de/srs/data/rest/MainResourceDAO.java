package iaas.uni.stuttgart.de.srs.data.rest;

import iaas.uni.stuttgart.de.srs.model.ObservedObject;
import iaas.uni.stuttgart.de.srs.model.Situation;
import iaas.uni.stuttgart.de.srs.model.Subscription;

import java.util.List;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class MainResourceDAO {

	private ObservedObjectDataSource obsObjData;
	private SituationDataSource sitData;
	private SubscriptionsSingleton subData;
	

	public MainResourceDAO(ObservedObjectDataSource obsObjData, SituationDataSource sitData, SubscriptionsSingleton subData) {
		this.obsObjData = obsObjData;
		this.sitData = sitData;
		this.subData = subData;
	}

	public List<ObservedObject> getObjs() {
		return this.obsObjData.getObjects();
	}

	public List<Situation> getSits() {
		return this.sitData.getSituations();
	}

	public List<Subscription> getSubs() {
		return this.subData.getSubscriptions();
	}
}
