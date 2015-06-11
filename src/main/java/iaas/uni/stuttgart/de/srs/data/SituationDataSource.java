package iaas.uni.stuttgart.de.srs.data;

import iaas.uni.stuttgart.de.srs.model.Situation;

import java.util.ArrayList;
import java.util.List;



/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class SituationDataSource {
	
	public List<Situation> situations = new ArrayList<Situation>();

	private SituationDataSource() {
		Situation testSituation1 = new Situation("sit.faults.MachineFaultX");
		Situation testSituation2 = new Situation("sit.faults.MachineRunning");
		testSituation1.observedProperties.add("testBoolean");
		testSituation2.observedProperties.add("testBoolean");
		
		this.situations.add(testSituation1);
		this.situations.add(testSituation2);
	}

	private static class SingletonHolder {
		private static final SituationDataSource INSTANCE = new SituationDataSource();
	}

	public static SituationDataSource getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
}
