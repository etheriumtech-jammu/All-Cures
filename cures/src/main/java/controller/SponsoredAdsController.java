package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.ChatDaoImpl;
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

	
}
