package assignment.service;

import assignment.model.Staff;
import assignment.repo.StaffRepository;
import java.util.List;

/**
 * Service layer for Staff domain.
 * Encapsulates business rules on top of StaffRepository.
 */
public class StaffService {

    private final StaffRepository staffRepo;

    public StaffService(StaffRepository staffRepo) {
        this.staffRepo = staffRepo;
    }

    /**
     * Returns all staff records.
     */
    public List<Staff> getAllStaff() {
        return staffRepo.loadAllStaff();
    }

    /**
     * Adds a new staff member if IC is unique.
     * Returns true if added, false if IC already exists.
     */
    public boolean addStaff(Staff newStaff) {
        if (staffRepo.existsByIc(newStaff.getStfIC())) {
            return false;
        }
        staffRepo.appendStaff(newStaff);
        return true;
    }

    /**
     * Deletes a staff member by IC. Returns true if deleted.
     */
    public boolean deleteByIc(String staffIc) {
        return staffRepo.deleteByIc(staffIc);
    }

    /**
     * Finds a staff member by ID.
     */
    public Staff findById(int staffId) {
        return staffRepo.findById(staffId);
    }

    /**
     * Attempts to log in with given credentials.
     * Returns the Staff if found, otherwise null.
     */
    public Staff login(String ic, String password) {
        return staffRepo.findByCredentials(ic, password);
    }
}


