package application.model.pojo.timesheet;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import application.model.descriptor.annotations.Calculated;
import application.model.descriptor.annotations.Link1N;
import application.model.descriptor.annotations.TextArea;

public class MonthReport {

	// @DateFormat
	private Date month;

	@Link1N
	private Person person;

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Calculated
	@TextArea
	public String activitySumUp() {
		Map<Category, Double> activityMap = new HashMap<>();
		if (person != null && month != null) {
			for (DayReport dr : person.getReports()) {
				if (dr.getDay().getMonth() == month.getMonth() && dr.getDay().getYear() == month.getYear()) {
					for (TimeBooking tb : dr.getBookings()) {
						if (activityMap.get(tb.getCategory()) == null) {
							activityMap.put(tb.getCategory(), tb.timeSpentInHourFractions());
						} else {
							activityMap.put(tb.getCategory(), activityMap.get(tb.getCategory()) + tb.timeSpentInHourFractions());
						}

					}
				}
			}
		}

		String sumUp = "";
		for (Category c : activityMap.keySet()) {
			sumUp += "\n" + c.getName() + " :" + activityMap.get(c);
		}

		return sumUp;

	}

	public String toString() {
		if (month == null ) return ""+person;
		return person + " " + (month.getMonth() + 1) + " " + (month.getYear() + 1900);
	}

}
