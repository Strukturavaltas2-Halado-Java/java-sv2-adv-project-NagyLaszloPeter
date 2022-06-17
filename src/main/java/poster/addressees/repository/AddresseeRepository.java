package poster.addressees.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poster.addressees.model.Addressee;

public interface AddresseeRepository extends JpaRepository<Addressee, Long> {
}