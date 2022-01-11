package models;

import java.util.Comparator;

public class ActsComparator implements Comparator<Acts> {
    /*
    This class is used for comparing each acts' length (duration).
     */

    @Override
    public int compare(Acts o1, Acts o2) {
        return Integer.compare(o1.getDuration(), o2.getDuration());
    }
}
