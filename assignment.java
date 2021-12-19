// "static void main" must be defined in a public class.
public class Main {
    public static void main(String[] args){
        Main myMain = new Main();
        // The schedule we would like to fill in.
        // {13, 180, 240} - the config info of a schedule time period
        // while means 13 - 1:00PM; 
        // 180, 240 - the protential time perido, the event may finish in 3hr to 4hr
        int[][] ranges = {{9, 180, 180}, {13, 180, 240}, {9, 180, 180}, {13, 180, 240}}; 

        // schedule map will look like this
        // 9 : "12:00PM Lunch"
        // 13 : "05:00PM Main Event"
        Map<Integer, String> schedule = new HashMap<>();
        schedule.put(9, "12:00PM Lunch"); 
        schedule.put(13, "05:00PM Main Event");
        
        myMain.makeOnePlan(ranges, schedule);
        
    }
    
    // run program and find an available plan
    public void makeOnePlan(int[][] ranges, Map<Integer, String> schedule){
        
        Solution solution = new Solution(ranges, schedule);
        List<String[]> events = solution.groupActs();
        List<List<String[]>> plans = solution.fillActs(events);
        solution.printOutPlans(plans);   
    }

    /**
     * The overall time complexity is o(n^2), which is calculated by 
     * n - the number of different combinations of events for 2 stages.
     * k - the number of events inside one combination(one solution).
     
     The program will be execuated in the following order
     1. Take the system input and convert it into acts list -> [act lengths, act title], 
     like this:
     
     [["30", "Bob Dylan & His Band"], ["45", "Post Malone"], ["60", "Mariya Takeuchi"] ...]
     
     2. DFS fill in plans, based on schedule configuration:
     int[][] ranges = {{9, 180, 180}, {13, 180, 240}, {9, 180, 180}, {13, 180, 240}}; 
     
     3. Print out the plan results.
     As Plans for 2 stages will be stored like this
     [List[Morning], List[Afternoon], List[Morning], List[Afternoon]]
     
    
     
     */
    
    public class Solution {
        
        int[][] ranges;
        Map<Integer, String> schedule;
            
        public Solution(int[][] ranges, Map<Integer, String> schedule){
            this.ranges = ranges;
            this.schedule = schedule;
        }
        
        // Fill in act into plans
        // @parameter
        // events: the act list the program get as an input
        public List<List<String[]>> fillActs(List<String[]> events){

            // The same act will be only used once (aviod duplication)
            boolean[] used = new boolean[events.size()];
            List<List<String[]>> plans = new ArrayList<>();

            // normal DFS to find suitable plan schedule
            fillOnePlan(events, 0, this.ranges, 0, new ArrayList<String[]>(), used, 0, plans);

            return plans;
        }

        // Print out the schedule plans
        public void printOutPlans(List<List<String[]>> plans){
            
            for (int i=0;i<plans.size(); i++){
                if (i%2==0){ // if i %2 == 0, then it means it is a morning plan
                    System.out.println("Stage " + String.valueOf(i/2+1));
                    printLines(plans.get(i), this.ranges[i][0]);
                    System.out.println(this.schedule.get(this.ranges[i][0]));    
                }else{
                    // if i %2 == 1, then it means it is an afternoon plan
                    printLines(plans.get(i), this.ranges[i][0]);
                    System.out.println(this.schedule.get(this.ranges[i][0])); 
                    System.out.println("-----------------------------");
                }
            }
        }

        // print one schedule in a line 
        public void printLines(List<String[]> group, int start){
            int res = 0;
            for (int i=0; i< group.size();i++){
                int hour = start + res/60;
                int mins = res%60;
                formatTime(mins, hour, group.get(i)[1], group.get(i)[0]);    
                res += Integer.valueOf(group.get(i)[0]);
            }
        }

