import java.util.ArrayList;

public class Player
{
	private String name;
	private ArrayList<UnoCard> deck;
	private Game game;
	
	public Player(String name, Game game)
	{	
		this.name = name;
		deck = new ArrayList<UnoCard>();
		this.game = game;
	}
	
	public void initialize()
	{
		deck = new ArrayList<UnoCard>();
	}
	
	public void drawCard(UnoCard unoCard)
	{
		deck.add(unoCard);
		game.getController().setPlayerDeck(deck);
	}
	
	public void drawCards(ArrayList<UnoCard> unoCards)
	{
		deck.addAll(unoCards);
		game.getController().setPlayerDeck(deck);
		game.getController().hideInfo();
	}
	
	public UnoCard playCard(UnoCard unoCard)
	{
		deck.remove(unoCard);
		return unoCard;
	}
	
	public ArrayList<UnoCard> getValidCards(UnoCard lastCard, UnoEnums.UnoCardColor wishColor, boolean challenge)
	{	
		ArrayList<UnoCard> validCards = new ArrayList<UnoCard>();		
		if(challenge)
		{
			for(UnoCard currentCard : deck)
			{	
				if(lastCard.getType().equals(UnoEnums.UnoCardValue.Draw_Two))
				{
						if(currentCard.getType().equals(UnoEnums.UnoCardValue.Draw_Two) || currentCard.getType().equals(UnoEnums.UnoCardValue.Draw_Four))
						{
							validCards.add(currentCard);
						}
				}
				else // lastCard == +4
				{
					
						if(currentCard.getType().equals(UnoEnums.UnoCardValue.Draw_Four))
						{
							validCards.add(currentCard);
						}	
						
						if(currentCard.getType().equals(UnoEnums.UnoCardValue.Draw_Two))
						{
							if(wishColor == UnoEnums.UnoCardColor.All)
							{
								validCards.add(currentCard);
							}
							else if(currentCard.getColor().equals(wishColor))
							{
								validCards.add(currentCard);
							}
						}					
				}
			}
		}
		else
		{
			if(wishColor == null)
			{	
				for(UnoCard currentCard : deck)
				{								
					if(currentCard.getColor().equals(lastCard.getColor()) || currentCard.getType().equals(lastCard.getType()) || currentCard.getType().equals(UnoEnums.UnoCardValue.Wild) || currentCard.getType().equals(UnoEnums.UnoCardValue.Draw_Four))
					{
						validCards.add(currentCard);
					}						
				}
			}
			else if(wishColor.equals(UnoEnums.UnoCardColor.All))
			{
				for(UnoCard currentCard : deck)
				{								
					if(!currentCard.getType().equals(UnoEnums.UnoCardValue.Wild) && !currentCard.getType().equals(UnoEnums.UnoCardValue.Draw_Four))
					{
						validCards.add(currentCard);
					}						
				}
			}
			else
			{
				for(UnoCard currentCard : deck)
				{
					if(currentCard.getColor().equals(wishColor))
					{
						validCards.add(currentCard);
					}	
				}
			}		
		}		
	
		return validCards;
	}
	
	public int getDeckSize()
	{
		return deck.size();
	}
	
	public String getName()
	{
		return name;
	}
	
	public ArrayList<UnoCard> getDeck()
	{
		return deck;
	}
	
	public void turn(UnoCard lastCard, UnoEnums.UnoCardColor wishColor, boolean challenge)
	{
		ArrayList<UnoCard> validDeck = getValidCards(lastCard, wishColor, challenge);
		if(validDeck.size() == 0)
		{
			if(challenge)
			{					
				game.setShowingInfo(true);
				game.getController().showInfo("No valid cards --> Draw " + game.getChallengeCounter() + " Cards.", game.getChallengeCounter());
			}
		}
	}
}