public class PlayingCard implements Comparable<PlayingCard>{

    private int face;
    private int suit;
    private String shortName;
    private String longName;

    public PlayingCard(int newFace, int newSuit){
        face = newFace;
        suit = newSuit;
        String[] faces = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "0", "j", "q", "k"};
        String[] suits = {"s", "c", "h", "d"};
        shortName = faces[face] + suits[suit];
        String[] longFaces = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
        String[] longSuits = {"Spades", "Clubs", "Hearts", "Diamonds"};
        longName = longFaces[face] + " of " + longSuits[suit];
    }

    public boolean same(String input){
        return shortName.equals(input);
    }

    public String getLongName(){
        return longName;
    }

    public int getFace(){
        return face;
    }

    public int getSuit(){
        return suit;
    }

    public int points(){
        return Integer.min(face+1, 10);
    }

    public int compareTo(PlayingCard other){
        Integer mine = face;
        Integer theirs = other.face;
        int result = mine.compareTo(theirs);
        if(result == 0){
            mine = suit;
            theirs = other.suit;
            return mine.compareTo(theirs);
        }
        return result;
    }

}