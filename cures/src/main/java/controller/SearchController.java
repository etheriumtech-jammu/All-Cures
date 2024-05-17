package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.DiseaseANDConditionDaoImpl;
import dao.SearchDaoImpl;

@RestController
@RequestMapping(path = "/isearch")
public class SearchController {

	@Autowired
	private DiseaseANDConditionDaoImpl diseaseANDconditionDaoImpl;
	
	@Autowired
	private SearchDaoImpl searchDaoImpl;

//	@RequestMapping(value = "/{article_id}", produces = "application/json", method = RequestMethod.GET)
//	public @ResponseBody Article getArticleDetails(@PathVariable int article_id, HttpServletRequest request) {
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpSession session = req.getSession(true);
//		/*
//		 * int reg_id = 0; if (session.getAttribute(Constant.USER) != null) {
//		 * Constant.log("#########USER IS IN SESSION########", 0); Registration user =
//		 * (Registration) session.getAttribute(Constant.USER); reg_id =
//		 * user.getRegistration_id(); System.out.println(reg_id); }
//		 */
//		return articleDaoImpl.getArticleDetails(article_id);
//
//	}
//
//	@RequestMapping(value = "/all", produces = "application/json", method = RequestMethod.GET)
//	public @ResponseBody ArrayList<Article> listArticlesAll() {
//		return diseaseANDconditionDaoImpl.getArticlesListAll();
//	}
	 
	@RequestMapping(value = "/{search_string}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listDataFromMatchingString(@PathVariable String search_string,@RequestParam(required = false) Integer limit,@RequestParam(required = false) Integer offset,@RequestParam(required = false) String search,@RequestParam(required = false) String order) {
		return diseaseANDconditionDaoImpl.getAllMatchingDCList(search_string, limit,  offset, order);
	}
	
	@RequestMapping(value = "/limit/{search_string}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listDataFromMatchingStringLimit(@PathVariable String search_string,@RequestParam(required = false) Integer limit,@RequestParam(required = false) Integer offset,@RequestParam(required = false) String search,@RequestParam(required = false) String order) {
		return diseaseANDconditionDaoImpl.getAllMatchingDCListLimit(search_string, limit,  offset, order);
	}
	
	
	@RequestMapping(value = "/medicinetype/{medicine_type}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listDataFromMatchingString(@PathVariable Integer medicine_type,@RequestParam(required = false) Integer limit,@RequestParam(required = false) Integer offset, @RequestParam(required = false) String order)  {
		return diseaseANDconditionDaoImpl.getAllarticlebymedicinetypeList(medicine_type, limit, offset, order);
	}
	
	@RequestMapping(value = "/diseases/{disease_condition_id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List dataFromMatchingString(@PathVariable Integer disease_condition_id,@RequestParam(required = false) Integer limit,@RequestParam(required = false) Integer offset, @RequestParam(required = false) String order)  {
		return diseaseANDconditionDaoImpl.getAllArticleByDiseaseList(disease_condition_id, limit, offset, order);
	}
	
	
	@RequestMapping(value = "/hierarchy/{parent_id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listParentChildDiseaseCondtion(@PathVariable Integer parent_id) {
		return diseaseANDconditionDaoImpl.getParentChildDataDiseaseConditon(parent_id);
	}
	
	@RequestMapping(value = "/combo/{search_string}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listDataMatchingStrDiseaseConditonAndArticleTables(@PathVariable String search_string) {
	//	return diseaseANDconditionDaoImpl.listDataMatchingStrDiseaseConditonAndArticleTables(search_string);
		 // Solr server URL
        String solrUrl = "http://localhost:8983/solr/article_new_core";

      
        // Perform search with relevance ranking and debug mode
        SolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build();
        SolrQuery solrQuery = new SolrQuery(search_string);
        solrQuery.set("defType", "edismax");
        solrQuery.set("qf", "title^3.0 keywords^2.5 dc_name^2.0 content_new "); // Apply boosts to title field
        solrQuery.set("debug", true); // Enable debug mode
//        solrQuery.setRows(20); // Limit the number of returned documents
        solrQuery.setSort("score", SolrQuery.ORDER.desc); // Sort by relevance first
        solrQuery.addSort("published_date", SolrQuery.ORDER.desc); // Then sort by published_date

        QueryResponse response = null;
		try {
			response = solrClient.query(solrQuery);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List hmFinal = new ArrayList();
        // Retrieve documents from response
        SolrDocumentList results = response.getResults();
        for (int i = 0; i < results.size(); i++) {
        	SolrDocument doc = results.get(i);
        	int articleId = Integer.parseInt((String) doc.getFieldValue("article_id"));
        	String title = (String) doc.getFieldValue("title");
        	
        	hmFinal.add(title);
        } 
        return hmFinal;
	}
	
	@RequestMapping(value = "/treatmentregions/{dc_id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listDataMatchingStrDiseaseConditonAndArticleTables(@PathVariable Integer dc_id) {
		return searchDaoImpl.findRegionsNameForDiseaseId(dc_id);
	}

//	@RequestMapping(value = "/{article_id}", produces = "application/json", method = RequestMethod.POST)
//	public @ResponseBody int updateArticle(@PathVariable int article_id, @RequestBody HashMap articleMap) {
//		return articleDaoImpl.updateArticleId(article_id, articleMap);
//	}
//
//	@RequestMapping(value = "/{article_id}", produces = "application/json", method = RequestMethod.DELETE)
//	public @ResponseBody int deleteArticle(@PathVariable int article_id) {
//		return articleDaoImpl.deleteArticleId(article_id);
//	}
//
//	@RequestMapping(value = "/readfile", produces = "application/json", method = RequestMethod.POST)
//	public @ResponseBody String readFile(@RequestBody HashMap filepath) {
//		String fp = (String) filepath.get("filepath");
//		return articleDaoImpl.readFile(fp);
//	}

}
