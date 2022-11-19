import java.util.*;

public class Main {

    private static final boolean debugInfo = false;

    private static boolean yes_or_no(String userInput) throws IllegalArgumentException{
        char input = userInput.toLowerCase().charAt(0);
        if(input == 'y') return true;
        if(input == 'n') return false;
        throw new IllegalArgumentException("Bad response. \"" + userInput + "\" is not reasonably interpreted as a \"y\" or an \"n\".");
    }

    private static HandPick pickHand(ArrayList<PlayingCard> cards, boolean dealer){
        ArrayList<HandPick> choices = new ArrayList<>();
        for(int i=0; i<6; i++){
            for(int j=i+1; j<6; j++){
                choices.add(new HandPick(cards, i, j, dealer));
            }
        }
        Collections.sort(choices);
        if(debugInfo){
            System.out.println("==== DEBUG ====");
            for(HandPick h : choices){
                System.out.println(h.fancyString());
                System.out.println();
            }
            System.out.println("==== DEBUG ====");
        }
        return choices.get(0);
    }

    public static void main(String[] args) throws Exception{
        CribbageDeck deck = new CribbageDeck();
        Scanner scan = new Scanner(System.in);
        int points = 0;
        ArrayList<PlayingCard> hand = new ArrayList<>(6);

        System.out.print("Are we beginning as the Dealer? (y/n) - ");
        boolean dealer = yes_or_no(scan.nextLine());
        System.out.println();

        System.out.println("  6-card draw  ");
        System.out.println("===============");
        for(int i=1; i<=6; i++){
            System.out.print("#" + i + ": ");
            hand.add(deck.getCard(scan.nextLine()));
        }
        System.out.println("Translated:");
        for(PlayingCard k : hand) System.out.println(k.getLongName());
        System.out.println();

        HandPick choice = pickHand(hand, dealer);
        System.out.println(choice.fancyString());
        System.out.println();

        System.out.print("What was the turn card? - ");
        PlayingCard turnCard = deck.getCard(scan.nextLine());
        System.out.println("Turn card was the " + turnCard.getLongName());
        System.out.println();

        int fullHandWorth = choice.fullWorth(turnCard);

        scan.close();
    }

}