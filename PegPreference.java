import java.util.ArrayList;
import java.util.Arrays;

public class PegPreference implements Pegging {

    private int[] ordering;

    public PegPreference(String newOrder){
        String faces = "a234567890jqk";
        int[] indices = new int[newOrder.length()];
        for(int i=0; i<newOrder.length(); i++){
            int lookup =  faces.indexOf(newOrder.charAt(i));
            if(lookup == -1) throw new IllegalArgumentException("\"" + newOrder.charAt(i) + "\" is not a valid card face.");
            indices[i] = lookup;
        }
        ordering = indices;
        //TODO: Remove the following when pegging is working.
        System.out.println("DEBUG: new PegPreference with ordering " + Arrays.toString(ordering));
    }

    public PlayingCard select(ArrayList<PlayingCard> hand, ArrayList<PlayingCard> history){
        PlayingCard found = null;
        for(int face : ordering){
            for(PlayingCard card : hand){
                if(card.getFace() == face){
                    found = card;
                    break;
                }
            }
            if(found != null) break;
        }
        return found;
    }
}
