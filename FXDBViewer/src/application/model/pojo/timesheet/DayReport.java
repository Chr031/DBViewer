package application.model.pojo.timesheet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import application.model.descriptor.annotations.Calculated;
import application.model.descriptor.annotations.LinkN1;

public class DayReport {

	private Date day;
	
	@LinkN1
	private Set<TimeBooking> bookings;
	
	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Set<TimeBooking> getBookings() {
		return bookings;
	}

	public void setBookings(Set<TimeBooking> bookings) {
		this.bookings = bookings;
	}

	@Calculated
	public double totalTime() {
		double totalTime[] =new double[]{0};
		if (bookings != null) bookings.forEach((tb)->{totalTime[0]+=tb.timeSpentInHourFractions();});
		return totalTime[0];
	}
	
	@Calculated
	public double checkHours() {
		return  totalTime() - 8.4;
	}

	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YY");
		return  day == null ? null:sdf.format(day) + " | " + totalTime() + "h";
	}
	
	
	
}
