package controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import editor.BaseEditor;
import editor.Casl2SyntaxPattern;
import editor.ExtensionCasl2Pattern;
import org.fxmisc.richtext.CodeArea;

import javafx.scene.control.Tab;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Action {
	private Stage stage;
	private ExecutorService service;

	public Action(Stage stage,ExecutorService service){
		this.stage = stage; this.service = service;
	}

	public void readText(CodeArea editor,Charset charset){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select File");

		Window window = editor.getParent().getScene().getWindow();
		File target = fileChooser.showOpenDialog(window);
		if(target!=null && editor != null){
			try {
				Stream<String> lines = Files.lines(target.toPath(),charset);
				lines.forEach(singleLine->{
					editor.appendText(singleLine);
					editor.appendText(System.lineSeparator());});
				lines.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void saveText(CodeArea editor,Charset charset){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save File");

		Window window = editor.getParent().getScene().getWindow();
		File target = fileChooser.showOpenDialog(window);
		if(target!=null && editor != null){
			try {
				Files.write(Paths.get(target.toURI()), editor.getText().getBytes(charset),
						StandardOpenOption.CREATE,StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void readText(Tab tab, Charset charset) {
		BaseEditor<Casl2SyntaxPattern, ExtensionCasl2Pattern> casl2Editor =
				new BaseEditor<>(service, "/Casl2SyntaxHighlighting.css",stage, Casl2SyntaxPattern.class, ExtensionCasl2Pattern.class);

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select File");

		Window window = casl2Editor.getCodeArea().getParent().getScene().getWindow();
		File target = fileChooser.showOpenDialog(window);
		if(target!=null){
			try {
				CodeArea codeArea = casl2Editor.getCodeArea();
				Stream<String> lines = Files.lines(target.toPath(),charset);
				lines.forEach(singleLine->{
					codeArea.appendText(singleLine);
					codeArea.appendText(System.lineSeparator());});
				lines.close();
				Path path = target.toPath();
				tab.setText(path.getFileName().toString());
				tab.setId(path.toString());
				
				tab.setContent(casl2Editor.getCodeArea());
				System.out.println(path.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}