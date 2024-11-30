package at.fhtw.bweng.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String externalId;
    private String name;
    private String contentType;

}
