package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.BatchDaoImpl;

@RestController
@RequestMapping(path = "/batchrating")
public class BatchProcessRatingUpdate {

	@Autowired
	private BatchDaoImpl batchDaoImpl;

	@RequestMapping(value = "/articledoctor", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody int syncArticleDoctor() {
		int ret = 0;
		// get Doctors rating
		batchDaoImpl.updateAverageRatingArticles();
		batchDaoImpl.updateAverageRatingDoctors();
		return ret;
	}

}
