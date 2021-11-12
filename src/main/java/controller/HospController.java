package controller;

import entities.Doctor;
import entities.Medication;
import entities.Patient;
import entities.Recipe;
import entities.enumerations.DoctorProfession;
import entities.enumerations.DoctorStatus;
import entities.enumerations.MedicationType;
import entities.enumerations.PatientStatus;
import services.DoctorServices;
import services.MedicationsServices;
import services.PatientServices;
import services.RecipeServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HospController {
    DoctorServices doctorServices = new DoctorServices();
    MedicationsServices medicationsServices = new MedicationsServices();
    PatientServices patientServices = new PatientServices();
    RecipeServices recipeServices = new RecipeServices();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public void run() {
        hospitalInit();
        Patient patient = addNewPatient();

        String action = "";
        while (!action.equals("0")) {

            System.out.println("""

                     Select an action, please:
                     ***************************************
                     write 1  if you want to create new Recipe for this patient
                     write 2  if you want to add new medication
                     write 3  if you want to see your Recipe
                     write 4  if you want to see all Doctors
                     write 5  if you want to see all Patients
                     write 6  if you want to see all Medications
                     write 7  if you want invite new Doctor in the Hospital
                     write 8  if you want discharge a patient
                     write 9  if you want change status of the Doctor
                     write 10  if patient is dead and get him to the pathologist
                     write 0  if you want to finish
                     ***************************************
                    """);

            try {
                action = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("\n");

            switch (action) {
                case "1" -> this.createRecipe(patient);
                case "2" -> createMedication();
                case "3" -> readRecipeFromPatient(patient);
                case "4" -> doctorServices.readAllDoctorsInfo();
                case "5" -> patientServices.readAllPatients();
                case "6" -> medicationsServices.readAllMedicationInfo();
                case "7" -> inviteNewDoctor();
                case "8" -> dischargeAPatient();
                case "9" -> changeStatusOfTheDoctor();
                case "10" -> deadlineOfPatient();
                case "0" -> System.out.println("Thank you, bye");
                default -> System.out.println("Something wrong, please try again");
            }
        }
    }

    private void createRecipe(Patient patient) {
        Recipe recipe = new Recipe();
        int idMed = 0;
        String action = "";
        while (true) {
            System.out.println("""
                     
                     Select an action, please:
                     ***************************************
                     enter ID of medication for ADD this to the recipe
                     enter 0  if you have finished placing your order
                     ***************************************
                    """);
            medicationsServices.getAllMedication().forEach(System.out::println);
            try {
                action = bufferedReader.readLine();
                if (action.equals("0")) {
                    System.out.println("Please add description for this recipe \n");
                    action = bufferedReader.readLine();
                    System.out.println("Thank you.");
                    recipe.setRecommendations(action);
                    patient.setRecipe(recipe);
                    patientServices.updatePatient(patient);
                    break;
                } else {
                    try {
                        idMed = Integer.parseInt(action);
                        recipe.getMedications().add(medicationsServices.getMedicationFromId(idMed));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("\n");
        }
    }

    private void deadlineOfPatient() {
        System.out.println("Please, enter ID Patient who is dead:\n");
        patientServices.readAllPatients();
        int idPat = 0;
        try {
            idPat = Integer.parseInt(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Patient patient = patientServices.getPatientFromId(idPat);
        if (patient != null) {
            if (patient.getStatus() == PatientStatus.DEAD) {
                System.out.println("I'm so sorry, but this patient is already dead...");
            } else {
                patient.setStatus(PatientStatus.DEAD);
                patient.setRecipe(null);
                patient.getDoctors().clear();
                patient.setDoctors(doctorServices.getAllPathologist());
                patient.setCheck_out_date(LocalDate.now());
                System.out.println("Patient with ID " + idPat + " is dead...\n");
                patientServices.updatePatient(patient);
            }
        } else {
            System.out.println("Patient with this ID is not found");
        }

    }

    private void changeStatusOfTheDoctor() {
        System.out.println("Please, enter ID Doctor, who status is changed:\n");
        doctorServices.readAllDoctorsInfo();
        int idDoc = 0;
        String title = "";
        try {
            idDoc = Integer.parseInt(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Doctor doctor = doctorServices.getDoctorById(idDoc);
        if (doctor != null) {
            System.out.println("With status of this doctor now? Chose number from the list\n");
            System.out.println("""
                    *******************************************
                    "1": SICK
                    "2": HOME
                    "3": ON_OPERATION
                    default = free.
                    *******************************************
                        """);
            try {
                title = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (title) {
                case "1" -> doctor.setStatus(DoctorStatus.SICK);
                case "2" -> doctor.setStatus(DoctorStatus.HOME);
                case "3" -> doctor.setStatus(DoctorStatus.ON_OPERATION);
                default -> doctor.setStatus(DoctorStatus.FREE);
            }
            System.out.println("Doctor status is changed for " + doctor.getStatus());
            doctorServices.updateDoctorInfo(doctor);
        } else {
            System.out.println("Sorry, but doctor with this ID is not found...");
        }
    }

    private void dischargeAPatient() {
        System.out.println("Please, enter ID patient, who is healthy.");
        patientServices.readAllPatients();
        int idPat = 0;
        try {
            idPat = Integer.parseInt(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Patient patient = patientServices.getPatientFromId(idPat);
        if (patient != null) {
            if (patient.getStatus() == PatientStatus.DEAD) {
                System.out.println("I'm so sorry, but this patient is dead...");
            } else {
                patient.setStatus(PatientStatus.HEALTHY);
                patient.setRecipe(null);
                patient.getDoctors().clear();
                patient.setCheck_out_date(LocalDate.now());
                System.out.println("Patient with ID " + idPat + " is healthy, good luck, bye. <3");
                patientServices.updatePatient(patient);
            }
        } else {
            System.out.println("Patient with this ID is not found");
        }

    }

    private void inviteNewDoctor() {
        Doctor doctor = new Doctor();
        System.out.println("Write a last name of the doctor");
        String title = "";
        try {
            title = bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        doctor.setLast_name(title);
        System.out.println("What his profession, chose from the list: ");
        System.out.println("""
                *******************************************
                "1": THERAPIST
                "2": CARDIOLOGIST
                "3": SURGEON
                "4": INFECTIONIST
                *******************************************
                """);
        int prof = 0;
        try {
            prof = Integer.parseInt(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (prof) {
            case 1 -> doctor.setProfession(DoctorProfession.THERAPIST);
            case 2 -> doctor.setProfession(DoctorProfession.CARDIOLOGIST);
            case 3 -> doctor.setProfession(DoctorProfession.SURGEON);
            case 4 -> doctor.setProfession(DoctorProfession.INFECTIONIST);
            default -> doctor.setProfession(DoctorProfession.INTERN);
        }
        System.out.println("Welcome to the work, mr. " + doctor.getLast_name());
        doctorServices.saveDoctor(doctor);
    }

    private void createMedication() {
        String title = null;
        System.out.println("Please, chose type of medication");
        System.out.println("""
                ******************************************
                Enter number of medication type for chose:
                "1": PILLS
                "2": INJECTIONS
                "3": DROPPER                       
                ******************************************                        
                """);
        try {
            title = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Medication medication = new Medication();
        switch (Objects.requireNonNull(title)) {
            case "1" -> medication.setMedication_type(MedicationType.PILLS);
            case "2" -> medication.setMedication_type(MedicationType.INJECTIONS);
            case "3" -> medication.setMedication_type(MedicationType.DROPPER);
            default -> medication.setMedication_type(MedicationType.UNKNOWN);
        }
        System.out.println("Please, enter the name for your Medication");
        try {
            title = bufferedReader.readLine();
            medication.setName(title);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("And write count of this in number format e.g: 1, 10, 100");
        try {
            title = bufferedReader.readLine();
            short count = Short.parseShort(title);
            medication.setCount(count);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        medicationsServices.saveMedical(medication);
        System.out.println("Medication is saved" + medication);
    }

    private List<Medication> getAllMedication() {
        return medicationsServices.getAllMedication();
    }


    private void readRecipeFromPatient(Patient patient) {
        patientServices.readRecipeFromPatient(patient);
    }

    private Patient addNewPatient() {
        System.out.println("\nPlease enter the first name of the new patient:");
        String firstName = null;
        try {
            firstName = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nEnter his last name:");
        String lastName = null;
        try {
            lastName = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nPlease, chose his doctor number ID:\n");
        doctorServices.readAllDoctorsInfo();
        String address = null;
        // if Doctors not found, all go to the Doctor House.
        int docId = 1;
        try {
            address = bufferedReader.readLine();
            docId = Integer.parseInt(address);
        } catch (IOException e) {
            System.out.println("Wrong ID");
            e.printStackTrace();
        }
        Patient patient = new Patient(firstName, lastName);
        patient.getDoctors().add(doctorServices.getDoctorById(docId));
        patient = patientServices.savePatient(patient);
        System.out.println("\n You add new Patient:" + patient);
        return patient;
    }

    private void hospitalInit() {

        // create 5 doctors
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("House", DoctorProfession.THERAPIST));
        doctors.add(new Doctor("Burceva", DoctorProfession.CARDIOLOGIST));
        doctors.add(new Doctor("Saw", DoctorProfession.SURGEON));
        doctors.add(new Doctor("Plague Man", DoctorProfession.INFECTIONIST));
        doctors.add(new Doctor("Cannibal", DoctorProfession.PATHOLOGIST));
        doctors.forEach(x -> doctorServices.saveDoctor(x));

        System.out.println("Loading...");

        // create 10 medications
        List<Medication> medications = new ArrayList<>();
        medications.add(new Medication("Aspirin", (short) 100, MedicationType.PILLS));
        medications.add(new Medication("Prednizolon", (short) 10, MedicationType.PILLS));
        medications.add(new Medication("Kardiostimulin", (short) 20, MedicationType.PILLS));
        medications.add(new Medication("Ceftriocson", (short) 20, MedicationType.INJECTIONS));
        medications.add(new Medication("Diolipon", (short) 10, MedicationType.DROPPER));
        medications.add(new Medication("Kalcii Chlorid", (short) 10, MedicationType.INJECTIONS));
        medications.add(new Medication("Acyclovir", (short) 10, MedicationType.DROPPER));
        medications.add(new Medication("Lizin", (short) 10, MedicationType.DROPPER));
        medications.add(new Medication("Deksometathone", (short) 10, MedicationType.INJECTIONS));
        medications.add(new Medication("L-cet", (short) 10, MedicationType.PILLS));

        // add medications to the Recipe
        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(new Recipe("Take Aspirin 4 tablets daily, \n + Ceftriocson two injections per day"));
        recipeList.get(0).getMedications().add(medications.get(0));
        recipeList.get(0).getMedications().add(medications.get(3));
        recipeList.add(new Recipe("Take Prednizolon 1 tablets daily, \n + Acyclovir one dropper per day"));
        recipeList.get(1).getMedications().add(medications.get(1));
        recipeList.get(1).getMedications().add(medications.get(6));
        recipeList.add(new Recipe("Take Kardiostimulin 2 tablets daily, \n + Lizin one dropper per day"));
        recipeList.get(2).getMedications().add(medications.get(2));
        recipeList.get(2).getMedications().add(medications.get(7));
        recipeList.add(new Recipe("Take Diolipon one dropper daily, \n + L-cet one tablets per day"));
        recipeList.get(3).getMedications().add(medications.get(4));
        recipeList.get(3).getMedications().add(medications.get(9));
        recipeList.add(new Recipe("Take Kalcii Chlorid one injections daily, \n " +
                "+ Deksometathone one injections per day"));
        recipeList.get(4).getMedications().add(medications.get(5));
        recipeList.get(4).getMedications().add(medications.get(8));
        recipeList.forEach(recipe -> recipeServices.createRecipe(recipe));

        // add 5 patients
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient("Vitalii", "Rakhmail"));
        patients.get(0).setRecipe(recipeList.get(0));
        patients.get(0).getDoctors().add(doctors.get(0));
        patients.add(new Patient("Maksim", "Dushkow"));
        patients.get(1).setRecipe(recipeList.get(1));
        patients.get(1).getDoctors().add(doctors.get(1));
        patients.add(new Patient("Alisa", "Kosenko"));
        patients.get(2).setRecipe(recipeList.get(2));
        patients.get(2).getDoctors().add(doctors.get(2));
        patients.add(new Patient("Anastasia", "Perec"));
        patients.get(3).setRecipe(recipeList.get(3));
        patients.get(3).setStatus(PatientStatus.DEAD);
        patients.get(3).getDoctors().add(doctors.get(4));
        patients.add(new Patient("Vladislad", "Rakhmail"));
        patients.get(4).setRecipe(recipeList.get(4));
        patients.get(4).getDoctors().add(doctors.get(3));

        patients.forEach(patient -> patientServices.savePatient(patient));
    }


}
