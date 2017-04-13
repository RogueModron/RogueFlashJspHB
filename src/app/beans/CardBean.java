package app.beans;

public class CardBean extends AbstractPageBean
{
	private static final long serialVersionUID = 1L;


	private int deckId = 0;
	private int cardId = 0;
	private String sideA = "";
	private String sideB = "";
	private String notes = "";
	private String tags = "";
	private boolean sideBToA = false;


	public CardBean()
	{
		//
	}

	public CardBean(
			int deckId,
			int cardId,
			String sideA,
			String sideB,
			String notes,
			String tags,
			boolean sideBToA)
	{
		this.deckId = deckId;
		this.cardId = cardId;
		this.sideA = sideA;
		this.sideB = sideB;
		this.notes = notes;
		this.tags = tags;
		this.sideBToA = sideBToA;
	}


	public int getDeckId()
	{
		return deckId;
	}

	public void setDeckId(int deckId)
	{
		this.deckId = deckId;
	}

	public int getCardId()
	{
		return cardId;
	}

	public void setCardId(int cardId)
	{
		this.cardId = cardId;
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

	public boolean isSideBToA()
	{
		return sideBToA;
	}

	public void setSideBToA(boolean sideBToA)
	{
		this.sideBToA = sideBToA;
	}
}
