package poster.addressees.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class LogisticsLoadOverLimitException extends AbstractThrowableProblem {
    public LogisticsLoadOverLimitException(int logisticsLoad, int logisticsCapacity) {
        super(URI.create("parcel/addressee-Logistics-Load-Over"),
                "To much load capacity",
                Status.NOT_ACCEPTABLE,
                String.format("This load: %d is over maximum limit of: %d",
                        logisticsLoad, logisticsCapacity));
    }
}
