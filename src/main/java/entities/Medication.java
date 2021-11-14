package entities;

import entities.enumerations.MedicationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "medication")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private short count;
    private String name;
    @Enumerated(EnumType.STRING)
    private MedicationType medication_type = MedicationType.UNKNOWN;

    public Medication(String name, short count, MedicationType medications_type) {
        this.name = name;
        this.count = count;
        this.medication_type = medications_type;
    }
}
