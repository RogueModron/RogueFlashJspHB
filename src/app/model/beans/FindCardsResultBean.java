package app.model.beans;

import java.io.Serializable;

public class FindCardsResultBean implements Serializable
{
	private static final long serialVersionUID = 1L;


	private int id = 0;
	private String sideA = "";
	private String sideB = "";
	private String notes = "";
	private String tags = "";
	private boolean instanceBToA = false;


	public FindCardsResultBean(
			int id,
			String sideA,
			String sideB,
			String notes,
			String tags,
			boolean instanceBToA)
	{
		this.id = id;
		this.sideA = sideA;
		this.sideB = sideB;
		this.notes = notes;
		this.tags = tags;
		this.instanceBToA = instanceBToA;
	}


	public int getId()
	{
		return id;
	}

	public String getSideA()
	{
		return sideA;
	}

	public String getSideB()
	{
		return sideB;
	}

	public String getNotes()
	{
		return notes;
	}

	public String getTags()
	{
		return tags;
	}

	public boolean isInstanceBToA()
	{
		return instanceBToA;
	}
}
