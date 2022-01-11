package models;

import java.util.ArrayList;
import java.util.List;

public class Stages {

    private List<Acts> actsInStage;
    public Stages(List<Acts> actsInStage){
        this.actsInStage = new ArrayList<>(actsInStage);
    }

    public List<Acts> getActsInStage() {
        return actsInStage;
    }
}
