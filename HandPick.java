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
        worth = CribbageDeck.card4Worth(kept);
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

        boolean keptFT = false;
        boolean keptBIG = false;
        boolean keptJack = false;
        int tossedSum = 0;
        boolean tossedFive = false;
        ArrayList<Integer> faces = new ArrayList<>(4);
        boolean keptPair = false;
        boolean tossedPair = false;

        for(PlayingCard k : kept){
            if(k.getFace()==2 || k.getFace()==3) keptFT = true;
            if(k.getFace()>=5 || k.getFace()<=8) keptBIG = true;
            if(k.getFace() == 10) keptJack = true;
            if(faces.contains(k.getFace())) keptPair = true;
            faces.add(k.getFace());
        }
        faces.clear();
        for(PlayingCard k : tossed){
            tossedSum += k.points();
            if(k.getFace() == 4) tossedFive = true;
            if(faces.contains(k.getFace())) tossedPair = true;
            faces.add(k.getFace());
        }

        if(keptFT) points += 2;
        if(keptBIG) points++;
        if(keptJack) points++;
        if(keptPair) points++;

        if(dealer){
            if(tossedSum == 15) points += 5;
            if(tossedFive) points += 2;
            if(tossedPair) points += 5;
        } else {
            if(tossedSum == 15) points -= 5;
            if(tossedFive) points -= 2;
            if(tossedPair) points -= 5;
        }

        return points;
    }

    public int compareTo(HandPick other){
        Integer mine = worth;
        Integer theirs = other.worth;
        return theirs.compareTo(mine);
    }

    public int getWorth(){
        return worth;
    }

    public int getBonus(){
        return bonusPoints;
    }

}