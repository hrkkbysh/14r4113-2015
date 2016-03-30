package controller;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import com.sun.javafx.robot.FXRobot;
import comet2casl2.AsmMode;
import comet2casl2.Casl2Parser;
import comet2casl2.MachineObserver;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.StatusBar;
import org.controlsfx.glyphfont.FontAwesome;
//import uicomponent.FileTreeV.FilePathTreeItem;
import uicomponent.SideNode;
import util.DetectUtils;
import netscape.javascript.JSObject;
import static uicomponent.GraphicCreator.*;

public class EditModeController extends BorderPane implements Initializable, Controllable<EditModeScene>,Threadable{

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Menu openRecentMenu;

	@FXML
	private MenuItem toggleCommentMenuItem;

	@FXML
	private MenuItem exAssembleMenuItem;

	@FXML
	private MenuItem saveMenuItem;

	@FXML
	private MenuItem copyMenuItem;

	@FXML
	private Button exploreButton;

	@FXML
	private MenuItem exitMenuItem;

	@FXML
	private MenuItem setEditorMenuItem;

	@FXML
	private BorderPane root;

	@FXML
	private MenuItem helpMenuItem;

	@FXML
	private Button exAssembleButton;

	@FXML
	private StackPane centerPane;

	@FXML
	private Menu fileMenu;

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
	private Button showSetVButton;

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
				curcsName.set(DetectUtils.getEncoding(target));
				currentPath = target.toPath();
				byte[] bytes = Files.readAllBytes(currentPath);
				String code = null;
				try {
					code = new String(bytes, curcsName.get());
				} catch (UnsupportedEncodingException ex) {
					// fallback
					code = new String(bytes);
				}
				code = code.replace("'", "\\'");
				code = code.replace(System.getProperty("line.separator"), "\\n");
				code = code.replace("\n", "\\n");
				code = code.replace("\r", "\\n");
				webEngine.executeScript("editor.setValue('" + code + "',-1)");
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
		//errView.storedLog("MS932");
		errView.getErrorLog().clear();
		try {
			System.out.println(currentPath);
			String code = (String)webEngine.executeScript("editor.getValue()");
			code = code.replaceAll("\n",System.lineSeparator());

			BufferedReader reader = Files.newBufferedReader(currentPath, Charset.forName(curcsName.get()));
			Casl2Parser parser = new Casl2Parser(code, AsmMode.NORMAL);
			parser.decode(currentPath);

			System.out.println("コンパイル完了");
			if(parser.hasError() || parser.hasWarning()) {
				List<String> ems = parser.getMessages();
				for(String s: ems){
					errView.getErrorLog().appendText(s);
					errView.getErrorLog().appendText(System.lineSeparator());
				}
				if (editorPane.isShowDetailNode()) {
					editorPane.setShowDetailNode(true);
				}
			}
			if(!parser.hasError()){
				errView.getErrorLog().appendText("コンパイル完了");
				sc.setScreen(EditModeScene.DEBUG);
				coec.setSimulateMode();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		//errView.stopLog();
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
			assembler.parse(path);
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
		coec.setSimulateMode();
		sc.setScreen(EditModeScene.DEBUG);
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
		curcsName.set("UTF-8");
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
			BufferedWriter writer = Files.newBufferedWriter(currentPath,Charset.forName(curcsName.get()));
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
			BufferedWriter writer = Files.newBufferedWriter(currentPath,Charset.forName(curcsName.get()));
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
		for(RegMember oi:RegMember.values()){
			System.out.println("{\"汎用レジスタ\": \""+oi.toString()+"\"},");
		}*/
		System.out.print("]");
	}

	@FXML
	void exploreAction(ActionEvent event) {
		if(masterDetailPane.isShowDetailNode()) {
			masterDetailPane.setShowDetailNode(false);
		}else{
			masterDetailPane.setShowDetailNode(true);
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert openRecentMenu != null : "fx:id=\"openRecentMenu\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert toggleCommentMenuItem != null : "fx:id=\"toggleCommentMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert exAssembleMenuItem != null : "fx:id=\"exAssembleMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert saveMenuItem != null : "fx:id=\"saveMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert copyMenuItem != null : "fx:id=\"copyMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert exploreButton != null : "fx:id=\"exploreButton\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert exitMenuItem != null : "fx:id=\"exitMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert setEditorMenuItem != null : "fx:id=\"setEditorMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert helpMenuItem != null : "fx:id=\"helpMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert exAssembleButton != null : "fx:id=\"exAssembleButton\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert centerPane != null : "fx:id=\"centerPane\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert fileMenu != null : "fx:id=\"fileMenu\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert saveAsMenuItem != null : "fx:id=\"saveAsMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert assembleButton != null : "fx:id=\"assembleButton\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert setLoaderMenuItem != null : "fx:id=\"setLoaderMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert cutMenuItem != null : "fx:id=\"cutMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert assembleMenuItem != null : "fx:id=\"assembleMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert prettyPrintMenuItem != null : "fx:id=\"prettyPrintMenuItem\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert showSetVButton != null : "fx:id=\"prettyPrintButton\" was not injected: check your FXML file 'EditScene.fxml'.";
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

		exploreButton.setGraphic(createEffectIcon(FontAwesome.Glyph.FOLDER_ALT));
		assembleButton.setGraphic(createEffectIcon(FontAwesome.Glyph.BUG));
		exAssembleButton.setGraphic(createEffectIcon(FontAwesome.Glyph.BUG).color(Color.BLUE));
		xRefButton.setGraphic(createEffectIcon(FontAwesome.Glyph.BOOK));
		showSetVButton.setGraphic(createEffectIcon(FontAwesome.Glyph.COGS));
		gotoHomeButton.setGraphic(createEffectIcon(FontAwesome.Glyph.HOME));

		newMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.FILE_ALT));
		openMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.FOLDER_OPEN_ALT));
		saveMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.SAVE));
		saveAsMenuItem.setGraphic(createIcon(FontAwesome.Glyph.SAVE).color(Color.ALICEBLUE));

		undoMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.ROTATE_LEFT));
		redoMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.ROTATE_RIGHT));
		cutMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.CUT));
		copyMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.COPY));
		pasteMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.PASTE));
		//selectAllMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.SELECT_ALL));
		toggleCommentMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.COMMENTS));
		prettyPrintMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.INDENT));

		assembleMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.BUG));
		exAssembleMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.BUG).color(Color.BLUE));
		xRefMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.BOOK));

		setEditorMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.COGS).color(Color.ALICEBLUE));
		setLoaderMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.COGS));

		helpMenuItem.setGraphic(createEffectIcon(FontAwesome.Glyph.QUESTION_CIRCLE));

		webView = new WebView();
		webEngine = webView.getEngine();
		webEngine.load(getClass().getResource("/conf/ace/Ace.html").toExternalForm());

		editorPane = new MasterDetailPane(Side.BOTTOM);
		editorPane.setMasterNode(webView);
		editorPane.setShowDetailNode(false);

		masterDetailPane = new MasterDetailPane(Side.LEFT);
		masterDetailPane.setAnimated(true);
		masterDetailPane.setShowDetailNode(false);
		masterDetailPane.setMasterNode(editorPane);

		masterDetailPane.getDetailNode().setOnMouseClicked(e->{
			System.out.println("clicked!!");
			final DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("select Directory");
			chooser.setTitle("Select Root Directory");
			File target = chooser.showDialog(stage);
			if(target!=null) {
				TreeItem<String> rootNode = new TreeItem<>(target.getAbsolutePath());
				rootNode.setGraphic(createEffectIcon(FontAwesome.Glyph.FOLDER));
				TreeView<String> treeView = new TreeView<>(rootNode);
				Iterable<Path> rootDirectories = FileSystems.getFileSystem(target.toURI()).getRootDirectories();
				for (Path name : rootDirectories) {
					//FilePathTreeItem treeNode = new FilePathTreeItem(name);
					//rootNode.getChildren().add(treeNode);
				}
				rootNode.setExpanded(true);
				masterDetailPane.setDetailNode(treeView);
			}
		});

		centerPane.getChildren().add(masterDetailPane);

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
						win.setMember("java", new Object() {
							public void showBPShene() {
								System.out.println("gutter clicked!!");
							}
						});
					}
				});

		statusBar = new StatusBar();
		root.setBottom(statusBar);
		Button rightB2 = new Button("",createEffectIcon(FontAwesome.Glyph.INFO));
		Label rightL1 = new Label();
		rightL1.setPadding(new Insets(4.0));
		rightL1.textProperty().bind(curcsName);
		statusBar.getRightItems().addAll(rightL1, new Separator(Orientation.VERTICAL),rightB2);

		errView = new SideNode(" Information View",editorPane);
		rightB2.setOnAction(e -> {
					if (editorPane.isShowDetailNode()) {
						editorPane.setShowDetailNode(false);
					} else {
						editorPane.setShowDetailNode(true);
					}
				}
		);
		statusBar.textProperty().set("CASL2プログラミング画面");
	}
	private void initStatusBar(){
	}
	private void initShortCut() {
		/*robot.keyRelease(KeyEvent.CTRL_DOWN_MASK);
		,robot.keyRelease(KeyEvent.VK_COMMA);*//*

		//if(stage.getScene().impl_processKeyEvent();
		robot = FXRobotFactory.createRobot(stage.getScene());

		setEditorMenuItem.setOnAction(e->{
			robot.keyPress(KeyCode.CONTROL);
			robot.keyPress(KeyCode.COMMA);

			robot.keyRelease(KeyCode.CONTROL);
			robot.keyRelease(KeyCode.COMMA);
		});*/
	}


	//骨格の自動生成はここまで
	private ScreensController<EditModeScene> sc;
	private ExecutorService service;

	private ObservableList<String> editorCodes;
	private Path currentPath;
	private SimpleStringProperty curcsName = new SimpleStringProperty("");
	private FXRobot robot;
	private WebView webView;
	private WebEngine webEngine;
	//PopOverCreator popOverCreator;
	private MasterDetailPane masterDetailPane;
	private MasterDetailPane editorPane;
	private SideNode errView;
	private StatusBar statusBar;
	private String modestr = "CASL2プログラミング画面";
	private String initCode = "TEST	START"+System.lineSeparator()+"	RET"+System.lineSeparator()+"	END";
	private MachineObserver mo;

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
	Stage stage;
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setCOEC(DebugModeController COEC) {
		this.coec = COEC;
		initShortCut();
	}

}
