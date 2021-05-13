package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//import org.apache.tomcat.jni.Directory;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
 /*String number ="237678997665";
 int length =number.length();
 char lenno;
 int l1=length-4;
// String arr[];
 System.out.println("******"+length);
 for(int j=0; j<4; j++){
	 if(j<=0){
 for(int i=0;i<length-4; i++){
	 char no[] = number.toCharArray();
	 lenno = no[i];
	 
	 
	 System.out.println(lenno);
 }
 }
 System.out.println("*");
 }*/
		//String dir = "C:\test";
	
            //Path path = Paths.get(dir);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
            LocalDateTime now = LocalDateTime.now();  
            System.out.println(dtf.format(now));

			File dir = new File("C:\\test\\"+dtf.format(now));
			
			
			boolean isCreated = dir.mkdirs();
			FileWriter myWriter = new FileWriter("C:\\test\\"+dtf.format(now)+"\\article.txt");
		      myWriter.write("Files in Java might be tricky, but it is fun enough!");
		      myWriter.close();
			System.out.println("Iscreated"+isCreated);

            System.out.println("Directory is created!");
		
            File file = new File("C:\\test\\"+dtf.format(now)+"\\article.txt"); 
            
            BufferedReader br = new BufferedReader(new FileReader(file)); 
            
            String st; 
            while ((st = br.readLine()) != null) 
              System.out.println(st);
 }
		
 

	}

