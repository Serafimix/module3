package services;

import dao.PatientDao;
import entities.Patient;

import java.util.List;

public class PatientServices {
    PatientDao patientDao = new PatientDao();

    public Patient savePatient(Patient patient) {
        List<Patient> patients = patientDao.getAllInstance();

        if (patients.isEmpty()) {
            patientDao.createInstance(patient);
        } else {
            for (var p : patients) {
                if (p.getFirst_name().equals(patient.getFirst_name()) && p.getLast_name().equals(patient.getLast_name())) {
                    patient = p;
                }
            }
        }
        if (patient.getId() == 0) {
            patientDao.createInstance(patient);
        }
        return patient;
    }

    public void readAllPatients() {
        List<Patient> patients = patientDao.getAllInstance();
        System.out.println("All patients in hospital");
        patients.forEach(System.out::println);
    }

    public void updatePatient(Patient patient) {
        patientDao.updateInstance(patient);
    }


    public void readRecipeFromPatient(Patient patient) {
        patientDao.readRecipeFromPatient(patient);
    }
}
