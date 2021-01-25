import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application
{
	@Override
	public void start(Stage stage)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("GameLauncher.fxml"));
			Parent root = (Parent)loader.load();

			Scene scene = new Scene(root, 1280, 680);

			stage.setResizable(true);
			stage.setTitle("UNO");
			stage.setScene(scene);		
			stage.setResizable(false);
			
			Controller controller = (Controller)loader.getController();
			controller.setStage(stage);			
			controller.init();			
			stage.getIcons().add(new Image("images/icon.png"));
			stage.show();		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}