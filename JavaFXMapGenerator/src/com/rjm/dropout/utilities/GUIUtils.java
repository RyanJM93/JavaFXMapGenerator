package com.rjm.dropout.utilities;

import com.rjm.dropout.mapgenerator.objects.Point;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class GUIUtils {

	private static Scene warningDialogScene;
	public static WarningDialogController warningDialogController;

	public static void showModalPopupWindow(String windowTitle, Scene scene) {
		showPopupWindow(windowTitle, scene, Modality.APPLICATION_MODAL);
	}

	public static Stage showPopupWindow(String windowTitle, Scene scene, Modality modality) {
		final Stage dialog = new Stage();
		dialog.setTitle(windowTitle);

		dialog.initModality(modality);
		dialog.setScene(scene);
		dialog.show();

		dialog.centerOnScreen();

		return dialog;
	}

	public static Scene getWarningDialogScene() {
		return warningDialogScene;
	}

	public static void setWarningDialogScene(Scene scene) {
		warningDialogScene = scene;
	}

	public static void showWarningPopupWindow(String title, String message, Modality none) {
		warningDialogController.setMessage(message);

		warningDialogController.setStage(showPopupWindow(title, getWarningDialogScene(), Modality.NONE));
	}
	
	public static void pointToPixel(Point point){
		
	}
}
