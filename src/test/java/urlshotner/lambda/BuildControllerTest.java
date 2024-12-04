package urlshotner.lambda;

import org.junit.jupiter.api.BeforeEach;
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

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuildControllerTest {

    @Mock
    private UrlsRepository urlsRepository;

    @InjectMocks
    private BuildController buildController;

    private Set<String> generatedTokens;

    @BeforeEach
    void setUp() {
        generatedTokens = new HashSet<>();
    }

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

    @Test
    void testGenerateToken() {
        // Arrange
        String url = "https://example.com";

        // Act
        ResponseEntity<?> response = buildController.build(url);

        // Assert
        assertNotNull(response, "A resposta não deve ser nula.");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "O status da resposta deve ser 200 OK.");

        Object responseBody = response.getBody();
        assertNotNull(responseBody, "O corpo da resposta não deve ser nulo.");
        assertTrue(responseBody instanceof BuildController.UrlEncodedResponse, "O corpo da resposta deve ser uma instância de UrlEncodedResponse.");

        BuildController.UrlEncodedResponse urlEncodedResponse = (BuildController.UrlEncodedResponse) responseBody;
        assertNotNull(urlEncodedResponse.getUrl_encoded(), "O campo 'url_encoded' não deve ser nulo.");
        assertTrue(urlEncodedResponse.getUrl_encoded().startsWith("https://pagar.vindi/"), "O campo 'url_encoded' deve começar com 'https://pagar.vindi/'.");

        // Verificar o token gerado
        String token = urlEncodedResponse.getUrl_encoded().replace("https://pagar.vindi/", "");
        assertEquals(20, token.length(), "O token deve ter exatamente 20 caracteres.");
        assertTrue(token.matches("[0-9A-F]+"), "O token deve conter apenas caracteres hexadecimais.");
    }
}
