package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Event {
    /*
    The models.Event class is the object which stores the info of the entire Fuji Rock Festival
     */

    private int stageNum; // the number of stages that the program needs to fill in. For example, if there are morning and afternoon stages in one day, we consider this as 2 stages.
    private List<Stages> plans; //The event details which are found by program
    private Schedule schedule; // The schedule we would like to fill in as a configuration
    private List<Acts> acts; // The Models.Acts information from the file
    private Map<Integer, String> defaultEventNames;

    public Event(Schedule schedule, List<Acts> acts, Map<Integer, String> defaultEventNames){
        this.plans = new ArrayList<>();
        this.schedule = schedule;
        this.acts = acts;
        this.stageNum = schedule.getConfigSchedule().length;
        this.defaultEventNames = defaultEventNames;
    }

    public void fill_acts(){
        /*
        Fill in list of Models.Acts into plans
        @return: null
         */

        // The same act will be only used once (avoid duplication)
        boolean[] used = new boolean[acts.size()]; // The array which stores the Models.Acts we already used
        int[][] ranges = this.schedule.getConfigSchedule();

        // normal DFS to find suitable event schedule
        fill_one_plan(this.acts, 0, ranges, 0, new ArrayList<Acts>(), used, 0);
    }

    // DFS
    public void fill_one_plan(List<Acts> acts, int time, int[][] ranges, int start, List<Acts> group, boolean[] used, int j){
        /*
        DFS look into the Models.Acts information and fill in one stage

        @param: acts   The Models.Acts information from the file as an input
        @param: time   The current plan length for a group of Models.Acts which we already fill in
        @param: ranges   The models.Schedule info we get as a configuration
        @param: start    The current index of models.Schedule range array which we are trying to fill in
        @param: group   The current group of Models.Acts which we already fill in
        @param: used    The array which stores the Models.Acts we already used
        @param: j    The start index of Models.Acts in the list
        @return: null
         */

        // if time is longer than schedule OR start position is longer than ranges OR plans is already filled in, then return
        if (start >= this.stageNum || time > ranges[start][1] || this.plans.size() >= this.stageNum) return;

        // if the length of group acts meet the requirements in config, then it means we find a valid group, then we add it into plans
        if (ranges[start][1] <= time && time <= ranges[start][2]){
            this.plans.add(new Stages(group));
            fill_one_plan(acts, 0, ranges,start+1, new ArrayList<Acts>(), used, 0);
            return;
        }

        // we put one act in group, and in DFS find a valid group
        for (int i=j; i<acts.size(); i++){
            if (used[i]) continue; // if the item is already used, then skip it.
            Acts act = acts.get(i);
            group.add(act);
            used[i] = true;
            fill_one_plan(acts, time+act.getDuration(),ranges,start, group, used, i+1);
            group.remove(group.size()-1);
            used[i] = false;
        }
    }

    // retrieve the event plan
    public List<Stages> getPlans() {
        return plans;
    }

    public Map<Integer, String> getDefaultEventNames() {
        return defaultEventNames;
    }
}
