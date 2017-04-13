package app.beans;

public class DeckBean extends AbstractPageBean
{
	private static final long serialVersionUID = 1L;


	private int deckId = 0;
	private String description = "";
	private String notes = "";


	public DeckBean()
	{
		//
	}

	public DeckBean(
			int deckId,
			String description,
			String notes)
	{
		this.deckId = deckId;
		this.description = description;
		this.notes = notes;
	}


	public int getDeckId()
	{
		return deckId;
	}

	public void setDeckId(int deckId)
	{
		this.deckId = deckId;
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
}
