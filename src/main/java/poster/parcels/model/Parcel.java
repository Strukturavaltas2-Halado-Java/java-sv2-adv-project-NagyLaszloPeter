package poster.parcels.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import poster.addressees.model.Addressee;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@Table(name = "parcels")
public class Parcel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_Id")
    private String senderId;

    @Column(name = "sending_date_of_time")
    private LocalDateTime sendingDateOfTime;

    @Enumerated(EnumType.STRING)
    private ParcelType parcelType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "addressee_id")
    private Addressee addressee;


    public Parcel(String senderId, LocalDateTime sendingDateOfTime, ParcelType parcelType) {
        this.senderId = senderId;
        this.sendingDateOfTime = sendingDateOfTime;
        this.parcelType = parcelType;
    }
}
