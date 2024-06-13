import java.util.ArrayList;
import java.util.Arrays;

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
        System.out.println("DEBUG: new PegPreference with ordering " + Arrays.toString(bonusOrder));
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
                if(endSum == 15) points += 2;
                if(endSum == 21) points -= 2;
                //TODO: Add pair checking.
                //TODO: Add straight checking.
                results[i] = points;
            }
        }
        //TODO: Add results scanning.
        //TODO: Add fallback.
    }
}