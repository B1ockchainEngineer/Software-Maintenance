package assignment.repo;

import assignment.model.GoldMember;
import assignment.model.Membership;
import assignment.model.NormalMember;
import assignment.model.PremiumMember;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository responsible for all file I/O for member data (members.txt).
 */
public class MemberRepository {

    private static final String MEMBER_FILE_PATH = "members.txt";
    private static final Logger LOGGER = Logger.getLogger(MemberRepository.class.getName());

    private void ensureFileExists() {
        File file = new File(MEMBER_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error creating member file", e);
            }
        }
    }

    /**
     * Loads all members from the file into a list of Membership instances.
     */
    public List<Membership> loadAllMembers() {
        ensureFileExists();
        List<Membership> members = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(MEMBER_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");

                if (parts.length >= 6) {
                    // memberName and memberIC wrong position
                    String memberName = parts[0];
                    String memberIC = parts[1];
                    String memberHP = parts[2];
                    int memberId = Integer.parseInt(parts[3]);
                    String membershipType = parts[4];
                    double discountRate = Double.parseDouble(parts[5]);

                    Membership member = switch (membershipType) {
                        case "Normal" -> new NormalMember(memberName, memberIC, memberId, memberHP, membershipType, discountRate);
                        case "Gold" -> new GoldMember(memberName, memberIC, memberId, memberHP, membershipType, discountRate);
                        case "Premium" -> new PremiumMember(memberName, memberIC, memberId, memberHP, membershipType, discountRate);
                        default -> null;
                    };

                    if (member != null) {
                        members.add(member);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading members file", e);
        }

        return members;
    }

    /**
     * Appends a new member record to the file.
     */
    public void appendMember(Membership member) {
        ensureFileExists();

        try (FileWriter writer = new FileWriter(MEMBER_FILE_PATH, true)) {
            writer.write(member.getName() + "\t");
            writer.write(member.getIc() + "\t");
            writer.write(member.getMemberHp() + "\t");
            writer.write(member.getId() + "\t");
            writer.write(member.getMemberType() + "\t");
            writer.write(member.calDiscount() + "\t");
            writer.write("\n");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing member record", e);
        }
    }

    /**
     * Deletes a member by ID, returns true if a record was removed.
     */
    public boolean deleteById(int memberIdToDelete) {
        ensureFileExists();
        File inputFile = new File(MEMBER_FILE_PATH);
        File tempFile = new File("dltTemp.txt");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");

                if (data.length >= 4) {
                    int dltMemberId = Integer.parseInt(data[3]);
                    if (dltMemberId == memberIdToDelete) {
                        found = true;
                        continue; // skip this record
                    }
                }

                writer.write(line + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deleting member", e);
            return false;
        }

        if (found) {
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                LOGGER.severe("Error finalizing member deletion.");
            }
        } else {
            tempFile.delete();
        }
        return found;
    }

    /**
     * Checks whether a member IC already exists in the file.
     */
    public boolean existsByIc(String targetIC) {
        ensureFileExists();

        try (BufferedReader br = new BufferedReader(new FileReader(MEMBER_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 2 && parts[1].equals(targetIC)) {
                    return true;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading members file", e);
        }
        return false;
    }
}


