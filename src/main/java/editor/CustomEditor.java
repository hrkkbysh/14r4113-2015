package editor;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;


public class CustomEditor<E1 extends Enum<E1> & SyntaxPattern,E2 extends Enum<E2> & SyntaxPattern> extends BaseEditor<E1>{
	
	private boolean sw=true;
	private ChangeListener<String> extCL;
//	public CustomEditor(ExecutorService service, Class<E1> sr1,String cssfile) {
//		super(service, sr1, cssfile);
//	}
	public CustomEditor(ExecutorService service, Class<E1> sr1,Class<E2> sr2,String cssfile,Stage stage){
		super(service, sr1, cssfile, stage);
		CodeArea codeArea = super.getCodeArea();
		extCL = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String ot, String nt) {
				codeArea.setStyleSpans(0,computeHighlighting(codeArea.getText(),sr2,initSyntax(sr2)));
			}
		};
	}
	public void switchSyntaxRule(){
		CodeArea codeArea = super.getCodeArea();
		if(sw){ codeArea.textProperty().addListener(extCL);
		System.out.println("extCL is applyed");}
		else{ codeArea.textProperty().removeListener(extCL);}
		sw = !sw;
	}
	
	private Pattern initSyntax(Class<E2> sp) {
		StringBuilder sb = new StringBuilder();
		for(E2 csp:sp.getEnumConstants()){
			sb.append("(?<");
			sb.append(csp.toString());
			sb.append(">");
			sb.append(csp.getPattern());
			sb.append(")|");
		}
		sb.deleteCharAt(sb.length()-1);
		return Pattern.compile(sb.toString());
	}

	public StyleSpans<Collection<String>> computeHighlighting(String text,Class<E2> sp,Pattern pattern){
		Matcher matcher = pattern.matcher(text);
        int lastKwEnd = 0;
        
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
        	String styleClass=null;
        	for(E2 csp:sp.getEnumConstants()){
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
}