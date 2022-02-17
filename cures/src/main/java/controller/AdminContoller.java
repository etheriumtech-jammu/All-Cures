package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.AdminDaoImpl;

@RestController
@RequestMapping(path = "/admin")
public class AdminContoller {

	@Autowired
	private AdminDaoImpl adminDaoImpl;

	@RequestMapping(value = "/create/{table}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int createTable(@PathVariable String table, @RequestBody HashMap tableDetails) {
		return adminDaoImpl.createTable(table, tableDetails);

	}
	
	@RequestMapping(value = "/fetchtable/{table}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<Map<String,Object>> fetchTable(@PathVariable String table) {
		return adminDaoImpl.fetchTable(table);

	}
}