

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

/**
 * This class allows the connection between the application and the SQLite Database.
 * The methods used establish connections used by the operations which manipulate character details.
 *
 * @author Morgan Lara
 */
public class CSDatabaseConnection
{
    private static String databaseURL;

    // Set the database path selected by the user
    public static void setDatabasePath(String filePath)
    {
        File databaseFile = new File(filePath);
        databaseURL = "jdbc:sqlite:" + databaseFile.getAbsolutePath();
    }

    // Connect to the selected database
    public static Connection connect() throws SQLException
    {

        if (databaseURL == null)
        {
            throw new SQLException("No database has been selected.");
        }
        return DriverManager.getConnection(databaseURL);
    }
}
