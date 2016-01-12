package controller;

import java.util.EnumMap;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ScreensController<T extends Enum<T> & SceneType>  extends StackPane {
	//Holds the screens to be displayed

	private Stage stage;
	private Scene dScene;
	protected Map<T, Node> screens;
	private Map<T, FXMLLoader> fxmlLoaders;

	public ScreensController(Class<T> sceneClass,Stage stage) {
		super();
		this.stage = stage;
		screens = new EnumMap<>(sceneClass);
		fxmlLoaders = new EnumMap<>(sceneClass);
	}

	//Add the screen to the collection
	public void addScreen(T sceneType, Node screen) {
		screens.put(sceneType, screen);
	}
	
	//Add the screen to the collection
	public void addLoader(T sceneType, FXMLLoader myLoader) {
		fxmlLoaders.put(sceneType, myLoader);
	}

	//Returns the Node with the appropriate name
	public Node getScreen(T sceneType) {
		return screens.get(sceneType);
	}

	//Loads the fxml file, add the screen to the screens collection and
	//finally injects the screenPane to the controller.
	public void loadScreen(T sceneType) {
		try {
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource(sceneType.getResource()));
			Parent loadScreen = myLoader.load();

			Controllable<T> myScreenController = myLoader.getController();
			myScreenController.setScreenParent(this);
			myScreenController.setStage(stage);

			addScreen(sceneType, loadScreen);
			addLoader(sceneType, myLoader);
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
						new KeyFrame(new Duration(1000), t -> {
							getChildren().remove(0);
							stage.setTitle(sceneType.toString());
							getChildren().add(0, node);     //add the screen
							stage.sizeToScene();
							prefHeightProperty().bind(stage.getScene().heightProperty());
							prefWidthProperty().bind(stage.getScene().widthProperty());
							Timeline fadeIn = new Timeline(
									new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
									new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
							fadeIn.play();
						}, new KeyValue(opacity, 0.0)));
				fade.play();

			} else {
				setOpacity(0.0);
				getChildren().add(node);       //no one else been displayed, then just show

				Timeline fadeIn = new Timeline(
						new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
						new KeyFrame(new Duration(2500), new KeyValue(opacity, 1.0)));
				fadeIn.play();
			}
			return true;
		} else {
			System.out.println("screen hasn't been loaded!!! \n");
			return false;
		}
	}



	public boolean setNewWindow(T sceneType) {
		Node node = screens.get(sceneType);
		//stage.setResizable(true);

		if (node != null) {   //screen loaded
			final DoubleProperty opacity = opacityProperty();
			if (!getChildren().isEmpty()) {    //if there is more than one screen
				Timeline fade = new Timeline(
						new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
						new KeyFrame(new Duration(1000), t -> {
							getChildren().remove(0);
							stage.setTitle(sceneType.toString());
							getChildren().add(0, node);     //add the screen

							stage.sizeToScene();
							//stage.setResizable(false);
							//getRoot().prefHeightProperty().bind(stage.heightProperty());
							//getRoot().prefWidthProperty().bind(stage.widthProperty());

							Timeline fadeIn = new Timeline(
									new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
									new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
							fadeIn.play();
						}, new KeyValue(opacity, 0.0)));
				fade.play();

			} else {
				setOpacity(0.0);
				getChildren().add(node);       //no one else been displayed, then just show

				Timeline fadeIn = new Timeline(
						new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
						new KeyFrame(new Duration(2500), new KeyValue(opacity, 1.0)));
				fadeIn.play();
			}
			return true;
		} else {
			System.out.println("screen hasn't been loaded!!! \n");
			return false;
		}
	}

	/*Node screenToRemove;
         if(screens.get(name) != null){   //screen loaded
         if(!getChildren().isEmpty()){    //if there is more than one screen
         getChildren().add(0, screens.get(name));     //add the screen
         screenToRemove = getChildren().get(1);
         getChildren().remove(1);                    //remove the displayed screen
         }else{
         getChildren().add(screens.get(name));       //no one else been displayed, then just show
         }
         return true;
         }else {
         System.out.println("screen hasn't been loaded!!! \n");
         return false;
         }*/

	//This method will remove the screen with the given name from the collection of screens
	public boolean unloadScreen(SceneType sceneType) {
		if (screens.remove(sceneType) == null) {
			System.out.println("Screen didn't exist");
			return false;
		} else {
			return true;
		}
	}
	public void unbindS(){
		prefHeightProperty().unbind();
		prefWidthProperty().unbind();}

	public Map<T,FXMLLoader> getFxmlLoaders() {
		return fxmlLoaders;
	}
	public Stage getStage(){
		return stage;
	}
}

