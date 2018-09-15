package spike.cdr.store;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * 
 *
 */
public class MySQLCabTripStore implements CabTripStore {

	private final String SQL_GET_CAB_TRIP_COUNT_PER_MEDALLION = "SELECT medallion, COUNT(*) FROM cab_trip_data WHERE DATE(pickup_datetime)='%s' AND medallion IN (%s) GROUP BY medallion";

	private DataSource ds;

	public MySQLCabTripStore(DataSource ds) {
		this.ds = ds;
	}

	public List<CabTrip> query(LocalDate forDate, List<String> cabs) throws StoreException {

		List<CabTrip> result = new ArrayList<>();

		try (Connection connection = ds.getConnection();
				ResultSet rs = connection.createStatement().executeQuery(SQL_GET_CAB_TRIP_COUNT_PER_MEDALLION);) {

			int resultCount = 0;

			while (rs.next()) {

				String medallion = rs.getString(1);

				int count = rs.getInt(2);

				result.add(new CabTrip(medallion, count));

				resultCount++;
			}

			System.out.println(String.format("Added %d results", resultCount));

		} catch (SQLException e) {

			throw new StoreException("Error querying database.", e);

		}

		return result;
	}

	private String getMedallions(List<String> cabs) {

		StringBuilder sb = new StringBuilder();

		cabs.forEach((medallion) -> {

			if (sb.length() > 0) {
				sb.append(",");
			}

			sb.append("'");
			sb.append(medallion);
			sb.append("'");

		});

		return sb.toString();
	}

}
