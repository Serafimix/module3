package entities;

import entities.enumerations.DoctorProfession;
import entities.enumerations.DoctorStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String last_name;
    @Enumerated(EnumType.STRING)
    private DoctorProfession profession;
    @Enumerated(EnumType.STRING)
    private DoctorStatus status = DoctorStatus.FREE;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(name="doctor_patients", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "patient_id"))
//    @ToString.Exclude
    private List<Patient> patients = new ArrayList<>();

    public Doctor(String last_name, DoctorProfession profession) {
        this.last_name = last_name;
        this.profession = profession;
    }

    @Override
    public String toString() {
        return "Doctor " +
                "last name is'" + last_name + '\'' +
                ", profession=" + profession +
                ", status=" + status +
                '}';
    }
}
