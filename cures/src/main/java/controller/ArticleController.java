package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.ArticleDaoImpl;
import model.Article;
import model.Article_dc_name;

@RestController
@RequestMapping(path = "/article")
public class ArticleController {

	@Autowired
	private ArticleDaoImpl articleDaoImpl;

	@RequestMapping(value = "/{article_id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody Article_dc_name getArticleDetails(@PathVariable int article_id, HttpServletRequest request) {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(true);
		/*
		 * int reg_id = 0; if (session.getAttribute(Constant.USER) != null) {
		 * Constant.log("#########USER IS IN SESSION########", 0); Registration user =
		 * (Registration) session.getAttribute(Constant.USER); reg_id =
		 * user.getRegistration_id(); System.out.println(reg_id); }
		 */
		return articleDaoImpl.getArticleDetails(article_id);

	}

	@RequestMapping(value = "/all", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody ArrayList<Article> listArticlesAll() {
		return articleDaoImpl.getArticlesListAll();
	}

	@RequestMapping(value = "/allkv", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listArticlesAllKeys() {
		return articleDaoImpl.getArticlesListAllKeys();
	}

	@RequestMapping(value = "/all/table/{table_name}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody ArrayList listTablesAllData(@PathVariable String table_name) {
		return articleDaoImpl.getTablesDataListAll(table_name);
	}

	@RequestMapping(value = "/{article_id}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int updateArticle(@PathVariable int article_id, @RequestBody HashMap articleMap) {
		return articleDaoImpl.updateArticleId(article_id, articleMap);
	}

	@RequestMapping(value = "/{article_id}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deleteArticle(@PathVariable int article_id) {
		return articleDaoImpl.deleteArticleId(article_id);
	}

}