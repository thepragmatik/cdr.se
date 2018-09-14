package spike.cdr.store;

import java.util.Date;

/**
 * State holder that captures the details on the trip for specified cab.
 */
public class CabTrip {

	private String medallion;

	private int tripCount;

	public CabTrip(String medallion, int tripCount) {
		super();
		this.medallion = medallion;
		this.tripCount = tripCount;
	}

	/**
	 * 
	 * @return Identifier for the cab in the underlying store.
	 */
	public String getMedallion() {
		return medallion;
	}

	/**
	 * 
	 * @return Number of trips retrieved for the cab specified by
	 *         {@link #getMedallion}.
	 */
	public int getTripCount() {
		return tripCount;
	}

	@Override
	public String toString() {
		return "CabTrip [medallion=" + medallion + ", tripCount=" + tripCount + "]";
	}

}
