/*
package editor;

import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;
import javafx.stage.Popup;

public class CodeAssistant{

	private ListView<Map.Entry<String,Integer>> instCandidate;
	private WebView webView = new WebView();
	private Popup popup,despPopup;
	private ObservableList<> instList;
	private String htmldata = CodeAssistant.class.getResource("LD.html").toExternalForm();
	public CodeAssistant() {
		instList = FXCollections.observableArrayList();
		InstructionEntry.map.forEach((k,v)->initListView(k,v));
		instCandidate = new ListView<>(instList);
		instCandidate.setPrefHeight(100);
		instCandidate.setCellFactory(value->new XCell());
		instCandidate.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		instCandidate.getSelectionModel().selectedItemProperty().addListener(projectItemSelected);
		popup = new Popup();
		
		popup.autoHideProperty().set(true);
		
		popup.getContent().add(instCandidate);

		despPopup = new Popup();
	
		webView.getEngine().load(htmldata);
		
		webView.prefHeight(3);
		despPopup.getContent().add(webView);
		despPopup.setWidth(10);
		webView.autosize();
		webView.setVisible(true);
//		AnchorPane.setBottomAnchor(instCandidate, 0.0);
//		AnchorPane.setTopAnchor(instCandidate, 0.0);
//		AnchorPane.setLeftAnchor(instCandidate, 0.0);
//		AnchorPane.setRightAnchor(instCandidate, 0.0);
	}
	public Popup getPopup(){
		return popup;
	}

	private void initListView(InstructionSet k, List<Casl2SyntaxPattern> v) {
		instList.add(k);
	}
//
//		@Override
//		protected Popup call() throws Exception {
//			Popup popup = new Popup();
//			popup.getContent().add(instCandidate);
//			return popup;
//		}

	private final ChangeListener<InstructionSet> projectItemSelected = new ChangeListener<InstructionSet>() {
		@Override
		public void changed(ObservableValue<? extends InstructionSet> observable, InstructionSet oldValue, InstructionSet newValue) {
			listItemUnselected(oldValue);
			listItemSelected(newValue);
		}
	};

	private void listItemUnselected(InstructionSet oldValue) {
		if (oldValue != null) {}
	}

	private void listItemSelected(InstructionSet newValue) {
		if (newValue != null) {
			System.out.println(newValue.toString());
			webView.getEngine().load(htmldata);
			despPopup.show(popup, popup.xProperty().get(),popup.yProperty().get());
		}
	}
}

class XCell extends ListCell<InstructionSet> {
	HBox hbox = new HBox();
	//ImageView imageView = new ImageView("null");
	Label leftLabelItem = new Label("(empty)");
	Pane pane = new Pane();
	Label rightLabelItem = new Label("(>)");
	InstructionSet lastItem;

	public XCell() {
		super();
//		imageView.setFitHeight(20.0);
//		imageView.setFitWidth(20.0);
		hbox.getChildren().addAll(leftLabelItem, pane, rightLabelItem);
		HBox.setHgrow(pane, Priority.ALWAYS);
		hbox.focusedProperty().addListener(e->showWebView());
	}
	public void showWebView(){
		System.out.println(leftLabelItem.getText());
	}



	@Override
	protected void updateItem(InstructionSet item, boolean empty) {
		super.updateItem(item, empty);
		setText(null);  // No text in label of super class
		if (empty) {
			lastItem = null;
			setText(null);
			//imageView.setImage(null);
			leftLabelItem.setText(null);
			rightLabelItem.setText(null);
			setGraphic(null);
		} else {
			lastItem = item;
			switch(item.getType()){
			case ASSEMBLER:
				update(item,"assembler.png", item.toString(),"アセンブラ命令");
				break;
			case MACRO:
				update(item,"assembler.png", item.toString(), "マクロ命令");
				break;
			case CPU:
				update(item,"assembler.png", item.toString(), "機械語命令");
				break;
			case UNKNOWN:
				break;
			}
			setGraphic(hbox);
		}
	}
	void update(InstructionSet item,String url,String leftLabel,String rightLabel){
		//imageView.setImage(item!=null ?new Image(url):null);
		leftLabelItem.setText(item!=null ? leftLabel : "<null>");
		rightLabelItem.setText(item!=null ? rightLabel : "<null>");
	}
}*/
