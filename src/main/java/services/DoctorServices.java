package services;

import dao.DoctorDao;
import entities.Doctor;
import entities.enumerations.DoctorProfession;

import java.util.ArrayList;
import java.util.List;

public class DoctorServices {
    DoctorDao doctorDao = new DoctorDao();

    public void saveDoctor(Doctor doctor) {
        List<Doctor> doctors = doctorDao.getAllInstance();
        List<String> names = new ArrayList<>();

        if (doctors.isEmpty()) {
            doctorDao.createInstance(doctor);
        } else {
            doctors.forEach(x -> names.add(x.getLast_name()));
            if (names.contains(doctor.getLast_name())) {
                System.out.println("This doctor already yet");
            } else {
                doctorDao.createInstance(doctor);
            }
        }
    }

    public Doctor getDoctorById(int id) {
        return doctorDao.getInstanceById(id);
    }

    public void readAllDoctorsInfo() {
        List<Doctor> doctors = doctorDao.getAllInstance();
        for (var d : doctors) {
            System.out.println("ID: " + d.getId() + " M.D.: " + d.getLast_name() + " Profession: " + d.getProfession());
            d.getPatients().forEach(System.out::println);
        }
    }
}
