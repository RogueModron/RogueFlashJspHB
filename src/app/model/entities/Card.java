package app.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "Cards")
@org.hibernate.annotations.DynamicUpdate(value = true)
public class Card implements Serializable
{
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id = 0;

	@Version
	private int version = 0;

	@ManyToOne
	@JoinColumn(name = "deckId")
	private Deck deck = null;

	private String sideA = "";
	private String sideB = "";
	private String notes = "";
	private String tags = "";

	@OneToMany(
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER,
			orphanRemoval = true,
			mappedBy = "card")
	List<CardInstance> instances = null;


	protected Card()
	{
		//
	}

	public Card(
			Deck deck,
			String sideA,
			String sideB)
	{
		this.deck = deck;

		this.sideA = sideA;
		this.sideB = sideB;

		instances = new ArrayList<CardInstance>();
		CardInstance instanceAToB = new CardInstance(this);
		instanceAToB.setSideAToB(true);
		instances.add(instanceAToB);
		CardInstance instanceBToA = new CardInstance(this);
		instanceBToA.setSideAToB(false);
		instances.add(instanceBToA);
	}


	public int getId()
	{
		return id;
	}

	public Deck getDeck()
	{
		return deck;
	}

	public String getSideA()
	{
		return sideA;
	}

	public void setSideA(String sideA)
	{
		this.sideA = sideA;
	}

	public String getSideB()
	{
		return sideB;
	}

	public void setSideB(String sideB)
	{
		this.sideB = sideB;
	}

	public String getNotes()
	{
		return notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	public String getTags()
	{
		return tags;
	}

	public void setTags(String tags)
	{
		this.tags = tags;
	}


	public CardInstance getInstanceSideAToB()
	{
		return findInstance(true);
	}

	public CardInstance getIntanceSideBToA()
	{
		return findInstance(false);
	}


	private CardInstance findInstance(boolean sideAToB)
	{
		for (CardInstance instance : instances)
		{
			if (instance.isSideAToB() == sideAToB)
			{
				return instance;
			}
		}
		return null;
	}
}
