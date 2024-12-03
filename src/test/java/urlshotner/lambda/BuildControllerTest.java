package urlshotner.lambda;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import urlshotner.lambda.entity.Urls;
import urlshotner.lambda.repository.UrlsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Importações das classes necessárias
import urlshotner.lambda.controller.BuildController;
import urlshotner.lambda.controller.BuildController.ErrorResponse;
import urlshotner.lambda.controller.BuildController.UrlEncodedResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuildControllerTest {

    @Mock
    private UrlsRepository urlsRepository;

    @InjectMocks
    private BuildController buildController;

    @Test
    void testBuild_Success() {
        // Arrange
        String url = "https://google.com.br";
        doNothing().when(urlsRepository).save(any(Urls.class));

        // Act
        ResponseEntity<?> response = buildController.build(url);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof UrlEncodedResponse);
        UrlEncodedResponse body = (UrlEncodedResponse) response.getBody();
        assertNotNull(body.getUrl_encoded());
        assertTrue(body.getUrl_encoded().startsWith("https://pagar.vindi/"));
    }

    @Test
    void testBuild_MissingUrlParameter() {
        // Act
        ResponseEntity<?> response = buildController.build(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse body = (ErrorResponse) response.getBody();
        assertEquals("Missing 'url' query parameter", body.getMessage());
    }

    @Test
    void testBuild_EmptyUrlParameter() {
        // Act
        ResponseEntity<?> response = buildController.build("");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse body = (ErrorResponse) response.getBody();
        assertEquals("Missing 'url' query parameter", body.getMessage());
    }

    @Test
    void testBuild_ExceptionThrown() {
        // Arrange
        String url = "https://google.com.br";
        doThrow(new RuntimeException("Database error")).when(urlsRepository).save(any(Urls.class));

        // Act
        ResponseEntity<?> response = buildController.build(url);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse body = (ErrorResponse) response.getBody();
        assertEquals("An error occurred while processing your request.", body.getMessage());
    }
}