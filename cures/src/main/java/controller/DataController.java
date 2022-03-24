package controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.DataDaoImpl;

@RestController
@RequestMapping(path = "/data")
public class DataController {

	@Autowired
	private DataDaoImpl dataDaoImpl;

	

	@RequestMapping(value = "/create", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int addDataDetails(@RequestBody HashMap dataMasterMap) {
		return dataDaoImpl.addDataDetails(dataMasterMap);
	}

}