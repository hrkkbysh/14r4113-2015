package controller.debugger;

/**
 * @author 14r4113 on 2016/01/08.
 */

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

public class VisL1SceneController implements Initializable{

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private GridPane regGrid;

	@FXML
	private GridPane memGrid;

	@FXML
	private GridPane memHeader;

	@FXML
	private GridPane regHeader;

	@FXML
	private AnchorPane regPane;

	@FXML private SpreadsheetView spreadsheet;

	private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
	private GlyphFont icoMoon = GlyphFontRegistry.font("icomoon");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert regGrid != null : "fx:id=\"regGrid\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert memGrid != null : "fx:id=\"memGrid\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert memHeader != null : "fx:id=\"memHeader\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert regHeader != null : "fx:id=\"regHeader\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert regPane != null : "fx:id=\"regPane\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		HBox hBox1 = new HBox();
		hBox1.getChildren().add(fontAwesome.create(FontAwesome.Glyph.TABLE).size(16.0));
		Label l1 = new Label("Memory");
		l1.setPadding(new Insets(0.0,10.0,0.0,10.0));
		hBox1.getChildren().add(l1);
		memGrid.add(hBox1, 0, 0);

		JFXComboBox<Label> jfxCombo = new JFXComboBox<>();
		jfxCombo.getItems().add(new Label("#1000"));
		jfxCombo.getItems().add(new Label("#2000"));
		jfxCombo.getItems().add(new Label("#3000"));
		jfxCombo.getItems().add(new Label("#4000"));

		jfxCombo.setEditable(true);
		jfxCombo.setPromptText("Select Label");
		hBox1.getChildren().add(jfxCombo);
		//memGrid.add(new VBox(jfxCombo),1,0);

		HBox hBox2 = new HBox();
		hBox2.getChildren().add(fontAwesome.create(FontAwesome.Glyph.TABLE).size(16.0));
		Label l2 = new Label("Register");
		l2.setPadding(new Insets(0.0,10.0,0.0,10.0));
		hBox2.getChildren().add(l2);
		regGrid.add(hBox2, 0, 0);

