package controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.FavouriteDaoImpl;


@RestController
@RequestMapping(path = "/favourite")
public class FavouriteController {

	@Autowired
	private FavouriteDaoImpl favouriteDaoImpl;
	
	@RequestMapping(value = "/userid/{user_id}/articleid/{article_id}/favourite", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getFavouriteDetails(@PathVariable int user_id, @PathVariable int article_id)  {
		return favouriteDaoImpl.getAllFavouriteDetails(user_id, article_id);

	}
	
	@RequestMapping(value = "/userid/{user_id}/favouritearticle", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getFavouriteArticleDetails(@PathVariable int user_id)  {
		return favouriteDaoImpl.getAllFavouriteUserDetails(user_id);

	}
	
	
	@RequestMapping(value = "/userid/{user_id}/articleid/{article_id}/status/{status}/create", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int addFavouriteDetails(@PathVariable int user_id, @PathVariable int article_id,@PathVariable int status)  {
		return favouriteDaoImpl.addFavouriteDetails(user_id, article_id,status);

	}
	
	@RequestMapping(value = "/userid/{user_id}/articleid/{article_id}/status/{status}/delete", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int addFavouDetails(@PathVariable int user_id, @PathVariable int article_id,@PathVariable int status)  {
		return favouriteDaoImpl. deleteFavourite(user_id, article_id,status);

	}
	
//	@RequestMapping(value = "/userid/{user_id}/articleid/{article_id}/delete", produces = "application/json", method = RequestMethod.DELETE)
//	public @ResponseBody int addDeleteDetails(@PathVariable int user_id, @PathVariable int article_id)  {
//		return favouriteDaoImpl.deleteFavouriteId(user_id, article_id);
//
//	}
	



}