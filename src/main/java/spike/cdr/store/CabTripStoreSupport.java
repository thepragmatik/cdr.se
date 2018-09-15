package spike.cdr.store;

import java.time.LocalDate;
import java.util.List;

/**
 * Abstract {@link CabTripStore} implementation that accepts a specialised
 * {@link CabTripStore} implementation and (typically) delegates to it.
 */
public abstract class CabTripStoreSupport implements CabTripStore {

	private CabTripStore store;

	/**
	 * 
	 */
	public CabTripStoreSupport(CabTripStore store) {
		this.store = store;
	}

	public List<CabTrip> query(LocalDate forDate, List<String> cabs) throws StoreException {
		return store.query(forDate, cabs);
	}

}
