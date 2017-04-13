package app.model.planner;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class PlannerResult implements Serializable
{
	private static final long serialVersionUID = 1L;


	private OffsetDateTime nextDate = null;
	private int daysNext = 0;

	private int passedDays = 0;


	public PlannerResult()
	{
		//
	}

	public PlannerResult(
			OffsetDateTime nextDate,
			int daysNext)
	{
		this.nextDate = nextDate;
		this.daysNext = daysNext;
	}


	public OffsetDateTime getNextDate()
	{
		return nextDate;
	}
	public void setNextDate(OffsetDateTime nextDate)
	{
		this.nextDate = nextDate;
	}
	public int getDaysNext()
	{
		return daysNext;
	}
	public void setDaysNext(int daysNext)
	{
		this.daysNext = daysNext;
	}

	public int getPassedDays()
	{
		return passedDays;
	}
	public void setPassedDays(int passedDays)
	{
		this.passedDays = passedDays;
	}
}
