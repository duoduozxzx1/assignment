package servicesImpl;

import models.Acts;
import models.ActsComparator;
import services.Utils;

import java.io.*;
import java.util.ArrayList;

public class UtilsImpl implements Utils {

    public ArrayList<String> read_inputs(String filename){
        /*
        Read the file input into a String list
        @param: filename  the absolute path of file
        @return: ArrayList<String>  each item in the list will present a line in the file
         */
        ArrayList<String> inputs = new ArrayList<>();
        try{
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(filename).getFile());
            InputStream inputStream = new FileInputStream(file);
            BufferedReader obj = new BufferedReader(new InputStreamReader(inputStream));
            String str = obj.readLine();
            while (str != null){
                // as in the example file, there is a special announcement event without length
                // information inside. Here I just keep it as the same format as the assignment doc
                if (str.equals("Special announcement")){
                    str = str + " 5min";
                }
                inputs.add(str);
                str = obj.readLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return inputs;
    }

    public ArrayList<Acts> group_acts(String filename){
        /*
        Sort and group artists together, based upon their act lengths
        @param: filename  the absolute path of file
        @return: ArrayList<Models.Acts>  a sorted list of Models.Acts based upon Models.Acts' length
         */

        ArrayList<String> res = read_inputs(filename);
        ArrayList<Acts> result = new ArrayList<>();

        for (String re : res) {
            String[] st = split(re);
            Acts act = new Acts(st[1], Integer.parseInt(st[0]));
            result.add(act);
        }

        result.sort(new ActsComparator());
        return result;
    }

    public String[] split(String s){
        /*
        Split act info into a String array -> [act length, act title]
        @param: s  a line (String obj) in the file
        @return:    a String array which contains one Models.Acts info, like [act length, act title]
         */
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
            e.printStackTrace();
            System.out.println("Invalid inputs");
        }

        return res;
    }
}
