package dao;

import entities.Doctor;
import entities.enumerations.DoctorProfession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorDaoTest {
    private static DoctorDao doctorDao;

    @BeforeAll
    static void beforeAll() {
        doctorDao = new DoctorDao();
        Doctor doctor1 = new Doctor("Test1", DoctorProfession.THERAPIST);
        Doctor doctor2 = new Doctor("Test2", DoctorProfession.PATHOLOGIST);
        Doctor doctor3 = new Doctor("Test3", DoctorProfession.INFECTIONIST);
        doctorDao.createInstance(doctor1);
        doctorDao.createInstance(doctor2);
        doctorDao.createInstance(doctor3);
    }

    @Test
    void save() {
        Doctor doctor4 = new Doctor("Test", DoctorProfession.INTERN);
        doctorDao.createInstance(doctor4);
        Assertions.assertEquals(doctor4.getId(), doctorDao.getInstanceById(doctor4.getId()).getId());
    }

    @Test
    void update() {
        Doctor doctor = doctorDao.getInstanceById(1);
        doctor.setLast_name("TESTChange");
        doctorDao.updateInstance(doctor);
        Assertions.assertEquals(doctor.getLast_name(), doctorDao.getInstanceById(doctor.getId()).getLast_name());
    }

    @Test
    void delete() {
        Doctor doctor = new Doctor();
        doctorDao.createInstance(doctor);
        doctorDao.deleteInstanceById(doctor.getId());
        assertNull(doctorDao.getInstanceById(doctor.getId()));
    }

    @Test
    void get() {
        Doctor doctor = new Doctor("testOne", DoctorProfession.INTERN);
        doctorDao.createInstance(doctor);
        Doctor doctorGetFromDB = doctorDao.getInstanceById(doctor.getId());
        Assertions.assertEquals(doctorGetFromDB.getLast_name(), doctor.getLast_name());
    }

    @Test
    void getAll() {
        Assertions.assertEquals(3, doctorDao.getAllInstance().size());
    }
}