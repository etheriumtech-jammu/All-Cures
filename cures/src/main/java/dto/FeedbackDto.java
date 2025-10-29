package dto;

import model.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackDto {
    Feedback create(Feedback feedback);
    Optional<Feedback> findById(Long id);
    Optional<Feedback> findByEmail(String email);
    List<Feedback> findAll(int limit, int offset);
    int update(Feedback feedback);
    int delete(Long id);
}
