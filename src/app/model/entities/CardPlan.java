package app.model.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CardsPlans")
@org.hibernate.annotations.DynamicUpdate(value = true)
public class CardPlan implements Serializable
{
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id = 0;

	@OneToOne(
			fetch = FetchType.LAZY)
	@JoinColumn(name = "cardInstanceId")
	private CardInstance instance = null;

	private OffsetDateTime nextDate = null;
	private int nextDays = 0;
	private OffsetDateTime lastDate = null;
	private int lastDays = 0;


	protected CardPlan()
	{
		//
	}

	public CardPlan(CardInstance instance)
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

	public OffsetDateTime getNextDate()
	{
		return nextDate;
	}

	public void setNextDate(OffsetDateTime nextDate)
	{
		this.nextDate = nextDate;
	}

	public int getNextDays()
	{
		return nextDays;
	}

	public void setNextDays(int nextDays)
	{
		this.nextDays = nextDays;
	}

	public OffsetDateTime getLastDate()
	{
		return lastDate;
	}

	public void setLastDate(OffsetDateTime lastDate)
	{
		this.lastDate = lastDate;
	}

	public int getLastDays()
	{
		return lastDays;
	}

	public void setLastDays(int lastDays)
	{
		this.lastDays = lastDays;
	}
}
