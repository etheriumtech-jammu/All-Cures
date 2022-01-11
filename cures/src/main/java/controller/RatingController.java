package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.RatingDaoImpl;

@RestController
@RequestMapping(path = "/rating")
public class RatingController {

	@Autowired
	private RatingDaoImpl ratingDaoImpl;

	@RequestMapping(value = "/target/{target_id}/targettype/{target_type_id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getRatingDetails(@PathVariable int target_id, @PathVariable int target_type_id, @RequestParam(required = false) Integer userid) {
		return ratingDaoImpl.findRatingByIdandType(target_id, target_type_id, userid);

	}

	@RequestMapping(value = "/target/{target_id}/targettype/{target_type_id}/avg", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody Float getAverageRatingDetails(@PathVariable int target_id, @PathVariable int target_type_id) {
		return ratingDaoImpl.findAverageRatingByIdandType(target_id, target_type_id);

	}

	@RequestMapping(value = "/reviewedby/{reviewed_by}/reviewed/{reviewed}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int getReviewDone(@PathVariable int reviewed_by, @PathVariable int reviewed,
			@RequestBody HashMap rateids) {
		// rateids is comma separated rate id's
		return ratingDaoImpl.getReviewDone(rateids, reviewed_by, reviewed);

	}

	@RequestMapping(value = "/reviewedby/{reviewed_by}/combined", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int getReviewDone(@PathVariable int reviewed_by,
			@RequestBody HashMap reviewedRateIdsCombined) {
		// rateids is comma separated rate id's
		return ratingDaoImpl.getReviewDone(reviewedRateIdsCombined, reviewed_by);

	}

	@RequestMapping(value = "/comments/{reviewed}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List allcommentsByReviewedStatus(@PathVariable int reviewed) {
		return ratingDaoImpl.allcommentsByReviewedStatus(reviewed);

	}

	// above also using same method in ratingDaoimpl.allcommentsByReviewedStatus
	// with {reviewed} removed
	@RequestMapping(value = "/comments", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List allcommentsByReviewedStatusNoPathVariable() {
		return ratingDaoImpl.allcommentsByReviewedStatus(-1);

	}

}
