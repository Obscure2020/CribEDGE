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
        //TODO: This function.
    }
}