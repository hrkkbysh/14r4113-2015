package comet2casl2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorTable {
	private final Map<Integer, String> errorDict = new HashMap<>();
	private final Map<Integer, String> warningDict = new HashMap<>();
	private int ec;
	private int wc;
	private List<String> messages = new ArrayList<>();

	public ErrorTable(){
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/main/resources/MyErrorDictionary.txt"));
			String code;
			try {
				code = new String(bytes, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				// fallback
				code = new String(bytes);
			}
			String[] lines = code.split("\r\n");
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

	public boolean hasError(){return ec!=0;}
	public boolean hasWarning(){return wc!=0;}

	public void clear(){
		ec=0;
		wc=0;
	}

	public void printError(int line,int errorType,String sval){
		String em = errorDict.get(errorType);
		em = em.replaceFirst("sval",sval);
		System.out.println("(" + line + ") " + em);
		messages.add("(" + line + ") " + em);
		ec++;
	}
	public void printError(int line,int errorType,int nval){
		String em = errorDict.get(errorType);
		em = em.replaceFirst("nval",Integer.toString(nval));
		System.out.println("("+line+") "+em);
		messages.add("(" + line + ") " + em);
		ec++;
	}
	public void printError(int line,int errorType){
		String em = errorDict.get(errorType);
		System.out.println("("+line+") "+em);
		messages.add("(" + line + ") " + em);
		ec++;
	}

	public void printError(int line, int errorType, String inst, int nval) {
		String em = errorDict.get(errorType);
		em = em.replaceFirst("inst",inst);
		em = em.replaceFirst("nval",Integer.toString(nval));
		System.out.println("("+line+") "+em);
		messages.add("(" + line + ") " + em);
		ec++;
	}

	public void printError(int line, int errorType, String inst, int nval, String sval) {
		String em = errorDict.get(errorType);
		em = em.replaceFirst("inst",inst);
		em = em.replaceFirst("sval",sval);
		em = em.replaceFirst("nval",Integer.toString(nval));
		System.out.println("("+line+") "+em);
		messages.add("(" + line + ") " + em);
		ec++;
	}

	public void printWarning(int line,int errorType,String sval){
		String em = warningDict.get(errorType);
		em = em.replaceFirst("sval",sval);
		System.out.println("("+line+") "+em);
		messages.add("(" + line + ") " + em);
		wc++;
	}
	public void printWarning(int line,int errorType,int nval){
		String em = warningDict.get(errorType);
		em = em.replaceFirst("nval",Integer.toString(nval));
		System.out.println("("+line+") "+em);
		messages.add("(" + line + ") " + em);
		wc++;
	}

	public void printWarning(int line,int errorType){
		String em = warningDict.get(errorType);
		System.out.println("("+line+") "+em);
		messages.add("(" + line + ") " + em);
		wc++;
	}

	public List<String> getMessages() {
		return messages;
	}
}