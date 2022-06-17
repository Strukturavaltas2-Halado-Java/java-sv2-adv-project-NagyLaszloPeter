package poster.addressees.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class LogisticsLoadOverLimitException extends AbstractThrowableProblem {
    public LogisticsLoadOverLimitException(int logisticsLoad) {
        super(URI.create("parcel/addressee-Logistics-Load-Over"),
                "To much capacity",
                Status.NOT_ACCEPTABLE,
                String.format("Capacity to Addressee: %d", logisticsLoad));
    }
}
