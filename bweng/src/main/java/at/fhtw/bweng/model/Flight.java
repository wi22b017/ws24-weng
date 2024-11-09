package at.fhtw.bweng.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
