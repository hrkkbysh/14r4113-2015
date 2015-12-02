package controller;
import java.io.*;
/**
 * Sample Skeleton for 'Casl2EditScene.fxml' Controller Class
 */
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import casl2.*;
import casl2.casl2ex.Casl2LexerA;
import casl2.casl2ex.Casl2SymbolA;
import editor.BaseEditor;
import editor.Casl2SyntaxPattern;
import editor.CustomEditor;
import editor.ExtensionCasl2Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import jfxtras.scene.menu.CornerMenu;

public class Casl2EditController extends BorderPane implements Initializable,Controllable<SimSceneType>,Threadable,ModeTogglable{

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML
	private Menu fileMenu;

	@FXML
	private Menu editMenu;
	@FXML
	private Menu compileMenu;
	@FXML
	private Menu settingMenu;
	@FXML
	private SplitPane contentPane;

	@FXML // fx:id="toggleCommentMenuItem"
	private MenuItem toggleCommentMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="setDataVisualMenuItem"
	private MenuItem setDataVisualMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="insertMacroStateButton"
	private Button insertMacroStateButton; // Value injected by FXMLLoader

	@FXML // fx:id="saveMenuItem"
	private MenuItem saveMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="copyMenuItem"
	private MenuItem copyMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="redoButton"
	private Button redoButton; // Value injected by FXMLLoader

	@FXML // fx:id="codingAssistMenuItem"
	private MenuItem codingAssistMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="leftStatus3"
	private Label leftStatus3; // Value injected by FXMLLoader

	@FXML // fx:id="toggleCommentButton"
	private Button toggleCommentButton; // Value injected by FXMLLoader

	@FXML // fx:id="leftStatus1"
	private Label leftStatus1; // Value injected by FXMLLoader

	@FXML // fx:id="leftStatus2"
	private Label leftStatus2; // Value injected by FXMLLoader

	@FXML // fx:id="setLoaderMenuItem"
	private MenuItem setLoaderMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="setDebuggerMenuItem"
	private MenuItem setDebuggerMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="cutMenuItem"
	private MenuItem cutMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="assembleMenuItem"
	private MenuItem assembleMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="copyButton"
	private Button copyButton; // Value injected by FXMLLoader

	@FXML // fx:id="zoomFontSizeSlider"
	private Slider zoomFontSizeSlider; // Value injected by FXMLLoader

	@FXML // fx:id="prettyPrintMenuItem"
	private MenuItem prettyPrintMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="prettyPrintButton"
	private Button prettyPrintButton; // Value injected by FXMLLoader

	@FXML // fx:id="xRefMenuItem"
	private MenuItem xRefMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="openMenuItem"
	private MenuItem openMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="newMenuItem"
	private MenuItem newMenuItem; // Value injected by FXMLLoader


	@FXML // fx:id="redoMenuItem"
	private MenuItem redoMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="pasteMenuItem"
	private MenuItem pasteMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="undoMenuItem"
	private MenuItem undoMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="xRefButton"
	private Button xRefButton; // Value injected by FXMLLoader

	@FXML // fx:id="undoButton"
	private Button undoButton; // Value injected by FXMLLoader

	@FXML // fx:id="openRecentMenu"
	private Menu openRecentMenu; // Value injected by FXMLLoader

	@FXML // fx:id="assembleAllMenuItem"
	private MenuItem assembleAllMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="expansionFontSizeButton"
	private Button expansionFontSizeButton; // Value injected by FXMLLoader

	@FXML // fx:id="editorTabPane"
	private TabPane editorTabPane; // Value injected by FXMLLoader

	@FXML // fx:id="modeToolTip"
	private Tooltip modeToolTip; // Value injected by FXMLLoader

	@FXML // fx:id="openButton"
	private Button openButton; // Value injected by FXMLLoader

	@FXML // fx:id="root"
	private BorderPane root; // Value injected by FXMLLoader

	@FXML // fx:id="helpMenuItem"
	private MenuItem helpMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="saveButton"
	private Button saveButton; // Value injected by FXMLLoader

	@FXML // fx:id="saveAllButton"
	private Button saveAllButton; // Value injected by FXMLLoader

	@FXML // fx:id="saveAsMenuItem"
	private MenuItem saveAsMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="pasteButton"
	private Button pasteButton; // Value injected by FXMLLoader

	@FXML // fx:id="quitMenuItem"
	private MenuItem quitMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="selectAllMenuItem"
	private MenuItem selectAllMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="reductionFontSizeButton"
	private Button reductionFontSizeButton; // Value injected by FXMLLoader

