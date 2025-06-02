package sk.tuke.bricksbreaking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.bricksbreaking.entity.Rating;


@Service
@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        try {
            // najskôr skúsime nájsť existujúce hodnotenie pre danú hru a hráča
            Rating existingRating = getRatingEntity(rating.getGame(), rating.getPlayer());

            if (existingRating == null) {
                entityManager.persist(rating);
            } else {
                existingRating.setRating(rating.getRating());
                existingRating.setRatedOn(rating.getRatedOn());
                entityManager.merge(existingRating);
            }
        } catch (Exception e) {
            throw new RatingException("Problem with inserting your rating", e);
        }
    }

    @Override
    public double getAverageRating(String game) {
        Double result = entityManager
                .createQuery("SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game", Double.class)
                .setParameter("game", game)
                .getSingleResult();

        return result != null ? result : 0;
    }

    @Override
    public int getRating(String game, String player) {
        Rating rating = getRatingEntity(game, player);
        return (rating != null) ? rating.getRating() : 0;
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Rating.delete").executeUpdate();
    }

    private Rating getRatingEntity(String game, String player) {
        try {
            return entityManager.createQuery(
                            "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player", Rating.class)
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // žiadny rating ešte neexistuje
        }
    }
}
