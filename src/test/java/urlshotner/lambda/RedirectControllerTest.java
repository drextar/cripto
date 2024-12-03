package urlshotner.lambda;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import urlshotner.lambda.controller.RedirectController;
import urlshotner.lambda.entity.Urls;
import urlshotner.lambda.repository.UrlsRepository;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedirectControllerTest {

    @Mock
    private UrlsRepository urlsRepository;

    @InjectMocks
    private RedirectController redirectController;

    private Urls sampleUrls;

    @BeforeEach
    void setUp() {
        sampleUrls = new Urls("https://example.com", "ABCDEF1234567890ABCDEF");
    }

    @Test
    void testRedirect_Success() {
        // Arrange
        String token = "ABCDEF1234567890ABCDEF";
        when(urlsRepository.findByUniqueToken(token)).thenReturn(sampleUrls);

        // Act
        Response response = redirectController.redirect(token);

        // Assert
        assertEquals(Response.Status.SEE_OTHER.getStatusCode(), response.getStatus());
        assertEquals(URI.create(sampleUrls.getUrlOriginal()), response.getLocation());
    }

    @Test
    void testRedirect_TokenNotFound() {
        // Arrange
        String token = "ABCDEF1234567890ABCDEF";
        when(urlsRepository.findByUniqueToken(token)).thenReturn(null);

        // Act
        Response response = redirectController.redirect(token);

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        RedirectController.ErrorResponse body = (RedirectController.ErrorResponse) response.getEntity();
        assertEquals("Token not found.", body.getMessage());
    }

//    @Test
//    void testRedirect_InvalidUrlFormat() {
//        // Arrange
//        String token = "ABCDEF1234567890ABCDEF";
//        Urls invalidUrls = new Urls("invalid-url", token);
//        when(urlsRepository.findByUniqueToken(token)).thenReturn(invalidUrls);
//
//        // Act
//        Response response = redirectController.redirect(token);
//
//        // Assert
////        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
//        RedirectController.ErrorResponse body = (RedirectController.ErrorResponse) response.getEntity();
//        assertEquals("Invalid URL format.", body.getMessage());
//    }
}
