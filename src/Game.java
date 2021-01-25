import java.util.ArrayList;
import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Game
{
	private UnoDeck unoDeck;
	private UnoDiscardPile unoDiscardPile;
	private Player player;
	private ArrayList<AI> ais;
	private int challengeCounter;
	private int currentPlayer;
	private UnoCard lastCard;
	private UnoEnums.UnoCardColor wishColor;
	private boolean challenge;
	private UnoEnums.UnoGameDirection direction;
	private Controller controller;
	private boolean lastPlayerDraw;
	private boolean skipped;
	private int counter;
	private boolean running;
	private boolean showingInfo;
	
	public Game(Controller controller, int numberOfPlayers)
	{
		this.controller = controller;
		unoDeck = new UnoDeck();
		unoDiscardPile = new UnoDiscardPile();
		player = new Player("Player", this);
		ais = new ArrayList<AI>();
		ais.add(new AI("Player 1", 1, this));
		ais.add(new AI("Player 2", 2, this));
		challengeCounter = 0;
	}

	public void newGame(int numberOfStartingCards)
	{
		unoDeck = new UnoDeck();	
		unoDeck.shuffle();
		unoDiscardPile = new UnoDiscardPile();
		challengeCounter = 0;
		lastCard = null;
		wishColor = null;
		challenge = false;
		direction = UnoEnums.UnoGameDirection.Right;
		controller.setGameDirectionImage(UnoEnums.UnoGameDirection.Right);		
		lastPlayerDraw = false;
		skipped = false;
		showingInfo = false;

		player.initialize();
	
		player.drawCards(unoDeck.drawCards(numberOfStartingCards, unoDiscardPile));
		
		for(AI currentAI : ais)
		{
			currentAI.initialize();
			currentAI.drawCards(unoDeck.drawCards(numberOfStartingCards, unoDiscardPile));
		}
		
		unoDiscardPile.add(unoDeck.drawCard(unoDiscardPile));
		lastCard = unoDiscardPile.getCards().get(unoDiscardPile.getCards().size()-1);			
		
		controller.setLastCard(lastCard);	
		if(lastCard.getType().equals(UnoEnums.UnoCardValue.Wild))
		{
			wishColor = UnoEnums.UnoCardColor.All;
			controller.chosenWishColor = wishColor;
			controller.showDeclareNextColorCircle(wishColor);
		}
		else if(lastCard.getType().equals(UnoEnums.UnoCardValue.Draw_Four))
		{
			wishColor = UnoEnums.UnoCardColor.All;
			controller.chosenWishColor = wishColor;
			controller.showDeclareNextColorCircle(wishColor);
			challenge = true;
			challengeCounter = 4;
		}
		else if(lastCard.getType().equals(UnoEnums.UnoCardValue.Draw_Two))
		{			
			challenge = true;
			challengeCounter = 2;
		}
	}

	public void start()
	{
		running = true;
		Random random = new Random();
		currentPlayer = random.nextInt(ais.size() + 1) + 1;			
	
		setCounter(0);	
		
		run();
	}
	
	private void run()
	{	
		if(running)
		{
			if(player.getDeckSize() == 0)
			{						
				end(player.getName());	
				return;
			}	
			
				for(AI winningAI : ais)
				{
					if(winningAI.getDeckSize() == 0)
					{
						end(winningAI.getName());
						return;
					}
				}		
			
			if(lastCard.getType().equals(UnoEnums.UnoCardValue.Reverse) && !lastPlayerDraw)
			{
				if(direction.equals(UnoEnums.UnoGameDirection.Right))
				{
					direction = UnoEnums.UnoGameDirection.Left;
					controller.setGameDirectionImage(UnoEnums.UnoGameDirection.Left);		
	
				}
				else
				{
					direction = UnoEnums.UnoGameDirection.Right;
					controller.setGameDirectionImage(UnoEnums.UnoGameDirection.Right);	
				}				
			}		
			
			determineNextPlayer();				
			
			if(skipped || !lastCard.getType().equals(UnoEnums.UnoCardValue.Skip))
			{	
				if(currentPlayer == 1)
				{			
					ArrayList<UnoCard> validDeck = player.getValidCards(lastCard, wishColor, challenge);
					controller.setValidPlayerDeck(player.getDeck(), validDeck);	
					
					controller.playerMustChallenge = false;
					if(challenge && validDeck.size() > 0)
					{
						controller.playerMustChallenge = true;
					}					
					
					player.turn(lastCard, wishColor, challenge);										
				}
				else
				{	
					if(running)
					{
						AI currentAI = ais.get(currentPlayer - 2);
						controller.setAIDeck(currentAI);	
						currentAI.turn(lastCard, wishColor, challenge);	
					}
				}
			}
			else
			{				
				if(!skipped)
				{	
					skipped = true;				
					run();
				}					
			}
			setCounter(getCounter() + 1);
		}
	}
		
	private void determineNextPlayer()
	{
		if(direction.equals(UnoEnums.UnoGameDirection.Right))
		{
			if(currentPlayer == ais.size() + 1)
			{
				currentPlayer = 1;
			}
			else
			{
				currentPlayer++;
			}
		}
		else
		{
			if(currentPlayer == 1)
			{
				currentPlayer = ais.size() + 1;
			}
			else
			{
				currentPlayer--;
			}
		}		
	}

	private void end(String name)
	{			
		controller.clearAllDecks(ais);
		controller.clearAll();		
		
		running = false;
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("UNO");
		alert.setHeaderText("");
		if(currentPlayer == 1)
		{		
			alert.setContentText("Player 3 Has Won.");
		}
		else {
			alert.setContentText(name + " Has Won.");
		}
		alert.initOwner(controller.stage);
		Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(controller.icon);
		alert.show();
		
		controller.showNeutralUI();
	}

	public UnoDeck getDeck()
	{
		return unoDeck;
	}

	public UnoDiscardPile getDiscardPile()
	{
		return unoDiscardPile;
	}
	
	public int getChallengeCounter()
	{
		return challengeCounter;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public ArrayList<AI> getAIs()
	{
		return ais;
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public int getCurrentPlayer()
	{
		return currentPlayer;
	}
	
	public Controller getController()
	{
		return controller;
	}
	
	public boolean isShowingInfo()
	{
		return showingInfo;
	}
	
	public void setShowingInfo(boolean showingInfo)
	{
		this.showingInfo = showingInfo;
	}
	
	public void draw()
	{		
		challenge = false;
		challengeCounter = 0;	
		lastPlayerDraw = true;
		controller.hideChallengeLabel();
		
		run();
	}		
	
	public void playCard(UnoCard unoCard, UnoEnums.UnoCardColor wishColor)
	{	
		unoDiscardPile.add(unoCard);
		lastCard = unoCard;
		this.wishColor = wishColor;
	
		if(unoCard.getType().equals(UnoEnums.UnoCardValue.Draw_Two))
		{
			challenge = true;
			challengeCounter += 2;
			controller.showChallengeLabel("Next Player Draws " + challengeCounter + " Cards");
		}	
		else if(unoCard.getType().equals(UnoEnums.UnoCardValue.Draw_Four))
		{
			challenge = true;
			challengeCounter += 4;
			controller.showChallengeLabel("Next Player Draws " + challengeCounter + " Cards");
		}
		else
		{
			challenge = false;
			challengeCounter = 0;
			controller.hideChallengeLabel();
		}
		
		lastPlayerDraw = false;
		skipped = false;
		controller.setLastCard(lastCard);		
		
		run();
	}
	
	public void stop()
	{
		running = false;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
}