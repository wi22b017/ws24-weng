package at.fhtw.bweng.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String flightNumber;
    private OffsetDateTime departureTime;
    private OffsetDateTime arrivalTime;
    @ManyToOne
    private Airport flightOrigin;
    @ManyToOne
    private Airport flightDestination;
    @ManyToOne
    private Aircraft aircraft;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    // Custom Constructor (Excluding Timestamps)
    public Flight(UUID id, String flightNumber, OffsetDateTime departureTime, OffsetDateTime arrivalTime,
                  Airport flightOrigin, Airport flightDestination, Aircraft aircraft) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.flightOrigin = flightOrigin;
        this.flightDestination = flightDestination;
        this.aircraft = aircraft;
    }
}
