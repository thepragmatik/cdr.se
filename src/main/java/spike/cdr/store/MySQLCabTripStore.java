package spike.cdr.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

/**
 * 
 *
 */
public class MySQLCabTripStore implements CabTripStore {

	private DataSource ds;

	public MySQLCabTripStore(DataSource ds) {
		this.ds = ds;
	}

	public List<CabTrip> query(Date forDate, List<String> cabs) throws StoreException {

		List<CabTrip> result = new ArrayList<>();

		ResultSet rs = null;

		try {
			Connection connection = ds.getConnection();

			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT medallion, COUNT(*) FROM cab_trip_data WHERE pickup_datetime=? GROUP BY medallion");

			pstmt.setDate(1, new java.sql.Date(forDate.getTime()));

			boolean returnedResultset = pstmt.execute();

			if (returnedResultset) {
				rs = pstmt.getResultSet();

				while (rs.next()) {

					String medallion = rs.getString(1);

					int count = rs.getInt(2);

					result.add(new CabTrip(medallion, count));
				}
			}

		} catch (SQLException e) {
			throw new StoreException("Error querying database.", e);
		} finally {
			if (null != rs) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.err.println("Error closing resultset");
					e.printStackTrace();

				}
			}
		}

		return result;
	}

}
