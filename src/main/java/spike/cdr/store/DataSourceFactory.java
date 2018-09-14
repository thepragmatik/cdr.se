package spike.cdr.store;

import java.io.File;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceFactory {

	static DataSource fromProperties(File properties) {
		HikariConfig config = new HikariConfig(properties.getAbsolutePath());
		return new HikariDataSource(config);
	}

}
