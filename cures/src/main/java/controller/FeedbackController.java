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

    // GET all feedbacks (with pagination)
    @GetMapping
    public ResponseEntity<List<Feedback>> getAll(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        List<Feedback> list = feedbackDao.findAll(limit, offset);
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    // GET feedback by ID
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getById(@PathVariable Long id) {
        return feedbackDao.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET feedback by email (optional convenience endpoint)
    @GetMapping("/email/{email}")
    public ResponseEntity<Feedback> getByEmail(@PathVariable String email) {
        return feedbackDao.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
