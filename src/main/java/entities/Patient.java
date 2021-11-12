package entities;

import entities.enumerations.PatientStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String first_name;
    private String last_name;
    @Enumerated(EnumType.STRING)
    private PatientStatus status = PatientStatus.SICK;
    private LocalDate arrival_date = LocalDate.now();
    private LocalDate check_out_date;

    @ManyToOne(cascade = CascadeType.ALL)
    private Recipe recipe;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Doctor> doctors = new ArrayList<>();

    public Patient(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", status=" + status +
                ", arrival_date=" + arrival_date +
                ", check_out_date=" + check_out_date +
                ", doctors=" + doctors +
                '}';
    }
}
