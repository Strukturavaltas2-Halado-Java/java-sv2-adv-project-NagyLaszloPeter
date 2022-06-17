package poster.parcels.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poster.parcels.model.Parcel;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {
}