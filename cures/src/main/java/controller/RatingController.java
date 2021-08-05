package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.RatingDaoImpl;

@RestController
@RequestMapping(path = "/rating")
public class RatingController {

	@Autowired
	private RatingDaoImpl ratingDaoImpl;

	@RequestMapping(value = "/target/{target_id}/targettype/{target_type_id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getCityDetails(@PathVariable int target_id, @PathVariable int target_type_id) {
		return ratingDaoImpl.findRatingByIdandType(target_id, target_type_id);

	}

}
