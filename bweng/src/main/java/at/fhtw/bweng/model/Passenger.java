package at.fhtw.bweng.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String seatNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "baggage_id", referencedColumnName = "id")
    private Baggage baggage;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    public Passenger(UUID id, String firstName, String lastName, LocalDate birthday, String seatNumber, Baggage baggage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.seatNumber = seatNumber;
        this.baggage = baggage;
    }
}