        /* format act start time String
        * as the plan we find will look like this
        [["30", "Bob Dylan & His Band"], ["45", "Post Malone"], ["60", "Mariya Takeuchi"] ...]
        * for each act, it will need to convert it to the expected output, like
        * 09:00AM One Ok Rock 60min
        */
        public void formatTime(int mins, int hour, String name, String len){
            StringBuilder sb = new StringBuilder();
            String[] line = new String[3];

            if (hour < 10){
                sb.append("0"+String.valueOf(hour));
            }else if (hour > 12) {
                sb.append("0"+ String.valueOf(hour%12));
            }else{
                sb.append(String.valueOf(hour));
            }
            sb.append(":");
            if (mins < 10){
                sb.append("0"+String.valueOf(mins));
            }else{
                sb.append(String.valueOf(mins));
            }
            
            if (hour > 12){
                sb.append("PM");    
            }else{
                sb.append("AM");
            }
            
            line[0] = sb.toString();
            line[1] = name;
            if (!name.equals("Special announcement")){
                line[2] = len + "min";
            }else{
                line[2] = "";
            }
            System.out.println(String.join(" ", line));    
        }

        /** DFS to fill acts into groups, then fill groups into plans
           @parameters:
           * events: the act list the program get as an input
           * time: the length of a squence of acts
           * ranges: used for doing schedule length match
           * start: tell which ranges the program is trying to fill in, e.g. start = 0, then 
           the first range {1, 180, 180} 
           * group: List storing the acts already put in
           * used: the boolean array saving used act which aviod duplications
           * j: the first position 
           * plans: the stage plan which is filled with List(group)
        */
        public void fillOnePlan(List<String[]> events, int time, int[][] ranges, int start, List<String[]> group, boolean[] used, int j, List plans){
            // if time is longer than schedule OR start position is longer than ranges OR plans is already filled in, then return
            if (start >= ranges.length || time > ranges[start][1] || plans.size() >= ranges.length) return;
            
            // if the length of group acts meet the requirements in config, then it means we find a valid group, then we add it into plans
            if (ranges[start][1] <= time && time <= ranges[start][2]){
                plans.add(new ArrayList<>(group));
                fillOnePlan(events, 0, ranges,start+1, new ArrayList<String[]>(), used, 0, plans);
                return;
            }
            
            // we put one act in group, and in DFS find a valid group
            for (int i=j; i<events.size(); i++){
                if (used[i]) continue; // if the item is already used, then skip it.
                String[] e = events.get(i);
                group.add(e);
                used[i] = true;
                fillOnePlan(events, time+Integer.valueOf(e[0]),ranges,start, group, used, i+1, plans);
                group.remove(group.size()-1);
                used[i] = false;
            }
        }
        
        // Format inputs into list
        // [["30", "Bob Dylan & His Band"], ["45", "Post Malone"], ["60", "Mariya Takeuchi"] ...]
        // Sort and group artists together, based upon their act lengths
        public ArrayList<String[]> groupActs(){
            ArrayList<String> res = readInputs();        
            ArrayList<String[]> result = new ArrayList<>();

            for (int i=0; i< res.size(); i++){
                String[] st = split(res.get(i));
                result.add(st);
            }

            result.sort((o1, o2)->{
                int a = Integer.valueOf(o1[0]);
                int b = Integer.valueOf(o2[0]);
                if ( a > b){return 1;}
                else if (a < b){ return -1;}
                else {return 0;}
            });
            return result;
        }

        // Split String act info into a String array -> [act length, act title]
        public String[] split(String s){
            char[] cs = s.toCharArray();
            String[] res = new String[2];
            try{
                for (int i = 0; i< s.length(); i++){
                    if (Character.isDigit(cs[i])){
                        res[1] = s.substring(0,i-1);
                        res[0] = s.substring(i, s.length()-3);
                        return res;
                    }
                }
            }catch(Exception e){
                System.out.println("Invalid inputs");
            }

            return res;
        }

        // Read the system input into a String list
        public ArrayList<String> readInputs(){

            ArrayList inputs = new ArrayList<String>();
            try{
                BufferedReader obj = new BufferedReader(new InputStreamReader(System.in)); 
                String str = obj.readLine();
                while (str != null){
                    if (str.equals("Special announcement")){
                        str = str + " 5min";
                    }
                    inputs.add(str);
                    str = obj.readLine();
                }
            }catch(IOException e){
                System.out.println("no inputs!");
            }
            return inputs; 
        }
    }
    
}