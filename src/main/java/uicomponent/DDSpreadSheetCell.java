package uicomponent;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableSet;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;

import java.util.Optional;

public class DDSpreadSheetCell implements SpreadsheetCell {
	@Override
	public boolean match(SpreadsheetCell spreadsheetCell) {
		return false;
	}

	@Override
	public void setItem(Object o) {

	}

	@Override
	public Object getItem() {
		return null;
	}

	@Override
	public ObjectProperty<Object> itemProperty() {
		return null;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public void setEditable(boolean b) {

	}

	@Override
	public boolean isWrapText() {
		return false;
	}

	@Override
	public void setWrapText(boolean b) {

	}

	@Override
	public void setStyle(String s) {

	}

	@Override
	public String getStyle() {
		return null;
	}

	@Override
	public StringProperty styleProperty() {
		return null;
	}

	@Override
	public void activateCorner(CornerPosition cornerPosition) {

	}

	@Override
	public void deactivateCorner(CornerPosition cornerPosition) {

	}

	@Override
	public boolean isCornerActivated(CornerPosition cornerPosition) {
		return false;
	}

	@Override
	public StringProperty formatProperty() {
		return null;
	}

	@Override
	public String getFormat() {
		return null;
	}

	@Override
	public void setFormat(String s) {

	}

	@Override
	public ReadOnlyStringProperty textProperty() {
		return null;
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public SpreadsheetCellType getCellType() {
		return null;
	}

	@Override
	public int getRow() {
		return 0;
	}

	@Override
	public int getColumn() {
		return 0;
	}

	@Override
	public int getRowSpan() {
		return 0;
	}

	@Override
	public void setRowSpan(int i) {

	}

	@Override
	public int getColumnSpan() {
		return 0;
	}

	@Override
	public void setColumnSpan(int i) {

	}

	@Override
	public ObservableSet<String> getStyleClass() {
		return null;
	}

	@Override
	public ObjectProperty<Node> graphicProperty() {
		return null;
	}

	@Override
	public void setGraphic(Node node) {

	}

	@Override
	public Node getGraphic() {
		return null;
	}

	@Override
	public Optional<String> getTooltip() {
		return null;
	}

	@Override
	public void addEventHandler(EventType<Event> eventType, EventHandler<Event> eventHandler) {

	}

	@Override
	public void removeEventHandler(EventType<Event> eventType, EventHandler<Event> eventHandler) {

	}
}
