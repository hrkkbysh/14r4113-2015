package boot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.EditModeScene;
import controller.ReferenceController;
import controller.ScreensController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppStarter extends Application {
	private ExecutorService service;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Application.launch(AppStarter.class, (String[])null);
	}

	@Override
	public void start(Stage stage) {
		try {
			service = Executors.newFixedThreadPool(3);

			ScreensController<EditModeScene> mainContainer = new ScreensController<>(EditModeScene.class,stage);
			for (EditModeScene st : EditModeScene.values()) {
				mainContainer.loadScreen(st);
			}
			ReferenceController.setReference(mainContainer, service);
			mainContainer.setScreen(EditModeScene.ROOT);

			Group root = new Group();
			root.getChildren().addAll(mainContainer);
			Scene scene = new Scene(root);
			root.autosize();


			mainContainer.prefWidthProperty().bind(scene.widthProperty());
			mainContainer.prefHeightProperty().bind(scene.heightProperty());

			stage.setScene(scene);
			stage.setTitle("モード選択画面");

			stage.setResizable(false);
			stage.show();
		} catch (Exception ex) {
			Logger.getLogger(AppStarter.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void stop(){
		service.shutdown();
	}
}
