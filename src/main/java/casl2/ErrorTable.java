package casl2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorTable {
	private static final Map<Integer, String> errorDict = new HashMap<>();
	private static final Map<Integer, String> warningDict = new HashMap<>();
	private List<String> errorMessages = new ArrayList<>();
	private List<String> warningMessages = new ArrayList<>();

	static{
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
	public ErrorTable(){
	}

	public List<String> getErrorMessages(){
		return errorMessages;
	}

	public List<String> getWarningMessages(){
		return warningMessages;
	}

	public boolean hasError(){return !errorMessages.isEmpty();}
	public boolean hasWarning(){return !warningMessages.isEmpty();}

	public void clear(){
		warningMessages.clear();
		errorMessages.clear();
	}

	public void writeError(int line,int errorType,String sval){
		String em = errorDict.get(errorType);
		em = em.replaceFirst("sval",sval);
		errorMessages.add("("+line+") "+em);
	}
    public void writeError(int line,int errorType,int nval){
		String em = errorDict.get(errorType);
		em = em.replaceFirst("nval",Integer.toString(nval));
		errorMessages.add("("+line+") "+em);
	}
    public void writeError(int line,int errorType){
		String em = errorDict.get(errorType);
		errorMessages.add("("+line+") "+em);
	}

	public void writeError(int line, int errorType, String inst, int nval, String sval) {
		String em = errorDict.get(errorType);
		em = em.replaceFirst("inst",inst);
		em = em.replaceFirst("sval",sval);
		em = em.replaceFirst("nval",Integer.toString(nval));
		errorMessages.add("("+line+") "+em);
	}

    public void writeWarning(int line,int errorType,String sval){
        String em = warningDict.get(errorType);
        em = em.replaceFirst("sval",sval);
        warningMessages.add("("+line+") "+em);
    }
    public void writeWarning(int line,int errorType,int nval){
        String em = warningDict.get(errorType);
        em = em.replaceFirst("nval",Integer.toString(nval));
        warningMessages.add("("+line+") "+em);
    }

    public void writeWarning(int line,int errorType){
        String wm = warningDict.get(errorType);
        warningMessages.add("("+line+") "+wm);
    }
}