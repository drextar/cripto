package urlshotner.lambda.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import urlshotner.lambda.entity.Urls;

@ApplicationScoped
public class UrlsRepository {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void save(Urls urls) {
        entityManager.persist(urls);
    }

}
