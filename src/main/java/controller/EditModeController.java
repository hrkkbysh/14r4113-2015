package controller;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.controlsfx.control.PopOver;
import util.DetectUtils;
import netscape.javascript.JSObject;

public class EditModeController extends BorderPane implements Initializable,Controllable<EditModeScene>,Threadable{

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Menu openRecentMenu;

	@FXML
	private MenuItem toggleCommentMenuItem;

	@FXML
	private MenuItem assembleAllMenuItem;

	@FXML
	private MenuItem saveMenuItem;

	@FXML
	private MenuItem copyMenuItem;

	@FXML
	private Button exploreButton;

	@FXML
	private MenuItem exitMenuItem;

	@FXML
	private MenuItem codingAssistMenuItem;

	@FXML
	private BorderPane root;

	@FXML
	private MenuItem helpMenuItem;

	@FXML
	private Button exAssembleButton;

	@FXML
	private Label leftStatus3;

	@FXML
	private AnchorPane centerPane;

	@FXML
	private Label leftStatus1;

	@FXML
	private Menu fileMenu;

	@FXML
	private Label leftStatus2;

	@FXML
	private MenuItem saveAsMenuItem;

	@FXML
	private Button assembleButton;

	@FXML
	private MenuItem setLoaderMenuItem;

	@FXML
	private MenuItem cutMenuItem;

	@FXML
	private MenuItem assembleMenuItem;

	@FXML
	private MenuItem prettyPrintMenuItem;

	@FXML
	private Button prettyPrintButton;

	@FXML
	private MenuItem selectAllMenuItem;

	@FXML
	private Menu compileMenu;

	@FXML
	private Button gotoHomeButton;

	@FXML
	private Menu editMenu;

	@FXML
	private MenuItem xRefMenuItem;

	@FXML
	private MenuItem openMenuItem;

	@FXML
	private MenuItem newMenuItem;

	@FXML
	private MenuItem redoMenuItem;

	@FXML
	private MenuItem pasteMenuItem;

	@FXML
	private Button xRefButton;

	@FXML
	private MenuItem undoMenuItem;
	private DebugModeController coec;

