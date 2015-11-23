package casl2;

import assembler.Token;
import javafx.scene.control.Tab;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ErrorTable {

	private static Map<Integer, String> errorDict = new HashMap<>();
	private static Map<Integer, String> warningDict = new HashMap<>();
	private static List<String> errorMessages = new ArrayList<>();
	private static List<String> warningMessages = new ArrayList<>();
	private int errorCount=0;
	private int warningCount =0;

	static{
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"resource/ErrorDict.txt"));

			String code = null;
			try {
				code = new String(bytes, "SHIFT_JIS");
			} catch (UnsupportedEncodingException ex) {
				// fallback
				code = new String(bytes);
			}

			String[] lines = code.split(System.lineSeparator());
			int i = 0;
			if(lines[i].equals("WARNING:")) {
				for (i++; lines[i].equals(";"); i++) {
					String[] wkv = lines[i].split(",");
					warningDict.put(Integer.parseInt(wkv[0]), wkv[1]);
				}
				i++;
				if(lines[i].equals("ERROR:")) {
					for (i++; lines[i].equals(";"); i++) {
						String[] ekv = lines[i].split(",");
						errorDict.put(Integer.parseInt(ekv[0]), ekv[1]);
					}
				}
			}

		} catch (IOException ex) {

			ex.printStackTrace();
		}
	}
	public ErrorTable(URL url) throws URISyntaxException {
		try {
			Stream<String> lines = Files.lines(Paths.get(url.toURI()),Charset.forName("SHIFT_JIS"));
			lines.forEach(e->importToDict(e));
			lines.close();
		} catch (IOException | URISyntaxException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private void importToDict(String line){
		String[] buf = line.split(",");
		errorDict.put(Integer.parseInt(buf[0]), buf[1]);
	}

	public void writeError(Integer errorType, Token token) {
		String errorMessage = errorDict.get(errorType);
		errorMessage.replaceAll("%s", token.getToken());
		errorMessages.add(errorMessage);
		errorCount++;
	}

	public void writeWarning(Integer errorType, Token token) {
		String errorMessage = errorDict.get(errorType);
		errorMessage.replaceAll("%s", token.getToken());
		errorMessages.add(errorMessage);
		errorCount++;
	}

	public List<String> getErrorMessages(){
		return errorMessages;
	}

	public List<String> getWarningMessages(){
		return errorMessages;
	}

	public boolean hasError(){return errorCount!=0;}
	public boolean hasWarning(){return warningCount!=0;}
}