package util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.mozilla.universalchardet.UniversalDetector;

public class DetectUtils {
	public static final String ENCODING_MS932="MS932";
	public static final String defaultEncoding="MS932";
	public static String lastEncoding;
	public static String getEncoding(File file){
		try{
			java.io.FileInputStream fis = new java.io.FileInputStream(file);
			byte[] buf = new byte[4096];
			UniversalDetector detector = new UniversalDetector(null);

			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}

			detector.dataEnd();
			String result = detector.getDetectedCharset();
			if(result!=null) return result;

			byte[] bytes = Files.readAllBytes(file.toPath());
			String code = null;
			try {
				result ="SHIFT-JIS";
				code = new String(bytes, result);
				return result;
			} catch (UnsupportedEncodingException ex) {
				// fallback
				code = new String(bytes);
				return "UTF-8";
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	public static String readFileToString(File file) throws IOException{
		String encode=getEncoding(file);
		if(encode==null){
			encode=defaultEncoding;
		}
		String text=FileUtils.readFileToString(file, encode);
		if(text.charAt(0) == 65279){//UTF-8 marker
			text=text.substring(1);
		}
		return text;
	}
}

