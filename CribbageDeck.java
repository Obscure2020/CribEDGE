import java.util.*;

public class CribbageDeck {

    private ArrayList<PlayingCard> deck;

    public CribbageDeck(){
        deck = new ArrayList<>();
        for(int s=0; s<4; s++){
            for(int f=0; f<13; f++){
                deck.add(new PlayingCard(f, s));
            }
        }
    }

    public PlayingCard getCard(String input) throws IllegalArgumentException{
        for(PlayingCard k : deck){
            if(k.same(input)) return k;
        }
        throw new IllegalArgumentException("Invalid card abbreviation. \"" + input + "\" not found in deck.");
    }

    public static int hand4Worth(ArrayList<PlayingCard> cards){
        int points = 0;
        boolean run4 = false;

        //2-card combos
        for(int i=0; i<4; i++){
            for(int j=i+1; j<4; j++){
                //Pair
                if(cards.get(i).getFace() == cards.get(j).getFace()) points += 2;
                //15
                if(cards.get(i).points() + cards.get(j).points() == 15) points += 2;
            }
        }

        //4-card combos
        ArrayList<Integer> faces = new ArrayList<>(4);
        //Run of 4
        for(PlayingCard k : cards) faces.add(k.getFace());
        Collections.sort(faces);
        if(faces.get(0)+1 == faces.get(1) && faces.get(1)+1 == faces.get(2) && faces.get(2)+1 == faces.get(3)){
            points += 4;
            run4 = true;
        }
        //Flush
        if(cards.get(0).getSuit() == cards.get(1).getSuit())
            if(cards.get(1).getSuit() == cards.get(2).getSuit())
                if(cards.get(2).getSuit() == cards.get(3).getSuit())
                    points += 4;
        //15
        if(cards.get(0).points() + cards.get(1).points() + cards.get(2).points() + cards.get(3).points() == 15) points += 2;

        //3-card combos
        for(int i=0; i<4; i++){
            for(int j=i+1; j<4; j++){
                for(int k=j+1; k<4; k++){
                    //15
                    if(cards.get(i).points() + cards.get(j).points() + cards.get(k).points() == 15) points += 2;
                    if(!run4){
                        //Run of 3
                        faces.clear();
                        faces.add(cards.get(i).getFace());
                        faces.add(cards.get(j).getFace());
                        faces.add(cards.get(k).getFace());
                        Collections.sort(faces);
                        if(faces.get(0)+1 == faces.get(1) && faces.get(1)+1 == faces.get(2)) points += 3;
                    }
                }
            }
        }
        return points;
    }

}