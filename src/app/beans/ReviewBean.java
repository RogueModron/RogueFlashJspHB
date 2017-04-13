package app.beans;

public class ReviewBean extends AbstractPageBean
{
	private static final long serialVersionUID = 1L;


	private int deckId = 0;


	public ReviewBean()
	{
		//
	}

	public ReviewBean(int deckId)
	{
		this.deckId = deckId;
	}


	public int getDeckId()
	{
		return deckId;
	}

	public void setDeckId(int deckId)
	{
		this.deckId = deckId;
	}
}
