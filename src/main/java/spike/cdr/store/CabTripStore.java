package spike.cdr.store;

import java.util.Date;
import java.util.List;

/**
 * Store interface that allows the application services to interact with the
 * underlying store using a configured store specific implementation.
 */
public interface CabTripStore {

	public List<CabTrip> query (Date forDate, List<String> cabs) throws StoreException;

}
