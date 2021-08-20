package controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.PromoDaoImpl;

@RestController
@RequestMapping(path = "/promo")
public class PromoController {

	@Autowired
	private PromoDaoImpl promoDaoImpl;

	@RequestMapping(value = "/all", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getAllPromoDetails() {
		return promoDaoImpl.getAllPromoDetails();
	}
	
	@RequestMapping(value = "/{promo_id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getPromoDetailsById(@PathVariable int promo_id) {
		return promoDaoImpl.getPromoDetailsById(promo_id);
	}

	@RequestMapping(value = "/create", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int addPromoDetails(@RequestBody HashMap promoMasterMap) {
		return promoDaoImpl.addPromoDetails(promoMasterMap);
	}

	@RequestMapping(value = "/{promo_id}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updatePromoMaster(@PathVariable int promo_id, @RequestBody HashMap promoMasterMap) {
		return promoDaoImpl.updatePromoDetails(promo_id, promoMasterMap);
	}

	@RequestMapping(value = "/{promo_id}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deletePromoMaster(@PathVariable int promo_id) {
		return promoDaoImpl.deletePromoId(promo_id);
	}

	@RequestMapping(value = "/articlespromostage/{stage}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List allArticleByPromotStage(@PathVariable int stage) {
		return promoDaoImpl.allArticleByPromotStage(stage);

	}
	
	@RequestMapping(value = "/articlespromostage", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List allArticleByPromotStageAll() {
		return promoDaoImpl.allArticleByPromotStage(-1);

	}

	@RequestMapping(value = "/paidstage/combined", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int setPromoPaidStage(@PathVariable int reviewed_by, @RequestBody HashMap articlePromoIds) {
		// articlePromoIds is comma separated id's
		return promoDaoImpl.setPromoPaidStage(articlePromoIds, reviewed_by);
	}
	
	@RequestMapping(value = "/reviewedby/{reviewed_by}/stage/{stage}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int getReviewDone(@PathVariable int reviewed_by, @PathVariable int stage,
			@RequestBody HashMap articleIds) {
		// rateids is comma separated rate id's
		return promoDaoImpl.setPromoPaidStage(articleIds, reviewed_by, stage);

	}

}