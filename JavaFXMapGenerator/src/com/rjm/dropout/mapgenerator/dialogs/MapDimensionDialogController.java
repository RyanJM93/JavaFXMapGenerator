package com.rjm.dropout.mapgenerator.dialogs;

import java.util.Arrays;

import com.rjm.dropout.mapgenerator.enums.MapDimensionPreset;
import com.rjm.dropout.mapgenerator.enums.MapGenScene;
import com.rjm.dropout.utilities.GUIUtils;
import com.rjm.dropout.utilities.MapGeneratorMain;
import com.rjm.dropout.utilities.MapGeneratorUtility;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class MapDimensionDialogController {
	
	private MapGeneratorMain mapGeneratorMain;

	// Custom Dimensions
	
    @FXML // fx:id="mapWidthTextField"
    private TextField mapWidthTextField; // Value injected by FXMLLoader
    @FXML // fx:id="mapHeightTextField"
    private TextField mapHeightTextField; // Value injected by FXMLLoader
    @FXML // fx:id="customContinueButton"
    private Button customContinueButton; // Value injected by FXMLLoader

    // Preset Dimensions
    
    @FXML // fx:id="presetComboBox"
    private ComboBox<MapDimensionPreset> presetComboBox; // Value injected by FXMLLoader
    @FXML // fx:id="presetContinueButton"
    private Button presetContinueButton; // Value injected by FXMLLoader

    @FXML
    void continueWithCustomDimensions(ActionEvent event) {
    	if (mapWidthTextField.getText() == null 
    			|| mapWidthTextField.getText().isEmpty() 
    			|| Integer.parseInt(mapWidthTextField.getText())<=0
    			
    			|| mapHeightTextField.getText() == null 
    			|| mapHeightTextField.getText().isEmpty() 
    			|| Integer.parseInt(mapHeightTextField.getText())<=0){
    		
			GUIUtils.showWarningPopupWindow("Warning!", "Enter a valid width and height before continuing", Modality.NONE);
		} else {
			System.out.println("Width = " + mapWidthTextField.getText());
			MapGeneratorUtility.BWIDTH = Integer.parseInt(mapWidthTextField.getText());
			
			System.out.println("Height = " + mapHeightTextField.getText());
			MapGeneratorUtility.BHEIGHT = Integer.parseInt(mapHeightTextField.getText());
			
        	mapGeneratorMain.switchScene(MapGenScene.GAME);
		}
    }

    @FXML
    void continueWithPresetDimensions(ActionEvent event) {
    	if(presetComboBox.getSelectionModel().getSelectedItem() != null){
    		
    		int mapX = presetComboBox.getSelectionModel().getSelectedItem().getX();
    		int mapY = presetComboBox.getSelectionModel().getSelectedItem().getY();
    		
    		System.out.println("Selected Map Width: " + mapX);
			MapGeneratorUtility.BWIDTH = mapX;
			
    		System.out.println("Selected Map Height: " + mapY);
			MapGeneratorUtility.BHEIGHT = mapY;
    		
        	mapGeneratorMain.switchScene(MapGenScene.GAME);
    	} else {
    		GUIUtils.showWarningPopupWindow("Warning!", "Select a preset map size before continuing", Modality.NONE);
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        presetComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(MapDimensionPreset.values())));
    }

	public MapGeneratorMain getMapGeneratorUtility() {
		return mapGeneratorMain;
	}

	public void setMapGeneratorMain(MapGeneratorMain mapGeneratorMain) {
		this.mapGeneratorMain = mapGeneratorMain;
	}
}
