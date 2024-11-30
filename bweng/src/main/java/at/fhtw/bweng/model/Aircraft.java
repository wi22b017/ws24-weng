package at.fhtw.bweng.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String serialNumber;
    private String manufacturer;
    private String model;
    private int capacity;
    @ManyToOne
    private Airline airline;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    // Custom Constructor (Excludes Timestamps)
    public Aircraft(UUID id, String serialNumber, String manufacturer, String model, int capacity, Airline airline) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.manufacturer = manufacturer;
        this.model = model;
        this.capacity = capacity;
        this.airline = airline;
    }

}
