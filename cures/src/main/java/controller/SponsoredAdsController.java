package controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.SponsoredAdsDaoImpl;
import model.SponsoredServicesMaster;
import model.ServiceContract;
@RestController
@RequestMapping(path = "/sponsored")
public class SponsoredAdsController {

	 // A map to store request counters for each date
    private Map<LocalDate, Integer> BannerCountMap = new ConcurrentHashMap<>();
    private Map<LocalDate, Integer> LeftCountMap = new ConcurrentHashMap<>();
    	private LocalDate lastRequestDate = null;
	@RequestMapping(value = "/create/company", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer addcompanies(@RequestBody HashMap Company_Map,HttpServletRequest request ) throws Exception {

		return SponsoredAdsDaoImpl.InsertCompaniesDetails(Company_Map);
	}

	@RequestMapping(value = "/create/campaign", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer addcampaign(@RequestBody HashMap Campaign_Map, HttpServletRequest request ) throws Exception {

		return SponsoredAdsDaoImpl.InsertCampaignDetails(Campaign_Map);
	}
	
	@RequestMapping(value = "/create/ad", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer addcampaignsads(@RequestParam("image") CommonsMultipartFile image,@RequestParam(value = "mobile_image", required = false) CommonsMultipartFile mobile_image,
            @RequestParam("AdMap") String adMapJson,
            HttpServletRequest request) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		HashMap<String, Object> AdMap = objectMapper.readValue(adMapJson, new TypeReference<HashMap<String, Object>>() {});
		return SponsoredAdsDaoImpl.InsertAdDetails(AdMap, image,mobile_image);
	}
	@RequestMapping(value = "/add/stats", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer addstats(@RequestBody HashMap StatsMap,HttpServletRequest request ) throws Exception {

		return SponsoredAdsDaoImpl.InsertAdStats(StatsMap);
	}
	@RequestMapping(value = "/all/companies", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List allcompanies(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.CompaniesDetails();
	}

	@RequestMapping(value = "/company/{CompanyID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List companiesID(HttpServletRequest request, @PathVariable int CompanyID ) throws Exception {

		return SponsoredAdsDaoImpl.CompaniesDetailsByID(CompanyID);
	}
	
	@RequestMapping(value = "/all/campaigns", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List allcampaigns(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.CampaignsDetails();
	}

	@RequestMapping(value = "/campaign/{CampaignID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List allcampaigns(HttpServletRequest request,@PathVariable int CampaignID ) throws Exception {

		return SponsoredAdsDaoImpl.CampaignsDetailsByID(CampaignID);
	}
	
	@RequestMapping(value = "/get/all/ads", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List allcampaignsads(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.CampaignsAds();
	}

	@RequestMapping(value = "/get/ads/{AdID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List campaignsadsID(HttpServletRequest request, @PathVariable int AdID) throws Exception {

		return SponsoredAdsDaoImpl.CampaignsAdsByID(AdID);
	}

	@RequestMapping(value = "/get/stats/{StatsID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List statsID(HttpServletRequest request, @PathVariable int StatsID) throws Exception {

		return SponsoredAdsDaoImpl.AdsStatsByID(StatsID);
	}
	@RequestMapping(value = "/update/company/{CompanyID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updateCompany(@PathVariable int CompanyID, @RequestBody HashMap companyMap, HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.updateCompanyId(CompanyID, companyMap);
	
	
	}
	
	@RequestMapping(value = "/update/campaign/{CampaignID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updateCampaign(@PathVariable int CampaignID, @RequestBody HashMap campaignMap, HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.updateCampaignId(CampaignID, campaignMap);
	
	
	}
	
	@RequestMapping(value = "/update/ad/{AdID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updateCampaignAds(@PathVariable int AdID, @RequestBody HashMap campaignAdsMap, HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.updateCampaignAdsId(AdID, campaignAdsMap);
	
	
	}

	@RequestMapping(value = "/update/adstats/{StatsID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updateAdsStats(@PathVariable int StatsID, @RequestBody HashMap StatsMap, HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.updateAdsStatsId(StatsID, StatsMap);
	
	}
	
	@RequestMapping(value = "/delete/campaign/{CampaignID}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deleteCampaign(@PathVariable int CampaignID,  HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.deleteCampaignId(CampaignID);
	
	
	}
	
	@RequestMapping(value = "/delete/company/{CompanyID}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deleteCompany(@PathVariable int CompanyID,  HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.deleteCompanyId(CompanyID);
	
	
	}

	@RequestMapping(value = "/list/companies", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listcompanies(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.ListCompanies();
	}
	
	@RequestMapping(value = "/list/campaigns", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listcampaigns(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.ListCampaigns();
	}
	

	@RequestMapping(value = "/list/adstypes", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listadstypes(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.ListAdsTypes();
	}
	
	@RequestMapping(value = "/list/adstargettypes", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listadstarget(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.ListAdsTargetTypes();
	}
	
	@RequestMapping(value = "/list/adsslottypes", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List listadsslots(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.ListAdsSlotTypes();
	}

	@RequestMapping(value = "/list/ads/url/{AdType}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody String listadsURL(@PathVariable  (required=false) int AdType,@RequestParam(required = false) Integer DC_Cond,HttpServletRequest request) throws Exception {
		LocalDate currentDate = LocalDate.now();
		String Res=null;
		if(DC_Cond!=null)
		{
			AdType=0;
		}
			  // Reset the request counter if a new day has started
		else {
			DC_Cond=0;
		}

	       Res= SponsoredAdsDaoImpl.AdsURL(AdType,DC_Cond);
//	       request.setAttribute("customData", Res );
		
		
		  
		return Res;
		
	}

	@RequestMapping(value="/parent_disease_id/{article_id}",produces = "application/json", method = RequestMethod.GET )
	public HashMap getParentDiseaseCond(@PathVariable int article_id) {
		
		return SponsoredAdsDaoImpl.getParentDiseaseId(article_id);
	}
	@RequestMapping(path="/ads/clicks/{adId}", produces = "application/json", method = RequestMethod.PUT )
	public int clickIncre(@PathVariable int  adId ) {
		return SponsoredAdsDaoImpl.clicksIncrement(adId);
	}

	@RequestMapping(value="/getall/parent_disease_id",produces = "application/json", method = RequestMethod.GET )
	public List getNameParentDisease() {
		
		return SponsoredAdsDaoImpl.getnameParentDisease();
	}
	
	@RequestMapping(value="/search/companies",produces = "application/json", method = RequestMethod.POST )
	public List<LinkedHashMap<String,Object>>  searchCompanies_byCompanyName(@RequestBody HashMap companies){
		
		return SponsoredAdsDaoImpl.searchCompanies_byCompanyName(companies);
	}
	
	@RequestMapping(value="/search/campaigns",produces = "application/json", method = RequestMethod.POST )
	public List<LinkedHashMap<String,Object>>  searchCompanies_bycampaigns(@RequestBody HashMap campaigns){
		
		return SponsoredAdsDaoImpl.searchCompanies_bycampaigns(campaigns);
	}
	
	@RequestMapping(value="/search/campaignsads",produces = "application/json", method = RequestMethod.POST )
	public List<LinkedHashMap<String,Object>>  searchCompanies_bycampaignsAds(@RequestBody HashMap campaignsAds){
		
		return SponsoredAdsDaoImpl.searchCompanies_bycampaignsAds(campaignsAds);
	}

	@RequestMapping(value = "/create/service", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer addservice(@RequestBody HashMap Service_Map,HttpServletRequest request ) throws Exception {

		return SponsoredAdsDaoImpl.InsertServices(Service_Map);
	}
	
	@RequestMapping(value = "/get/all/services", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<SponsoredServicesMaster> allservices(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.getAllServices();
	}
	
	@RequestMapping(value = "update/service/{ServiceID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updateService(@PathVariable(name = "ServiceID") Integer ServiceID, @RequestBody HashMap ServiceMap, HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.updateService(ServiceID, ServiceMap);
		
	}
	
	@RequestMapping(value = "delete/service/{ServiceID}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deleteService(@PathVariable int ServiceID,  HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.deleteService(ServiceID);
	
	}
	
	@RequestMapping(value = "/get/service/{ServiceID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<SponsoredServicesMaster> getservice(@PathVariable int ServiceID,HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.getService(ServiceID);
	}

	@RequestMapping(value = "/get/services/list/doc", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List servicesList(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.getServicesListDoc();
	}

	@RequestMapping(value = "/get/services/list/doctor", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List servicesListDoctor(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.getServicesListDoctor();
	}
	@RequestMapping(value = "/create/contract", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer addContract(@RequestParam(value = "document" , required = false) CommonsMultipartFile document,@RequestParam("Contract_Map") String ContractMap,
            HttpServletRequest request) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		HashMap<String, Object> Contract_Map = objectMapper.readValue(ContractMap, new TypeReference<HashMap<String, Object>>() {});
		
		return SponsoredAdsDaoImpl.InsertContract(Contract_Map, document);
	}
	@RequestMapping(value = "/get/all/contracts", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<ServiceContract> allcontracts(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.getAllContracts();
	}
	
	@RequestMapping(value = "/update/contract/{ContractID}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer updateContract(@PathVariable int ContractID,@RequestParam(value = "document" , required = false) CommonsMultipartFile document,@RequestParam("Contract_Map") String ContractMap,
            HttpServletRequest request) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		HashMap<String, Object> Contract_Map = objectMapper.readValue(ContractMap, new TypeReference<HashMap<String, Object>>() {});
	
		return SponsoredAdsDaoImpl.updateContract(ContractID, Contract_Map, document);
	}
	
	@RequestMapping(value = "delete/contract/{ContractID}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deleteContract(@PathVariable int ContractID,  HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.deleteContract(ContractID);
	
	}
	
	@RequestMapping(value = "/get/contract/{ContractID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<ServiceContract> getcontract(@PathVariable int ContractID,HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.getContract(ContractID);
	}
}
