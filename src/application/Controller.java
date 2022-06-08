package application;

import javafx.fxml.FXML;
import javafx.stage.Stage;


public class Controller {
	@FXML
	protected void easyClick() {
        Stage stage = new Stage();
		GameController obj = new GameController();
		obj.start(stage);
		obj.setDifficulty(1);
  
	}	
	
	@FXML
	protected void mediumClick() {
        Stage stage = new Stage();
		GameController obj = new GameController();
		obj.start(stage);
		obj.setDifficulty(2);
  
	}	
	
	@FXML
	protected void hardClick() {
        Stage stage = new Stage();
		GameController obj = new GameController();
		obj.start(stage);
		obj.setDifficulty(3);
	}	
}
