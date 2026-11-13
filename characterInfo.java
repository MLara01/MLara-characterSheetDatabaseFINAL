/**
 * The CharacterInfo class represents a single Tabletop Game Character.
 * The class allows the program to store the individual Character's ID, health, level, experience, and if
 *  the character has inspiration.
 * This class contains constructors, setters, and getters to allow the modification of these values.
 *
 * @author Morgan Lara
 */
public class characterInfo
{
        // Setting up variables
        private int charID;
        private String charName;
        private double charHealth;
        private int charLevel;
        private double charExp;
        private boolean charInspo;

    /**
     * Creates a character with the information input by the user.
     *
     * @param charID the unique character ID
     * @param charName the character's name
     * @param charHealth the character's starting health
     * @param charLevel the character's current level
     * @param charExp the character's experience points
     * @param charInspo the character's inspiration status
     */
        public characterInfo(int charID, String charName, double charHealth, int charLevel, double charExp, boolean charInspo)
        {
            this.charID = charID;
            this.charName = charName;
            this.charHealth = charHealth;
            this.charLevel = charLevel;
            this.charExp = charExp;
            this.charInspo = charInspo;
        }

    /**
     * Creates an empty character object.
     * Used when retrieving character information from the database and assigning its values afterward.
     */
        public characterInfo() {}

        // Getters
        public int getCharID() {return charID;}

        public String getCharName() {return charName;}

        public double getCharHealth() {return charHealth;}

        public int getCharLevel() {return charLevel;}

        public double getCharExp() {return charExp;}

        public boolean isCharInspo() {return charInspo;}

        // Setters
        public void setCharID(int charID) {this.charID = charID;}

        public void setCharName(String charName) {this.charName = charName;}

        public void setCharHealth(double charHealth) {this.charHealth = charHealth;}

        public void setCharLevel(int charLevel) {this.charLevel = charLevel;}

        public void setCharExp(double charExp) {this.charExp = charExp;}

        public void setCharInspo(boolean charInspo) {this.charInspo = charInspo;}

    }

