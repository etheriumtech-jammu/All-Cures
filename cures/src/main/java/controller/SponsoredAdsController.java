package controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import dao.SponsoredAdsDaoImpl;
@RestController
@RequestMapping(path = "/sponsored")
public class SponsoredAdsController {

	@RequestMapping(value = "/create/company", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer addcompanies(@RequestBody HashMap Company_Map,HttpServletRequest request ) throws Exception {

		return SponsoredAdsDaoImpl.InsertCompaniesDetails(Company_Map);
	}

	@RequestMapping(value = "/create/campaign", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer addcampaign(@RequestBody HashMap Campaign_Map, HttpServletRequest request ) throws Exception {

		return SponsoredAdsDaoImpl.InsertCampaignDetails(Campaign_Map);
	}
	
	@RequestMapping(value = "/create/ad", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer addcampaignsads(@RequestBody HashMap AdMap,HttpServletRequest request ) throws Exception {

		return SponsoredAdsDaoImpl.InsertAdDetails(AdMap);
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
	
	@RequestMapping(value = "/delete/campaign/{CampaignID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int deleteCampaign(@PathVariable int CampaignID,  HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.deleteCampaignId(CampaignID);
	
	
	}
	
	@RequestMapping(value = "/delete/company/{CompanyID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int deleteCompany(@PathVariable int CompanyID,  HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.deleteCompanyId(CompanyID);
	
	
	}
	
}
