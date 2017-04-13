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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "Decks")
@org.hibernate.annotations.DynamicUpdate(value = true)
public class Deck implements Serializable
{
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id = 0;

	@Version
	private int version = 0;

	private String description = "";
	private String notes = "";

	@OneToMany(
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			orphanRemoval = true,
			mappedBy = "deck")
	List<Card> cards = null;


	protected Deck()
	{
		//
	}

	public Deck(String description)
	{
		this.description = description;
	}


	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getNotes()
	{
		return notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}


	public Card addCard()
	{
		Card card = new Card(this, "", "");
		getOrInitializeCards().add(card);
		return card;
	}

	public List<Card> getCards()
	{
		return Collections.unmodifiableList(getOrInitializeCards());
	}


	private List<Card> getOrInitializeCards()
	{
		if (cards == null)
		{
			cards = new ArrayList<Card>();
		}
		return cards;
	}
}
