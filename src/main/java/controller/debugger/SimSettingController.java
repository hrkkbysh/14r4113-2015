package controller.debugger;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PropertySheet;
import sun.plugin.javascript.navig.Anchor;

/**
 * @author 14r4113 on 2016/01/08.
 */
public class SimSettingController implements Initializable{

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane root;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<PropertySheet.Item> list = FXCollections.observableArrayList();
		list.addAll(customDataMap.keySet().stream().map(CustomPropertyItem::new).collect(Collectors.toList()));
		propertySheet = new PropertySheet(list);
		root.getChildren().add(propertySheet);
		AnchorPane.setTopAnchor(propertySheet,0.0);
		AnchorPane.setBottomAnchor(propertySheet,0.0);
		AnchorPane.setRightAnchor(propertySheet,0.0);
		AnchorPane.setLeftAnchor(propertySheet,0.0);
	}
	private PropertySheet propertySheet;
	private static Map<String, Object> customDataMap = new LinkedHashMap<>();

	static {
		customDataMap.put("1. ロード設定#開始", "1000");
		customDataMap.put("1. ロード設定#オプション", "Giles");
		customDataMap.put("2. ブレークポイント#行", "");
/*		customDataMap.put("2. 停止条件文#Address 2", "");
		customDataMap.put("2. Billing Address#City", "");
		customDataMap.put("2. Billing Address#State", "");
		customDataMap.put("2. Billing Address#Zip", "");
		customDataMap.put("3. Phone#Home", "123-123-1234");
		customDataMap.put("3. Phone#Mobile", "234-234-2345");
		customDataMap.put("3. Phone#Work", "");*/
	}

	class CustomPropertyItem implements PropertySheet.Item {

		private String key;
		private String category, name;

		public CustomPropertyItem(String key) {
			this.key = key;
			String[] skey = key.split("#");
			category = skey[0];
			name = skey[1];
		}

		@Override
		public Class<?> getType() {
			return customDataMap.get(key).getClass();
		}

		@Override
		public String getCategory() {
			return category;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public Object getValue() {
			return customDataMap.get(key);
		}

		@Override
		public void setValue(Object value) {
			customDataMap.put(key, value);
		}

		@Override
		public Optional<ObservableValue<? extends Object>> getObservableValue() {
			return Optional.empty();
		}

	}


}