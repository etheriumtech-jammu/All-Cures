package controller;

import dao.FeedbackDao;
import model.Feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackDao feedbackDao;

    public FeedbackController(FeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }

    // CREATE feedback
    @PostMapping
    public ResponseEntity<Feedback> create(@RequestBody Feedback f) {
        if (!StringUtils.hasText(f.getFirstname()) ||
            !StringUtils.hasText(f.getFeedback()) ||
            !StringUtils.hasText(f.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        Feedback created = feedbackDao.create(f);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

   
}
