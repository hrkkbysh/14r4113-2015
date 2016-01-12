package uicomponent;

import javafx.scene.control.TextArea;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
/**
 * @author 14r4113 on 2016/01/10.
 */
public class Console extends OutputStream {

	private TextArea output;

	public Console(TextArea ta) {

		this.output = ta;
	}

	@Override
	public void write(int i) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(48).putInt(i);
		String s = null;
		try {
			s = new String(byteBuffer.array(), "MS932");
		} catch (UnsupportedEncodingException e) {
			s = new String(byteBuffer.array());
		}
		output.appendText(s);
	}
/*
	public void write(char[] buf, int off, int len) {
		String s = new String(buf, off, len,"UTF-8");
		output.appendText(s);
	}*/
}
