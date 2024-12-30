package at.fhtw.bweng.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
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
    private LocalDate dateOfBirth;
    private String seatNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "baggage_id", referencedColumnName = "id")
    private Baggage baggage;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    public Passenger(UUID id, String firstName, String lastName, LocalDate dateOfBirth, String seatNumber, Baggage baggage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.seatNumber = seatNumber;
        this.baggage = baggage;
    }
}
