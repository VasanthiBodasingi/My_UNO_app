import java.util.ArrayList;

public class UnoDiscardPile
{
	private ArrayList<UnoCard> unoCards;

	public UnoDiscardPile()
	{
		unoCards = new ArrayList<UnoCard>();
	}
	
	public void add(UnoCard unoCard)
	{
		unoCards.add(unoCard);
	}

	public ArrayList<UnoCard> getCards()
	{
		return unoCards;
	}	
}