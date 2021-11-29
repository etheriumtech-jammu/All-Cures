package controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dao.ArticleDaoImpl;
import model.Article;
import model.Article_dc_name;
import util.ArticleUtils;

@RestController
@RequestMapping(path = "/article")
public class ArticleController {

	@Autowired
	private ArticleDaoImpl articleDaoImpl;

	private String saveDirectory = "C:/test/";

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

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody HashMap uploadFile(@RequestParam CommonsMultipartFile image, HttpServletRequest request,
			HttpSession session) {
		//String path = session.getServletContext().getRealPath("/uitest");
		String curesProperties = "cures.properties";
		Properties prop = null;
		try {
			prop = new ArticleUtils().readPropertiesFile(curesProperties);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ARTICLES_UPLOAD_DIR : " + prop.getProperty("ARTICLES_UPLOAD_DIR"));
		String cures_articleimages = prop.getProperty("cures_articleimages");
		String path = System.getProperty( "catalina.base" ) + "/webapps/"+cures_articleimages;

		System.out.println(path);
		// path = path+"/uitest";
		String filename = image.getOriginalFilename();

		System.out.println(path + " " + filename);
		try {
			byte barr[] = image.getBytes();

			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + "/" + filename));
			bout.write(barr);
			bout.flush();
			bout.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		HashMap hm = new HashMap();
		hm.put("success", 1);
		HashMap hm2 = new HashMap();
		String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();

		System.out.println(baseUrl);
		hm2.put("url", baseUrl + "/"+cures_articleimages+"/" + filename);
		hm.put("file", hm2);
		return hm;
	}

	@RequestMapping(value = "/fetchUrl", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody HashMap fetchUrl(@RequestBody HashMap dataMap, HttpServletRequest request, HttpSession session)
			throws MalformedURLException {
		String url = (String) dataMap.get("url");
		URL urlExt = new URL(url);

//		String path = session.getServletContext().getRealPath("/uitest");
		String curesProperties = "cures.properties";
		Properties prop = null;
		try {
			prop = new ArticleUtils().readPropertiesFile(curesProperties);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("cures_articleimages : " + prop.getProperty("cures_articleimages"));
		String cures_articleimages = prop.getProperty("cures_articleimages");
		String path = System.getProperty( "catalina.base" ) + "/webapps/"+cures_articleimages;

		String filename = urlExt.toString().substring(urlExt.toString().lastIndexOf('/') + 1,
				urlExt.toString().length());
		System.out.println(path + File.separator + filename);
		try {
			try (InputStream in = urlExt.openStream()) {
				Files.copy(in, Paths.get(path + File.separator + filename));
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		HashMap hm = new HashMap();
		hm.put("success", 1);
		HashMap hm2 = new HashMap();
		String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();

		System.out.println(baseUrl);
		hm2.put("url", baseUrl + "/cures/"+cures_articleimages+"/" + filename);
		hm.put("file", hm2);
		return hm;
	}

}