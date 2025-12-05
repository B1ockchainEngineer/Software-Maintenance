package assignment.repo;

import assignment.model.Staff;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository responsible for all file I/O for staff data (staff.txt).
 */
public class StaffRepository {

    private static final String STAFF_FILE_PATH = "staff.txt";
    private static final Logger LOGGER = Logger.getLogger(StaffRepository.class.getName());

    private void ensureFileExists() {
        File file = new File(STAFF_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error creating staff file", e);
            }
        }
    }

    /**
     * Loads all staff records from the file into a list.
     */
    public List<Staff> loadAllStaff() {
        ensureFileExists();
        List<Staff> staffList = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(STAFF_FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\t");

                if (parts.length >= 6) {
                    int id = Integer.parseInt(parts[0]); // staff ID
                    String ic = parts[1];
                    String name = parts[2];
                    String password = parts[3];
                    int age = Integer.parseInt(parts[4]);
                    double salary = Double.parseDouble(parts[5]);

                    Staff staff = new Staff(name, ic, age, salary, password);
                    staff.setId(id);
                    staffList.add(staff);
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error reading staff file", e);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing staff data", e);
        }

        return staffList;
    }

    /**
     * Appends a new staff record to the file.
     */
    public void appendStaff(Staff staff) {
        ensureFileExists();

        try (FileWriter writer = new FileWriter(STAFF_FILE_PATH, true)) {
            writer.write(staff.getId() + "\t");
            writer.write(staff.getIc() + "\t");
            writer.write(staff.getName() + "\t");
            writer.write(staff.getStfPassword() + "\t");
            writer.write(staff.getStfAge() + "\t");
            writer.write(staff.getStfSalary() + "\t");
            writer.write("\n");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing staff record", e);
        }
    }

    /**
     * Deletes a staff record by IC, returns true if a record was removed.
     */
    public boolean deleteByIc(String targetIc) {
        ensureFileExists();
        File inputFile = new File(STAFF_FILE_PATH);
        File tempFile = new File("dltStaffTemp.txt");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] staffDetails = line.split("\t");
                if (staffDetails.length >= 2) {
                    String staffIC = staffDetails[1];
                    if (staffIC.equals(targetIc)) {
                        found = true;
                        continue; // skip this record
                    }
                }
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deleting staff", e);
            return false;
        }

        if (found) {
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                LOGGER.severe("Error finalizing staff deletion.");
            }
        } else {
            tempFile.delete();
        }
        return found;
    }

    /**
     * Checks whether a staff IC already exists in the file.
     */
    public boolean existsByIc(String targetIc) {
        ensureFileExists();

        try (BufferedReader br = new BufferedReader(new FileReader(STAFF_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 2 && parts[1].equals(targetIc)) {
                    return true;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading staff file", e);
        }
        return false;
    }

    /**
     * Finds a staff member by ID.
     */
    public Staff findById(int staffId) {
        for (Staff staff : loadAllStaff()) {
            if (staff.getId() == staffId) {
                return staff;
            }
        }
        return null;
    }

    /**
<<<<<<< HEAD
     * Finds a staff member by IC.
     */
    public Staff findByIc(String ic) {
        for (Staff staff : loadAllStaff()) {
            if (staff.getIc().equals(ic)) {
                return staff;
            }
        }
        return null;
    }

    /**
     * Finds staff members by name (case-insensitive partial match).
     */
    public List<Staff> findByName(String name) {
        List<Staff> results = new ArrayList<>();
        String searchName = name.toUpperCase().trim();
        for (Staff staff : loadAllStaff()) {
            if (staff.getName().toUpperCase().contains(searchName)) {
                results.add(staff);
            }
        }
        return results;
    }

    /**
     * Updates a staff record. Returns true if updated successfully.
     */
    public boolean updateStaff(Staff updatedStaff) {
        ensureFileExists();
        File inputFile = new File(STAFF_FILE_PATH);
        File tempFile = new File("updateStaffTemp.txt");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] staffDetails = line.split("\t");
                if (staffDetails.length >= 1) {
                    try {
                        int id = Integer.parseInt(staffDetails[0]);
                        if (id == updatedStaff.getId()) {
                            // Write updated staff record
                            writer.write(updatedStaff.getId() + "\t");
                            writer.write(updatedStaff.getIc() + "\t");
                            writer.write(updatedStaff.getName() + "\t");
                            writer.write(updatedStaff.getStfPassword() + "\t");
                            writer.write(updatedStaff.getStfAge() + "\t");
                            writer.write(updatedStaff.getStfSalary() + "\t");
                            writer.write("\n");
                            found = true;
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        // Invalid ID format, keep original line
                    }
                }
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error updating staff", e);
            return false;
        }

        if (found) {
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                LOGGER.severe("Error finalizing staff update.");
                return false;
            }
        } else {
            tempFile.delete();
        }
        return found;
    }

    /**
     * Deletes a staff record by ID, returns true if a record was removed.
     */
    public boolean deleteById(int staffId) {
        ensureFileExists();
        File inputFile = new File(STAFF_FILE_PATH);
        File tempFile = new File("dltStaffTemp.txt");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] staffDetails = line.split("\t");
                if (staffDetails.length >= 1) {
                    try {
                        int id = Integer.parseInt(staffDetails[0]);
                        if (id == staffId) {
                            found = true;
                            continue; // skip this record
                        }
                    } catch (NumberFormatException e) {
                        // Invalid ID format, skip this line
                    }
                }
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deleting staff by ID", e);
            return false;
        }

        if (found) {
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                LOGGER.severe("Error finalizing staff deletion by ID.");
            }
        } else {
            tempFile.delete();
        }
        return found;
    }

    /**
=======
>>>>>>> 93b2678c1e7167896ea46aa3955f0958e6d6ea66
     * Finds a staff by IC and password (for login).
     */
    public Staff findByCredentials(String stfIc, String stfPassword) {
        ensureFileExists();

        try (Scanner sc = new Scanner(new File(STAFF_FILE_PATH))) {
            while (sc.hasNextLine()) {
                String stfLine = sc.nextLine();
                String[] stfDtls = stfLine.split("\t");
                if (stfDtls.length >= 4) {
                    String staffIC = stfDtls[1];
                    String staffPassword = stfDtls[3];

                    if (stfIc.equals(staffIC) && stfPassword.equals(staffPassword)) {
                        int id = Integer.parseInt(stfDtls[0]);
                        String name = stfDtls[2];
                        int age = Integer.parseInt(stfDtls[4]);
                        double salary = Double.parseDouble(stfDtls[5]);
                        Staff staff = new Staff(name, staffIC, age, salary, staffPassword);
                        staff.setId(id);
                        return staff;
                    }
                }
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error reading staff credentials", e);
        }
        return null;
    }
}


