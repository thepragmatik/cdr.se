package spike.cdr.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

	private DataSource ds;

	public MySQLCabTripStore(DataSource ds) {
		this.ds = ds;
	}

	public List<CabTrip> query(LocalDate forDate, List<String> cabs) throws StoreException {

		List<CabTrip> result = new ArrayList<>();

		ResultSet rs = null;

		try {
			Connection connection = ds.getConnection();

			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT medallion, COUNT(*) FROM cab_trip_data WHERE DATE(pickup_datetime)=? AND medallion IN ("
							+ getValuePlaceholders(cabs) + ") GROUP BY medallion");

			System.out.println(getValuePlaceholders(cabs));

			pstmt.setObject(1, forDate.toString());

			int index = 2;

			for (String cab : cabs) {
				pstmt.setString(index++, cab);
			}

			pstmt.setString(1, getMedallions(cabs));

//			System.out.println(getMedallions(cabs));
//
			System.out.println(pstmt.toString());

			boolean returnedResultset = pstmt.execute();

			if (returnedResultset) {
				rs = pstmt.getResultSet();

				int resultCount = 0;

				while (rs.next()) {

					String medallion = rs.getString(1);

					int count = rs.getInt(2);

					result.add(new CabTrip(medallion, count));

					resultCount++;
				}

				System.out.println(String.format("Added %d results", resultCount));

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

	private String getValuePlaceholders(List<String> cabs) {
		StringBuilder sb = new StringBuilder();

		cabs.forEach((c) -> {

			if (sb.length() > 0) {
				sb.append(",");
			}

			sb.append("?");

		});

		return sb.toString();
	}

	private String getMedallions(List<String> cabs) {

		StringBuilder sb = new StringBuilder();

		cabs.forEach((c) -> {

			if (sb.length() > 0) {
				sb.append(",");
			}

			sb.append("'");
			sb.append(c);
			sb.append("'");
		});

		return sb.toString();
	}

}
