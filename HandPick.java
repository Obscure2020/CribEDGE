import java.util.ArrayList;

public class HandPick implements Comparable<HandPick>{

    private ArrayList<PlayingCard> kept;
    private ArrayList<PlayingCard> tossed;
    private int worth;
    private int bonusPoints;

    public HandPick(ArrayList<PlayingCard> cards, int first, int second, boolean dealer){
        kept = new ArrayList<>();
        tossed = new ArrayList<>();
        for(int i=0; i<6; i++){
            if(i==first || i==second){
                tossed.add(cards.get(i));
            } else {
                kept.add(cards.get(i));
            }
        }
        worth = CribbageDeck.hand4worth(kept);
        bonusPoints = calculateBonus(dealer);
    }

    public String fancyString(){
        StringBuilder sb = new StringBuilder("Keep: ");
        for(PlayingCard k : kept){
            sb.append(k.getLongName());
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        sb.append("\nToss: ");
        for(PlayingCard k : tossed){
            sb.append(k.getLongName());
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        sb.append("\nWorth: ");
        sb.append(worth);
        sb.append(" / Bonus: ");
        sb.append(bonusPoints);
        return sb.toString();
    }

    private int calculateBonus(boolean dealer){
        int points = 0;

        boolean keptGoodPeg = false;
        boolean keptBIG = false;
        boolean keptAce = false;
        boolean keptJack = false;
        int tossedSum = 0;
        boolean tossedFive = false;
        ArrayList<Integer> faces = new ArrayList<>(4);
        boolean keptPair = false;
        boolean tossedPair = false;

        for(PlayingCard k : kept){
            if(k.points()==3 || k.points()==4) keptGoodPeg = true;
            if(k.points()==1) keptAce = true;
            if(k.points()>=6 && k.points()<=9) keptBIG = true;
            if(k.getFace() == 10) keptJack = true;
            if(faces.contains(k.getFace())) keptPair = true;
            faces.add(k.getFace());
        }
        faces.clear();
        for(PlayingCard k : tossed){
            tossedSum += k.points();
            if(k.points() == 5) tossedFive = true;
            if(faces.contains(k.getFace())) tossedPair = true;
            faces.add(k.getFace());
        }

        if(keptGoodPeg) points += 2;
        if(keptBIG) points++;
        if(keptAce) points++;
        if(keptPair) points++;

        if(dealer){
            /*All of the point values from here down to the "-= 2" below were adjusted on Nov 10th, 2022,
            in response to growing concern from the DailyCribbageHand community regarding how often
            CribEDGE had decided to put a Five in the opponent's crib.*/
            if(tossedSum == 15) points += 3;
            if(tossedFive) points += 5;
            if(tossedPair) points += 2;
        } else {
            if(tossedSum == 15) points -= 3;
            if(tossedFive) points -= 5;
            if(tossedPair) points -= 2;
            /*Thanks to u/james-500 for suggesting this next line.
            I hadn't thought of avoiding opponent-crib flushes.*/
            if(tossed.get(0).getSuit() != tossed.get(1).getSuit()) points++;
            /*The knobs-detecting code was moved here, because I realized that if you're the dealer,
            you'll get the knobs point (if one is available) regardless of whether the Jack's in
            your hand or your crib.*/
            if(keptJack) points++;
        }

        return points;
    }

    public int compareTo(HandPick other){
        Integer mine = worth + bonusPoints;
        Integer theirs = other.worth + other.bonusPoints;
        int result = theirs.compareTo(mine);
        if(result == 0){
            /* All else being equal, I'm getting a sense from the Daily Cribbage Hand community
            that it's advantageous to keep lower-valued cards for the pegging section.*/
            mine = 0;
            for(PlayingCard k : kept) mine += k.points();
            theirs = 0;
            for(PlayingCard k : other.kept) theirs += k.points();
            return mine.compareTo(theirs);
        }
        return result;
    }

    public int getWorth(){
        return worth;
    }

    public int getBonus(){
        return bonusPoints;
    }

    public int fullWorth(PlayingCard turnCard){
        return CribbageDeck.hand5worth(kept, turnCard);
    }

}