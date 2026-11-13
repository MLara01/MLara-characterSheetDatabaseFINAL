import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This class is primarily handling the manipulation of the Tabletop Characters' details.
 * This class performs the functions that the application requires such as adding a character to the database, removing
 *  a character from the database, updating the character information in the database, and calculating the average level
 *  of the characters currently stored within the database.
 *
 * @author Morgan Lara
 */
public class characterManip
{
    /**
     * This method adds a new character to the SQLite database.
     * Takes the supplied character object and inserts the input information into the characters table.
     *
     * @param character the character object containing the
     *                  information to be stored
     * @return true if the character was successfully added;
     *         false if the operation failed
     */
    public boolean addCharacter(characterInfo character)
    {

        String sql =
            """
            INSERT INTO characters
            (CharacterID, CharacterName, CharacterHealth, CharacterLevel, CharacterExperience, CharacterInspiration)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = CSDatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setInt(1, character.getCharID());
            stmt.setString(2, character.getCharName());
            stmt.setDouble(3, character.getCharHealth());
            stmt.setInt(4, character.getCharLevel());
            stmt.setDouble(5, character.getCharExp());
            stmt.setString(6, Boolean.toString(character.isCharInspo()));

            stmt.executeUpdate();

            System.out.println("Character added successfully.");
            return true;

        }
        catch (SQLException e)
        {
            System.out.println("Could not add character.");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * This method searches the database for a character using its ID and retrieves the character matching the ID.
     *
     * @param id the unique ID of the character being searched for
     * @return the matching characterInfo object, or null if the character could not be found
     */
    public characterInfo findCharacter(int id)
    {

        String sql =
            """
            SELECT * FROM characters
            WHERE CharacterID = ?
            """;

        try (Connection conn = CSDatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery())
            {

                if (rs.next())
                {

                    characterInfo character = new characterInfo();

                    character.setCharID(rs.getInt("CharacterID"));
                    character.setCharName(rs.getString("CharacterName"));
                    character.setCharHealth(rs.getDouble("CharacterHealth"));
                    character.setCharLevel(rs.getInt("CharacterLevel"));
                    character.setCharExp(rs.getDouble("CharacterExperience"));
                    character.setCharInspo(Boolean.parseBoolean(rs.getString("CharacterInspiration")));

                    return character;
                }
            }

        }
        catch (SQLException e)
        {
            System.out.println("Could not find character.");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method updates an existing character's information in the database.
     * Locates the original character using its existing ID and replaces its information with the values input
     *  by the user.
     * The character ID can also be changed if the new ID is valid and not already assigned to another character.
     *
     * @param oldID the character's original database ID
     * @param newID the new ID to assign to the character
     * @param name the updated character name
     * @param health the updated character health
     * @param level the updated character level
     * @param exp the updated character experience
     * @param inspo the updated inspiration status
     * @return true if the character was successfully updated;
     *         false if the update failed
     */
    public boolean updateCharacter(int oldID, int newID, String name, double health, int level, double exp, boolean inspo)
    {
        String sql =
        """
        UPDATE characters
        SET CharacterID = ?,
            CharacterName = ?,
            CharacterHealth = ?,
            CharacterLevel = ?,
            CharacterExperience = ?,
            CharacterInspiration = ?
        WHERE CharacterID = ?
        """;

        if (newID <= 0)
        {
            System.out.println("ID must be greater than zero.");
            return false;
        }

        characterInfo character = findCharacter(oldID);

        if (character == null)
        {
            System.out.println("Character not found.");
            return false;
        }

        String checkID = "SELECT CharacterID FROM characters WHERE CharacterID = ?";

        try (Connection conn = CSDatabaseConnection.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkID))
        {
            checkStmt.setInt(1, newID);

            try (ResultSet rs = checkStmt.executeQuery())
            {
                if (rs.next() && newID != oldID)
                {
                    System.out.println("That ID is already in use.");
                    return false;
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql))
            {

                stmt.setInt(1, newID);
                stmt.setString(2, name);
                stmt.setDouble(3, health);
                stmt.setInt(4, level);
                stmt.setDouble(5, exp);
                stmt.setString(6, Boolean.toString(inspo));
                stmt.setInt(7, oldID);

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0)
                {
                    System.out.println("Character updated successfully.");
                    return true;
                }
            }

        } catch (SQLException e)
        {
            System.out.println("Could not update character.");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * This method deletes a character from the database using its ID.
     *
     * @param id the unique ID of the character to delete
     * @return true if the character was successfully deleted;
     *         false if the character was not found or deletion failed
     */
    public boolean deleteCharacter(int id)
    {

        String sql =
            """
            DELETE FROM characters
            WHERE CharacterID = ?
            """;

        try (Connection conn = CSDatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0)
            {
                System.out.println("Character deleted successfully.");
                return true;
            }
            else
            {
                System.out.println("Character not found.");
            }

        }
        catch (SQLException e)
        {
            System.out.println("Could not delete character.");
            e.printStackTrace();
        }

        return false;
    }


    /**
     * This method retrieves all character records from the database.
     * This method also creates a list containing the character information retrieved from the characters table.
     *
     * @return a list of characterInfo objects; an empty list
     *         is returned if no characters are retrieved
     */
    public List<characterInfo> getCharacters()
    {

        List<characterInfo> characters = new ArrayList<>();

        String sql = "SELECT * FROM characters";

        try (Connection conn = CSDatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {

            while (rs.next())
            {
                characterInfo character = new characterInfo();

                character.setCharID(rs.getInt("CharacterID"));
                character.setCharName(rs.getString("CharacterName"));
                character.setCharHealth(rs.getDouble("CharacterHealth"));
                character.setCharLevel(rs.getInt("CharacterLevel"));
                character.setCharExp(rs.getDouble("CharacterExperience"));
                character.setCharInspo(Boolean.parseBoolean(rs.getString("CharacterInspiration")));

                characters.add(character);
            }

        }
        catch (SQLException e)
        {
            System.out.println("\nCould not retrieve characters.\n");
            e.printStackTrace();
        }

        return characters;
    }


    /**
     * This method allows the list of characters from getCharacters to be displayed accordingly to the GUI.
     */
    public void displayAllCharacters()
    {

        List<characterInfo> characters = getCharacters();

        if (characters.isEmpty())
        {
            System.out.println("No characters found.");
            return;
        }

        for (characterInfo character : characters)
        {

            System.out.println(
                    "ID: " + character.getCharID() + " | Name: " + character.getCharName()
                            + " | Health: " + character.getCharHealth() + " | Level: " + character.getCharLevel()
                            + " | Experience: " + character.getCharExp() + " | Inspiration: " + character.isCharInspo()
            );
        }
    }

    /**
     * This method calculates the average level of all characters currently stored in the database.
     *
     * @return the average character level, or 0.0 if the
     *         calculation fails or the database contains no records
     */
    public double averageLevel()
    {

        String sql = "SELECT AVG(CharacterLevel) FROM characters";

        try (Connection conn = CSDatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {

            if (rs.next())
            {
                return rs.getDouble(1);
            }

        }
        catch (SQLException e)
        {
            System.out.println("Could not calculate average level.");
            e.printStackTrace();
        }

        return 0.0;
    }

}
