package urlshotner.lambda;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

class UrlsTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        Urls urls = new Urls();

        // Assert
        assertNotNull(urls.getCreatedAt(), "createdAt should be initialized in default constructor");
        // Verifica se createdAt está próximo ao momento atual (até 1 segundo de diferença)
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
        // Verifica se createdAt está próximo ao momento atual (até 1 segundo de diferença)
        assertTrue(ChronoUnit.SECONDS.between(urls.getCreatedAt(), LocalDateTime.now()) < 1,
                "createdAt should be set to current time");
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Urls urls = new Urls();
        Long id = 1L;
        String originalUrl = "https://example.com";
        String uniqueToken = "1234567890ABCDEF1234";
        LocalDateTime createdAt = LocalDateTime.of(2023, 12, 2, 12, 0);

        // Act
        // Note: Não há setter para id, pois geralmente é gerado automaticamente pelo banco de dados.
        // Mas para cobertura completa, podemos usar reflection ou simplesmente ignorar o teste do setter de id.
        // Aqui, apenas testaremos os getters e setters disponíveis.
        urls.setUrlOriginal(originalUrl);
        urls.setUniqueToken(uniqueToken);
        // Assume que você tem um setter para createdAt se quiser testá-lo; caso contrário, ignore.
        // urls.setCreatedAt(createdAt); // Comentado se não houver setter

        // Assert
        assertEquals(originalUrl, urls.getUrlOriginal(), "setUrlOriginal/getUrlOriginal should work correctly");
        assertEquals(uniqueToken, urls.getUniqueToken(), "setUniqueToken/getUniqueToken should work correctly");
        // assertEquals(createdAt, urls.getCreatedAt(), "setCreatedAt/getCreatedAt should work correctly"); // Se houver setter
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
        // Verifica se createdAt está próximo ao momento atual (até 1 segundo de diferença)
        assertTrue(ChronoUnit.SECONDS.between(createdAt, now) < 1,
                "createdAt should be set to current time");
    }

    // Opcional: Teste para verificar a geração do ID (se houver setter, o que geralmente não há)
    // @Test
    // void testSetId() {
    //     Urls urls = new Urls();
    //     Long newId = 100L;
    //     urls.setId(newId);
    //     assertEquals(newId, urls.getId(), "setId should update the id field");
    // }
}
