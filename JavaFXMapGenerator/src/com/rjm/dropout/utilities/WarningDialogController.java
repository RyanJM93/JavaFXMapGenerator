package com.rjm.dropout.utilities;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WarningDialogController {
	
	Stage stage;

	@FXML // fx:id="warningOKButton"
    private Button warningOKButton; // Value injected by FXMLLoader

    @FXML // fx:id="warningMessageLabel"
    private Label warningMessageLabel; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	warningOKButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				stage.close();
			}
		});
    }
    
    public void setMessage(String message){
    	this.warningMessageLabel.setText(message);
    }

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Button getOKButton() {
		return warningOKButton;
	}
}
