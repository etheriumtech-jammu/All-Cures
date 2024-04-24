package controller;

import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import dao.CityDaoImpl;
import model.Registration;
import com.google.gson.Gson;
@RestController
@RequestMapping(path = "/city")
public class CityController {

	@Autowired
	private CityDaoImpl cityDaoImpl;

/*	@RequestMapping(value = "/all", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getCityDetails() {
		return cityDaoImpl.getAllCityDetails();

	}
*/	
	@RequestMapping(value = "/all", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getCityDetails(HttpServletResponse response) {
		
		try {
			// Get the JSON data from DAO
			// Assuming articleDaoImpl.getArticlesListAllKeysFeatured() returns
			// List<HashMap<String, Object>>
			List<HashMap<String, Object>> data = cityDaoImpl.getAllCityDetails();
			// Convert data to JSON string
			String json = new Gson().toJson(data);

			// Compression
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
				gzipStream.write(json.getBytes(StandardCharsets.UTF_8));
			}
			byte[] compressedData = byteStream.toByteArray();

			// Set appropriate headers
			response.setHeader("Content-Encoding", "gzip");
			response.setContentLength(compressedData.length);
			response.setContentType("application/json");

			// Write compressed data to the response output stream
			response.getOutputStream().write(compressedData);
			response.getOutputStream().flush();

			// Return ResponseEntity indicating success
			return ResponseEntity.ok().build();
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@RequestMapping(value = "/newsletter/{mobile}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody Integer getNewsletterDetails(@PathVariable String mobile) {
		return cityDaoImpl.getNewsletterDetails(mobile);     
	}	
	
	@RequestMapping(value = "/state", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getStateDetails() {
		return cityDaoImpl.getAllStateDetails();

	}
	
	@RequestMapping(value = "/disease", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getDiseaseDetails() {
		return cityDaoImpl.getAllDiseaseDetails();

	}
	@RequestMapping(value = "/mobile", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getmobileDetails() {
		return cityDaoImpl.getAllMobileDetails();

	}

}
