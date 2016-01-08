package controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author 14r4113 on 2016/01/08.
 */
public class NodeController<T extends Enum<T> & SceneType>  extends StackPane {
	//Holds the screens to be displayed

	private Stage stage;
	protected Map<T, Node> screens;

	public NodeController(Class<T> sceneClass,Stage stage) {
		super();
		this.stage = stage;
		screens = new EnumMap<>(sceneClass);
	}

	//Add the screen to the collection
	public void addScreen(T sceneType, Node screen) {
		screens.put(sceneType, screen);
	}

	//Loads the fxml file, add the screen to the screens collection and
	//finally injects the screenPane to the controller.
	public void loadScreen(T sceneType) {
		try {
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource(sceneType.getResource()));
			Parent loadScreen = myLoader.load();
			addScreen(sceneType, loadScreen);
			System.out.println(sceneType.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage()+"doesnt  exist.");
			System.out.println(sceneType.getResource());

		}
	}

	public boolean setScreen(T sceneType) {
		Node node = screens.get(sceneType);
		//stage.setResizable(true);

		if (node != null) {   //screen loaded
			final DoubleProperty opacity = opacityProperty();
			if (!getChildren().isEmpty()) {    //if there is more than one screen
				Timeline fade = new Timeline(
						new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
						new KeyFrame(new Duration(100), t -> {
							getChildren().remove(0);
							getChildren().add(0, node);     //add the screen
							Timeline fadeIn = new Timeline(
									new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
									new KeyFrame(new Duration(80), new KeyValue(opacity, 1.0)));
							fadeIn.play();
						}, new KeyValue(opacity, 0.0)));
				fade.play();

			} else {
				setOpacity(0.0);
				getChildren().add(node);       //no one else been displayed, then just show

				Timeline fadeIn = new Timeline(
						new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
						new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0)));
				fadeIn.play();
			}
			return true;
		} else {
			System.out.println("screen hasn't been loaded!!! \n");
			return false;
		}
	}
	public Stage getStage(){
		return stage;
	}
}
