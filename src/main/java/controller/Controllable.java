package controller;

public interface Controllable<T extends Enum<T> & SceneType>{    
    //This method will allow the injection of the Parent ScreenPane
    void setScreenParent(ScreensController<T> screenPage);
}