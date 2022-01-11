package models;

public class Schedule {
    /*
    The models.Schedule class is used for storing the event start and end time as a configuration
     */
    private int[][] configSchedule;

    public Schedule(int[][] schedule){
        this.configSchedule = schedule;
    }

    public int[][] getConfigSchedule() {
        return configSchedule;
    }

    public void setConfigSchedule(int[][] configSchedule) {
        this.configSchedule = configSchedule;
    }
}
