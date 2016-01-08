package uicomponent;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.web.WebView;
import org.controlsfx.control.PopOver;

import java.util.List;

/**
 * Created by WadaLabS on 2016/01/07.
 */
public class PopOverCreator {

    private WebView webView;
    private ObservableList<PopOver> errorPops;

    public PopOverCreator(WebView webView){
        this.webView = webView;
    }

    public StackPane markError(StackPane stackPane,List<String> ems){
        for(int i=0,size = ems.size();i>size;i++){
            String to = ems.get(i);
            webView.getEngine().executeScript("editor.");
            PopOver po = createPopOver(new Label(to));
            Group group = new Group();
            Circle circle = new Circle();
            circle.setStroke(Color.BLACK);
            circle.setFill(Color.WHITE);
            group.getChildren().add(circle);

            Line line1 = new Line();
            line1.setFill(Color.BLACK);
            group.getChildren().add(line1);

            Line line2 = new Line();
            line2.setFill(Color.BLACK);
            group.getChildren().add(line2);

             /*TO DO 位置設定*/
            po.show(webView);
            stackPane.getChildren().add(group);
            circle.setOnMouseClicked(e-> {
                if(!po.isShowing()) {
                    po.show(webView);
                }else{
                    po.hide();
                }
            });
        }
        return stackPane;
    }

    private PopOver createPopOver(Label label) {
        PopOver popOver = new PopOver();
        popOver.detachedProperty().set(true);
        popOver.setDetachable(true);
        popOver.setDetached(true);
        popOver.setDetachedTitle("ERROR");
        popOver.setContentNode(label);
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        return popOver;
    }
}
