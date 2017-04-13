package app.daos;

import java.time.OffsetDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import app.model.beans.FindDecksResultBean;
import app.model.entities.Card;
import app.model.entities.CardInstance;
import app.model.entities.CardInstance_;
import app.model.entities.CardPlan;
import app.model.entities.CardPlan_;
import app.model.entities.Card_;
import app.model.entities.Deck;
import app.model.entities.Deck_;

public class DeckDao extends AbstractDao
{
	public DeckDao(EntityManager em)
	{
		super(em);
	}


	public boolean checkDecksExistance()
	{
		CriteriaBuilder builder = getEM().getCriteriaBuilder();
		CriteriaQuery<Tuple> criteria = builder.createQuery(Tuple.class);

		Root<Deck> root = criteria.from(Deck.class);

		Path<Integer> pId = root.get(Deck_.id);

		criteria.multiselect(pId);

		TypedQuery<Tuple> query = getEM().createQuery(criteria);

		try
		{
			query.setMaxResults(1);
			query.getSingleResult();

			return true;
		}
		catch (NoResultException e)
		{
			return false;
		}
	}

	public void deleteDeck(int deckId)
	{
		CriteriaBuilder builder = getEM().getCriteriaBuilder();
		CriteriaDelete<Deck> criteria = builder.createCriteriaDelete(Deck.class);

		Root<Deck> root = criteria.from(Deck.class);

		Path<Integer> pId = root.get(Deck_.id);

		Predicate filter = builder.equal(pId, deckId);

		criteria.where(filter);

		Query query = getEM().createQuery(criteria);
		query.executeUpdate();
	}

	public List<FindDecksResultBean> findDecks(
			String descriptionFilter,
			int firstResult,
			int maxResults)
	{
		if (descriptionFilter == null)
		{
			throw new IllegalArgumentException();
		}
		if (firstResult < 0)
		{
			throw new IllegalArgumentException();
		}
		if (maxResults <= 0)
		{
			throw new IllegalArgumentException();
		}

		CriteriaBuilder builder = getEM().getCriteriaBuilder();
		CriteriaQuery<FindDecksResultBean> criteria = builder.createQuery(FindDecksResultBean.class);

		Root<Deck> root = criteria.from(Deck.class);
		Join<Deck, Card> cardJoin = root.join(Deck_.cards, JoinType.LEFT);
		Join<Card, CardInstance> cardInstanceJoin = cardJoin.join(Card_.instances, JoinType.LEFT);

		Path<Integer> pId = root.get(Deck_.id);
		Path<String> pDescription = root.get(Deck_.description);
		Path<String> pNotes = root.get(Deck_.notes);

		Path<Boolean> pSideDisabled = cardInstanceJoin.get(CardInstance_.disabled);

		criteria.select(builder.construct(
				FindDecksResultBean.class,
				builder.least(pId),
				builder.least(pDescription),
				builder.least(pNotes),
				builder.count(cardInstanceJoin)));

		Predicate filter = builder.or(
				builder.isNull(pSideDisabled),
				builder.equal(pSideDisabled, false));

		if (descriptionFilter.length() > 0)
		{
			Predicate filterDescription = builder.like(pDescription, "%" + descriptionFilter + "%");
			filter = builder.and(
					filter,
					filterDescription);
		}

		if (filter != null)
		{
			criteria.where(filter);
		}

		criteria.groupBy(pId);

		criteria.orderBy(
				builder.asc(pDescription),
				builder.asc(pId));

		TypedQuery<FindDecksResultBean> query = getEM().createQuery(criteria);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return query.getResultList();
	}

	public Deck getDeck(int deckId)
	{
		return getEM().find(Deck.class, deckId);
	}

	public long getNumberOfCardInstancesToReview(int deckId)
	{
		CriteriaBuilder builder = getEM().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

		Root<Deck> root = criteria.from(Deck.class);
		Join<Deck, Card> cardJoin = root.join(Deck_.cards, JoinType.LEFT);
		Join<Card, CardInstance> cardInstanceJoin = cardJoin.join(Card_.instances, JoinType.LEFT);
		Join<CardInstance, CardPlan> cardPlanJoin = cardInstanceJoin.join(CardInstance_.plan, JoinType.LEFT);

		Path<Integer> pDeckId = root.get(Deck_.id);
		Path<String> pSideA = cardJoin.get(Card_.sideA);
		Path<String> pSideB = cardJoin.get(Card_.sideB);
		Path<Boolean> pSideDisabled = cardInstanceJoin.get(CardInstance_.disabled);
		Path<OffsetDateTime> pNextDate = cardPlanJoin.get(CardPlan_.nextDate);

		criteria.select(builder.count(cardInstanceJoin));

		OffsetDateTime now = OffsetDateTime.now();
		Predicate filterNextDate = builder.or(
				builder.isNull(pNextDate),
				builder.lessThanOrEqualTo(pNextDate, now));

		Predicate filter = builder.and(
				builder.equal(pDeckId, deckId),
				builder.notEqual(pSideA, ""),
				builder.notEqual(pSideB, ""),
				builder.isFalse(pSideDisabled),
				filterNextDate);

		criteria.where(filter);

		TypedQuery<Long> query = getEM().createQuery(criteria);
		return query.getSingleResult();
	}

	public void persistDeck(Deck deck)
	{
		getEM().persist(deck);
	}
}
