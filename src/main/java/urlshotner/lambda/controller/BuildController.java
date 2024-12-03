package urlshotner.lambda.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.inject.Inject;
import urlshotner.lambda.entity.Urls;
import urlshotner.lambda.repository.UrlsRepository;

import java.security.SecureRandom;

@RestController
@RequestMapping("/build")
public class BuildController {

    @Inject
    private UrlsRepository urlsRepository;

    @PostMapping
    public ResponseEntity<?> build(@RequestParam("url") String url) {
        if (url == null || url.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Missing 'url' query parameter"));
        }

        try {
            String token = generateToken(20);
            Urls urls = new Urls(url, token);
            urlsRepository.save(urls);

            String urlEncoded = "https://pagar.vindi/" + token;
            return ResponseEntity.ok(new UrlEncodedResponse(urlEncoded));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while processing your request."));
        }
    }

    private String generateToken(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        String hexChars = "0123456789ABCDEF";

        for (int i = 0; i < length; i++) {
            sb.append(hexChars.charAt(random.nextInt(16)));
        }

        return sb.toString();
    }

    public static class UrlEncodedResponse {
        private String url_encoded;

        public UrlEncodedResponse() {}

        public UrlEncodedResponse(String url_encoded) {
            this.url_encoded = url_encoded;
        }

        public String getUrl_encoded() {
            return url_encoded;
        }

        public void setUrl_encoded(String url_encoded) {
            this.url_encoded = url_encoded;
        }
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse() {}

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}