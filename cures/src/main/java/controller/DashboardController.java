package controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.ArticleDaoImpl;

@RestController
@RequestMapping(path = "/dashboard")
public class DashboardController {

	@Autowired
	private ArticleDaoImpl articleDaoImpl;

	@RequestMapping(path = "/articlecount", produces = "application/json",method = RequestMethod.GET)
	public @ResponseBody Map<String, Integer> getDashboardDetails() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("published_article", articleDaoImpl.findPublishedArticle().size());
		map.put("draft_article", articleDaoImpl.findDraftAricle().size());
		map.put("approval_article", articleDaoImpl.findApprovalArticle().size());
		map.put("review_article", articleDaoImpl.findReviwArticle().size());
		
		return map;
	}

}