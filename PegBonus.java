import java.util.*;

public class PegBonus implements Pegging {

    private int[] bonusOrder;
    private PegPreference fallback;

    public PegBonus(String newBonus, String fallbackOrder){
        String faces = "a234567890jqk";
        int[] indices = new int[newBonus.length()];
        for(int i=0; i<newBonus.length(); i++){
            int lookup =  faces.indexOf(newBonus.charAt(i));
            if(lookup == -1) throw new IllegalArgumentException("\"" + newBonus.charAt(i) + "\" is not a valid card face.");
            indices[i] = lookup;
        }
        bonusOrder = indices;
        //TODO: Remove the following two lines when pegging is working.
        System.out.println("DEBUG: new PegBonus with ordering " + Arrays.toString(bonusOrder));
        System.out.print("        ");
        fallback = new PegPreference(fallbackOrder);
    }

    public PlayingCard select(ArrayList<PlayingCard> hand, ArrayList<PlayingCard> history){
        int[] results = new int[bonusOrder.length];
        for(int i=0; i<results.length; i++){
            PlayingCard found = null;
            for(PlayingCard card : hand){
                if(card.getFace() == bonusOrder[i]){
                    found = card;
                    break;
                }
            }
            if(found == null){
                results[i] = -1;
            } else {
                int points = 0;
                ArrayList<PlayingCard> hypothetical = new ArrayList<>();
                for(PlayingCard card : history) hypothetical.add(card);
                hypothetical.add(found);
                int endSum = 0;
                for(PlayingCard card : hypothetical) endSum += card.points();
                final int cardQuant = hypothetical.size();
                if(endSum == 15) points += 2;
                if(endSum == 21) points -= 2;
                if(endSum == 31) points += 2;
                //Pair Checking
                if(cardQuant>=2 && found.getFace()==hypothetical.get(cardQuant-2).getFace()){
                    int pairPoints = 2;
                    if(cardQuant>=3 && found.getFace()==hypothetical.get(cardQuant-3).getFace()){
                        pairPoints =  6;
                        if(cardQuant>=4 && found.getFace()==hypothetical.get(cardQuant-4).getFace()){
                            pairPoints = 12;
                        }
                    }
                    points += pairPoints;
                }
                //Straight Checking
                if(cardQuant >= 3){
                    int straightPoints = 0;
                    ArrayList<Integer> lastFaces = new ArrayList<>();
                    lastFaces.add(hypothetical.get(cardQuant-1).getFace());
                    lastFaces.add(hypothetical.get(cardQuant-2).getFace());
                    for(int check=3; check<=cardQuant; check++){
                        lastFaces.add(hypothetical.get(cardQuant-check).getFace());
                        Collections.sort(lastFaces);
                        boolean correct = true;
                        int expected = lastFaces.get(0);
                        for(int f : lastFaces){
                            if(f == expected){
                                expected = f + 1;
                            } else {
                                correct = false;
                                break;
                            }
                        }
                        if(correct){
                            straightPoints = check;
                        } else {
                            break;
                        }
                    }
                    points += straightPoints;
                }
                results[i] = points;
            }
        }
        int maxBonus = Integer.MIN_VALUE;
        for(int k : results){
            if(k > maxBonus) maxBonus = k;
        }
        if(maxBonus > 0){
            int index = -1;
            for(int i=0; i<results.length; i++){
                if(results[i] == maxBonus){
                    index = i;
                    break;
                }
            }
            PlayingCard selection = null;
            for(PlayingCard card : hand){
                if(card.getFace() == index){
                    selection = card;
                    break;
                }
            }
            return selection;
        } else {
            return fallback.select(hand, history);
        }
    }
}