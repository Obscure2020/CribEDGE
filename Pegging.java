import java.util.ArrayList;

public interface Pegging {
    public PlayingCard select(ArrayList<PlayingCard> hand, ArrayList<PlayingCard> history, int currentCount);
}