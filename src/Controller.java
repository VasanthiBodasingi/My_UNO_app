import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Controller
{
	@FXML private ImageView discardPileView;
	@FXML private ImageView deckView;
	@FXML private AnchorPane gameLauncher;
	@FXML private Label declareNextColorLabel;
	@FXML private Circle declareNextColorCircle;
	@FXML private ImageView declareNextColorImage;
	@FXML private HBox information;
	@FXML private Label informationLabel;
	@FXML private Button informationButton;
	@FXML private Label challengeLabel;
	@FXML private ImageView gameDirectionImage1;
	@FXML private ImageView gameDirectionImage2;
	@FXML private ImageView gameDirectionImage3;
	@FXML private ImageView gameDirectionImage4;
	@FXML private Label player3Label;
	@FXML private Label player1Label;
	@FXML private Label player2Label;
	@FXML private Button callUno;	
	@FXML private Button home;
	@FXML private ImageView welcomeLogo;
	@FXML private Label welcomeLabel;
	@FXML private Button buttonNewGame;
	@FXML private Rectangle DeclareNextColorYellow;
	@FXML private Rectangle DeclareNextColorRed;
	@FXML private Rectangle DeclareNextColorBlue;
	@FXML private Rectangle DeclareNextColorGreen;

	public Game game;
	public UnoEnums.UnoCardColor chosenWishColor;
	public int drawCounter;
	private boolean playerHasDrawn;
	public boolean playerMustChallenge;
	public TranslateTransition cardTransferSpeed;
	private int numberOfAIs = 2;
	private int numberOfStartingCards = 7;

	public Stage stage;
	public Image icon = new Image("images/icon.png");

	private final double CARD_HEIGHT = 90.0;
	private final double CARD_WIDTH = 57.0;	
	
	private final double CARD_SPACING_LARGE = 14.0;
	private final double CARD_SPACING_MEDIUM = 0.0;
	private final double CARD_SPACING_SMALL = - 25.0;
	private final double CARD_SPACING_ULTRA_SMALL = - 35.0;	

	private Point2D PLAYER_STARTING_POINT;
	private final Point2D AI_1_STARTING_POINT = new Point2D(400.0, 75.0);	
	private Point2D AI_2_STARTING_POINT;
	private Point2D AI_3_STARTING_POINT;

	private final javafx.scene.paint.Color COLOR_YELLOW = javafx.scene.paint.Color.web("#FFAA00");
	private final javafx.scene.paint.Color COLOR_RED = javafx.scene.paint.Color.web("#FF5555");
	private final javafx.scene.paint.Color COLOR_BLUE = javafx.scene.paint.Color.web("#5555FD");
	private final javafx.scene.paint.Color COLOR_GREEN = javafx.scene.paint.Color.web("#55AA55");	

	public void init()
	{
		declareNextColorImage.setImage(new Image("/images/Circle_All.png"));

		PLAYER_STARTING_POINT = new Point2D(400.0, stage.getScene().getHeight() - 50.0 - CARD_HEIGHT);
		AI_2_STARTING_POINT = new Point2D(stage.getScene().getWidth() - CARD_HEIGHT - 30, 70.0);
		AI_3_STARTING_POINT = new Point2D(60.0, 70.0);
		
		clearAll();
		showNeutralUI();
	}
	
	public void init(Stage stage, Controller controller)
	{
		DeclareNextColorYellow.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				controller.chosenWishColor = UnoEnums.UnoCardColor.Yellow;
				stage.close();
			}
		});

		DeclareNextColorRed.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				controller.chosenWishColor = UnoEnums.UnoCardColor.Red;
				stage.close();
			}
		});

		DeclareNextColorBlue.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				controller.chosenWishColor = UnoEnums.UnoCardColor.Blue;
				stage.close();
			}
		});

		DeclareNextColorGreen.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				controller.chosenWishColor = UnoEnums.UnoCardColor.Green;
				stage.close();
			}
		});
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent event)
			{
				event.consume();				
			}
		});
	}

	public void setStage(Stage stage)
	{
		this.stage = stage;
	}

	public void startGame()
	{
		if(game != null)
		{
			game.stop();
		}
		
		clearAll();
		
		drawCounter = 0;
		playerHasDrawn = false;
		playerMustChallenge = false;
		
		player3Label.setVisible(true);
		player3Label.setText("Player 3");
		
		deckView.setImage(createEmptyBackCard());
		deckView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(game.isRunning() && game.getCurrentPlayer() == 1 && !game.isShowingInfo() && !playerHasDrawn && !playerMustChallenge)
				{	
					playerHasDrawn = true;
					playerMustChallenge = false;
					UnoCard drawedCard = game.getDeck().drawCard(game.getDiscardPile());
					ArrayList<UnoCard> allCards = new ArrayList<UnoCard>();
					allCards.add(drawedCard);				
					moveCardFromDeckToPlayer(allCards);			
				}
			}
		});
		
		game = new Game(this, numberOfAIs);
		setLabelNames(game.getPlayer(), game.getAIs());
		game.newGame(numberOfStartingCards);
		game.start();
		
		home.setVisible(true);
		
		home.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				showMainMenu();				
			}
		});
	}
	
	public void showMainMenu()
	{
		if(game != null)
		{
			game.stop();	
		}
		
		clearAll();
		clearPlayerDeck();
		clearAllDecks(game.getAIs());
		
		showNeutralUI();
	}
	
	public void showNeutralUI()
	{
		welcomeLogo.setVisible(true);
		welcomeLabel.setVisible(true);
		buttonNewGame.setVisible(true);
		callUno.setVisible(false);
		home.setVisible(false);
	}
	
	public void hideNeutralUI()
	{
		welcomeLogo.setVisible(false);
		welcomeLabel.setVisible(false);
		buttonNewGame.setVisible(false);
	}
	
	public void setLabelNames(Player player, ArrayList<AI> ais)
	{	
		player1Label.setText(ais.get(0).getName());
		player1Label.setVisible(true);	
		player2Label.setText(ais.get(1).getName());
		player2Label.setVisible(true);
	}	

	public void showDeclareNextColorCircle(UnoEnums.UnoCardColor color)
	{
		hideDeclareNextColorImage();

		switch(color)
		{
			case Yellow:
				declareNextColorCircle.setFill(COLOR_YELLOW);
				declareNextColorCircle.setVisible(true);
				break;
			case Red:
				declareNextColorCircle.setFill(COLOR_RED);
				declareNextColorCircle.setVisible(true);
				break;
			case Blue:
				declareNextColorCircle.setFill(COLOR_BLUE);
				declareNextColorCircle.setVisible(true);
				break;
			case Green:
				declareNextColorCircle.setFill(COLOR_GREEN);
				declareNextColorCircle.setVisible(true);
				break;
			case All:
				showDeclareNextColorImage();
				break;
			default:
				break;
		}

		declareNextColorLabel.setVisible(true);
	}

	public void showDeclareNextColorImage()
	{
		hideDeclareNextColorCircle();
		declareNextColorImage.setVisible(true);
	}

	public void hideDeclareNextColorCircle()
	{
		declareNextColorLabel.setVisible(false);
		declareNextColorCircle.setVisible(false);
	}

	public void hideDeclareNextColorImage()
	{
		declareNextColorImage.setVisible(false);
		declareNextColorCircle.setVisible(false);
	}

	public void hideWishColor()
	{
		hideDeclareNextColorCircle();
		hideDeclareNextColorImage();
	}

	public void hideInfo()
	{		
		information.setVisible(false);
	}

	public void showInfo(String text, int numberOfCards)
	{
		informationLabel.setText(text);
		informationButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				moveCardFromDeckToPlayer(game.getDeck().drawCards(game.getChallengeCounter(), game.getDiscardPile()));				
			}
		});

		information.setVisible(true);
	}
	
	public void hideChallengeLabel()
	{
		challengeLabel.setVisible(false);
	}
	
	public void showChallengeLabel(String text)
	{
		challengeLabel.setText(text);
		challengeLabel.setVisible(true);
	}
	
	public void hideGameDirectionImage()
	{
		gameDirectionImage1.setVisible(false);
		gameDirectionImage2.setVisible(false);
		gameDirectionImage3.setVisible(false);
		gameDirectionImage4.setVisible(false);
	}
	
	public void setGameDirectionImage(UnoEnums.UnoGameDirection direction)
	{
		gameDirectionImage1.setVisible(true);
		gameDirectionImage2.setVisible(true);
		gameDirectionImage3.setVisible(true);
		gameDirectionImage4.setVisible(true);
		
		if(direction.equals(UnoEnums.UnoGameDirection.Right))
		{
			gameDirectionImage1.setImage(new Image("/images/Direction_Right1.png"));
			gameDirectionImage2.setImage(new Image("/images/Direction_Right2.png"));
			gameDirectionImage3.setImage(new Image("/images/Direction_Right3.png"));
			gameDirectionImage4.setImage(new Image("/images/Direction_Right4.png"));
		}
		else
		{
			gameDirectionImage1.setImage(new Image("/images/Direction_Left1.png"));
			gameDirectionImage2.setImage(new Image("/images/Direction_Left2.png"));
			gameDirectionImage3.setImage(new Image("/images/Direction_Left3.png"));
			gameDirectionImage4.setImage(new Image("/images/Direction_Left4.png"));
		}
	}

	public void setPlayer3Label(String text)
	{
		player3Label.setText(text);
	}

	public void setLastCard(UnoCard unoCard)
	{
		discardPileView.setImage(createCard(unoCard, true).getImage());
	}

	private Image createEmptyBackCard()
	{
		return new Image("images/Card_Back.png");
	}

	private ImageView createBackCard()
	{
		ImageView imageView = new ImageView(new Image("images/Card_Back.png"));
		imageView.setFitHeight(CARD_HEIGHT);
		imageView.setFitWidth(CARD_WIDTH);

		return imageView;
	}

	private ImageView createCard(UnoCard unoCard, boolean valid)
	{
		ImageView imageView = new ImageView(new Image("images/" + unoCard.getColor() + "_" + unoCard.getType() + ".png"));
		imageView.setFitHeight(CARD_HEIGHT);
		imageView.setFitWidth(CARD_WIDTH);
		imageView.setSmooth(true);

		if(!valid)
		{
			SnapshotParameters parameters = new SnapshotParameters();
			parameters.setFill(javafx.scene.paint.Color.TRANSPARENT);
			WritableImage snapshot = imageView.snapshot(parameters, null);

			if(unoCard.getType().equals(UnoEnums.UnoCardValue.Draw_Four) && unoCard.getType().equals(UnoEnums.UnoCardValue.Wild))
			{
				for(int x = 0; x < snapshot.getWidth(); x++)
				{
					for(int y = 0; y < snapshot.getHeight(); y++)
					{
						javafx.scene.paint.Color oldColor = snapshot.getPixelReader().getColor(x, y).desaturate().desaturate().brighter();
						snapshot.getPixelWriter().setColor(x, y, new javafx.scene.paint.Color(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), oldColor.getOpacity() * 1.0));
					}
				}
				imageView.setImage(snapshot);
			}
			else
			{
				for(int x = 0; x < snapshot.getWidth(); x++)
				{
					for(int y = 0; y < snapshot.getHeight(); y++)
					{
						javafx.scene.paint.Color oldColor = snapshot.getPixelReader().getColor(x, y).darker().desaturate();						
						snapshot.getPixelWriter().setColor(x, y, new javafx.scene.paint.Color(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), oldColor.getOpacity() * 1.0));
					}
				}
				imageView.setImage(snapshot);
			}
		}
		Controller main = this;

		imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(game.isRunning() && game.getCurrentPlayer() == 1)
				{
					if(valid)
					{
						if(unoCard.getType().equals(UnoEnums.UnoCardValue.Wild) || unoCard.getType().equals(UnoEnums.UnoCardValue.Draw_Four))
						{
							try
							{
								FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DeclareNextColor.fxml"));

								Parent root = (Parent)fxmlLoader.load();
								Stage newStage = new Stage();
								newStage.setScene(new Scene(root, 300, 300));
								newStage.setTitle("ColorChooser");
								newStage.initOwner(stage);

								newStage.getIcons().add(icon);

								Controller newController = fxmlLoader.getController();
								newController.init(newStage, main);

								newStage.initModality(Modality.APPLICATION_MODAL);
								newStage.setResizable(false);
								newStage.showAndWait();

							}
							catch(IOException e1)
							{
								e1.printStackTrace();
							}
						}
						else
						{
							chosenWishColor = null;
						}

						moveCardToDeadDeck(imageView, unoCard, chosenWishColor);
					}
				}
			}
		});

		return imageView;
	}

	public void moveCardToDeadDeck(ImageView view, UnoCard unoCard, UnoEnums.UnoCardColor newWishColor)
	{
		Point2D deckPosition = discardPileView.localToScene(Point2D.ZERO);

		cardTransferSpeed = new TranslateTransition();
		cardTransferSpeed.setDuration(Duration.millis(500));
		cardTransferSpeed.setNode(view);
		cardTransferSpeed.setCycleCount(1);
		cardTransferSpeed.setAutoReverse(false);
		cardTransferSpeed.setFromX(0);
		cardTransferSpeed.setFromY(0);
		cardTransferSpeed.setToX( - (view.getX() - deckPosition.getX()));
		cardTransferSpeed.setToY( - (view.getY() - deckPosition.getY()));
		cardTransferSpeed.setOnFinished(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if(game.isRunning())
				{
					if(newWishColor != null)
					{
						showDeclareNextColorCircle(newWishColor);
					}
					else
					{
						hideWishColor();
					}
					UnoCard playedCard	= game.getPlayer().playCard(unoCard);
					
					setPlayerDeck(game.getPlayer().getDeck());
					game.playCard(playedCard, newWishColor);
				}
			}
		});

		if(game.isRunning())
		{
			cardTransferSpeed.play();
		}
	}

	public void moveAICardToDeadDeck(AI ai, int currentPlayer, UnoCard unoCard, int cardPosition, UnoEnums.UnoCardColor newWishColor)
	{
		ObservableList<Node> nodes = gameLauncher.getChildren();
		ArrayList<Integer> possibleNodes = new ArrayList<Integer>();
		for(int i = 0; i < nodes.size(); i++)
		{
			Node current = nodes.get(i);
			if(current.getId().contains("ai" + ai.getID()))
			{
				possibleNodes.add(i);
			}
		}
		
		ImageView view = (ImageView)gameLauncher.getChildren().get(possibleNodes.get(cardPosition));
		view.setImage(new Image("images/" + unoCard.getColor() + "_" + unoCard.getType() + ".png"));

		Point2D deckPosition = discardPileView.localToScene(Point2D.ZERO);

		cardTransferSpeed = new TranslateTransition();
		cardTransferSpeed.setDuration(Duration.millis(700));
		cardTransferSpeed.setNode(view);
		cardTransferSpeed.setCycleCount(1);
		cardTransferSpeed.setAutoReverse(false);
		cardTransferSpeed.setFromX(0);
		cardTransferSpeed.setFromY(0);
		cardTransferSpeed.setToX( - (view.getX() - deckPosition.getX()));
		cardTransferSpeed.setToY( - (view.getY() - deckPosition.getY()));
		cardTransferSpeed.setOnFinished(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if(game.isRunning())
				{
					if(newWishColor != null)
					{
						showDeclareNextColorCircle(newWishColor);
					}
					else
					{
						hideWishColor();
					}
					UnoCard playedCard = ai.playCard(unoCard);
					setAIDeck(ai);
					game.playCard(playedCard, newWishColor);
				}
			}
		});

		if(game.isRunning())
		{
			cardTransferSpeed.play();
		}
	}
	
	public void moveCardFromDeckToPlayer(ArrayList<UnoCard> unoCards)
	{
		if(game.isRunning())
		{	
			Point2D deckPosition = deckView.localToScene(Point2D.ZERO);
			
			ImageView view = createCard(unoCards.get(drawCounter), true);
			view.setId("drawAnimation");
			view.setX(deckPosition.getX());
			view.setY(deckPosition.getY());
			gameLauncher.getChildren().add(view);	
			
			cardTransferSpeed = new TranslateTransition();
			cardTransferSpeed.setDuration(Duration.millis(700));
			cardTransferSpeed.setNode(view);
			cardTransferSpeed.setCycleCount(1);
			cardTransferSpeed.setAutoReverse(false);
			cardTransferSpeed.setFromX(0);
			cardTransferSpeed.setFromY(0);
			cardTransferSpeed.setToX( - (view.getX() - getPositionOfRightCard(null)));
			cardTransferSpeed.setToY( - (view.getY() - PLAYER_STARTING_POINT.getY()));
			cardTransferSpeed.setOnFinished(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent event)
				{					
					ObservableList<Node> nodes = gameLauncher.getChildren();
					Iterator<Node> iterator = nodes.iterator();
					while(iterator.hasNext())
					{
						if(iterator.next().getId().equals("drawAnimation"))
						{
							iterator.remove();
						}
					}
					if(game.isRunning())
					{					
						game.getPlayer().drawCard(unoCards.get(drawCounter));
						setPlayerDeck(game.getPlayer().getDeck());
						drawCounter++;
						playerHasDrawn = false;
						
						if(drawCounter < unoCards.size())
						{				
							moveCardFromDeckToPlayer(unoCards);
						}
						else				
						{
							game.setShowingInfo(false);
							hideInfo();
							drawCounter = 0;
							game.draw();
						}		
					}
				}
			});
	
			if(game.isRunning())
			{
				cardTransferSpeed.play();
			}
		}
	}
	
	private double getPositionOfRightCard(AI ai)
	{	
		if(ai == null)
		{
			double maxWidth = stage.getScene().getWidth() - (PLAYER_STARTING_POINT.getX() * 2) - CARD_WIDTH;
			int deckSize = game.getPlayer().getDeckSize();
			if((deckSize * (CARD_WIDTH + CARD_SPACING_LARGE)) > maxWidth)
			{
				if((deckSize * (CARD_WIDTH + CARD_SPACING_MEDIUM)) > maxWidth)
				{
					if((deckSize * (CARD_WIDTH + CARD_SPACING_SMALL)) > maxWidth)
					{
						return PLAYER_STARTING_POINT.getX() + ((deckSize + 1) * (CARD_WIDTH + CARD_SPACING_ULTRA_SMALL)) - CARD_SPACING_ULTRA_SMALL;
					}
					else
					{
						return PLAYER_STARTING_POINT.getX() + ((deckSize + 1) * (CARD_WIDTH + CARD_SPACING_SMALL)) - CARD_SPACING_SMALL;
					}
				}
				else
				{
					return PLAYER_STARTING_POINT.getX() + ((deckSize + 1) * (CARD_WIDTH + CARD_SPACING_MEDIUM)) - CARD_SPACING_MEDIUM;
				}
			}
			else
			{
				return PLAYER_STARTING_POINT.getX() + ((deckSize + 1) * (CARD_WIDTH + CARD_SPACING_LARGE)) - CARD_SPACING_LARGE;
			}	
		}
		//AI 1 (Above Player)
		else
		{
			double maxWidth = stage.getScene().getWidth() - (AI_1_STARTING_POINT.getX() * 2) - CARD_WIDTH;
			int deckSize = ai.getDeckSize();
			if((deckSize * (CARD_WIDTH + CARD_SPACING_LARGE)) > maxWidth)
			{
				if((deckSize * (CARD_WIDTH + CARD_SPACING_MEDIUM)) > maxWidth)
				{
					if((deckSize * (CARD_WIDTH + CARD_SPACING_SMALL)) > maxWidth)
					{
						return AI_1_STARTING_POINT.getX() + ((deckSize + 1) * (CARD_WIDTH + CARD_SPACING_ULTRA_SMALL)) - CARD_SPACING_ULTRA_SMALL;
					}
					else
					{
						return AI_1_STARTING_POINT.getX() + ((deckSize + 1) * (CARD_WIDTH + CARD_SPACING_SMALL)) - CARD_SPACING_SMALL;
					}
				}
				else
				{
					return AI_1_STARTING_POINT.getX() + ((deckSize + 1) * (CARD_WIDTH + CARD_SPACING_MEDIUM)) - CARD_SPACING_MEDIUM;
				}
			}
			else
			{
				return AI_1_STARTING_POINT.getX() + ((deckSize + 1) * (CARD_WIDTH + CARD_SPACING_LARGE)) - CARD_SPACING_LARGE;
			}	
		}		
	}
	
	private double getPositionOfBottomCard(AI ai)
	{			
		double maxHeight = stage.getScene().getHeight() - ((AI_2_STARTING_POINT.getY() + 40.0) * 2) - CARD_WIDTH;
		int deckSize = ai.getDeckSize();					

		if((deckSize * (CARD_WIDTH + CARD_SPACING_LARGE)) > maxHeight)
		{
			if((deckSize * (CARD_WIDTH + CARD_SPACING_MEDIUM)) > maxHeight)
			{
				if((deckSize * (CARD_WIDTH + CARD_SPACING_SMALL)) > maxHeight)
				{
					return AI_2_STARTING_POINT.getY() + ((deckSize + 1) * (CARD_WIDTH + CARD_SPACING_ULTRA_SMALL)) - CARD_SPACING_ULTRA_SMALL;
				}
				else
				{
					return AI_2_STARTING_POINT.getY() + ((deckSize + 1) * (CARD_WIDTH + CARD_SPACING_SMALL)) - CARD_SPACING_SMALL;
				}
			}
			else
			{
				return AI_2_STARTING_POINT.getY() + ((deckSize + 1) * (CARD_WIDTH + CARD_SPACING_MEDIUM)) - CARD_SPACING_MEDIUM;
			}
		}
		else
		{
			return AI_2_STARTING_POINT.getY() + ((deckSize + 1) * (CARD_WIDTH + CARD_SPACING_LARGE)) - CARD_SPACING_LARGE;
		}			
	}
	
	@SuppressWarnings("unused")
	public void moveCardFromDeckToAI(AI ai, ArrayList<UnoCard> unoCards)
	{
		if(game.isRunning())
		{		
			UnoCard unoCard = game.getDeck().drawCard(game.getDiscardPile());
			
			Point2D deckPosition = deckView.localToScene(Point2D.ZERO);
			
			ImageView view = createBackCard();
			view.setId("drawAnimation");
			view.setX(deckPosition.getX());
			view.setY(deckPosition.getY());
			gameLauncher.getChildren().add(view);	
			
			cardTransferSpeed = new TranslateTransition();
			cardTransferSpeed.setDuration(Duration.millis(700));
			cardTransferSpeed.setNode(view);
			cardTransferSpeed.setCycleCount(1);
			cardTransferSpeed.setAutoReverse(false);
			cardTransferSpeed.setFromX(0);
			cardTransferSpeed.setFromY(0);
			
			switch(ai.getID())
			{
				case 1:		cardTransferSpeed.setToX( - (view.getX() - getPositionOfRightCard(ai)));
							cardTransferSpeed.setToY( - (view.getY() - AI_1_STARTING_POINT.getY()));
							break;
				case 2:		cardTransferSpeed.setToX( - (view.getX() - AI_2_STARTING_POINT.getX()));
							cardTransferSpeed.setToY( - (view.getY() - getPositionOfBottomCard(ai)));
							break;				
				case 3:		cardTransferSpeed.setToX( - (view.getX() - AI_3_STARTING_POINT.getX()));
							cardTransferSpeed.setToY( - (view.getY() - getPositionOfBottomCard(ai)));
							break;				
				default:	break;
			}
		
			cardTransferSpeed.setOnFinished(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent event)
				{
					ObservableList<Node> nodes = gameLauncher.getChildren();
					Iterator<Node> iterator = nodes.iterator();
					while(iterator.hasNext())
					{
						if(iterator.next().getId().equals("drawAnimation"))
						{
							iterator.remove();
						}
					}
					
					if(game.isRunning())
					{
						ai.drawCard(unoCards.get(drawCounter));
						setAIDeck(ai);
						drawCounter++;
						
						if(drawCounter < unoCards.size())
						{				
							moveCardFromDeckToAI(ai, unoCards);
						}
						else				
						{
							game.setShowingInfo(false);
							hideInfo();
							drawCounter = 0;
							game.draw();
						}	
					}
				}
			});
	
			if(game.isRunning())
			{
				cardTransferSpeed.play();
			}
		}
	}

	public void clearPlayerDeck()
	{
		ObservableList<Node> nodes = gameLauncher.getChildren();
		Iterator<Node> iterator = nodes.iterator();
		while(iterator.hasNext())
		{
			if(iterator.next().getId().equals("player"))
			{
				iterator.remove();
			}
		}
	}

	public void setPlayerDeck(ArrayList<UnoCard> deck)
	{
		clearPlayerDeck();

		int counter = 1;
		
		callUno.setVisible(true);
		
		callUno.setOnAction(new EventHandler<ActionEvent>()
		{	
			@Override
			public void handle(ActionEvent event)
			{	
				showChallengeLabel("");
				if(deck.size() == 2) {
					callUno.setVisible(false);
					showChallengeLabel("Player 3 called UNO");
				}
			}
		});

		for(int i = 0; i < deck.size(); i++)
		{			
			ImageView current = createCard(deck.get(i), true);

			current.setId("player");
			gameLauncher.getChildren().add(current);			

			if(i == 0)
			{
				current.setX(AI_1_STARTING_POINT.getX() + CARD_WIDTH);
			}
			else
			{	
				double maxWidth = stage.getScene().getWidth() - (PLAYER_STARTING_POINT.getX() * 2) - CARD_WIDTH;
				if((deck.size() * (CARD_WIDTH + CARD_SPACING_LARGE)) > maxWidth)
				{
					if((deck.size() * (CARD_WIDTH + CARD_SPACING_MEDIUM)) > maxWidth)
					{
						if((deck.size() * (CARD_WIDTH + CARD_SPACING_SMALL)) > maxWidth)
						{
							current.setX(PLAYER_STARTING_POINT.getX() + (counter * (CARD_WIDTH + CARD_SPACING_ULTRA_SMALL)) - CARD_SPACING_ULTRA_SMALL);
						}
						else
						{
							current.setX(PLAYER_STARTING_POINT.getX() + (counter * (CARD_WIDTH + CARD_SPACING_SMALL)) - CARD_SPACING_SMALL);
						}
					}
					else
					{
						current.setX(PLAYER_STARTING_POINT.getX() + (counter * (CARD_WIDTH + CARD_SPACING_MEDIUM)) - CARD_SPACING_MEDIUM);
					}
				}
				else
				{
					current.setX(PLAYER_STARTING_POINT.getX() + (counter * (CARD_WIDTH + CARD_SPACING_LARGE)) - CARD_SPACING_LARGE);
				}
			}

			current.setY(PLAYER_STARTING_POINT.getY());

			counter++;
		}
	}

	public void setValidPlayerDeck(ArrayList<UnoCard> deck, ArrayList<UnoCard> validDeck)
	{
		clearPlayerDeck();

		int counter = 1;
		
		callUno.setVisible(true);
		
		callUno.setOnAction(new EventHandler<ActionEvent>()
		{	
			@Override
			public void handle(ActionEvent event)
			{	
				showChallengeLabel("");
				if(deck.size() == 2) {
					callUno.setVisible(false);
					showChallengeLabel("Player 3 called UNO");
				}
			}
		});

		for(int i = 0; i < deck.size(); i++)
		{			
			UnoCard currentCard = deck.get(i);
			ImageView current;

			if(validDeck.contains(currentCard))
			{
				current = createCard(currentCard, true);
				current.setY(PLAYER_STARTING_POINT.getY() - CARD_HEIGHT/4);
			}
			else
			{
				current = createCard(currentCard, false);
				current.setY(PLAYER_STARTING_POINT.getY());
			}

			current.setId("player");

			gameLauncher.getChildren().add(current);

			if(i == 0)
			{
				current.setX(AI_1_STARTING_POINT.getX() + CARD_WIDTH);
			}
			else
			{	
				double maxWidth = stage.getScene().getWidth() - (PLAYER_STARTING_POINT.getX() * 2) - CARD_WIDTH;
				if((deck.size() * (CARD_WIDTH + CARD_SPACING_LARGE)) > maxWidth)
				{
					if((deck.size() * (CARD_WIDTH + CARD_SPACING_MEDIUM)) > maxWidth)
					{
						if((deck.size() * (CARD_WIDTH + CARD_SPACING_SMALL)) > maxWidth)
						{
							current.setX(PLAYER_STARTING_POINT.getX() + (counter * (CARD_WIDTH + CARD_SPACING_ULTRA_SMALL)) - CARD_SPACING_ULTRA_SMALL);
						}
						else
						{
							current.setX(PLAYER_STARTING_POINT.getX() + (counter * (CARD_WIDTH + CARD_SPACING_SMALL)) - CARD_SPACING_SMALL);
						}
					}
					else
					{
						current.setX(PLAYER_STARTING_POINT.getX() + (counter * (CARD_WIDTH + CARD_SPACING_MEDIUM)) - CARD_SPACING_MEDIUM);
					}
				}
				else
				{
					current.setX(PLAYER_STARTING_POINT.getX() + (counter * (CARD_WIDTH + CARD_SPACING_LARGE)) - CARD_SPACING_LARGE);
				}
			}
			
			counter++;
		}
	}

	public void clearAIDeck(AI ai)
	{
		ObservableList<Node> nodes = gameLauncher.getChildren();
		Iterator<Node> iterator = nodes.iterator();
		while(iterator.hasNext())
		{
			if(iterator.next().getId().contains("ai" + ai.getID()))
			{
				iterator.remove();
			}
		}
	}

	public void setAIDeck(AI ai)
	{
		clearAIDeck(ai);
		
		ArrayList<UnoCard> deck = ai.getDeck();

		int counter = 1;
		
		callUno.setVisible(true);
		
		callUno.setOnAction(new EventHandler<ActionEvent>()
		{	
			@Override
			public void handle(ActionEvent event)
			{	
				showChallengeLabel("");
				if(deck.size() == 2) {
					callUno.setVisible(false);
					showChallengeLabel(ai.getName()+" called UNO");
				}
			}
		});
		
		
		for(int i = 0; i < deck.size(); i++)
		{
			ImageView current = createBackCard();

			current.setId("ai" + ai.getID());
			
			gameLauncher.getChildren().add(current);
			
			double maxWidth;
			double maxHeight;
			int deckSize;			
			
			switch(ai.getID())
			{
				case 1:	maxWidth = stage.getScene().getWidth() - ((AI_1_STARTING_POINT.getX() + 0.0) * 2) - CARD_WIDTH;
						deckSize = ai.getDeckSize();

						if(i == 0)
						{
							current.setX(AI_1_STARTING_POINT.getX() + CARD_WIDTH);
						}
						else
						{					
							if((deckSize * (CARD_WIDTH + CARD_SPACING_LARGE)) > maxWidth)
							{
								if((deckSize * (CARD_WIDTH + CARD_SPACING_MEDIUM)) > maxWidth)
								{
									if((deckSize * (CARD_WIDTH + CARD_SPACING_SMALL)) > maxWidth)
									{
										current.setX(AI_1_STARTING_POINT.getX() + (counter * (CARD_WIDTH + CARD_SPACING_ULTRA_SMALL)) - CARD_SPACING_ULTRA_SMALL);
									}
									else
									{
										current.setX(AI_1_STARTING_POINT.getX() + (counter * (CARD_WIDTH + CARD_SPACING_SMALL)) - CARD_SPACING_SMALL);
									}
								}
								else
								{
									current.setX(AI_1_STARTING_POINT.getX() + (counter * (CARD_WIDTH + CARD_SPACING_MEDIUM)) - CARD_SPACING_MEDIUM);
								}
							}
							else
							{
								current.setX(AI_1_STARTING_POINT.getX() + (counter * (CARD_WIDTH + CARD_SPACING_LARGE)) - CARD_SPACING_LARGE);
							}
						}
		
						current.setY(AI_1_STARTING_POINT.getY());
						break;
						
				case 2:	maxHeight = stage.getScene().getHeight() - ((AI_2_STARTING_POINT.getY() + 40.0) * 2) - CARD_WIDTH;
						deckSize = ai.getDeckSize();
						
						current.setRotate(-90.0);						
						
						if(i == 0)
						{
							current.setY(AI_2_STARTING_POINT.getY() + CARD_WIDTH);							
						}
						else
						{						
							if((deckSize * (CARD_WIDTH + CARD_SPACING_LARGE)) > maxHeight)
							{
								if((deckSize * (CARD_WIDTH + CARD_SPACING_MEDIUM)) > maxHeight)
								{
									if((deckSize * (CARD_WIDTH + CARD_SPACING_SMALL)) > maxHeight)
									{
										current.setY(AI_2_STARTING_POINT.getY() + (counter * (CARD_WIDTH + CARD_SPACING_ULTRA_SMALL)) - CARD_SPACING_ULTRA_SMALL);
									}
									else
									{
										current.setY(AI_2_STARTING_POINT.getY() + (counter * (CARD_WIDTH + CARD_SPACING_SMALL)) - CARD_SPACING_SMALL);
									}
								}
								else
								{
									current.setY(AI_2_STARTING_POINT.getY() + (counter * (CARD_WIDTH + CARD_SPACING_MEDIUM)) - CARD_SPACING_MEDIUM);
								}
							}
							else
							{
								current.setY(AI_2_STARTING_POINT.getY() + (counter * (CARD_WIDTH + CARD_SPACING_LARGE)) - CARD_SPACING_LARGE);
							}
						}
		
						current.setX(AI_2_STARTING_POINT.getX());
						break;
						
				case 3:	maxHeight = stage.getScene().getHeight() - ((AI_3_STARTING_POINT.getY() + 40.0) * 2) - CARD_WIDTH;
						deckSize = ai.getDeckSize();
						
						current.setRotate(90.0);
		
						if(i == 0)
						{
							current.setY(AI_3_STARTING_POINT.getY() + CARD_WIDTH);
						}
						else
						{							
							if((deckSize * (CARD_WIDTH + CARD_SPACING_LARGE)) > maxHeight)
							{
								if((deckSize * (CARD_WIDTH + CARD_SPACING_MEDIUM)) > maxHeight)
								{
									if((deckSize * (CARD_WIDTH + CARD_SPACING_SMALL)) > maxHeight)
									{
										current.setY(AI_3_STARTING_POINT.getY() + (counter * (CARD_WIDTH + CARD_SPACING_ULTRA_SMALL)) - CARD_SPACING_ULTRA_SMALL);
									}
									else
									{
										current.setY(AI_3_STARTING_POINT.getY() + (counter * (CARD_WIDTH + CARD_SPACING_SMALL)) - CARD_SPACING_SMALL);
									}
								}
								else
								{
									current.setY(AI_3_STARTING_POINT.getY() + (counter * (CARD_WIDTH + CARD_SPACING_MEDIUM)) - CARD_SPACING_MEDIUM);
								}
							}
							else
							{
								current.setY(AI_3_STARTING_POINT.getY() + (counter * (CARD_WIDTH + CARD_SPACING_LARGE)) - CARD_SPACING_LARGE);
							}
						}
		
						current.setX(AI_3_STARTING_POINT.getX());
						break;
				default: break;
			}	
			
			counter++;
		}
	}

	public void clearAllDecks(ArrayList<AI> ais)
	{
		clearPlayerDeck();

		for(AI currentAI : ais)
		{
			clearAIDeck(currentAI);
		}
	}	

	public void clearAll()
	{
		hideNeutralUI();	
		hideWishColor();
		hideInfo();
		player3Label.setVisible(false);
		hideChallengeLabel();
		hideGameDirectionImage();
		player1Label.setVisible(false);
		player2Label.setVisible(false);
		deckView.setImage(null);
		discardPileView.setImage(null);
	}
	
}