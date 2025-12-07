package assignment.repo;

import assignment.model.GoldMember;
import assignment.model.Membership;
import assignment.model.NormalMember;
import assignment.model.PremiumMember;
import assignment.util.MemberConfig;

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


    private static final Logger LOGGER = Logger.getLogger(MemberRepository.class.getName());

    /**
     * Checks if the file exists.
     * Creates a new file if it does not exist.
     */
    private void ensureFileExists() {
        File file = new File(MemberConfig.MEMBER_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, MemberConfig.ErrorMessage.FILE_CREATE_ERROR, e);
            }
        }
    }

    /**
     * Reads all members from the text file.
     * Returns a list of Membership objects.
     */
    public List<Membership> loadAllMembers() {
        ensureFileExists();
        List<Membership> members = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(MemberConfig.MEMBER_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");

                if (parts.length >= 5) {
                    // memberName and memberIC wrong position
                    String memberName = parts[0];
                    String memberIC = parts[1];
                    String memberHP = parts[2];
                    int memberId = Integer.parseInt(parts[3]);
                    String membershipType = parts[4];

                    Membership member = switch (membershipType) {
                        case MemberConfig.MEMBER_TYPE_NORMAL -> new NormalMember(memberName, memberIC, memberId, memberHP, membershipType);
                        case MemberConfig.MEMBER_TYPE_GOLD -> new GoldMember(memberName, memberIC, memberId, memberHP, membershipType);
                        case MemberConfig.MEMBER_TYPE_PREMIUM -> new PremiumMember(memberName, memberIC, memberId, memberHP, membershipType);
                        default -> null;
                    };

                    if (member != null) {
                        members.add(member);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, MemberConfig.ErrorMessage.FILE_READ_ERROR, e);
        }

        return members;
    }

    /**
     * Adds a new member to the end of the file.
     */
    public void appendMember(Membership member) {
        ensureFileExists();

        try (FileWriter writer = new FileWriter(MemberConfig.MEMBER_FILE_PATH, true)) {
            writer.write(member.getName() + "\t");
            writer.write(member.getIc() + "\t");
            writer.write(member.getMemberHp() + "\t");
            writer.write(member.getId() + "\t");
            writer.write(member.getMemberType() + "\t");
            //Deleted discount rate, take it by reading its type
            writer.write("\n");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, MemberConfig.ErrorMessage.FILE_WRITE_ERROR, e);
        }
    }

    /**
     * Saves the list of members to the file.
     * This overwrites all existing data in the file.
     */

    public void saveAllMembers(List<Membership> members) {
        ensureFileExists();

        try (FileWriter fw = new FileWriter(MemberConfig.MEMBER_FILE_PATH, false)) { // overwrite file
            for (Membership member : members) {
                String line =
                        member.getName() + "\t" +
                                member.getIc() + "\t" +
                                member.getMemberHp() + "\t" +
                                member.getId() + "\t" +
                                member.getMemberType() + "\t" +
                                System.lineSeparator();

                fw.write(line);
            }
        } catch (IOException e) {
            System.out.println(MemberConfig.ErrorMessage.SAVE_MEMBERS_FAILED_TEMPLATE + e.getMessage());
        }
    }


    /**
     * Deletes a member from the file by their ID.
     * Returns true if the member was deleted.
     */
    public boolean deleteById(int memberIdToDelete) {
        ensureFileExists();
        File inputFile = new File(MemberConfig.MEMBER_FILE_PATH);
        File tempFile = new File(MemberConfig.TEMP_DELETE_FILE_PATH);

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

                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, MemberConfig.ErrorMessage.FILE_DELETE_ERROR, e);
            return false;
        }

        if (found) {
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                LOGGER.severe(MemberConfig.ErrorMessage.FILE_DELETE_ERROR);
            }
        } else {
            tempFile.delete();
        }
        return found;
    }

    /**
     * Checks if an IC number is already in the file.
     * Returns true if found, false otherwise.
     */
    public boolean existsByIc(String targetIC) {
        ensureFileExists();

        try (BufferedReader br = new BufferedReader(new FileReader(MemberConfig.MEMBER_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts[1].equals(targetIC)) {
                    return true;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, MemberConfig.ErrorMessage.FILE_READ_ERROR, e);
        }
        return false;
    }
}


