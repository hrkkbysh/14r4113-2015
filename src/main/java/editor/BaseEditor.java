package editor;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.PopupAlignment;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.fxmisc.undo.UndoManager;
import org.fxmisc.undo.UndoManagerFactory;

public class BaseEditor<E extends Enum<E> & SyntaxPattern>{
	
	private Pattern syntaxRegex;
	private String path;
	
	private CodeArea codeArea;
	private ExecutorService service;
	private Task<StyleSpans<Collection<String>>> task;
	private static int ff= 0;
	
	public BaseEditor(ExecutorService service,Class<E> sr,String cssfile,Stage stage) {
		this.service = service;
		initSyntax(sr);

		codeArea = new CodeArea();

		String styleSheet = sr.getResource(cssfile).toExternalForm();
		codeArea.getStylesheets().add(styleSheet);

		IntFunction<String> format = (digits -> " %" + digits + "d ");
		IntFunction<Node> lineNoFactory = LineNumberFactory.get(codeArea,format);
		IntFunction<Node> bpFactory = new BreakPoint(codeArea.currentParagraphProperty());
        IntFunction<Node> leftHeaderFactory = line -> {
            HBox hbox = new HBox(
                lineNoFactory.apply(line),
                bpFactory.apply(line));
            hbox.setAlignment(Pos.CENTER_LEFT);
            return hbox;
        };
		codeArea.setParagraphGraphicFactory(leftHeaderFactory);
		
		
		//即適用
		codeArea.textProperty().addListener(
				(ObservableValue<? extends String> ov,String ot,String nt)->{
			int cp = codeArea.getCaretPosition();
			String rc = codeArea.getText(cp-1, cp);
			//String caretlineString = codeArea.getText(cc);
			if(nt.length()>ot.length())
				codeArea.replaceText(cp-1, cp, rc.toUpperCase());
			codeArea.setStyleSpans(0,computeHighlighting(codeArea.getText(),sr));
		});
		
		codeArea.caretPositionProperty().addListener(
		(ObservableValue<? extends Integer> observable,
			Integer oldValue, Integer newValue) ->{
				
					//↓これ！！					
					System.out.println(" (ff, codeArea.getCaretPosition():  "+codeArea.getText(ff, newValue));
					
					//System.out.println("codeArea.getText(newValue)"+codeArea.getText(codeArea.getCurrentParagraph()));
					
			});
		codeArea.setStyle(0, Collections.singleton("activeLine"));
	
		codeArea.currentParagraphProperty().addListener((ObservableValue<? extends Integer> observable,
				Integer oldValue, Integer newValue) ->{
					
					ff=0;
					for(int i=0,k=newValue; i < k ; i++){
						ff += codeArea.getText(i).length()+1;
					}
		});
		/*
		CodeAssistant ca = new CodeAssistant();
		codeArea.setPopupWindow(ca.getPopup());
	    codeArea.setPopupAlignment(PopupAlignment.CARET_BOTTOM);

	    codeArea.setOnKeyPressed(e -> onHandleKeyEvent(e, codeArea, ca.getPopup(), stage));*/

	}

	private void initSyntax(Class<E> sp) {
		StringBuilder sb = new StringBuilder();
		for(E csp:sp.getEnumConstants()){
			sb.append("(?<");
			sb.append(csp.toString());
			sb.append(">");
			sb.append(csp.getPattern());
			sb.append(")|");
		}
		sb.deleteCharAt(sb.length()-1);
		syntaxRegex = Pattern.compile(sb.toString());
	}

	public StyleSpans<Collection<String>> computeHighlighting(String text,Class<E> sp){
		Matcher matcher = syntaxRegex.matcher(text);
        int lastKwEnd = 0;
        
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
        	String styleClass=null;
        	for(E csp:sp.getEnumConstants()){
        		if(matcher.group(csp.toString()) !=null)styleClass = csp.toString();
        	}
        	assert styleClass != null;
                   
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);        
        return spansBuilder.create();
	}

	public void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
		codeArea.setStyleSpans(0, highlighting);
	}
	
	private void onHandleKeyEvent(KeyEvent keyEvent, CodeArea area, Popup popup, Stage primaryStage)
	   {

	      if (new KeyCodeCombination(KeyCode.A).match(keyEvent))
	      {
	    	  popup.setWidth(10);
	    	  popup.setHeight(20);
	         area.setPopupAlignment(PopupAlignment.CARET_BOTTOM);
	         popup.show(primaryStage);
	      }
	   }
	

	public CodeArea getCodeArea() { return codeArea;}
	public ExecutorService getExecutor() { return service;}
	public Task<StyleSpans<Collection<String>>> getTask() { return task; }

	public String getPath() {return path;}
	public void setPath(String path) {this.path = path;}

	public UndoManager getUndoManager(){return getCodeArea().getUndoManager();}
}
