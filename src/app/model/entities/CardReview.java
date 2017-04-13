package app.model.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import app.model.planner.ReviewValues;

@Entity
@Table(name = "CardsReviews")
@org.hibernate.annotations.DynamicUpdate(value = true)
public class CardReview implements Serializable
{
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id = 0;

	@ManyToOne(
			fetch = FetchType.LAZY)
	@JoinColumn(name = "cardInstanceId")
	private CardInstance instance = null;

	@Enumerated
	private ReviewValues value = ReviewValues.VALUE_0;

	private OffsetDateTime dateTime = null;


	protected CardReview()
	{
		//
	}

	public CardReview(CardInstance instance)
	{
		this.instance = instance;
	}


	public int getId()
	{
		return id;
	}

	public CardInstance getInstance()
	{
		return instance;
	}

	public ReviewValues getValue()
	{
		return value;
	}

	public void setValue(ReviewValues value)
	{
		this.value = value;
	}

	public OffsetDateTime getDateTime()
	{
		return dateTime;
	}

	public void setDateTime(OffsetDateTime dateTime)
	{
		this.dateTime = dateTime;
	}
}
