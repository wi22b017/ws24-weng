package at.fhtw.bweng.model;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String gender;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private LocalDate dateOfBirth;
    private String role;
    private String status;
    @ManyToOne
    private Address address;
    @ManyToOne
    private PaymentMethod paymentMethod;

    @OneToOne
    @JoinColumn(name = "profile_picture_id")
    private Picture profilePicture;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    // Custom Constructor (Excluding Timestamps)
    public User(UUID id, String gender, String firstName, String lastName, String username, String password,
                String email, LocalDate dateOfBirth, String role, String status,
                Address address, PaymentMethod paymentMethod) {
        this.id = id;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.status = status;
        this.address = address;
        this.paymentMethod = paymentMethod;
    }
}
