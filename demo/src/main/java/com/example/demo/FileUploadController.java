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
	
	@PostMapping("/file/upload")
	public ResponseEntity<?> upload(@RequestBody MultipartFile uploadfile){
		if (uploadfile.isEmpty()) {
			return new ResponseEntity<>("No file selected!", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}
		
		try {
			storeFile(uploadfile);
			storeProperty(uploadfile);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	private void storeFile(MultipartFile file) throws IOException {
		OutputStream out = new FileOutputStream(targetPath + file.getOriginalFilename());
		byte[] bytes = file.getBytes();
		
		out.write(bytes);
		out.flush();
		out.close();
	}
	 
	private void storeProperty(MultipartFile file) throws IOException {
		OutputStream out = new FileOutputStream(targetPath + file.getOriginalFilename() + ".xml");
		
		props.setProperty("fileName", file.getOriginalFilename());
		props.setProperty("fileSize", String.valueOf(file.getSize()));
		props.setProperty("fileType", file.getContentType());
		props.storeToXML(out, "File meta data", "UTF-8");
		
	}
}
