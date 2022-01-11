package servicesImpl;

import models.Acts;
import models.Event;
import models.Stages;
import services.Outputs;

import java.util.List;
import java.util.Map;

public class OutputsImpl implements Outputs {

    // Print out the schedule plan
    public void printOutPlans(Event event, int[][] ranges){
        List<Stages> plans = event.getPlans();
        Map<Integer, String> defaultEventNames = event.getDefaultEventNames();

        try{
            // if plans is empty, then it means we can't find a valid solution
            if (plans.size() == 0){
                System.out.println("Can't find a valid plan.");
            }

            for (int i=0;i<plans.size(); i++){
                if (i%2==0){ // if i %2 == 0, then it means it is a morning plan
                    System.out.println("Stage " + (i/2+1));
                    printLines(plans.get(i).getActsInStage(), ranges[i][0]);
                    System.out.println(defaultEventNames.get(ranges[i][0]));
                }else{
                    // if i %2 == 1, then it means it is an afternoon plan
                    printLines(plans.get(i).getActsInStage(), ranges[i][0]);
                    System.out.println(defaultEventNames.get(ranges[i][0]));
                    System.out.println("-----------------------------");
                }
            }
        }catch(Exception e){
            System.out.println("Can't find a valid plan.");
        }
    }

    // print one schedule in a line
    public void printLines(List<Acts> group, int start){
        int res = 0;
        for (Acts acts : group) {
            int hour = start + res / 60;
            int mins = res % 60;
            formatTime(mins, hour, acts.getName(), acts.getDuration());
            res += acts.getDuration();
        }
    }

    /* format act start time String
    * as the plan we find will look like this
    [["30", "Bob Dylan & His Band"], ["45", "Post Malone"], ["60", "Mariya Takeuchi"] ...]
    * for each act, it will need to convert it to the expected output, like
    * 09:00AM One Ok Rock 60min
    */
    public void formatTime(int mins, int hour, String name, int len){

        try{
            StringBuilder sb = new StringBuilder();
            String[] line = new String[3];

            if (hour < 10){
                sb.append("0").append(hour);
            }else if (hour > 12) {
                sb.append("0").append(hour % 12);
            }else{
                sb.append(hour);
            }
            sb.append(":");
            if (mins < 10){
                sb.append("0").append(mins);
            }else{
                sb.append(mins);
            }

            if (hour > 12){
                sb.append("PM"); // if hour > 12 will be afternoon "PM"
            }else{
                sb.append("AM"); // if hour < 12 will be morning "AM"
            }

            line[0] = sb.toString();
            line[1] = name;
            if (!name.equals("Special announcement")){
                line[2] = len + "min";
            }else{
                line[2] = "";
            }
            System.out.println(String.join(" ", line));
        }catch(Exception e){
            System.out.println("time format is invalid");
        }
    }
}
