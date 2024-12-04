package urlshotner.lambda.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "urls")
public class Urls {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "url_original", nullable = false)
    private String urlOriginal;

    @Column(name = "unique_token", nullable = false, unique = true)
    private String uniqueToken;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Urls() {
        this.createdAt = LocalDateTime.now();
    }

    public Urls(String urlOriginal, String uniqueToken) {
        this.urlOriginal = urlOriginal;
        this.uniqueToken = uniqueToken;
        this.createdAt = LocalDateTime.now();
    }

    // Getters e Setters

//    public Long getId() {
//        return id;
//    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    public String getUniqueToken() {
        return uniqueToken;
    }

    public void setUniqueToken(String uniqueToken) {
        this.uniqueToken = uniqueToken;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
