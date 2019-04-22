package laiho.tuni.fi.noteit;

/**
 * Note is a Object which holds information of the things a user wants to note down. It has a
 * description of the notes content as well as a randomly generated point value which is the amount
 * of points that's awarded to the user upon clearing the Note.
 *
 * @author Lauri Laiho
 * @version 1.0
 * @since 2019-04-07
 */
public class Note {

    /**
     * Description of the Note
     */
    private String description;

    /**
     * Amount of points awarded for clearing the Note
     */
    private int awardPoints;

    /**
     * Boolean whether the Note is cleared or not.
     */
    private boolean isCleared;

    /**
     * Constructor for Notes. Uses the given String as a Description.
     *
     * @param desc The given description for the Note
     */
    public Note(String desc) {
        setDescription(desc);
        setAwardPoints(generateRandomPoints(50, 200));
        setCleared(false);
    }

    /**
     * Constructor for Notes. Uses the given parameters to fully construct a full Note.
     *
     * @param desc The given description for the Note.
     * @param points The given points for clearing the Note.
     * @param cleared Whether or not the task is cleared or not.
     */
    public Note(String desc, int points, boolean cleared) {
        setDescription(desc);
        setAwardPoints(points);
        setCleared(cleared);
    }

    /**
     * Method generates a random value from the given range.
     *
     * @param min Generator range minimum.
     * @param max Generator range maximum.
     * @return int The generated number.
     */
    private int generateRandomPoints(int min, int max) {
        int result = min + (int)(Math.random() * ((max - min) + 1));
        return result;
    }

    /**
     * @return int Amount of points the note has.
     */
    public int getAwardPoints() {
        return awardPoints;
    }

    /**
     * @param awardPoints Amount of points to set.
     */
    public void setAwardPoints(int awardPoints) {
        this.awardPoints = awardPoints;
    }

    /**
     * @return String The description the Note has.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description of the Note
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return boolean Whether the Note has been cleared
     */
    public boolean isCleared() {
        return isCleared;
    }

    /**
     * @param cleared Setting the Note to be cleared or not.
     */
    public void setCleared(boolean cleared) {
        this.isCleared = cleared;
    }
}
