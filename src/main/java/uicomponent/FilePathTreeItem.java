package uicomponent;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import static uicomponent.GraphicCreator.*;

/**
 * @author 14r4113 on 2016/01/29.
 */

public class FilePathTreeItem extends TreeItem<String>{

	public static Glyph folderCollapseImage = createEffectIcon(FontAwesome.Glyph.FOLDER).color(Color.RED);
	public static Glyph folderExpandImage = createEffectIcon(FontAwesome.Glyph.FOLDER).color(Color.BLACK);
	public static Glyph fileImage = createEffectIcon(FontAwesome.Glyph.FOLDER).color(Color.BLUE);

	//this stores the full path to the file or directory

	private String fullPath;

	public String getFullPath(){return(this.fullPath);}

	private boolean isDirectory;

	public boolean isDirectory(){return(this.isDirectory);}


	public FilePathTreeItem(Path file) {

		super(file.toString());

		this.fullPath = file.toString();

		//test if this is a directory and set the icon
		if (Files.isDirectory(file)) {
			this.isDirectory = true;
			this.setGraphic(folderCollapseImage);
		} else {
			this.isDirectory = false;
			this.setGraphic(fileImage);
			//if you want different icons for different file types this is where you'd do it
		}

		//set the value
		if (!fullPath.endsWith(File.separator)) {
			//set the value (which is what is displayed in the tree)
			String value = file.toString();
			int indexOf = value.lastIndexOf(File.separator);
			if (indexOf > 0) {
				this.setValue(value.substring(indexOf + 1));
			} else {
				this.setValue(value);
			}
		}

		this.addEventHandler(TreeItem.branchExpandedEvent(), new EventHandler<TreeModificationEvent<String>>() {
			@Override
			public void handle(TreeModificationEvent<String> e) {
				FilePathTreeItem source = (FilePathTreeItem) e.getSource();
				if (source.isDirectory() && source.isExpanded()) {
					Glyph iv = (Glyph) source.getGraphic();
					iv.setIcon(folderExpandImage);
				}
				try {
					if (source.getChildren().isEmpty()) {
						Path path = Paths.get(source.getFullPath());
						BasicFileAttributes attribs = Files.readAttributes(path, BasicFileAttributes.class);
						if (attribs.isDirectory()) {
							DirectoryStream<Path> dir = Files.newDirectoryStream(path);
							for (Path file : dir) {
								FilePathTreeItem treeNode = new FilePathTreeItem(file);
								source.getChildren().add(treeNode);
							}
						}
					} else {
						//if you want to implement rescanning a directory for changes this would be the place to do it
					}
				} catch (IOException x) {
					x.printStackTrace();
				}
			}
		});

		this.addEventHandler(TreeItem.branchCollapsedEvent(), new EventHandler<TreeModificationEvent<String>>() {
			@Override
			public void handle(TreeModificationEvent<String> e) {
				FilePathTreeItem source = (FilePathTreeItem) e.getSource();
				if (source.isDirectory() && !source.isExpanded()) {
					Glyph iv = (Glyph) source.getGraphic();
					iv.setIcon(folderCollapseImage);
				}
			}
		});
	}

	/*try {
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
		if (!webView.isVisible())
			webView.setVisible(true);
	} catch (IOException ex) {
		ex.printStackTrace();
	}*/
}