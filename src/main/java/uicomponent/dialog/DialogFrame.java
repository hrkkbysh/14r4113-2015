package uicomponent.dialog;


import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DialogFrame {
	Alert alert;
	Image image;
	String title="",headerText="",contentText="";

	
	public static void showError(String errordesc){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("例外が発生しました");
		alert.setContentText(errordesc);
		alert.showAndWait();
	}
	
	public void showInfomation(Image image,String title) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Infomation");
		alert.getDialogPane().setPrefSize(image.getHeight()+10, image.getWidth()+10);
		alert.setHeaderText(null);
		alert.setContentText(null);
		ImageView tableImage = new ImageView(image);
		alert.setGraphic(tableImage);
		alert.showAndWait();
	}
	public static Optional<String> showLoadSettingDialog(){
		TextInputDialog dialog = new TextInputDialog("100");
		dialog.setTitle("Program Load");
		dialog.setHeaderText("アセンブルした結果で生成されたオブジェクトファイルを記憶装置に読み込みます。\n"
        		+ "リロケーションするにあたって必要な情報を設定してください");
		dialog.setContentText("先頭番地:");
		Optional<String> result = dialog.showAndWait();
		return result;
	}
//	public DialogFrame setTitle(String title){
//		this.title = title;
//		return this;
//	}
	
	
}