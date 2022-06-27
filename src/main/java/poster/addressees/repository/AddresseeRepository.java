package poster.addressees.repository;

import net.bytebuddy.implementation.bind.annotation.Empty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poster.addressees.model.Addressee;
import poster.parcels.model.ParcelType;

import java.util.List;
import java.util.Optional;

public interface AddresseeRepository extends JpaRepository<Addressee, Long> {
    @Query("""
            SELECT a FROM Addressee a INNER JOIN a.parcels p
             WHERE (:settlement is null or a.settlement = :settlement)
             AND (:parcelType is null or p.parcelType = :parcelType)
             ORDER BY a.addresseeName""")
    List<Addressee> readAddresseesAndParcelsOptionalSettlementAndParcelType(
            @Empty @Param("settlement") Optional<String> settlement,
            @Empty @Param("parcelType") Optional<ParcelType> parcelType);
}