package poster.addressees.model;

import lombok.*;
import poster.parcels.model.Parcel;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "addressees")
public class Addressee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "addressee_name")
    private String addresseeName;

    @Column(name = "post_code")
    private int postCode;

    private String settlement;

    @Column(name = "addressee_number")
    private String addresseeNumber;

    @OneToMany(mappedBy = "addressee", cascade = CascadeType.PERSIST)
    private List<Parcel> parcels;

    public Addressee(String addresseeName, int postCode, String settlement, String addresseeNumber, List<Parcel> packages) {
        this.addresseeName = addresseeName;
        this.postCode = postCode;
        this.settlement = settlement;
        this.addresseeNumber = addresseeNumber;
        this.parcels = packages;
    }


    public void addPackages(Parcel parcel) {
        parcels.add(parcel);
        parcel.setAddressee(this);
    }
}
