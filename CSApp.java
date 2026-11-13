/*
Name: Morgan Lara
This project is a tabletop RPG character database where users can create a character, remove a character
from the database, update a character's information, display all saved characters, and return the average level of all
stored characters. The program will be run using a Java Swing GUI to allow the user to manipulate the project and allows
for database connections using SQLite.
*/

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CSApp
{

    public static void main(String[] args)
    {

        SwingUtilities.invokeLater(() ->
        {
            // Create file chooser
            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setDialogTitle("Select Your Character Database");

            // Only display SQLite database files
            FileNameExtensionFilter filter =
                    new FileNameExtensionFilter("SQLite Database (*.db, *.sqlite, *.sqlite3)",
                            "db", "sqlite", "sqlite3");

            fileChooser.setFileFilter(filter);

            // Ask user to select database
            int result = fileChooser.showOpenDialog(null);

            // Check whether user selected a file
            if (result == JFileChooser.APPROVE_OPTION)
            {

                File selectedFile = fileChooser.getSelectedFile();

                // Set selected database path
                CSDatabaseConnection.setDatabasePath(selectedFile.getAbsolutePath());

                // Test database connection and table
                try (Connection conn = CSDatabaseConnection.connect();
                     Statement stmt = conn.createStatement())
                {

                    stmt.executeQuery("SELECT 1 FROM characters LIMIT 1");
                    JOptionPane.showMessageDialog(null, "Database connected successfully!");

                    // Launch main application
                    characterAppGUI frame = new characterAppGUI();
                    frame.setVisible(true);

                }
                catch (SQLException e)
                {
                    JOptionPane.showMessageDialog(null, "Could not connect to the selected database.\n"
                                    + "Make sure it contains a characters table.\n\n" + e.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
            {

                JOptionPane.showMessageDialog(null, "No database selected. Closing Application.");
                System.exit(0);
            }
        });
    }
}