package urlshotner.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import urlshotner.lambda.entity.Urls;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import urlshotner.lambda.repository.UrlsRepository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UrlsRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UrlsRepository urlsRepository;

    private Urls sampleUrls;

    @BeforeEach
    void setUp() {
        sampleUrls = new Urls("https://example.com", "ABCDEF1234567890ABCDEF");
    }

    @Test
    void testSave_Success() {
        // Arrange
        doNothing().when(entityManager).persist(sampleUrls);

        // Act
        urlsRepository.save(sampleUrls);

        // Assert
        verify(entityManager, times(1)).persist(sampleUrls);
    }

    @Test
    void testSave_PersistenceExceptionThrown() {
        // Arrange
        doThrow(new PersistenceException("Persistence error")).when(entityManager).persist(sampleUrls);

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            urlsRepository.save(sampleUrls);
        });

        assertEquals("Persistence error", exception.getMessage());
        verify(entityManager, times(1)).persist(sampleUrls);
    }
}