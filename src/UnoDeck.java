import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class UnoDeck
{
	private Stack<UnoCard> unoCards;

	public UnoDeck()
	{
		unoCards = new Stack<UnoCard>();
		
		for(UnoEnums.UnoCardColor currentColor : UnoEnums.UnoCardColor.values())
		{			
			if(currentColor != UnoEnums.UnoCardColor.Black && currentColor != UnoEnums.UnoCardColor.All)
			{	
				unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Zero, currentColor, 0));
				
				for(int i = 0; i < 2; i++)
				{													
					unoCards.add(new UnoCard(UnoEnums.UnoCardValue.One, currentColor, 0));
					unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Two, currentColor, 0));
					unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Three, currentColor, 0));
					unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Four, currentColor, 0));
					unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Five, currentColor, 0));
					unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Six , currentColor, 0));
					unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Seven, currentColor, 0));
					unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Eight, currentColor, 0));					
					unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Nine, currentColor, 0));
					
					unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Reverse, currentColor, 1));
					unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Skip, currentColor, 1));
					unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Draw_Two, currentColor, 2));					
				}
			}
		}
		
		unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Draw_Four, UnoEnums.UnoCardColor.Black, 10));
		unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Draw_Four, UnoEnums.UnoCardColor.Black, 10));
		unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Draw_Four, UnoEnums.UnoCardColor.Black, 10));
		unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Draw_Four, UnoEnums.UnoCardColor.Black, 10));
		
		unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Wild, UnoEnums.UnoCardColor.Black, 5));
		unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Wild, UnoEnums.UnoCardColor.Black, 5));
		unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Wild, UnoEnums.UnoCardColor.Black, 5));
		unoCards.add(new UnoCard(UnoEnums.UnoCardValue.Wild, UnoEnums.UnoCardColor.Black, 5));
	}
	
	public void shuffle()
	{
		Collections.shuffle(unoCards);
	}
	
	public UnoCard drawCard(UnoDiscardPile unoDiscardPile)
	{
		if(unoCards.size() > 0)
		{
			return unoCards.pop();	
		}
		else
		{
			refill(unoDiscardPile);
			return unoCards.pop();
		}
	}
	
	public ArrayList<UnoCard> drawCards(int numberOfCards, UnoDiscardPile unoDiscardPile)
	{
		ArrayList<UnoCard> drawedCards = new ArrayList<UnoCard>();
		for(int i = 0; i < numberOfCards; i++)
		{
			drawedCards.add(drawCard(unoDiscardPile));
		}

		return drawedCards;	
	}
	
	public void refill(UnoDiscardPile unoDiscardPile)
	{
		for(UnoCard currentCard : unoDiscardPile.getCards())
		{
			unoCards.add(currentCard);
		}
		
		shuffle();
	}

	public Stack<UnoCard> getCards()
	{
		return unoCards;
	}	
	
}