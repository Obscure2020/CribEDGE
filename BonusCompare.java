import java.util.Comparator;

public class BonusCompare implements Comparator<HandPick>{

    public int compare(HandPick first, HandPick second){
        Integer one = first.getBonus();
        Integer two = second.getBonus();
        return two.compareTo(one);
    }

}