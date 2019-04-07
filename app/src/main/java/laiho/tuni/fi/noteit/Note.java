package laiho.tuni.fi.noteit;

public class Note {
    private int id;
    private String description;
    private int awardPoints;
    private boolean isCleared;

    public Note() {

    }

    public Note(int id, String desc, int points, boolean cleared) {
        setId(id);
        setDescription(desc);
        setAwardPoints(points);
        setCleared(cleared);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
