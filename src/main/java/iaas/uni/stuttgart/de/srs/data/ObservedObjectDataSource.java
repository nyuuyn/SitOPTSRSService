package iaas.uni.stuttgart.de.srs.data;

import iaas.uni.stuttgart.de.srs.model.ObservedObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class ObservedObjectDataSource {
	
	public List<ObservedObject> objects = new ArrayList<ObservedObject>();

	private ObservedObjectDataSource(){
		ObservedObject testObj = new ObservedObject("X221");
		testObj.getProperties().put("testBoolean", false);
		objects.add(testObj);
	}
	
	private static class SingletonHolder{
		private static final ObservedObjectDataSource INSTANCE = new ObservedObjectDataSource();		
	}
	
	public static ObservedObjectDataSource getInstance(){
		return SingletonHolder.INSTANCE;
	}
}
