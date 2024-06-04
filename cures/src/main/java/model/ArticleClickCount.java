package model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class ArticleClickCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_id", nullable = false)
    private Long articleID;

    @Column(name = "click_date", nullable = false)
    private LocalDate clickDate;

    @Column(name = "click_count", nullable = false)
    private Long clickCount;

    @Column(name = "CreatedDate")
    private Timestamp CreatedDate;

    @Column(name = "LastUpdatedDate")
    private Timestamp LastUpdatedDate;

    @Column(name = "Status")
    private Integer Status;
    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleID;
    }

    public void setArticleId(Long articleId) {
        this.articleID = articleId;
    }

    public LocalDate getClickDate() {
        return clickDate;
    }

    public void setClickDate(LocalDate clickDate) {
        this.clickDate = clickDate;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }
}
