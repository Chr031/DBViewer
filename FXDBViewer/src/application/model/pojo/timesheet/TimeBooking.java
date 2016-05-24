package application.model.pojo.timesheet;

import application.model.descriptor.annotations.Calculated;
import application.model.descriptor.annotations.Link1N;
import application.model.descriptor.annotations.TextArea;

public class TimeBooking {

	@Link1N
	private Category category;

	private String startTime;
	private String endTime;

	private String breakTime;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getBreakTime() {
		return breakTime;
	}

	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
	}

	@TextArea
	private String comments;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	private double getFractionalHour(String time) {
		try {
			String[] hoursAndMinutes = time.split(":");
			double hoursFractions = Double.parseDouble(hoursAndMinutes[0]) + Double.parseDouble(hoursAndMinutes[1])/60;
			return hoursFractions;
		} catch (Throwable t) {
			return 0;
		}
	}

	@Calculated
	public double timeSpentInHourFractions() {

		double start = getFractionalHour(startTime);
		double end = getFractionalHour(endTime);
		if (start > end)
			start += 24;

		return end - start - getFractionalHour(breakTime);
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String toString() {
		return category + " : " + timeSpentInHourFractions() + "h";
	}

}
