package uicomponent;

import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.MasterDetailPane;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * @author 14r4113 on 2016/01/10.
 */
public class SideNode extends VBox {

	TextArea errorLog;
	public SideNode(final String title, final MasterDetailPane pane) {
		super();
		Label titleLabel = new Label(title);
		errorLog = new TextArea();
		errorLog.setEditable(false);
		//console = new Console(errorLog);
		setPrefSize(200, 200);

		getChildren().addAll(titleLabel,errorLog);
		pane.setDetailNode(this);
		setOnMouseClicked(e -> {
					if (pane.isShowDetailNode()) {
						titleLabel.setText(title + " (unpinned)");
						pane.setShowDetailNode(false);
					} else {
						titleLabel.setText(title + " (pinned)");
						pane.setShowDetailNode(false);
					}
				}
		);

	}
	public TextArea getErrorLog(){
		return errorLog;
	}

	/*PrintStream backUpO = System.out;
	PrintStream backUpE = System.err;
	PrintStream ps;
	Console console;

	public SideNode(final String title, final Side side, final HiddenSidesPane pane) {
		super();
		Label titleLabel = new Label(title);
		TextArea errorLog = new TextArea();
		errorLog.setEditable(false);
		console = new Console(errorLog);
		setPrefSize(200, 200);

		getChildren().addAll(titleLabel,errorLog);
		setOnMouseClicked(e -> {
					if (pane.getPinnedSide() != null) {
						titleLabel.setText(title + " (unpinned)");
						pane.setPinnedSide(null);
					} else {
						titleLabel.setText(title + " (pinned)");
						pane.setPinnedSide(side);
					}
				}
		);

	}
	public void storedLog(String encoding){
		try {
			ps =  new PrintStream(console, true,encoding);
		} catch (UnsupportedEncodingException e) {
			//fall back
			ps =  new PrintStream(console, true);
		}
		System.setOut(ps);
		System.setErr(ps);
	}

	public void stopLog(){
		ps.close();
		System.setOut(backUpO);
		System.setErr(backUpE);
	}*/
}
