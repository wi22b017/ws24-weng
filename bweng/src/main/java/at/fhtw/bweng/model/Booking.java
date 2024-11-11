package at.fhtw.bweng.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String status;
    private BigDecimal price;
    private LocalDateTime bookingDate;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingPassenger> bookingPassengers;

}
