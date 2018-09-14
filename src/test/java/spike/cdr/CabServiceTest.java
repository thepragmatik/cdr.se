package spike.cdr;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.helidon.webserver.WebServer;

class CabServiceTest {

	private static WebServer webServer;

	@BeforeAll
	public static void startTheServer() throws Exception {
		webServer = Main.startServer();

		while (!webServer.isRunning()) {
			Thread.sleep(1 * 1000);
		}
	}

	@AfterAll
	public static void stopServer() throws Exception {
		if (webServer != null) {
			webServer.shutdown().toCompletableFuture().get(10, TimeUnit.SECONDS);
		}
	}

	@Test
	void testEmptyMediallionRequestReturnsUnsupportedOperation() throws Exception {
		HttpURLConnection conn;

		conn = getURLConnection("GET", "/cabtrips");

		Assertions.assertEquals(501, conn.getResponseCode(), "HTTP response1");
	}

	private HttpURLConnection getURLConnection(String method, String path) throws Exception {
		URL url = new URL("http://localhost:" + webServer.port() + path);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod(method);

		conn.setRequestProperty("Accept", "application/json");

		return conn;
	}

}
