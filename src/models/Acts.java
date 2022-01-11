package models;

public class Acts {
    /*
    The models.Acts class is used to store details of each act.
     */
    private String name;
    private int duration;

    public Acts(String name, int duration){
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
