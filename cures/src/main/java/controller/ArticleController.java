package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.ArticleDaoImpl;
import model.Article;
import model.Registration;
import util.Constant;

@RestController
@RequestMapping(path = "/article")
public class ArticleController {

	@Autowired
	private ArticleDaoImpl articleDaoImpl;

	@RequestMapping(value = "/{article_id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody Article getArticleDetails(@PathVariable int article_id, HttpServletRequest request) {
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

}