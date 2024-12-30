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
@Table(name = "address", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"street", "number", "zip", "city","country"})
})
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String street;
    private int number;
    private int zip;
    private String city;
    private String country;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    // Custom Constructor (Excludes Timestamps)
    public Address(UUID id, String street, int number, int zip, String city, String country) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.zip = zip;
        this.city = city;
        this.country = country;
    }
}
