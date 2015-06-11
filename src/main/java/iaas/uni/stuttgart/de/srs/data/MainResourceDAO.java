package iaas.uni.stuttgart.de.srs.data;

import iaas.uni.stuttgart.de.srs.model.ObservedObject;
import iaas.uni.stuttgart.de.srs.model.Situation;
import iaas.uni.stuttgart.de.srs.model.Subscription;

import java.util.List;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class MainResourceDAO {

	private final List<ObservedObject> objs;
	private final List<Situation> sits;
	private final List<Subscription> subs;

	public MainResourceDAO() {
		this.objs = ObservedObjectDataSource.getInstance().objects;
		this.sits = SituationDataSource.getInstance().situations;
		this.subs = SubscriptionsSingleton.getInstance().subscriptions;
	}

	public List<ObservedObject> getObjs() {
		return objs;
	}

	public List<Situation> getSits() {
		return sits;
	}

	public List<Subscription> getSubs() {
		return subs;
	}
}
