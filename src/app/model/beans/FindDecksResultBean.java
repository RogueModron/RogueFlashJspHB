package app.model.beans;

import java.io.Serializable;

public class FindDecksResultBean implements Serializable
{
	private static final long serialVersionUID = 1L;


	private int id = 0;
	private String description = "";
	private String notes = "";
	private long numberOfSides = 0;


	public FindDecksResultBean(
			int id,
			String description,
			String notes,
			long numberOfSides)
	{
		this.id = id;
		this.description = description;
		this.notes = notes;
		this.numberOfSides = numberOfSides;
	}


	public int getId()
	{
		return id;
	}

	public String getDescription()
	{
		return description;
	}

	public String getNotes()
	{
		return notes;
	}

	public long getNumberOfSides()
	{
		return numberOfSides;
	}
}