	@FXML
	void openAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Casl2 File", "*.CASL2", "*.cas", "*.txt"),
				new FileChooser.ExtensionFilter("All File", "*.*"));
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		Window window = root.getParent().getScene().getWindow();
		File target = fileChooser.showOpenDialog(window);
		if(target!=null){
			try {
				curcsName = DetectUtils.getEncoding(target);
				currentPath = target.toPath();
				byte[] bytes = Files.readAllBytes(currentPath);
				String code = null;
				try {
					code = new String(bytes, curcsName);
					leftStatus3.setText(curcsName);
				} catch (UnsupportedEncodingException ex) {
					// fallback
					code = new String(bytes);
				}
				code = code.replace("'", "\\'");
				code = code.replace(System.getProperty("line.separator"), "\\n");
				code = code.replace("\n", "\\n");
				code = code.replace("\r", "\\n");
				webEngine.executeScript("editor.setValue('" + code + "',-1)");
				leftStatus3.setText(Charset.defaultCharset().displayName());
				assembleMenuItem.setDisable(false);
				if(!webView.isVisible())
					webView.setVisible(true);
			} catch (IOException ex) {

				ex.printStackTrace();
			}
		}
	}

	@FXML
	void prettyPrintAction(ActionEvent event) {

	}

	@FXML
	void assembleAction(ActionEvent event) {
		/*try {
			System.out.println(activeEditor.getPath());
			BufferedReader reader = Files.newBufferedReader(Paths.get(activeEditor.getPath()), activeEditor.getCharset());
			Path path = null;
			Casl2Parser parser = new Casl2Parser(reader, AsmMode.NORMAL);
			parser.enter(activeEditor.getPath());
			System.out.println("コンパイル完了");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	@FXML
	void exAssembleAction(ActionEvent event) {
		/*try {
			System.out.println(activeEditor.getPath());
			BufferedReader reader = Files.newBufferedReader(Paths.get(activeEditor.getPath()), activeEditor.getCharset());
			Path path;
			System.out.println(activeEditor.getCodeArea().getText());
			MacroAssembler assembler = new MacroAssembler(reader);
			path = Files.createTempFile("tmp", ".tmp");
			assembler.checkMacro(path);
			if (assembler.hasError()) {
				return;
			}else{
				reader = Files.newBufferedReader(path);
			}

			Casl2Parser parser = new Casl2Parser(reader,AsmMode.EXTEND);
			parser.enter(activeEditor.getPath());*//*
			emView.getItems().clear();
			if (parser.hasError() || parser.hasWarning()) {
				emView.getItems().addAll(parser.getErrorMessages());
				emView.getItems().addAll(parser.getWarningMessages());
			}*//*
			path.toFile().deleteOnExit();
			System.out.println("コンパイル完了");

		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	@FXML
	void xRefAction(ActionEvent event) {

	}

	@FXML
	void gotoHomeAction(ActionEvent event) {
		sc.setScreen(EditModeScene.ROOT);
	}

	@FXML
	void newAction(ActionEvent event){
		currentPath = null;
		initCode = initCode.replace("'", "\\'");
		initCode = initCode.replace(System.getProperty("line.separator"), "\\n");
		initCode = initCode.replace("\n", "\\n");
		initCode = initCode.replace("\r", "\\n");
		webEngine.executeScript("editor.setValue('" + initCode + "',-1)");
		if(!webView.isVisible())
			webView.setVisible(true);
		leftStatus3.setText(curcsName);
	}

	@FXML
	void saveAction(ActionEvent event) {
		try {
			if(currentPath==null) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Save File.");
				fileChooser.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter("Casl2 File", "*.CASL2", "*.cas", "*.txt"),
						new FileChooser.ExtensionFilter("All File", "*.*"));
				fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
				File file = fileChooser.showSaveDialog( root.getParent().getScene().getWindow());
				if (file == null) return;
				currentPath = file.toPath();
			}
			BufferedWriter writer = Files.newBufferedWriter(currentPath,Charset.forName(curcsName));
			String code = (String)webEngine.executeScript("editor.getValue()");
			code = code.replaceAll("\n",System.lineSeparator());
			writer.write(code);
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	void saveAsAction(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Please Save File.");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Casl2 File", "*.CASL2", "*.cas", "*.txt"),
					new FileChooser.ExtensionFilter("All File", "*.*"));
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
			File file = fileChooser.showSaveDialog(root.getParent().getScene().getWindow());
			if (file == null) return;
			currentPath = file.toPath();
			BufferedWriter writer = Files.newBufferedWriter(currentPath,Charset.forName(curcsName));
			String code = (String)webEngine.executeScript("editor.getValue()");
			code = code.replaceAll("\n",System.lineSeparator());
			writer.write(code);
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	void exitAction(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void undoAction(ActionEvent event) {

	}

	@FXML
	void redoAction(ActionEvent event) {

	}

	@FXML
	void cutAction(ActionEvent event) {

	}

	@FXML
	void copyAction(ActionEvent event) {

	}

	@FXML
	void pasteAction(ActionEvent event) {

	}

	@FXML
	void selectAllAction(ActionEvent event) {

	}

	@FXML
	void toggleCommentAction(ActionEvent event) {

	}


	@FXML
	void checkEditorState(ActionEvent event) {

	}

	@FXML
	void assembleAllAction(ActionEvent event) {
	 sc.setScreen(EditModeScene.DEBUG);
	}

	@FXML
	void checkEditorsState(ActionEvent event) {

	}

	@FXML
	void setCAAction(ActionEvent event) {

	}

	@FXML
	void setLoaderAction(ActionEvent event) {

	}

	@FXML
	void helpAction(ActionEvent event) {
		System.out.print("[");
		/*for(ObjectInstruction oi:ObjectInstruction.values()){
			System.out.println("{\"機械語命令\": \""+oi.toString()+"\"},");
		}
		for(AssemblyInstrution oi:AssemblyInstrution.values()){
			System.out.println("{\"アセンブラ命令\": \""+oi.toString()+"\"},");
		}
		for(MacroInstruction oi:MacroInstruction.values()){
			System.out.println("{\"マクロ命令\": \""+oi.toString()+"\"},");
		}
		for(Comet2Register oi:Comet2Register.values()){
			System.out.println("{\"汎用レジスタ\": \""+oi.toString()+"\"},");
		}*/
		System.out.print("]");
	}

	@FXML
	void exploreAction(ActionEvent event) {

	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert openRecentMenu != null : "fx:id=\"openRecentMenu\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert toggleCommentMenuItem != null : "fx:id=\"toggleCommentMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert assembleAllMenuItem != null : "fx:id=\"assembleAllMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert saveMenuItem != null : "fx:id=\"saveMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert copyMenuItem != null : "fx:id=\"copyMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert exploreButton != null : "fx:id=\"exploreButton\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert exitMenuItem != null : "fx:id=\"exitMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert codingAssistMenuItem != null : "fx:id=\"codingAssistMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert helpMenuItem != null : "fx:id=\"helpMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert exAssembleButton != null : "fx:id=\"exAssembleButton\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert leftStatus3 != null : "fx:id=\"leftStatus3\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert centerPane != null : "fx:id=\"centerPane\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert leftStatus1 != null : "fx:id=\"leftStatus1\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert fileMenu != null : "fx:id=\"fileMenu\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert leftStatus2 != null : "fx:id=\"leftStatus2\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert saveAsMenuItem != null : "fx:id=\"saveAsMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert assembleButton != null : "fx:id=\"assembleButton\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert setLoaderMenuItem != null : "fx:id=\"setLoaderMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert cutMenuItem != null : "fx:id=\"cutMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert assembleMenuItem != null : "fx:id=\"assembleMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert prettyPrintMenuItem != null : "fx:id=\"prettyPrintMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert prettyPrintButton != null : "fx:id=\"prettyPrintButton\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert selectAllMenuItem != null : "fx:id=\"selectAllMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert compileMenu != null : "fx:id=\"compileMenu\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert gotoHomeButton != null : "fx:id=\"gotoHomeButton\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert editMenu != null : "fx:id=\"editMenu\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert xRefMenuItem != null : "fx:id=\"xRefMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert openMenuItem != null : "fx:id=\"openMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert newMenuItem != null : "fx:id=\"newMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert redoMenuItem != null : "fx:id=\"redoMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert pasteMenuItem != null : "fx:id=\"pasteMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert xRefButton != null : "fx:id=\"xRefButton\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert undoMenuItem != null : "fx:id=\"undoMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";

		webView = new WebView();
		webEngine = webView.getEngine();
		webEngine.load(getClass().getResource("/conf/ace/Ace.html").toExternalForm());
		//webView.setMaxHeight(500);
		/*centerPane.maxHeightProperty().bind(webView.heightProperty());
		centerPane.maxWidthProperty().bind(webView.widthProperty());*/
		webView.maxHeightProperty().bind(centerPane.heightProperty());
		webView.maxWidthProperty().bind(centerPane.widthProperty());
		webView.prefWidthProperty().bind(centerPane.widthProperty());
		webView.prefHeightProperty().bind(centerPane.heightProperty());
		centerPane.getChildren().add(webView);
		webView.setVisible(false);
		webEngine.setJavaScriptEnabled(true);
		webEngine.getLoadWorker().stateProperty().addListener(
				(ObservableValue<? extends State> p, State oldState, State newState) -> {
			if (newState == Worker.State.SUCCEEDED) {
				JSObject win = (JSObject) webEngine.executeScript("window");
				win.setMember("java", new Object() {
					public void paste() {
						String content = (String) Clipboard.getSystemClipboard().getContent(DataFormat.PLAIN_TEXT);
						if (content != null) {
							webEngine.executeScript(" pasteContent(\"" + content.replace("\n", "\\n") + "\") ");
						}
					}
				});
			}
		});
		initShortCut();
	}
	private void initShortCut() {
		final KeyCombination saveSC = new KeyCodeCombination(KeyCode.S,
				KeyCombination.CONTROL_DOWN,KeyCodeCombination.SHIFT_DOWN);
		saveMenuItem.acceleratorProperty().set(saveSC);

		final KeyCombination saveasSC = new KeyCodeCombination(KeyCode.S,
				KeyCombination.CONTROL_DOWN);
		saveMenuItem.acceleratorProperty().set(saveasSC);

		final KeyCombination openSC = new KeyCodeCombination(KeyCode.O,
				KeyCombination.CONTROL_DOWN);
		openMenuItem.acceleratorProperty().set(openSC);

		final KeyCombination newSC = new KeyCodeCombination(KeyCode.N,
				KeyCombination.CONTROL_DOWN);
		newMenuItem.acceleratorProperty().set(newSC);
	}


	//骨格の自動生成はここまで
	private ScreensController<EditModeScene> sc;
	private ExecutorService service;

	private ObservableList<String> editorCodes;
	private Path currentPath;
	private String curcsName = "SHIFT-JIS";
	private WebView webView;
	private WebEngine webEngine;

	private String initCode = "TEST	START"+System.lineSeparator()+"	RET"+System.lineSeparator()+"	END";

	@Override
	public void setExecutorService(ExecutorService service) {
		this.service = service;
//		fileTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//		leftStatus1.textProperty().bind(activeFilePath);
	}

	@Override
	public void setScreenParent(ScreensController<EditModeScene> sc) {
		this.sc = sc;
	}


	private void initOther(){
		leftStatus1.setText("CASL2プログラミング画面");
		//editorTabPane.setDisable(true);
		//consolePane.setDisable(true);
		//fileTreeView.setDisable(true);

	}

	public void setCOEC(DebugModeController COEC) {
		this.coec = COEC;
	}
}
