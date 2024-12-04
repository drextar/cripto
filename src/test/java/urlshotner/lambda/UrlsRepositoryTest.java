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
import jakarta.persistence.TypedQuery;
import urlshotner.lambda.repository.UrlsRepository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UrlsRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Urls> typedQuery;

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

    @Test
    void testFindByUniqueToken_Success() {
        // Arrange
        String uniqueToken = "ABCDEF1234567890ABCDEF";
        when(entityManager.createQuery(
                "SELECT u FROM Urls u WHERE u.uniqueToken = :uniqueToken", Urls.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("uniqueToken", uniqueToken)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(sampleUrls);

        // Act
        Urls result = urlsRepository.findByUniqueToken(uniqueToken);

        // Assert
        assertNotNull(result);
        assertEquals(sampleUrls, result);
        verify(entityManager, times(1)).createQuery(
                "SELECT u FROM Urls u WHERE u.uniqueToken = :uniqueToken", Urls.class);
        verify(typedQuery, times(1)).setParameter("uniqueToken", uniqueToken);
        verify(typedQuery, times(1)).getSingleResult();
    }

    @Test
    void testFindByUniqueToken_NoResult() {
        // Arrange
        String uniqueToken = "INVALID_TOKEN";
        when(entityManager.createQuery(
                "SELECT u FROM Urls u WHERE u.uniqueToken = :uniqueToken", Urls.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("uniqueToken", uniqueToken)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(new PersistenceException("No result"));

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            urlsRepository.findByUniqueToken(uniqueToken);
        });

        assertEquals("No result", exception.getMessage());
        verify(entityManager, times(1)).createQuery(
                "SELECT u FROM Urls u WHERE u.uniqueToken = :uniqueToken", Urls.class);
        verify(typedQuery, times(1)).setParameter("uniqueToken", uniqueToken);
        verify(typedQuery, times(1)).getSingleResult();
    }
}
