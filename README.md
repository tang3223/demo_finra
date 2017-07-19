# demo_finra
Finra demo file upload

----------------------------------------------------------------------------------------------------
* The first part is main application
----------------------------------------------------------------------------------------------------
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
* Version 0.1.0
* Chas
*/

@SpringBootApplication
public class DemoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}



---------------------------------------------------------------------------------------------------
* This part is the fileUploadController
---------------------------------------------------------------------------------------------------
package com.example.demo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/*
* Version 0.1.0
* Chas
*/

@RestController
public class FileUploadController {
	
	private static String targetPath = "./";
	private Properties props = new Properties();
	
	/* 
	 * Params: MultipartFile file
	 * Description: Spring Resource URL path mapping and receive the file 
	 */
	@PostMapping("/file/upload")
	public ResponseEntity<?> upload(@RequestBody MultipartFile uploadfile){
		if (uploadfile.isEmpty()) {
			return new ResponseEntity<>("No file selected!", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}
		
		try {
			// call method to store uploaded file
			storeFile(uploadfile);
			//call method to store file's property
			storeProperty(uploadfile);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/* 
	 * Params: MultipartFile file
	 * Description: Stores file using standard outputstream to file system
	 */
	private void storeFile(MultipartFile file) throws IOException {
		OutputStream out = new FileOutputStream(targetPath + file.getOriginalFilename());
		byte[] bytes = file.getBytes();
		
		out.write(bytes);
		out.flush();
		out.close();
	}
	 
	/* 
	 * Params: MultipartFile file
	 * Description: Stores file property using property.storeToXML to file system
	 */
	private void storeProperty(MultipartFile file) throws IOException {
		OutputStream out = new FileOutputStream(targetPath + file.getOriginalFilename() + ".xml");
		
		props.setProperty("fileName", file.getOriginalFilename());
		props.setProperty("fileSize", String.valueOf(file.getSize()));
		props.setProperty("fileType", file.getContentType());
		props.storeToXML(out, "File meta data", "UTF-8");
		
	}
}
