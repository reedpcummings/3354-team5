package application;

public class Event{
	
	String eventName;
	String eventStart;
	String eventEnd;
	String label;
	

	public Event()
	{
		
	}
	
	
	public Event(String name, String start, String end)
	{
		eventName = name;
		eventStart = start;
		eventEnd = end;
	}
	
	public String getEventName() {
		return eventName;
	}


	public String getEventStart() {
		return eventStart;
	}


	public String getEventEnd() {
		return eventEnd;
	}


	public void setEventName(String eventName) {
		this.eventName = eventName;
	}


	public void setEventStart(String eventStart) {
		this.eventStart = eventStart;
	}


	public void setEventEnd(String eventEnd) {
		this.eventEnd = eventEnd;
	}
	
	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}

}
