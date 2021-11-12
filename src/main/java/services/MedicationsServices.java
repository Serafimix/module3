package services;

import dao.MedicationDao;
import entities.Medication;

import java.util.List;

public class MedicationsServices {
    MedicationDao dao = new MedicationDao();

    public void saveMedical(Medication medication) {
        dao.createInstance(medication);
    }

    public List<Medication> getAllMedication() {
        return dao.getAllInstance();
    }

    public Medication getMedicationFromId(int id) {
        return dao.getInstanceById(id);
    }

    public void readAllMedicationInfo() {
        System.out.println();
        dao.getAllInstance().forEach(System.out::println);
    }
}
