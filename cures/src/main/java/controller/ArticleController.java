package controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Constant;
import java.util.Properties;
import java.util.zip.DataFormatException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;


import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.nio.charset.StandardCharsets;
import dao.ArticleDaoImpl;
import dao.ip_detaildao;
import model.Article;
import model.Article_dc_name;
import model.Registration;
import util.ArticleUtils;
import util.SolrIndexer;
@RestController
@RequestMapping(path = "/article")
public class ArticleController {

	@Autowired
	private ArticleDaoImpl articleDaoImpl;

	private String saveDirectory = "C:/test/";

	@RequestMapping(value = "/{article_id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody Article_dc_name getArticleDetails(@PathVariable int article_id, HttpServletRequest request,@RequestHeader Map<String,String> headers) {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(true);
		
		return articleDaoImpl.getArticleDetails(article_id);

	}
	
	@RequestMapping(value = "/{article_id}/{user_id}/{cookie}/{whats_app}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int  PostDetails(@PathVariable int article_id,@PathVariable int user_id,@PathVariable String cookie,@PathVariable String whats_app, HttpServletRequest request,@RequestHeader Map<String,String> headers) {
		
	return	 ip_detaildao.Insert(article_id,request,user_id,cookie,whats_app);
		
	}
	
	
//	@RequestMapping(value = "/title/{article_title}", produces = "application/json", method = RequestMethod.GET)
//	public @ResponseBody Article_dc_name getArticleDetailsByTitle(@PathVariable String article_title, HttpServletRequest request) {
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpSession session = req.getSession(true);
//		/*
//		 * int reg_id = 0; if (session.getAttribute(Constant.USER) != null) {
//		 * Constant.log("#########USER IS IN SESSION########", 0); Registration user =
//		 * (Registration) session.getAttribute(Constant.USER); reg_id =
//		 * user.getRegistration_id(); System.out.println(reg_id); }
//		 */
////		try {
////			return articleDaoImpl.getArticleDetails(URLDecoder.decode(article_title,"UTF-8").toString());
////		} catch (UnsupportedEncodingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		articleDaoImpl.getArticleDetails(article_title.replaceAll("-", " "));
//		return null;
//
//	}

	@RequestMapping(value = "/all", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody ArrayList<Article> listArticlesAll(@RequestParam(required = false) Integer limit,@RequestParam(required = false) Integer offset) {
		return articleDaoImpl.getArticlesListAll(limit, offset);
	}

	@RequestMapping(value = "/allkv", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listArticlesAllKeys(@RequestParam(required = false) Integer limit,@RequestParam(required = false) Integer offset,@RequestParam(required = false) String search,@RequestParam(required = false) String order) {
		return articleDaoImpl.getArticlesListAllKeys(limit,offset,search,order);
	}
	
	@RequestMapping(value = "/allkvList", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listArticlesAllKeysList(@RequestParam(required = false) Integer limit,@RequestParam(required = false) Integer offset,@RequestParam(required = false) String search,@RequestParam(required = false) String order) {
		return articleDaoImpl.getArticlesListAllKeysList(limit,offset,search,order);
	}
	
	@RequestMapping(value = "/allkvfeatured", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getCompressedData(HttpServletResponse response,
                                                     @RequestParam(required = false) Integer limit,
                                                     @RequestParam(required = false) Integer offset,
                                                     @RequestParam(required = false) String search,
                                                     @RequestParam(required = false) String order) {
        try {
            // Get the JSON data from DAO
        	// Assuming articleDaoImpl.getArticlesListAllKeysFeatured() returns List<HashMap<String, Object>>
            List<HashMap<String, Object>> data = articleDaoImpl.getArticlesListAllKeysFeatured(limit, offset,
                    search, order);

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

	@RequestMapping(value = "/allkvranked", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listArticlesAllKeysRanked(@RequestParam(required = false) Integer limit,@RequestParam(required = false) Integer offset,@RequestParam(required = false) String search,@RequestParam(required = false) String order) {
		return articleDaoImpl.getArticlesListAllKeysRanked(limit,offset,search,order);
	}
	@RequestMapping(value = "/authallkv/reg_type/{reg_type}/reg_doc_pat_id/{reg_doc_pat_id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listArticlesAllKeysByRegTypeAndID(@RequestParam(required = false) Integer limit, @PathVariable int reg_type, @PathVariable int reg_doc_pat_id, @RequestParam(required = false) Integer offset) {
		return articleDaoImpl.getArticlesListAllKeysbyAuthIdandregType(reg_type, reg_doc_pat_id, limit, offset);
	}

	@RequestMapping(value = "/all/table/{table_name}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody ArrayList listTablesAllData(@PathVariable String table_name) {
		return articleDaoImpl.getTablesDataListAll(table_name);
	}

	 
	@RequestMapping(value = "/update/{article_id}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int updateArticle(@PathVariable int article_id, @RequestBody HashMap articleMap, HttpServletRequest request) {
	String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
	
		
return articleDaoImpl.updateArticleId(article_id, articleMap, baseUrl);
	}

	@RequestMapping(value = "/delete/{article_id}", produces = "application/json", method = RequestMethod.DELETE)
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
		//System.out.println("ARTICLES_UPLOAD_DIR : " + prop.getProperty("ARTICLES_UPLOAD_DIR"));
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
		hm2.put("url", baseUrl + "/"+cures_articleimages+"/" + filename);
		hm.put("file", hm2);
		return hm;
	}

	// Endpoint for liking an article
	@RequestMapping(value = "/like/{articleId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody int likeArticle(@PathVariable Long articleId)
	
	{
       return  ArticleDaoImpl.updateLikes(articleId, true);
    }

    // Endpoint for disliking an article
	@RequestMapping(value = "/dislike/{articleId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody int dislikeArticle(@PathVariable Long articleId)
	
	{
		return ArticleDaoImpl.updateLikes(articleId, false);
    }
	
	@RequestMapping(value = "/like/count/{articleId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Map<String, Integer> getlikesanddislikescount(@PathVariable Long articleId)
	
	{
	
		return ArticleDaoImpl.getLikesAndDislikesCount(articleId);
    }
	@RequestMapping(value = "/solr/add", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody void AddArticle()
	
	{
	String directoryPath = "/home/uat/Production/installers/tomcat/webapps/cures_articleimages"; // Directory containing files
        String solrUrl = "http://localhost:8983/solr/article_new_core"; // Replace with your Solr collection URL
        System.out.println(solrUrl);
        SolrIndexer indexer = new SolrIndexer();
        try {
            indexer.indexFiles(directoryPath, solrUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }	
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody int AddArticle(HttpServletRequest request, HttpServletResponse response) throws IOException
	
	{
		HttpSession session = request.getSession(false);
		Registration regi=(Registration)session.getAttribute(Constant.USER);
		System.out.println(regi);
		try {
			return ArticleDaoImpl.createArticle(request,response,regi);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}
	
	

}