	@FXML // fx:id="modeMenuPane"
	private StackPane modeMenuPane; // Value injected by FXMLLoader

	@FXML // fx:id="consolePane"
	private AnchorPane consolePane; // Value injected by FXMLLoader

	@FXML // fx:id="fileTreeView"
	private TreeView<?> fileTreeView; // Value injected by FXMLLoader

	@FXML // fx:id="newButton"
	private Button newButton; // Value injected by FXMLLoader

	@FXML
	void reductionFontSizeAction(ActionEvent event) {

	}

	@FXML
	void expansionFontSizeAction(ActionEvent event) {

	}

	@FXML
	void newAction(ActionEvent event) {
		CustomEditor<Casl2SyntaxPattern, ExtensionCasl2Pattern> casl2Editor =
				new CustomEditor<>(service, Casl2SyntaxPattern.class, ExtensionCasl2Pattern.class,"/Casl2SyntaxHighLighting.css",sc.getStage());
		casl2Editor.setPath("new Tab");
		Tab tab = new Tab();
		tab.setContent(casl2Editor.getCodeArea());
		editorTabPane.getTabs().add(tab);
		editorMap.put(tab,casl2Editor);
		activeEditor = casl2Editor;
	}

	@FXML
	void openAction(ActionEvent event) {
		CustomEditor<Casl2SyntaxPattern,ExtensionCasl2Pattern> casl2Editor =
				new CustomEditor<>(service, Casl2SyntaxPattern.class, ExtensionCasl2Pattern.class, "/Casl2SyntaxHighlighting.css",sc.getStage());

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select File");

		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Casl2 File", "*.CASL2", "*.cas", "*.txt"),
				new ExtensionFilter("All File", "*.*"));
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		Window window = root.getParent().getScene().getWindow();
		File target = fileChooser.showOpenDialog(window);
		if(target!=null){
			try {
				byte[] bytes = Files.readAllBytes(target.toPath());

				String code = null;
				try {
					code = new String(bytes, "SHIFT_JIS");
				} catch (UnsupportedEncodingException ex) {
					// fallback
					code = new String(bytes);
				}

				casl2Editor.getCodeArea().appendText(code);
				casl2Editor.setPath(target.toPath().toString());
				Tab tab = new Tab(target.getName(),casl2Editor.getCodeArea());
				editorTabPane.getTabs().add(tab);
				editorMap.put(tab, casl2Editor);
				casl2Editor.getUndoManager().mark();
				activeEditor = casl2Editor;
				assembleMenuItem.setDisable(false);
				//markdownEditorPane.setMarkdown(code);
				//markdownEditorPane.getUndoManager().mark();
			} catch (IOException ex) {

				ex.printStackTrace();
			}
		}
	}

	@FXML
	void openRecentAction(ActionEvent event) {

	}

	@FXML
	void saveAction(ActionEvent event) {
		String code =activeEditor.getCodeArea().getText();

		byte[] bytes;
		try {
			bytes = code.getBytes("SHIFT_JIS");
		} catch (UnsupportedEncodingException ex) {
			// fallback
			bytes = code.getBytes();
		}

		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Please Save File.");
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter("Casl2 File", "*.CASL2", "*.cas", "*.txt"),
					new ExtensionFilter("All File", "*.*"));
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
			File file = fileChooser.showSaveDialog( root.getParent().getScene().getWindow());

			if (file == null) return;

			activeEditor.setPath(file.getPath());

			Files.write(Paths.get(activeEditor.getPath()), bytes, StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE);
			activeEditor.getUndoManager().mark();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	void saveAsAction(ActionEvent event) {
		String code =activeEditor.getCodeArea().getText();

		byte[] bytes;
		try {
			bytes = code.getBytes("SHIFT_JIS");
		} catch (UnsupportedEncodingException ex) {
			// fallback
			bytes = code.getBytes();
		}

		try {
			if(activeEditor.getPath()==null) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Save File.");
				fileChooser.getExtensionFilters().addAll(
						new ExtensionFilter("Casl2 File", "*.CASL2", "*.cas", "*.txt"),
						new ExtensionFilter("All File", "*.*"));
				fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
				File file = fileChooser.showSaveDialog( root.getParent().getScene().getWindow());
				if (file == null) return;

				activeEditor.setPath(file.getPath());
			}
			Files.write(Paths.get(activeEditor.getPath()), bytes);
			activeEditor.getUndoManager().mark();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	void quitAction(ActionEvent event) {

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
	void prettyPrintAction(ActionEvent event) {

	}

	@FXML
	void assembleAction(ActionEvent event) {
/*	String code = activeEditor.getCodeArea().getText();
		System.out.println(code);
		Casl2Lexer lexer = new Casl2Lexer(code);
		BinaryGenerator bg = new Comet2BG(new Comet2InstructionTable(),new LabelTable());
		bg.setPath(activeEditor.getPath());
		Casl2Parser parser = new Casl2Parser(lexer,bg);
		parser.enter();*/
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(activeEditor.getPath()));
            Casl2LexerA lexerLexer = new Casl2LexerA(is);
           /* try {
                for(Casl2SymbolA symbol = lexerLexer.nextToken();symbol!=Casl2SymbolA.EOF;symbol = lexerLexer.nextToken() ){
                   System.out.print("Symbol : " + symbol.toString());
                   *//* if(symbol == Casl2Symbol.MACHINEINST || symbol == Casl2Symbol.ASSEMBLERINST  || symbol == Casl2Symbol.LABEL
                            || symbol == Casl2Symbol.MACROINST || symbol == Casl2Symbol.REGISTER || symbol == Casl2Symbol.STR_CONST){
                        System.out.print("{ sval : " + lexerLexer.getSval() + " } ");
                    }else if(symbol == Casl2Symbol.NUM_CONST){
                        System.out.print("{ nval : " + lexerLexer.getNval() +" } ");
                    }*//*
                }
            } *//*catch (IOException e) {
                e.printStackTrace();
            }*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println();
        for(int i = 0; i<70; i++){
            System.out.println("private Casl2Symbol state" +i+"()throws IOException{}");
        }
        for(int i = 0; i<7; i++){
            System.out.println("private Casl2Symbol stateL" +i+"()throws IOException{}");
        }

		/*StringBuilder buf = new StringBuilder();
		for(Token token = lexer.nextToken();token.getSymbol()!= Casl2Symbol.EOF;token = lexer.nextToken(),buf = new StringBuilder()){
			buf.append("[ ");buf.append(token.getToken());
			buf.append(": ");buf.append(token.getSymbol().toString());
			buf.append(" ]");
			System.out.print(buf.toString());
		}*/
	}

	@FXML
	void checkEditorState(ActionEvent event) {

	}

	@FXML
	void assembleAllAction(ActionEvent event) {

	}

	@FXML
	void checkEditorsState(ActionEvent event) {

	}

	@FXML
	void xRefAction(ActionEvent event) {

	}

	@FXML
	void codingAssistAction(ActionEvent event) {

	}

	@FXML
	void setLoaderAction(ActionEvent event) {

	}

	@FXML
	void setDebuggerAction(ActionEvent event) {

	}

	@FXML
	void setDataVisualAction(ActionEvent event) {

	}

	@FXML
	void helpAction(ActionEvent event) {

	}

	//toolbar

	@FXML
	void saveAllAction(ActionEvent event) {

	}

	@FXML
	void insertMacroStateAction(ActionEvent event) {

	}

	//画面遷移
	@FXML // fx:id="gotoLoadModeMenuItem"
	private MenuItem gotoLoadModeMenuItem; // Value injected by FXMLLoader
	@FXML // fx:id="gotoLoadModeButton"
	private Button gotoLoadModeButton; // Value injected by FXMLLoader
	@FXML // fx:id="invokedComet2MenuItem"
	private MenuItem invokedComet2MenuItem; // Value injected by FXMLLoader
	@FXML // fx:id="invokedComet2Button"
	private Button invokedComet2Button; // Value injected by FXMLLoader

	@FXML
	void gotoLoadModeAction(ActionEvent event) {
		sc.setScreen(SimSceneType.RELOCATABLE);
	}
	@FXML
	void invokedComet2Action(ActionEvent event) {
		sc.setScreen(SimSceneType.VISUALIZATION);
	}

	@FXML
	void checkCASL2CodeState(ActionEvent event) {

	}

	@FXML // fx:id="transitionCasl2EditMenuItem"
	private MenuItem transitionCasl2EditMenuItem; // Value injected by FXMLLoadeer
	@FXML // fx:id="transitionCasl2ExtensionMenuItem"
	private MenuItem transitionCasl2ExtensionMenuItem; // Value injected by FXMLLoader
	@FXML // fx:id="transitionComet2EditMenuItem"
	private MenuItem transitionComet2EditMenuItem; // Value injected by FXMLLoader
	@FXML // fx:id="modeIcon"
	private ImageView modeIcon; // Value injected by FXMLLoader
	@FXML // fx:id="normalMode"
	private ImageView normalModeIcon; // Value injected by FXMLLoader
	@FXML // fx:id="extensionModeIcon"
	private ImageView extensionModeIcon; // Value injected by FXMLLoader

	@FXML
	void transitionCasl2EditAction(ActionEvent event) {
		asmMode = AssemblerMode.NORMALCASL2;

		setNormalMode();
	}

	@FXML
	void transitionCasl2ExtensionAction(ActionEvent event) {
		asmMode = AssemblerMode.EXTENSIONCASL2;

		setExtensionMode();
	}

	@FXML
	void transitionComet2EditAction(ActionEvent event) {
		sc.setScreen(SimSceneType.COMET2_EDIT);
	}

	@FXML
	void showModeMenuAction(ActionEvent event) {
		if(modeMenu.shownProperty().get()){modeMenu.hide();}
		else {modeMenu.show();}
	}

	private void setModeMenu() {
		insertMacroStateButton.setVisible(false);
		modeMenu = new CornerMenu(CornerMenu.Location.BOTTOM_LEFT, this.modeMenuPane, true);
		modeMenu.getItems().addAll(transitionCasl2EditMenuItem,transitionCasl2ExtensionMenuItem,transitionComet2EditMenuItem);
		modeMenu.hide();

		switch(asmMode){
			case NORMALCASL2: setNormalMode();
				break;
			case EXTENSIONCASL2: setExtensionMode();
				break;
			default: setNormalMode();
		}
	}

	private void setNormalMode() {
		insertMacroStateButton.setVisible(false);
		modeIcon.setImage(normalModeIcon.getImage());
		modeToolTip.setText(asmMode.getTooltip());

	}
	private void setExtensionMode() {
		insertMacroStateButton.setVisible(true);
		modeIcon.setImage(extensionModeIcon.getImage());
		modeToolTip.setText(asmMode.getTooltip());
	}
	//初期設定
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert toggleCommentMenuItem != null : "fx:id=\"toggleCommentMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert setDataVisualMenuItem != null : "fx:id=\"setDataVisualMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert insertMacroStateButton != null : "fx:id=\"insertMacroStateButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert saveMenuItem != null : "fx:id=\"saveMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert copyMenuItem != null : "fx:id=\"copyMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert invokedComet2MenuItem != null : "fx:id=\"invokedComet2MenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert redoButton != null : "fx:id=\"redoButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert invokedComet2Button != null : "fx:id=\"invokedComet2Button\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert codingAssistMenuItem != null : "fx:id=\"codingAssistMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert leftStatus3 != null : "fx:id=\"leftStatus3\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert toggleCommentButton != null : "fx:id=\"toggleCommentButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert leftStatus1 != null : "fx:id=\"leftStatus1\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert leftStatus2 != null : "fx:id=\"leftStatus2\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert transitionComet2EditMenuItem != null : "fx:id=\"transitionComet2EditMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert setLoaderMenuItem != null : "fx:id=\"setLoaderMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert setDebuggerMenuItem != null : "fx:id=\"setDebuggerMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert cutMenuItem != null : "fx:id=\"cutMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert assembleMenuItem != null : "fx:id=\"assembleMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert copyButton != null : "fx:id=\"copyButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert zoomFontSizeSlider != null : "fx:id=\"zoomFontSizeSlider\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert prettyPrintMenuItem != null : "fx:id=\"prettyPrintMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert prettyPrintButton != null : "fx:id=\"prettyPrintButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert xRefMenuItem != null : "fx:id=\"xRefMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert openMenuItem != null : "fx:id=\"openMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert newMenuItem != null : "fx:id=\"newMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert modeIcon != null : "fx:id=\"modeIcon\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert normalModeIcon != null : "fx:id=\"normalModeIcon\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert extensionModeIcon != null : "fx:id=\"extensionModeIcon\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert redoMenuItem != null : "fx:id=\"redoMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert pasteMenuItem != null : "fx:id=\"pasteMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert undoMenuItem != null : "fx:id=\"undoMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert xRefButton != null : "fx:id=\"xRefButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert undoButton != null : "fx:id=\"undoButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert openRecentMenu != null : "fx:id=\"openRecentMenu\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert assembleAllMenuItem != null : "fx:id=\"assembleAllMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert expansionFontSizeButton != null : "fx:id=\"expansionFontSizeButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert editorTabPane != null : "fx:id=\"editorTabPane\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert gotoLoadModeButton != null : "fx:id=\"gotoLoadModeButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert modeToolTip != null : "fx:id=\"modeToolTip\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert openButton != null : "fx:id=\"openButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert gotoLoadModeMenuItem != null : "fx:id=\"gotoLoadModeMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert helpMenuItem != null : "fx:id=\"helpMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert transitionCasl2ExtensionMenuItem != null : "fx:id=\"transitionCasl2ExtensionMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert saveAllButton != null : "fx:id=\"saveAllButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert saveAsMenuItem != null : "fx:id=\"saveAsMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert pasteButton != null : "fx:id=\"pasteButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert transitionCasl2EditMenuItem != null : "fx:id=\"transitionCasl2EditMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert quitMenuItem != null : "fx:id=\"quitMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert selectAllMenuItem != null : "fx:id=\"selectAllMenuItem\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert reductionFontSizeButton != null : "fx:id=\"reductionFontSizeButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert modeMenuPane != null : "fx:id=\"modeMenuPane\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert consolePane != null : "fx:id=\"consolePane\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert fileTreeView != null : "fx:id=\"fileTreeView\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";
		assert newButton != null : "fx:id=\"newButton\" was not injected: check your FXML file 'Casl2EditScene.fxml'.";

		initMenu();
		initButtons();
		initOther();
		setDisableSliderState(true);
	}

	//骨格の自動生成はここまで
	private RelocatableController rc;
	private VisualizationController vc;
	private ScreensController<SimSceneType> sc;
	private ExecutorService service;
	private CornerMenu modeMenu;

	private AssemblerMode asmMode;

	private ObservableList<BaseEditor> editors;
	private ObservableMap<Tab,CustomEditor> editorMap;
	private CustomEditor<Casl2SyntaxPattern,ExtensionCasl2Pattern> activeEditor;

	public void setRC(RelocatableController rc) {
		this.rc = rc;
	}

	public void setVC(VisualizationController vc) {
		this.vc = vc;
	}

	@Override
	public void setExecutorService(ExecutorService service) {
		this.service = service;
		initEditor();
//		fileTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//		leftStatus1.textProperty().bind(activeFilePath);
	}

	@Override
	public void setScreenParent(ScreensController<SimSceneType> sc) {
		this.sc = sc;
	}

	@Override
	public void setAssemblerMode(AssemblerMode asmMode) {
		this.asmMode = asmMode;
		setModeMenu();
	}
	//アクションのグループ化(アクティブにしたりインアクティブにする、画面を変化させる等)
	//Inner Method
	private void initMenu(){
		setDisableMenuItems(fileMenu.getItems(), true);
		quitMenuItem.setDisable(false);
		newMenuItem.setDisable(false);
		openMenuItem.setDisable(false);
		openRecentMenu.setDisable(false);
		quitMenuItem.setDisable(false);
		editMenu.setDisable(true);
		setDisableMenuItems(compileMenu.getItems(), true);
		settingMenu.setDisable(false);
	}

	private void initButtons(){
		saveButton.setDisable(true);
		saveAllButton.setDisable(true);
		copyButton.setDisable(true);
		pasteButton.setDisable(true);
		undoButton.setDisable(true);
		redoButton.setDisable(true);
		prettyPrintButton.setDisable(true);
		toggleCommentButton.setDisable(true);
		invokedComet2Button.setDisable(true);
		xRefButton.setDisable(true);
		gotoLoadModeButton.setDisable(true);
		insertMacroStateButton.setDisable(true);
	}
	private void initOther(){
		leftStatus1.setText("CASL2プログラミング画面");
		//editorTabPane.setDisable(true);
		//consolePane.setDisable(true);
		//fileTreeView.setDisable(true);

	}
	private void initEditor() {

		contentPane.setDividerPositions(0.01);

		editorMap = FXCollections.observableHashMap();

		editorTabPane.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
		editorTabPane.getSelectionModel().selectedItemProperty().addListener
				((observable, oldValue, newValue) -> {
					if(newValue!=null){
						System.out.println("Tab Selection changed");

						activeEditor = editorMap.get(newValue);

						saveButton.setDisable(false);
						saveAsMenuItem.setDisable(false);
						prettyPrintButton.setDisable(false);
					}
				});
	}

	private void setDisableSliderState(boolean value){
		expansionFontSizeButton.setDisable(value);
		reductionFontSizeButton.setDisable(value);
		zoomFontSizeSlider.setDisable(value);
	}

	private void setDisableMenuItems(List<MenuItem> miList,boolean value){
		for (MenuItem mi : miList) {
			mi.setDisable(value);
		}
	}
}