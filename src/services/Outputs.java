package services;

import models.Acts;
import models.Event;

import java.util.List;

public interface Outputs {

    void printOutPlans(Event event, int[][] ranges);
    void printLines(List<Acts> group, int start);
    void formatTime(int mins, int hour, String name, int len);
}
