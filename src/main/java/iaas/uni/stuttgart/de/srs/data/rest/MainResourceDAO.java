package iaas.uni.stuttgart.de.srs.data.rest;

import iaas.uni.stuttgart.de.srs.model.Thing;
import iaas.uni.stuttgart.de.srs.model.SituationTemplate;
import iaas.uni.stuttgart.de.srs.model.Subscription;

import java.util.List;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class MainResourceDAO {

	private ThingDataSource obsObjData;
	private SituationTemplateDataSource sitData;
	private SubscriptionDataSource subData;
	

	public MainResourceDAO(ThingDataSource obsObjData, SituationTemplateDataSource sitData, SubscriptionDataSource subData) {
		this.obsObjData = obsObjData;
		this.sitData = sitData;
		this.subData = subData;
	}

	public List<Thing> getObjs() {
		return this.obsObjData.getThings();
	}

	public List<SituationTemplate> getSits() {
		return this.sitData.getSituationTemplates();
	}

	public List<Subscription> getSubs() {
		return this.subData.getSubscriptions();
	}
}
