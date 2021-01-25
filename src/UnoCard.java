public class UnoCard
{
	private UnoEnums.UnoCardValue cardValue;
	private UnoEnums.UnoCardColor cardColor;
	private int value;	
	
	public UnoCard(UnoEnums.UnoCardValue cardValue, UnoEnums.UnoCardColor cardColor, int value)
	{
		this.cardValue = cardValue;
		this.cardColor = cardColor;
		this.value = value;
	}

	public UnoEnums.UnoCardValue getType()
	{
		return cardValue;
	}
	
	public String getTypeBeautyfied()
	{
		switch(cardValue)
		{
			case Zero:		return "0";
			case One:		return "1";
			case Two:		return "2";
			case Three:		return "3";
			case Four:		return "4";	
			case Five:		return "5";	
			case Six:		return "6";
			case Seven:		return "7";
			case Eight:		return "8";
			case Nine:		return "9";
			case Reverse:	return "<-->";
			case Skip:		return "X";
			case Draw_Two:	return "+2";
			case Wild:		return "*";
			case Draw_Four:	return "+4";
			default:		return "";	
		}
	}

	public UnoEnums.UnoCardColor getColor()
	{
		return cardColor;
	}
	
	public String getColorAsHex()
	{
		switch(cardColor)
		{
			case Yellow:	return "#FFFF00";
			case Red:		return "#FF0000";
			case Blue:		return "#0000FF";
			case Green:		return "#00FF00";	
			case Black:		return "#000000";	
			default:		return "#000000";	
		}
	}

	public int getValue()
	{
		return value;
	}
	
	public boolean equals(UnoCard other)
	{
		return cardValue.equals(other.getType()) && cardColor.equals(other.getColor());
	}		

	@Override
	public String toString()
	{
		return "(" + cardValue + ", " + cardColor + ", value=" + value + ")\n";
	}	
}