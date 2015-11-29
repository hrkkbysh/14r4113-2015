package casl2;

import assembler.Token;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorTable {

	private static ErrorTable errorTable;

	private Map<Integer, String> errorDict = new HashMap<>();
	private Map<Integer, String> warningDict = new HashMap<>();
	private List<String> errorMessages = new ArrayList<>();
	private List<String> warningMessages = new ArrayList<>();
	private int errorCount;
	private int warningCount;

	private ErrorTable(){
		errorCount =0;
		warningCount = 0;
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/main/resources/ErrorDictionary.txt"));

			String code;
			try {
				code = new String(bytes, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				// fallback
				code = new String(bytes);
			}
			String[] lines = code.split("\n");
			int i = 0;
			if(lines[i].equals("WARNING:")) {
				for (i++; !lines[i].equals(";"); i++) {
					String[] wkv = lines[i].split(",");
					warningDict.put(Integer.parseInt(wkv[0]), wkv[1]);
				}
				i++;
				if(lines[i].equals("ERROR:")) {
					for (i++; !lines[i].equals(";"); i++) {
						String[] ekv = lines[i].split(",");
						errorDict.put(Integer.parseInt(ekv[0]), ekv[1]);
					}
				}
			}
			StringBuilder buf = new StringBuilder();
			for (Map.Entry<Integer, String> errorMessage : errorDict.entrySet()) {
				buf.append(errorMessage.getKey());
				buf.append(',');
				buf.append(errorMessage.getValue());
				buf.append(System.lineSeparator());
			}
			for (Map.Entry<Integer, String> warningMessage : warningDict.entrySet()) {
				buf.append(warningMessage.getKey());
				buf.append(',');
				buf.append(warningMessage.getValue());
				buf.append(System.lineSeparator());
			}
			System.out.println(buf.toString());

		} catch (IOException  e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public void writeError(int line,Integer errorType, Token token) {
		String errorMessage = errorDict.get(errorType);
		errorMessage.replaceAll("%s", token.getToken());
		errorMessages.add(errorMessage);
		errorCount++;
	}

	public void writeWarning(int line,Integer errorType, Token token) {
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
	public void outErrors(){errorMessages.forEach(System.out::println);}
	public void writeTemp(int line, Token token ,String error){
		errorMessages.add("("+line +")"+error+"("+token.getToken() +")");
		errorCount++;
	}

	public static ErrorTable getInstance() {
		if(errorTable==null){
			errorTable = new ErrorTable();
		}
		return errorTable;
	}
	public void clear(){
		warningMessages.clear();
		errorMessages.clear();
		errorCount=0;
		warningCount=0;
	};
}