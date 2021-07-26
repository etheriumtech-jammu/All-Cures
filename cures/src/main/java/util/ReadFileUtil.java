package util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadFileUtil {
	public static void main(String args[]) {
		try (InputStream stream = Files.newInputStream(Paths.get("C:\\test\\4\\2021/07/06\\article_81.json"))) {
			// Convert stream to string
			String contents = new String(stream.readAllBytes(), StandardCharsets.UTF_8);

			// To print the string content
			System.out.println(contents);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public String readFilebyPath(String filePath) {
		System.out.println("in readFilebyPath for filepath"+filePath);
		String contents = "";
		try (InputStream stream = Files.newInputStream(Paths.get("C:"+filePath))) {
			// Convert stream to string
			contents = new String(stream.readAllBytes(), StandardCharsets.UTF_8);

			// To print the string content
			System.out.println(contents);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return contents;
		
	}
}
