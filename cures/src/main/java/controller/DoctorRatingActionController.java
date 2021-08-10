package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DoctorsratingDaoImpl;
import dao.RatingDaoImpl;
import util.Constant;

/**
 * Servlet implementation class DoctorRatingActionController TODO: Add Exception
 * Handling TODO: Every API must return some status code to the front end and an
 * error code and error msg if unsuccessful
 */
public class DoctorRatingActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DoctorRatingActionController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Constant.log("In Rating Action Controller", 0);
		String cmd = request.getParameter("cmd");
		if (cmd != null && cmd.equals("rateAsset")) {
			rateAssetReq(request);
		} else if (cmd != null && cmd.equals("getTopRatedAssets")) {
			// get the AssetType (Doctors or Articles or Medicine or Whatever
			// get How Many e.g. Top 5 or Top 10
			// Return these Assets as JSON
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public void rateAsset(String comments, int ratedBy, int target, int targetType, int ratedByType, float rating) {
		RatingDaoImpl ratingDao = new RatingDaoImpl();
		List listOfRatings = ratingDao.findRatingByIdandTypeandRatedByandRatedByType(target, targetType, ratedBy,
				ratedByType);
		if (null != listOfRatings && listOfRatings.size() > 0) {
			System.out.println("rating updated");
			int value = ratingDao.updateRatingCommentsCombined(target, targetType, ratedBy, ratedByType, rating, comments);
		} else {
			System.out.println("rating created");
			DoctorsratingDaoImpl docrating = new DoctorsratingDaoImpl();
			String value = docrating.saveRating(comments, ratedBy, ratedByType, target, targetType, rating);
		}
	}

	public void rateAssetReq(HttpServletRequest request) {
		String comments = request.getParameter("comments");
		String ratedbyid = request.getParameter("ratedbyid");
		String ratedbytype = request.getParameter("ratedbytype");
		String targetid = request.getParameter("targetid");
		String targetType = request.getParameter("targetTypeid");
		String ratingval = request.getParameter("ratingVal");
		System.out.println("ratingVal" + ratingval);
		int targetTypeid = 0;
		int ratebyTypeid = 0;
		if (targetType != null && (targetType.equals("doctor") || "1".equalsIgnoreCase(targetType))) {
			targetTypeid = 1;
		} else if (targetType != null && (targetType.equals("article") || "2".equalsIgnoreCase(targetType))) {
			targetTypeid = 2;
		} else if (targetType != null && (targetType.equals("medicine") || "3".equalsIgnoreCase(targetType))) {
			targetTypeid = 3;
		}

		if (ratedbytype != null && (ratedbytype.equals("doctor") || "1".equalsIgnoreCase(ratedbytype))) {
			ratebyTypeid = 1;
		} else if (ratedbytype != null && (ratedbytype.equals("patient") || "2".equalsIgnoreCase(targetType))) {
			ratebyTypeid = 2;
		}
		rateAsset(comments, Integer.parseInt(ratedbyid), Integer.parseInt(targetid), targetTypeid, ratebyTypeid,
				Float.parseFloat(ratingval));
	}

}
