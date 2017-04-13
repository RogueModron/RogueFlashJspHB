package app.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CardsInstances")
@org.hibernate.annotations.DynamicUpdate(value = true)
public class CardInstance implements Serializable
{
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id = 0;

	@ManyToOne
	@JoinColumn(name = "cardId")
	private Card card = null;

	@OneToOne(
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			orphanRemoval = true,
			mappedBy = "instance")
	private CardPlan plan = null;

	@OneToMany(
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			orphanRemoval = true,
			mappedBy = "instance")
	private List<CardReview> reviews = null;

	private boolean sideAToB = false;
	private boolean disabled = false;


	protected CardInstance()
	{
		//
	}

	public CardInstance(Card card)
	{
		this.card = card;

		this.plan = new CardPlan(this);
	}


	public int getId()
	{
		return id;
	}

	public Card getCard()
	{
		return card;
	}

	public CardPlan getPlan()
	{
		return plan;
	}

	public boolean isSideAToB()
	{
		return sideAToB;
	}

	public void setSideAToB(boolean sideAToB)
	{
		this.sideAToB = sideAToB;
	}

	public boolean isDisabled()
	{
		return disabled;
	}

	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}


	public CardReview addReview()
	{
		CardReview review = new CardReview(this);
		getOrInitializeReviews().add(review);
		return review;
	}

	public List<CardReview> getReviews()
	{
		return Collections.unmodifiableList(getOrInitializeReviews());
	}


	private List<CardReview> getOrInitializeReviews()
	{
		if (reviews == null)
		{
			reviews = new ArrayList<CardReview>();
		}
		return reviews;
	}
}
