package controller;

import javafx.stage.Stage;

public interface Controllable<T extends Enum<T> & SceneType>{
    //This method will allow the injection of the Parent ScreenPane
    void setScreenParent(ScreensController<T> screenPage);
    void setStage(Stage stage);
}