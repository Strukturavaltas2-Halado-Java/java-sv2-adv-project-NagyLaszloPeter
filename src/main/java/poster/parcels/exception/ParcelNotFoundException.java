package poster.parcels.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class ParcelNotFoundException extends AbstractThrowableProblem {
    public ParcelNotFoundException(long id) {
        super(URI.create("parcels/parcel-NOT-FOUND"),
                "Parcel not found",
                Status.NOT_FOUND,
                String.format("Parcel not found with ID: %d", id));
    }
}
