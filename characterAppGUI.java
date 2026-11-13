import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;

/**
 * This class provides the graphical user interface (or GUI) for the database application.
 * This allows the user to add characters, remove characters, update character details, and calculating the average
 *  character level through a Java Swing based user interface.
 * This class also allows the user to manually select characters from the table by clicking on the entry within the table.
 *
 * @author Morgan Lara
 */
public class characterAppGUI extends JFrame
{
    // Defines the different fields for the application
    private characterManip manager;

    private JTextField characterIDField;
    private JTextField characterNameField;
    private JTextField healthField;
    private JTextField levelField;
    private JTextField expField;
    private JCheckBox inspoCheckBox;
    private int originalCharacterID = -1;

    private DefaultTableModel tableModel;
    private JTable characterTable;


    // ** THE FOLLOWING METHODS ARE USED TO SET UP THE APPLICATION **

    /**
     * This method initializes the application and sets the details for the GUI window for the application.
     */
    public characterAppGUI()
    {
        manager = new characterManip();

        setTitle("Tabletop Character Manager");
        setSize(850, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Using BorderLayout to organize the window
        setLayout(new BorderLayout(10, 10));

        createCharacterForm();
        createCharacterTable();
        createButtons();

        refreshTable();
    }

    private void createCharacterForm()
    {

        // Character Text Fields
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Character Information"));

        characterIDField = new JTextField();
        characterNameField = new JTextField();
        healthField = new JTextField();
        levelField = new JTextField();
        expField = new JTextField();
        inspoCheckBox = new JCheckBox("Has Inspiration");

        formPanel.add(new JLabel("Character ID:"));
        formPanel.add(characterIDField);

        formPanel.add(new JLabel("Character Name:"));
        formPanel.add(characterNameField);

        formPanel.add(new JLabel("Health:"));
        formPanel.add(healthField);

        formPanel.add(new JLabel("Level:"));
        formPanel.add(levelField);

        formPanel.add(new JLabel("Experience:"));
        formPanel.add(expField);

        formPanel.add(new JLabel("Inspiration:"));
        formPanel.add(inspoCheckBox);

        add(formPanel, BorderLayout.NORTH);
    }

    private void createCharacterTable()
    {
        String[] columns = {"ID", "Name", "Health", "Level", "Experience", "Inspiration"};

        tableModel = new DefaultTableModel(columns, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        characterTable = new JTable(tableModel);

        // ** Customizing the table colors and sizes **

        // Custom Column Header color
        characterTable.getTableHeader().setBackground(Color.gray);

        // Custom Column Header Text Color
        characterTable.getTableHeader().setForeground(Color.lightGray);

        // Customizing the Columns

        // Custom Character ID Column Size
        characterTable.getColumnModel().getColumn(0).setMaxWidth(60);
        // Custom Name Column Size
        characterTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        // Custom Health Column Size
        characterTable.getColumnModel().getColumn(2).setPreferredWidth(90);
        // Custom Level Column Size
        characterTable.getColumnModel().getColumn(3).setMaxWidth(60);
        // Custom Experience Column Size
        characterTable.getColumnModel().getColumn(4).setPreferredWidth(110);
        // Custom Inspiration Column Size
        characterTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(characterTable);

        add(scrollPane, BorderLayout.CENTER);

        characterTable.addMouseListener(new java.awt.event.MouseAdapter()
        {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e)
            {
                int selectedRow = characterTable.getSelectedRow();

                if (selectedRow != -1)
                {
                    loadSelectedCharacter();
                }
            }
        }
        );
    }

    private void createButtons()
    {

        JPanel buttonPanel = new JPanel(new GridLayout(0,3,5,5));

        // Sets the button text
        JButton addButton = new JButton("Add Character");
        JButton deleteButton = new JButton("Delete Selected Character");
        JButton updateButton = new JButton("Update Character Details");
        JButton refreshButton = new JButton("Refresh The Table");
        JButton averageButton = new JButton("Calculate Average Character Level");
        JButton exitButton = new JButton("Exit Program");

        // Sets the button colors
        addButton.setBackground(Color.green);
        deleteButton.setBackground(Color.pink);
        updateButton.setBackground(Color.YELLOW);
        refreshButton.setBackground(Color.cyan);
        averageButton.setBackground(Color.magenta);
        exitButton.setBackground(Color.red);

        // Sets button listeners
        addButton.addActionListener(e -> addCharacter());
        deleteButton.addActionListener(e -> deleteCharacter());
        refreshButton.addActionListener(e -> refreshTable());
        updateButton.addActionListener(e -> updateCharacter());

        // Calculate and display the average only when button is clicked
        averageButton.addActionListener(e -> showAverageLevel());

        // Exits the program when the user presses the exit button
        exitButton.addActionListener(e ->
        {

            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }

        });
        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(averageButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // ** THE FOLLOWING METHODS ARE USED TO DISPLAY THE CRUD METHODS **

    // Allows the user to load characters from a pre-existing text file
    private void loadCharactersFromFile()
    {

        // Create a window for choosing a file
        JFileChooser fileChooser = new JFileChooser();

        // Open the file selection window
        int result = fileChooser.showOpenDialog(this);

        // Continue only if the user selects a file
        if (result == JFileChooser.APPROVE_OPTION)
        {
            // Get the selected file's full path
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Update the table to show the loaded characters
            refreshTable();

            JOptionPane.showMessageDialog(this, "File selected: " + fileChooser.getSelectedFile().getName() +
                            "\nLoading finished.", "Load Characters", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Allows the user to add a character to the stored list
    private void addCharacter()
    {
        try
        {
            int id = Integer.parseInt(characterIDField.getText().trim());
            String name = characterNameField.getText().trim();
            double health = Double.parseDouble(healthField.getText().trim());
            int level = Integer.parseInt(levelField.getText().trim());
            double exp = Double.parseDouble(expField.getText().trim());
            boolean inspo = inspoCheckBox.isSelected();

            if (id <= 0 || name.isEmpty() || !Double.isFinite(health) || health < 0 || level < 1
                    || !Double.isFinite(exp) || exp < 0)
            {

                JOptionPane.showMessageDialog(this, "Please enter valid character information.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            characterInfo character = new characterInfo(id, name, health, level, exp, inspo);

            if (manager.addCharacter(character))
            {
                JOptionPane.showMessageDialog(this, "Character added successfully!");

                clearForm();
                refreshTable();

            }
            else
            {
                JOptionPane.showMessageDialog(this, "Character could not be added. Check if the ID already exists.");
            }

        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this,
                    "ID and level must be whole numbers. Health and experience must be valid numbers.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Provides the user with the option to delete a selected character
    private void deleteCharacter()
    {

        int selectedRow = characterTable.getSelectedRow();

        if (selectedRow == -1)
        {
            JOptionPane.showMessageDialog(this, "Please select a character from the table.");
            return;
        }

        int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

        int answer = JOptionPane.showConfirmDialog(this, "Delete character with selected ID " + id + "?",
                "Confirm deletion of character", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION)
        {

            if (manager.deleteCharacter(id))
            {
                JOptionPane.showMessageDialog(this, "Character deleted successfully.");
                refreshTable();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Character could not be deleted.");
            }
        }
    }

    // Loads the selected character's details
    private void loadSelectedCharacter()
    {

        int selectedRow = characterTable.getSelectedRow();

        if (selectedRow == -1)
        {
            return;
        }

        // Convert selected row to the table model row
        int row = characterTable.convertRowIndexToModel(selectedRow);

        // This stores the original ID for the update operation
        originalCharacterID = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

        // Populate the form
        characterIDField.setText(tableModel.getValueAt(row, 0).toString());

        characterNameField.setText(tableModel.getValueAt(row, 1).toString());

        healthField.setText(tableModel.getValueAt(row, 2).toString());

        levelField.setText(tableModel.getValueAt(row, 3).toString());

        expField.setText(tableModel.getValueAt(row, 4).toString());

        inspoCheckBox.setSelected(Boolean.parseBoolean(tableModel.getValueAt(row, 5).toString()));
    }

    // Once the selected character is loaded and the user inputs new data, this overwrites the old data
    private void updateCharacter()
    {
        if (originalCharacterID == -1)
        {
            JOptionPane.showMessageDialog(this, "Please select a character to update first.");
            return;
        }

        try
        {
            int newID = Integer.parseInt(characterIDField.getText().trim());
            String name = characterNameField.getText().trim();
            double health = Double.parseDouble(healthField.getText().trim());
            int level = Integer.parseInt(levelField.getText().trim());
            double exp = Double.parseDouble(expField.getText().trim());
            boolean inspo = inspoCheckBox.isSelected();

            boolean success = manager.updateCharacter(originalCharacterID, newID, name, health, level, exp, inspo);

            if (success)
            {
                JOptionPane.showMessageDialog(this, "Character updated successfully!");

                originalCharacterID = -1;
                clearForm();
                refreshTable();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Update failed. Please check the character's ID and information.");
            }

        } catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID, health, level, and experience.");
        }
    }

    // Allows the user to calculate the average level of all stored characters once the user clicks the button
    private void showAverageLevel()
    {
        double average = manager.averageLevel();

        JOptionPane.showMessageDialog(this, String.format("Average Character Level: %.2f", average),
                "Character Level Average", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable()
    {

        tableModel.setRowCount(0);

        for (characterInfo character : manager.getCharacters())
        {
            Object[] row =
                    {
                            character.getCharID(), character.getCharName(), character.getCharHealth(),
                            character.getCharLevel(), character.getCharExp(), character.isCharInspo()
                    };
            tableModel.addRow(row);
        }
    }

    // This method is called to set the fields to be blank after each manipulation method used
    private void clearForm()
    {
        characterIDField.setText("");
        characterNameField.setText("");
        healthField.setText("");
        levelField.setText("");
        expField.setText("");
        inspoCheckBox.setSelected(false);
    }
}