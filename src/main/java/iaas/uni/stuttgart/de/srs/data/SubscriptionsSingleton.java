package iaas.uni.stuttgart.de.srs.data;

import iaas.uni.stuttgart.de.srs.model.Subscription;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class SubscriptionsSingleton {

	public List<Subscription> subscriptions = new ArrayList<Subscription>();

	private SubscriptionsSingleton() {
	}

	private static class SingletonHolder {
		private static final SubscriptionsSingleton INSTANCE = new SubscriptionsSingleton();
	}

	public static SubscriptionsSingleton getInstance() {
		return SingletonHolder.INSTANCE;
	}

}
