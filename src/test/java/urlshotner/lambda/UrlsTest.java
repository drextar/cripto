package urlshotner.lambda;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import urlshotner.lambda.entity.Urls;

class UrlsTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        Urls urls = new Urls();

        // Assert
        assertNotNull(urls.getCreatedAt(), "createdAt should be initialized in default constructor");
        assertTrue(ChronoUnit.SECONDS.between(urls.getCreatedAt(), LocalDateTime.now()) < 1,
                "createdAt should be set to current time");
    }

    @Test
    void testParametrizedConstructor() {
        // Arrange
        String originalUrl = "https://google.com.br";
        String uniqueToken = "ABCDEF1234567890ABCDEF";

        // Act
        Urls urls = new Urls(originalUrl, uniqueToken);

        // Assert
        assertEquals(originalUrl, urls.getUrlOriginal(), "urlOriginal should be set correctly");
        assertEquals(uniqueToken, urls.getUniqueToken(), "uniqueToken should be set correctly");
        assertNotNull(urls.getCreatedAt(), "createdAt should be initialized in parametrized constructor");
        assertTrue(ChronoUnit.SECONDS.between(urls.getCreatedAt(), LocalDateTime.now()) < 1,
                "createdAt should be set to current time");
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Urls urls = new Urls();
        String originalUrl = "https://example.com";
        String uniqueToken = "1234567890ABCDEF1234";

        // Act
        urls.setUrlOriginal(originalUrl);
        urls.setUniqueToken(uniqueToken);

        // Assert
        assertEquals(originalUrl, urls.getUrlOriginal(), "setUrlOriginal/getUrlOriginal should work correctly");
        assertEquals(uniqueToken, urls.getUniqueToken(), "setUniqueToken/getUniqueToken should work correctly");
    }

    @Test
    void testSetUrlOriginal() {
        // Arrange
        Urls urls = new Urls();
        String newUrl = "https://openai.com";

        // Act
        urls.setUrlOriginal(newUrl);

        // Assert
        assertEquals(newUrl, urls.getUrlOriginal(), "setUrlOriginal should update the urlOriginal field");
    }

    @Test
    void testSetUniqueToken() {
        // Arrange
        Urls urls = new Urls();
        String newToken = "FEDCBA0987654321FEDCBA";

        // Act
        urls.setUniqueToken(newToken);

        // Assert
        assertEquals(newToken, urls.getUniqueToken(), "setUniqueToken should update the uniqueToken field");
    }

    @Test
    void testGetCreatedAt() {
        // Arrange
        Urls urls = new Urls();
        LocalDateTime now = LocalDateTime.now();

        // Act
        LocalDateTime createdAt = urls.getCreatedAt();

        // Assert
        assertNotNull(createdAt, "getCreatedAt should not return null");
        assertTrue(ChronoUnit.SECONDS.between(createdAt, now) < 1,
                "createdAt should be set to current time");
    }
}
