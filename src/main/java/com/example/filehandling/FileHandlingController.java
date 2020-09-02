package com.example.filehandling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class FileHandlingController {

	// uploading a file in server
	@ApiOperation(value = "uploading a file")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "File Uploaded Successfully", response = String.class),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "File not found") })
	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
		File convertFile = new File("/var/tmp/" + file.getOriginalFilename());
		convertFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertFile);
		fout.write(file.getBytes());
		fout.close();
		return "File is upload successfully";

	}

	// downloading file from server
	@ApiOperation(value = "Downloading a file")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "File Downloaded Successfully", response = Object.class),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "File not found") })
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ResponseEntity<Object> downloadFile() throws IOException {
		String filename = "/var/tmp/example.txt";
		File file = new File(filename);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders headers = new HttpHeaders();

		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/txt")).body(resource);

		return responseEntity;
	}

	// deleting a file
	@ApiOperation(value = "Deleting a file")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "File Deleted Successfully", response = String.class),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "File not found") })
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String deleteFile(@RequestParam("file") MultipartFile file) throws IOException {
		File tempFile = new File(file.getOriginalFilename());
		boolean deleted = tempFile.delete();
		return "File Deleted successfully";

	}

}
