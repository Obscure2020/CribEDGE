import java.util.*;

public class Main {

    private static final boolean debugInfo = false;

    private static boolean yes_or_no(String userInput) throws IllegalArgumentException{
        char input = userInput.toLowerCase().charAt(0);
        if(input == 'y') return true;
        if(input == 'n') return false;
        throw new IllegalArgumentException("Bad response. \"" + userInput + "\" is not reasonably interpreted as a \"y\" or an \"n\".");
    }

    private static HandPick pickHand(ArrayList<PlayingCard> cards, boolean dealer, int myPoints, int theirPoints){
        ArrayList<HandPick> choices = new ArrayList<>();
        for(int i=0; i<6; i++){
            for(int j=i+1; j<6; j++){
                choices.add(new HandPick(cards, i, j, dealer));
            }
        }
        Collections.sort(choices, Comparator.comparingInt(HandPick::getWorth).reversed());
        int maxWorth = choices.get(0).getWorth();
        ArrayList<HandPick> worthiest = new ArrayList<>();
        for(HandPick hp : choices){
            if(hp.getWorth() < maxWorth) break;
            worthiest.add(hp);
        }
        Collections.sort(worthiest, Comparator.comparingInt(HandPick::getBonus).reversed());
        boolean victory = worthiest.get(0).getWorth() + myPoints > 120;
        boolean worried = theirPoints >= 80;
        if(victory || worried){
            if(victory){
                System.out.println("Victory lap activated. Maximizing hand worth.");
            } else {
                System.out.println("Worried about opponent score. Prioritizing hand worth.");
            }
            if(debugInfo){
                System.out.println("==== DEBUG ====");
                for(HandPick hp : worthiest){
                    System.out.println(hp.fancyString());
                    System.out.println();
                }
                System.out.println("==== DEBUG ====");
            }
            return worthiest.get(0);
        }
        Collections.sort(choices);
        if(debugInfo){
            System.out.println("==== DEBUG ====");
            for(HandPick hp : choices){
                System.out.println(hp.fancyString());
                System.out.println();
            }
            System.out.println("==== DEBUG ====");
        }
        return choices.get(0);
    }

    public static void main(String[] args) throws Exception{
        CribbageDeck deck = new CribbageDeck();
        Scanner scan = new Scanner(System.in);
        ArrayList<PlayingCard> hand = new ArrayList<>(6);

        System.out.print("     Our score: ");
        int myPoints = Integer.parseInt(scan.nextLine());
        System.out.print("Opponent score: ");
        int theirPoints = Integer.parseInt(scan.nextLine());

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

        HandPick choice = pickHand(hand, dealer, myPoints, theirPoints);
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