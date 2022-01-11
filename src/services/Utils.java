package services;

import models.Acts;

import java.util.ArrayList;

public interface Utils {

    ArrayList<String> read_inputs(String filename);
    ArrayList<Acts> group_acts(String filename);
    String[] split(String s);

}
