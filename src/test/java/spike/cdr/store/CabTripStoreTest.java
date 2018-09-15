package spike.cdr.store;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
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

	private static final String TEST_CONFIG_YAML = "src/test/resources/test-config.yaml";

	private static final String CONF_KEY_IMPLEMENTATION = "implementation";

	private static final String CONF_KEY_STORE = "store";

	private static final String CONF_KEY_DATA_SOURCE_CONFIGURATION = "dataSourceConfiguration";

	private static CabTripStore store;

	@BeforeAll
	static void initStore() {
		Config testConfig = Config.from(ConfigSources.file(TEST_CONFIG_YAML));

		final Config dbConfig = testConfig.get(CONF_KEY_STORE);

		String dataSourceConfiguration = dbConfig.get(CONF_KEY_DATA_SOURCE_CONFIGURATION).asString();

		final File dataSourceConfigFile = Paths.get(dataSourceConfiguration).toFile();

		DataSource dataSource = DataSourceFactory.fromProperties(dataSourceConfigFile);

		try {
			@SuppressWarnings("unchecked")
			Class<CabTripStore> klass = (Class<CabTripStore>) Class
					.forName(dbConfig.get(CONF_KEY_IMPLEMENTATION).asString());

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

		LocalDate from = LocalDate.of(2013, 12, 03);

		List<CabTrip> cabTrips = store.query(from,
				List.of("00FD1D146C1899CEDB738490659CAD30", "FE4EC2CB0AC48EBE26EF3E489B91D941", "YADA-YADA-YA"));

		Assertions.assertNotNull(cabTrips);

		Assertions.assertTrue(cabTrips.size() > 0);

//		cabTrips.forEach((c) -> {
//			System.out.println(c.toString());
//		});
	}

	@Test
	void testCabTripCountIsZeroForThisYearsDate() throws SQLException {
		LocalDate from = LocalDate.of(2018, 12, 03);

		List<CabTrip> cabTrips = store.query(from,
				List.of("00FD1D146C1899CEDB738490659CAD30", "FE4EC2CB0AC48EBE26EF3E489B91D941", "YADA-YADA-YA"));

		Assertions.assertNotNull(cabTrips);

		Assertions.assertTrue(cabTrips.size() == 0);
	}

}
