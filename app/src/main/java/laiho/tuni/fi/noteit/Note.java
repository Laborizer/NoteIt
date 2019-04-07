package laiho.tuni.fi.noteit;

public class Note {
    private String description;
    private int awardPoints;
    private boolean isCleared;

    public Note(String desc) {
        setDescription(desc);
        setAwardPoints(generateRandomPoints(50, 200));
        setCleared(false);
    }

    public Note(String desc, int points, boolean cleared) {
        setDescription(desc);
        setAwardPoints(points);
        setCleared(cleared);
    }

    private int generateRandomPoints(int min, int max) {
        int result = min + (int)(Math.random() * ((max - min) + 1));
        return result;
    }

    public int getAwardPoints() {
        return awardPoints;
    }

    public void setAwardPoints(int awardPoints) {
        this.awardPoints = awardPoints;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCleared() {
        return isCleared;
    }

    public void setCleared(boolean cleared) {
        this.isCleared = cleared;
    }
}
