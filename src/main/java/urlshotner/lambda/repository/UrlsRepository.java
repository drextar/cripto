package urlshotner.lambda.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import urlshotner.lambda.entity.Urls;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class UrlsRepository {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void save(Urls urls) {
        entityManager.persist(urls);
    }

    public Urls findByUniqueToken(String uniqueToken) {
        TypedQuery<Urls> query = entityManager.createQuery(
                "SELECT u FROM Urls u WHERE u.uniqueToken = :uniqueToken", Urls.class);
        query.setParameter("uniqueToken", uniqueToken);
        return query.getSingleResult();
    }

}
