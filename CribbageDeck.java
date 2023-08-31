import java.util.*;
import java.util.stream.Stream;

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

    public static int hand4worth(ArrayList<PlayingCard> cards){
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

    public static int hand5worth(ArrayList<PlayingCard> cards, PlayingCard turnCard){
        int points = 0;
        boolean run5 = false;
        boolean run4 = false;
        boolean flush5 = false;

        ArrayList<String> info_15_2 = new ArrayList<>();
        ArrayList<String> info_15_3 = new ArrayList<>();
        ArrayList<String> info_15_4 = new ArrayList<>();
        ArrayList<String> info_15_5 = new ArrayList<>();
        ArrayList<String> info_pair = new ArrayList<>();
        ArrayList<String> info_run = new ArrayList<>();
        ArrayList<String> info_flush = new ArrayList<>();
        ArrayList<String> info_knobs = new ArrayList<>();

        ArrayList<PlayingCard> composite = new ArrayList<>(5);
        for(PlayingCard c : cards) composite.add(c);
        composite.add(turnCard);
        Collections.sort(composite);

        //2-card combos
        for(int i=0; i<5; i++){
            for(int j=i+1; j<5; j++){
                //Pair
                if(composite.get(i).getFace() == composite.get(j).getFace()){
                    points += 2;
                    StringBuilder sb = new StringBuilder("2 pts - Pair: ");
                    sb.append(composite.get(i).getLongName());
                    sb.append(" and ");
                    sb.append(composite.get(j).getLongName());
                    info_pair.add(sb.toString());
                }
                //15
                if(composite.get(i).points() + composite.get(j).points() == 15){
                    points += 2;
                    StringBuilder sb = new StringBuilder("2 pts - 15: ");
                    sb.append(composite.get(i).getLongName());
                    sb.append(" + ");
                    sb.append(composite.get(j).getLongName());
                    info_15_2.add(sb.toString());
                }
            }
        }

        //5-card combos
        ArrayList<Integer> faces = new ArrayList<>(4);
        //Run of 5
        for(PlayingCard c : composite) faces.add(c.getFace());
        if(faces.get(0)+1 == faces.get(1) && faces.get(1)+1 == faces.get(2) && faces.get(2)+1 == faces.get(3) && faces.get(3)+1 == faces.get(4)){
            points += 5;
            run5 = true;
            StringBuilder sb = new StringBuilder("5 pts - Run of 5: ");
            for(int i=0; i<5; i++){
                PlayingCard c = composite.get(i);
                sb.append(c.getLongName());
                if(i<4) sb.append(", ");
            }
            info_run.add(sb.toString());
        }
        //Flush
        if(composite.get(0).getSuit() == composite.get(1).getSuit()){
            if(composite.get(1).getSuit() == composite.get(2).getSuit()){
                if(composite.get(2).getSuit() == composite.get(3).getSuit()){
                    if(composite.get(3).getSuit() == composite.get(4).getSuit()){
                        points += 5;
                        flush5 = true;
                        StringBuilder sb = new StringBuilder("5 pts - Flush: ");
                        String[] longSuits = {"Spades", "Clubs", "Hearts", "Diamonds"};
                        sb.append(longSuits[composite.get(0).getSuit()]);
                        info_flush.add(sb.toString());
                    }
                }
            }
        }
        //15
        if(composite.get(0).points() + composite.get(1).points() + composite.get(2).points() + composite.get(3).points() + composite.get(4).points() == 15){
            points += 2;
            StringBuilder sb = new StringBuilder("2 pts - 15: ");
            for(int i=0; i<5; i++){
                PlayingCard c = composite.get(i);
                sb.append(c.getLongName());
                if(i<4) sb.append(" + ");
            }
            info_15_5.add(sb.toString());
        }

        //4-card combos
        for(int i=0; i<5; i++){
            for(int j=i+1; j<5; j++){
                for(int k=j+1; k<5; k++){
                    for(int l=k+1; l<5; l++){
                        //15
                        if(composite.get(i).points() + composite.get(j).points() + composite.get(k).points() + composite.get(l).points() == 15){
                            points += 2;
                            StringBuilder sb = new StringBuilder("2 pts - 15: ");
                            sb.append(composite.get(i).getLongName());
                            sb.append(" + ");
                            sb.append(composite.get(j).getLongName());
                            sb.append(" + ");
                            sb.append(composite.get(k).getLongName());
                            sb.append(" + ");
                            sb.append(composite.get(l).getLongName());
                            info_15_4.add(sb.toString());
                        }
                        //Run of 4
                        if(!run5){
                            faces.clear();
                            faces.add(composite.get(i).getFace());
                            faces.add(composite.get(j).getFace());
                            faces.add(composite.get(k).getFace());
                            faces.add(composite.get(l).getFace());
                            Collections.sort(faces);
                            if(faces.get(0)+1 == faces.get(1) && faces.get(1)+1 == faces.get(2) && faces.get(2)+1 == faces.get(3)){
                                points += 4;
                                run4 = true;
                                StringBuilder sb = new StringBuilder("4 pts - Run of 4: ");
                                sb.append(composite.get(i).getLongName());
                                sb.append(", ");
                                sb.append(composite.get(j).getLongName());
                                sb.append(", ");
                                sb.append(composite.get(k).getLongName());
                                sb.append(", ");
                                sb.append(composite.get(l).getLongName());
                                info_run.add(sb.toString());
                            }
                        }
                    }
                }
            }
        }
        if(!flush5){
            if(cards.get(0).getSuit() == cards.get(1).getSuit()){
                if(cards.get(1).getSuit() == cards.get(2).getSuit()){
                    if(cards.get(2).getSuit() == cards.get(3).getSuit()){
                        points += 4;
                        StringBuilder sb = new StringBuilder("4 pts - Flush without turn card: ");
                        String[] longSuits = {"Spades", "Clubs", "Hearts", "Diamonds"};
                        sb.append(longSuits[cards.get(0).getSuit()]);
                        info_flush.add(sb.toString());
                    }
                }
            }
        }

        //3-card combos
        for(int i=0; i<5; i++){
            for(int j=i+1; j<5; j++){
                for(int k=j+1; k<5; k++){
                    //15
                    if(composite.get(i).points() + composite.get(j).points() + composite.get(k).points() == 15){
                        points += 2;
                        StringBuilder sb = new StringBuilder("2 pts - 15: ");
                        sb.append(composite.get(i).getLongName());
                        sb.append(" + ");
                        sb.append(composite.get(j).getLongName());
                        sb.append(" + ");
                        sb.append(composite.get(k).getLongName());
                        info_15_3.add(sb.toString());
                    }
                    //Run of 3
                    if(!run5 && !run4){
                        faces.clear();
                        faces.add(composite.get(i).getFace());
                        faces.add(composite.get(j).getFace());
                        faces.add(composite.get(k).getFace());
                        Collections.sort(faces);
                        if(faces.get(0)+1 == faces.get(1) && faces.get(1)+1 == faces.get(2)){
                            points += 3;
                            StringBuilder sb = new StringBuilder("3 pts - Run of 3: ");
                            sb.append(composite.get(i).getLongName());
                            sb.append(", ");
                            sb.append(composite.get(j).getLongName());
                            sb.append(", ");
                            sb.append(composite.get(k).getLongName());
                            info_run.add(sb.toString());
                        }
                    }
                }
            }
        }

        //Knobs
        for(PlayingCard c : cards){
            if(c.getFace()==10 && c.getSuit()==turnCard.getSuit()){
                points += 1;
                StringBuilder sb = new StringBuilder("1 pt  - Knobs: ");
                sb.append(c.getLongName());
                sb.append(" and ");
                sb.append(turnCard.getLongName());
                info_knobs.add(sb.toString());
                break;
            }
        }

        //Final Report
        Stream.of(info_15_2, info_15_3, info_15_4, info_15_5, info_pair, info_run, info_flush, info_knobs).flatMap(Collection::stream).forEachOrdered(System.out::println);
        System.out.println(points + " total points.");

        return points;
    }

}