package at.fhtw.bweng.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class BookingPassenger {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonIgnore // Prevents infinite recursion
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    @JsonIgnore // Prevents infinite recursion
    private Passenger passenger;

}
