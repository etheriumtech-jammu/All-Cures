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

	@RequestMapping(value = "/all/companies", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List allcompanies(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.CompaniesDetails();
	}

	@RequestMapping(value = "/all/campaigns", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List allcampaigns(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.CampaignsDetails();
	}
	
	@RequestMapping(value = "/get/all/ads", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List allcampaignsads(HttpServletRequest request) throws Exception {

		return SponsoredAdsDaoImpl.CampaignsAds();
	}

	@RequestMapping(value = "/update/company/{CompanyID}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int updateCompany(@PathVariable int CompanyID, @RequestBody HashMap companyMap, HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.updateCompanyId(CompanyID, companyMap);
	
	
	}
	
	@RequestMapping(value = "/update/campaign/{CampaignID}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int updateCampaign(@PathVariable int CampaignID, @RequestBody HashMap campaignMap, HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.updateCampaignId(CampaignID, campaignMap);
	
	
	}
	
	@RequestMapping(value = "/update/ad/{AdID}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int updateCampaignAds(@PathVariable int AdID, @RequestBody HashMap campaignAdsMap, HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.updateCampaignAdsId(AdID, campaignAdsMap);
	
	
	}

	@RequestMapping(value = "/delete/campaign/{CampaignID}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int deleteCampaign(@PathVariable int CampaignID,  HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.deleteCampaignId(CampaignID);
	
	
	}
	
	@RequestMapping(value = "/delete/company/{CompanyID}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int deleteCompany(@PathVariable int CompanyID,  HttpServletRequest request) {
	
	return SponsoredAdsDaoImpl.deleteCompanyId(CompanyID);
	
	
	}
	
}
