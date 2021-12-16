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
		// get Doctors & Articles rating
		int ret1 = batchDaoImpl.updateAverageRatingArticles();
		int ret2 = batchDaoImpl.updateAverageRatingDoctors();
		if(ret1==1 && ret2==1) {//if both process return success or 1
			ret = 1;
		}
		return ret;
	}
	
	@RequestMapping(value = "/article", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody int syncArticle() {
		int ret = 0;
		// get Article rating
		ret = batchDaoImpl.updateAverageRatingArticles();
		return ret;
	}
	@RequestMapping(value = "/doctor", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody int syncDoctor() {
		int ret = 0;
		// get Doctors rating
		ret = batchDaoImpl.updateAverageRatingDoctors();
		return ret;
	}

}