		int rowCount = 2;
		int columnCount = 10;
		GridBase grid = new GridBase(rowCount, columnCount);

		ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
		for (int row = 0; row < grid.getRowCount(); ++row) {
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
			for (int column = 0; column < grid.getColumnCount(); ++column) {
				list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"value"));
			}
			rows.add(list);
		}
		grid.setRows(rows);
		spreadsheet.setGrid(grid);
	}
		/*// Add columns
		TableColumn<MemoryTableData,String> c1 = new TableColumn<>("アドレス");
		c1.setCellValueFactory(new PropertyValueFactory<>("address"));
		c1.setCellFactory(new TextFieldCellFactory());

		TableColumn<MemoryTableData,String> c2 = new TableColumn<>("主記憶");
		c2.setCellValueFactory(new PropertyValueFactory<>("content"));
		c2.setCellFactory(new TextFieldCellFactory());

		memoryTable.getColumns().addAll(c1,c2);
		ObservableList<MemoryTableData> data = memoryTable.getItems();
		for(int i = 0; i < 65535 ; i++ ){
			data.add(new MemoryTableData("", ""));
			//data.add(new MemoryTableData("",""));
		}
	}

	public void setEditMode() {
	}

	public static class MemoryTableData{

		private final SimpleStringProperty address;
		private final SimpleStringProperty content;

		public MemoryTableData(String address, String content) {
			this.address = new SimpleStringProperty(address);
			this.content = new SimpleStringProperty(content);
		}

		public String getAddress() {
			return address.get();
		}

		public SimpleStringProperty addressProperty() {
			return address;
		}

		public void setAddress(String address) {
			this.address.set(address);
		}

		public String getContent() {
			return content.get();
		}

		public SimpleStringProperty contentProperty() {
			return content;
		}

		public void setContent(String content) {
			this.content.set(content);
		}
	}
	public static class TextFieldCellFactory
			implements Callback<TableColumn<MemoryTableData,String>,TableCell<MemoryTableData,String>> {

		@Override
		public TableCell<MemoryTableData, String> call(TableColumn<MemoryTableData, String> param) {
			return new TextFieldCell();
		}

		public static class TextFieldCell extends TableCell<MemoryTableData,String> {
			private TextField textField;
			private StringProperty boundToCurrently = null;

			public TextFieldCell() {
				String strCss;
				// Padding in Text field cell is not wanted - we want the Textfield itself to "be"
				// The cell.  Though, this is aesthetic only.  to each his own.  comment out
				// to revert back.
				strCss = "-fx-padding: 0;";


				this.setStyle(strCss);

				textField = new TextField();

				//
				// Default style pulled from caspian.css. Used to play around with the inset background colors
				// ---trying to produce a text box without borders
				strCss = "" +
						//"-fx-background-color: -fx-shadow-highlight-color, -fx-text-box-border, -fx-control-inner-background;" +
						"-fx-background-color: -fx-control-inner-background;" +
						//"-fx-background-insets: 0, 1, 2;" +
						"-fx-background-insets: 0;" +
						//"-fx-background-radius: 3, 2, 2;" +
						"-fx-background-radius: 0;" +
						"-fx-padding: 3 5 3 5;" +   *//**//*Play with this value to center the text depending on cell height??*//**//*
						//"-fx-padding: 0 0 0 0;" +
						"-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);" +
						"-fx-cursor: text;" +
						"";
				// Focused and hover states should be set in the CSS.  This is just a test
				// to see what happens when we set the style in code
				textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
					TextField tf = (TextField)getGraphic();
					String strStyleGotFocus = "-fx-background-color: purple, -fx-text-box-border, -fx-control-inner-background;" +
							"-fx-background-insets: -0.4, 1, 2;" +
							"-fx-background-radius: 3.4, 2, 2;";
					String strStyleLostFocus = //"-fx-background-color: -fx-shadow-highlight-color, -fx-text-box-border, -fx-control-inner-background;" +
							"-fx-background-color: -fx-control-inner-background;" +
									//"-fx-background-insets: 0, 1, 2;" +
									"-fx-background-insets: 0;" +
									//"-fx-background-radius: 3, 2, 2;" +
									"-fx-background-radius: 0;" +
									"-fx-padding: 3 5 3 5;" +   *//**//**//**//*
									//"-fx-padding: 0 0 0 0;" +
									"-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);" +
									"-fx-cursor: text;" +
									"";
					if(newValue)
						tf.setStyle(strStyleGotFocus);
					else
						tf.setStyle(strStyleLostFocus);
				});
				textField.hoverProperty().addListener((observable, oldValue, newValue) -> {
					TextField tf = (TextField)getGraphic();
					String strStyleGotHover = "-fx-background-color: derive(purple,90%), -fx-text-box-border, derive(-fx-control-inner-background, 10%);" +
							"-fx-background-insets: 1, 2.8, 3.8;" +
							"-fx-background-radius: 3.4, 2, 2;";
					String strStyleLostHover = //"-fx-background-color: -fx-shadow-highlight-color, -fx-text-box-border, -fx-control-inner-background;" +
							"-fx-background-color: -fx-control-inner-background;" +
									//"-fx-background-insets: 0, 1, 2;" +
									"-fx-background-insets: 0;" +
									//"-fx-background-radius: 3, 2, 2;" +
									"-fx-background-radius: 0;" +
									"-fx-padding: 3 5 3 5;" +   *//**//**//**//*
									//"-fx-padding: 0 0 0 0;" +
									"-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);" +
									"-fx-cursor: text;" +
									"";
					String strStyleHasFocus = "-fx-background-color: purple, -fx-text-box-border, -fx-control-inner-background;" +
							"-fx-background-insets: -0.4, 1, 2;" +
							"-fx-background-radius: 3.4, 2, 2;";
					if(newValue) {
						tf.setStyle(strStyleGotHover);
					}
					else {
						if(!tf.focusedProperty().get()) {
							tf.setStyle(strStyleLostHover);
						}
						else {
							tf.setStyle(strStyleHasFocus);
						}
					}

				});
				textField.setStyle(strCss);
				this.setGraphic(textField);
			}

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if(!empty) {
					// Show the Text Field
					this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

					// Retrieve the actual String Property that should be bound to the TextField
					// If the TextField is currently bound to a different StringProperty
					// Unbind the old property and rebind to the new one
					ObservableValue<String> ov = getTableColumn().getCellObservableValue(getIndex());
					SimpleStringProperty sp = (SimpleStringProperty)ov;

					if(this.boundToCurrently==null) {
						this.boundToCurrently = sp;
						this.textField.textProperty().bindBidirectional(sp);
					}
					else {
						if(this.boundToCurrently != sp) {
							this.textField.textProperty().unbindBidirectional(this.boundToCurrently);
							this.boundToCurrently = sp;
							this.textField.textProperty().bindBidirectional(this.boundToCurrently);
						}
					}
					System.out.println("item=" + item + " ObservableValue<String>=" + ov.getValue());
					//this.textField.setText(item);  // No longer need this!!!
				}
				else {
					this.setContentDisplay(ContentDisplay.TEXT_ONLY);
				}
			}

		}
	}
	public static void printNodeKidsRecursively(Node n, String tabs) {
		String toTab = tabs == null ? "" : tabs;
		String msg1 = toTab + n.getClass().getName();
		String msg2 = ":" + n.toString();

		// Spit out and text data from Text classes
		if(javafx.scene.text.Text.class.isAssignableFrom(n.getClass())) {
			javafx.scene.text.Text t = (javafx.scene.text.Text)n;
			msg2 += " \"" +t.getText() + "\"";
		}

		// if this Node does not extend from Parent, then it can't have kids.
		if(!Parent.class.isAssignableFrom(n.getClass())) {
			System.out.println(msg1+msg2);
			return;
		}

		Parent p = (Parent)n;
		System.out.println(toTab + n.getClass().getName() +
				"(KIDS=" +
				Integer.toString(p.getChildrenUnmodifiable().size()) + ")" +
				msg2);

		ObservableList<Node> kids = p.getChildrenUnmodifiable();
		toTab +="  ";
		for(Node n2 : kids) {
			printNodeKidsRecursively(n2, toTab);
		}
	}*/
}

