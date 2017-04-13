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

import app.model.beans.FindCardsResultBean;
import app.model.entities.Card;
import app.model.entities.CardInstance;
import app.model.entities.CardInstance_;
import app.model.entities.CardPlan;
import app.model.entities.CardPlan_;
import app.model.entities.Card_;
import app.model.entities.Deck;
import app.model.entities.Deck_;

public class CardDao extends AbstractDao
{
	public CardDao(EntityManager em)
	{
		super(em);
	}


	public boolean checkCardsExistance(int deckId)
	{
		CriteriaBuilder builder = getEM().getCriteriaBuilder();
		CriteriaQuery<Tuple> criteria = builder.createQuery(Tuple.class);

		Root<Card> root = criteria.from(Card.class);

		Join<Card, Deck> deckJoin = root.join(Card_.deck, JoinType.LEFT);

		Path<Integer> pCardId = root.get(Card_.id);

		criteria.multiselect(pCardId);

		Path<Integer> pDeckId = deckJoin.get(Deck_.id);

		Predicate filter = builder.equal(pDeckId, deckId);
		criteria.where(filter);

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

	public void deleteCard(int cardId)
	{
		CriteriaBuilder builder = getEM().getCriteriaBuilder();
		CriteriaDelete<Card> criteria = builder.createCriteriaDelete(Card.class);

		Root<Card> root = criteria.from(Card.class);

		Path<Integer> pId = root.get(Card_.id);

		Predicate filter = builder.equal(pId, cardId);

		criteria.where(filter);

		Query query = getEM().createQuery(criteria);
		query.executeUpdate();
	}

	public List<FindCardsResultBean> findCards(
			int deckId,
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
		CriteriaQuery<FindCardsResultBean> criteria = builder.createQuery(FindCardsResultBean.class);

		Root<Card> root = criteria.from(Card.class);
		Join<Card, CardInstance> cardInstanceJoin = root.join(Card_.instances, JoinType.LEFT);

		Path<Integer> pCardId = root.get(Card_.id);
		Path<String> pSideA = root.get(Card_.sideA);
		Path<String> pSideB = root.get(Card_.sideB);
		Path<String> pNotes = root.get(Card_.notes);
		Path<String> pTags = root.get(Card_.tags);

		Path<Boolean> pSideAToB = cardInstanceJoin.get(CardInstance_.sideAToB);
		Path<Boolean> pSideAToBDisabled = cardInstanceJoin.get(CardInstance_.disabled);

		criteria.select(builder.construct(
				FindCardsResultBean.class,
				pCardId,
				pSideA,
				pSideB,
				pNotes,
				pTags,
				pSideAToBDisabled));

		Predicate filter = builder.equal(pSideAToB, true);

		if (deckId > 0)
		{
			Join<Card, Deck> deckJoin = root.join(Card_.deck, JoinType.LEFT);

			Path<Integer> pDeckId = deckJoin.get(Deck_.id);

			Predicate filterDeckId = builder.equal(pDeckId, deckId);
			filter = builder.and(
					filter,
					filterDeckId);
		}

		if (descriptionFilter.length() > 0)
		{
			Predicate filterDescription = builder.or(
					builder.like(pSideA, "%" + descriptionFilter + "%"),
					builder.like(pSideB, "%" + descriptionFilter + "%"),
					builder.like(pTags, "%" + descriptionFilter + "%"));
			filter = builder.and(
					filter,
					filterDescription);
		}

		if (filter != null)
		{
			criteria.where(filter);
		}

		criteria.orderBy(
				builder.asc(pTags),
				builder.asc(pSideA),
				builder.asc(pSideB),
				builder.asc(pCardId));

		TypedQuery<FindCardsResultBean> query = getEM().createQuery(criteria);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return query.getResultList();
	}

	public Card getCard(int cardId)
	{
		return getEM().find(Card.class, cardId);
	}

	public CardInstance getCardInstance(int cardInstanceId)
	{
		return getEM().find(CardInstance.class, cardInstanceId);
	}

	public CardInstance getNextCardInstanceToReview(int deckId)
	{
		CriteriaBuilder builder = getEM().getCriteriaBuilder();
		CriteriaQuery<CardInstance> criteria = builder.createQuery(CardInstance.class);

		Root<CardInstance> root = criteria.from(CardInstance.class);
		Join<CardInstance, CardPlan> cardPlanJoin = root.join(CardInstance_.plan, JoinType.LEFT);
		Join<CardInstance, Card> cardJoin = root.join(CardInstance_.card, JoinType.LEFT);
		Join<Card, Deck> deckJoin = cardJoin.join(Card_.deck, JoinType.LEFT);

		Path<Integer> pDeckId = deckJoin.get(Deck_.id);
		Path<String> pSideA = cardJoin.get(Card_.sideA);
		Path<String> pSideB = cardJoin.get(Card_.sideB);
		Path<Boolean> pSideDisabled = root.get(CardInstance_.disabled);
		Path<OffsetDateTime> pNextDate = cardPlanJoin.get(CardPlan_.nextDate);

		criteria.select(root);

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

		criteria.orderBy(builder.asc(pNextDate));

		TypedQuery<CardInstance> query = getEM().createQuery(criteria);

		try
		{
			query.setMaxResults(1);
			CardInstance cardInstance = query.getSingleResult();

			return cardInstance;
		}
		catch (NoResultException e)
		{
			return null;
		}
	}
}
