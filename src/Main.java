import models.Acts;
import models.Event;
import models.Schedule;
import servicesImpl.OutputsImpl;
import servicesImpl.UtilsImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    /**
     * The overall time complexity is o(n^2), which is calculated by
     * n - the number of different combinations of events for 2 stages.
     * k - the number of events inside one combination(one solution).

     The program will be executed in the following order
     1. Take the system input and convert it into acts list -> [act lengths, act title],
     like this:

     [["30", "Bob Dylan & His Band"], ["45", "Post Malone"], ["60", "Mariya Takeuchi"] ...]

     2. DFS fill in plans, based on schedule configuration:
     int[][] ranges = {{9, 180, 180}, {1, 180, 240}, {9, 180, 180}, {1, 180, 240}};

     3. Print out the plan results.
     As Plans for 2 stages will be stored like this
     [List[Morning], List[Afternoon], List[Morning], List[Afternoon]]

     */

    // absolute file path of test file
    static String filename = "testcases/test.txt";

    // The schedule we would like to fill in.
    // {1, 180, 240} - the config info of a schedule time period
    // while means 1 - 1:00PM;
    // 180, 240 - the potential time period, the event may finish in 3hr to 4hr
    static int[][] ranges = {{9, 180, 180}, {13, 180, 240}, {9, 180, 180}, {13, 180, 240}};

    public static void main(String[] args){

        UtilsImpl utils = new UtilsImpl();
        Schedule schedule = new Schedule(ranges); // The models.Schedule class is used for storing the event start and end time as a configuration

        // Inputs from test file
        List<Acts> acts = utils.group_acts(filename);

        // defaultEventNames map will look like this
        // 9 : "12:00PM Lunch"
        // 13 : "05:00PM Main Models.Event"
        // This hashmap will be a part of default configuration data as what it requires in assignment doc
        Map<Integer, String> defaultEventNames = new HashMap<>();
        defaultEventNames.put(9, "12:00PM Lunch");
        defaultEventNames.put(13, "05:00PM Main Event");

        // current a new event with details of event requirements
        Event event = new Event(schedule, acts, defaultEventNames);
        event.fill_acts();

        // print out event details
        OutputsImpl outputs = new OutputsImpl();
        outputs.printOutPlans(event,schedule.getConfigSchedule());
    }
}
