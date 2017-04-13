package app.model.planner;

public enum ReviewValues
{
	VALUE_0(  0.00, 0.00 ),
	VALUE_1(  1.00, 1.00 ),
	VALUE_2(  3.00, 1.10 ),
	VALUE_3(  7.00, 1.20 ),
	VALUE_4( 12.00, 1.30 );


	private double daysBase = 0d;
	private double daysMultiplier = 0d;


	private ReviewValues(
			double daysBase,
			double daysMultiplier)
	{
		this.daysBase = daysBase;
		this.daysMultiplier = daysMultiplier;
	}


	double getDaysBase()
	{
		return daysBase;
	}
	double getDaysMultiplier()
	{
		return daysMultiplier;
	}

	public static ReviewValues getFromOrdinal(int ordinal)
	{
		ReviewValues[] values = ReviewValues.values();
		if (0 <= ordinal && ordinal < values.length)
		{
			return values[ordinal];
		}
		return null;
	}
}
