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
        Collections.sort(choices);
        final int maxWorth = choices.stream().mapToInt(HandPick::getWorth).max().getAsInt();
        final boolean victory = maxWorth + myPoints > 120;
        final boolean worried = (theirPoints >= 80) || (theirPoints > myPoints * 1.49);
        final boolean schmoovin = dealer && maxWorth >= 10;
        if(maxWorth == choices.get(0).getWorth()){
            //If the default logic picked a highest-worth hand already, do the following:
            if(victory){
                System.out.println("Default logic allows us to go out this round.");
            } else if (worried) {
                System.out.println("Worried about opponent score, but default logic sufficed.");
            }
        } else {
            //If there is a hand available that is worth more than the default pick, do the following:
            ArrayList<HandPick> worthiest = new ArrayList<>();
            choices.stream().filter(hp -> hp.getWorth() == maxWorth).forEachOrdered(worthiest::add);
            Collections.sort(worthiest, Comparator.comparingInt(HandPick::getBonus).reversed());
            if(victory || worried || schmoovin){
                if(victory){
                    System.out.println("Victory lap activated. Maximizing hand worth.");
                } else if (worried) {
                    System.out.println("Worried about opponent score. Prioritizing hand worth.");
                } else {
                    System.out.println("We're schmoovin' this round. Default logic bypassed.");
                }
                if(debugInfo){
                    StringBuilder sb = new StringBuilder();
                    for(HandPick hp : worthiest){
                        sb.append(hp.fancyString());
                        sb.append("\n\n");
                        choices.remove(hp);
                    }
                    sb.append("---------------\n\n");
                    for(HandPick hp : choices){
                        sb.append(hp.fancyString());
                        sb.append("\n\n");
                    }
                    System.out.println("==== DEBUG ====");
                    System.out.println(sb.toString().trim());
                    System.out.println("==== DEBUG ====");
                }
                return worthiest.get(0);
            }
        }
        if(debugInfo){
            StringBuilder sb = new StringBuilder();
            for(HandPick hp : choices){
                sb.append(hp.fancyString());
                sb.append("\n\n");
            }
            System.out.println("==== DEBUG ====");
            System.out.println(sb.toString().trim());
            System.out.println("==== DEBUG ====");
        }
        return choices.get(0);
    }

    private static Pegging prefRow(String newOrder){
        return new PegPreference(newOrder);
    }

    private static Pegging bonusRow(String bonusOrder, String fallbackOrder){
        return new PegBonus(bonusOrder, fallbackOrder);
    }

    public static void main(String[] args) throws Exception{
        Pegging[] peggingRows = new Pegging[]{
            prefRow("4398762a0jqk5"),
            prefRow("a0jqk87653294"),
            bonusRow("2a", "2a0jqk9765483"),
            bonusRow("3a", "a0jqk98654372"),
            //TODO: Fill in the rest of these from the notebook pages I compiled with my dad.
        };

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