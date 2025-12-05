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
     * Updates an existing staff member.
     * Returns true if updated successfully, false if staff not found.
     */
    public boolean updateStaff(Staff updatedStaff) {
        // Verify staff exists
        Staff existing = staffRepo.findById(updatedStaff.getId());
        if (existing == null) {
            return false;
        }
        
        // Check if IC is being changed and if new IC already exists
        if (!existing.getIc().equals(updatedStaff.getIc())) {
            if (staffRepo.existsByIc(updatedStaff.getIc())) {
                return false; // New IC already exists
            }
        }
        
        return staffRepo.updateStaff(updatedStaff);
    }

    /**
     * Deletes a staff member by IC. Returns true if deleted.
     */
    public boolean deleteByIc(String staffIc) {
        return staffRepo.deleteByIc(staffIc);
    }

    /**
     * Deletes a staff member by ID. Returns true if deleted.
     */
    public boolean deleteById(int staffId) {
        return staffRepo.deleteById(staffId);
    }

    /**
     * Finds a staff member by ID.
     */
    public Staff findById(int staffId) {
        return staffRepo.findById(staffId);
    }

    /**
     * Finds a staff member by IC.
     */
    public Staff findByIc(String ic) {
        return staffRepo.findByIc(ic);
    }

    /**
     * Finds staff members by name (case-insensitive partial match).
     */
    public List<Staff> findByName(String name) {
        return staffRepo.findByName(name);
    }

    /**
     * Attempts to log in with given credentials.
     * Returns the Staff if found, otherwise null.
     */
    public Staff login(String ic, String password) {
        return staffRepo.findByCredentials(ic, password);
    }
}


