import java.util.ArrayList;

public class AI
{
	private String name;
	private int id;
	private ArrayList<UnoCard> deck;
	private Game game;

	public AI(String name, int id, Game game)
	{
		this.name = name;
		this.id = id;
		deck = new ArrayList<UnoCard>();
		this.game = game;
	}

	public void initialize()
	{
		deck = new ArrayList<UnoCard>();
	}

	public int getID()
	{
		return id;
	}

	public void drawCard(UnoCard unoCard)
	{
		deck.add(unoCard);
		game.getController().setAIDeck(this);
	}

	public void drawCards(ArrayList<UnoCard> unoCards)
	{
		deck.addAll(unoCards);
		game.getController().setAIDeck(this);
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
				ArrayList<UnoCard> drawedCards = game.getDeck().drawCards(game.getChallengeCounter(), game.getDiscardPile());
				if(game.isRunning())
				{
					game.getController().moveCardFromDeckToAI(this, drawedCards);
				}
			}
			else
			{
				ArrayList<UnoCard> drawedCards = new ArrayList<UnoCard>();
				drawedCards.add(game.getDeck().drawCard(game.getDiscardPile()));
				if(game.isRunning())
				{
					game.getController().moveCardFromDeckToAI(this, drawedCards);
				}
			}
		}
		else
		{

			UnoCard playedCard = getHighestValuedCard(validDeck);
			UnoEnums.UnoCardColor newWishColor = null;

			if(playedCard.getType().equals(UnoEnums.UnoCardValue.Wild) || playedCard.getType().equals(UnoEnums.UnoCardValue.Draw_Four))
			{
				newWishColor = getBestColor();				
			}

			if(game.isRunning())
			{
				game.getController().moveAICardToDeadDeck(this, game.getCurrentPlayer(), playedCard, getCardPositionInDeck(playedCard), newWishColor);
			}
		}
	}

	private UnoCard getHighestValuedCard(ArrayList<UnoCard> validDeck)
	{
		UnoCard highestValuedCard = validDeck.get(0);
		for(UnoCard currentCard : validDeck)
		{
			if(currentCard.getValue() > highestValuedCard.getValue())
			{
				highestValuedCard = currentCard;
			}
		}

		return highestValuedCard;
	}
	
	private int getCardPositionInDeck(UnoCard unoCard)
	{
		for(int i = 0; i < deck.size(); i++)
		{
			if(deck.get(i).equals(unoCard))
			{
				return i;
			}
		}
		return 0;
	}

	private UnoEnums.UnoCardColor getBestColor()
	{
		int[] times = new int[4];

		for(UnoCard currentCard : deck)
		{
			switch(currentCard.getColor())
			{
				case Yellow:
					times[0]++;
					break;
				case Red:
					times[0]++;
					break;
				case Blue:
					times[0]++;
					break;
				case Green:
					times[0]++;
					break;
				default:
					break;
			}
		}

		int maxIndex = 0;
		for(int i = 1; i < times.length; i++)
		{
			int newnumber = times[i];
			if((newnumber > times[maxIndex]))
			{
				maxIndex = i;
			}
		}
		
		switch(maxIndex)
		{
			case 0:	return UnoEnums.UnoCardColor.Yellow;
			case 1: return UnoEnums.UnoCardColor.Red;
			case 2: return UnoEnums.UnoCardColor.Blue;
			case 3: return UnoEnums.UnoCardColor.Green;
			default: return null;
		}		
	}
}