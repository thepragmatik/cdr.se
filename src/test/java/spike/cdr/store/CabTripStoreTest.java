package spike.cdr.store;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.helidon.config.Config;
import io.helidon.config.ConfigMappingException;
import io.helidon.config.ConfigSources;
import io.helidon.config.MissingValueException;

class CabTripStoreTest {

	private static CabTripStore store;

	@BeforeAll
	static void initStore() {
		Config testConfig = Config.from(ConfigSources.file("src/test/resources/test-config.yaml"));

		final Config dbConfig = testConfig.get("store");

		String dataSourceConfiguration = dbConfig.get("dataSourceConfiguration").asString();

		final File dataSourceConfigFile = Paths.get(dataSourceConfiguration).toFile();

		DataSource dataSource = DataSourceFactory.fromProperties(dataSourceConfigFile);

		try {
			@SuppressWarnings("unchecked")
			Class<CabTripStore> klass = (Class<CabTripStore>) Class.forName(dbConfig.get("implementation").asString());

			Constructor<CabTripStore> declaredConstructor = klass.getDeclaredConstructor(DataSource.class);

			store = declaredConstructor.newInstance(dataSource);

		} catch (MissingValueException | ConfigMappingException | ClassNotFoundException | NoSuchMethodException
				| SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException("Unexpected Error", e);
		}
	}

	@Test
	void testCabTripCountIsGreaterThanZero() throws SQLException {

		List<CabTrip> cabTrips = store.query(new Date(), List.of("YADA-YADA-YA"));

		Assertions.assertNotNull(cabTrips);

		Assertions.assertTrue(cabTrips.size() > 0);

		cabTrips.forEach((c) -> {
			System.out.println(c.toString());
		});
	}

}
