package spike.cdr;

import io.helidon.webserver.Http.ResponseStatus;
import io.helidon.webserver.Routing.Rules;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

/**
 * Service that enables querying the underlying storage for cab trips as per the
 * medallion(s) and a pick up date specified in its request.
 *
 */
public class CabService implements Service {

	@Override
	public void update(Rules rules) {

		rules.post("/", this::getDefaultCount);

	}

	protected void getDefaultCount(ServerRequest request, ServerResponse response) {

		response.status(ResponseStatus.from(501)).send();
	}

}
