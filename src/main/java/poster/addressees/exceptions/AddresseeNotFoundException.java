package poster.addressees.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class AddresseeNotFoundException extends AbstractThrowableProblem {
    public AddresseeNotFoundException(Long id) {
        super(URI.create("addressees/addressee-NOT-FOUND"),
                "Addressee not found",
                Status.NOT_FOUND,
                String.format("Addressee not found with ID: %d", id));
    }
}
