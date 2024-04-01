package com.n11.conference.scheduler.constant;

import java.time.LocalTime;

public class ConferenceSchedulerConstants {

	public static final int DURATION_BEFORE_LUNCH_IN_MINUTE = 180;
	public static final int DURATION_AFTER_LUNCH_IN_MINUTE  = 240;
	public static final int DURATION_LUNCH_IN_MINUTE = 60;
	public static final int DURATION_LIGHTNING_IN_MINUTE = 5;
	public static final int MINUTE_SIXTY = 60;
	
	public static final String EVENT_LUNCH = "Lunch";
	public static final String EVENT_NETWORKING = "Networking Event";
	public static final String GAP = "Gap";
	public static final String EVENT_LIGHTNING = "lightning";

	public static final LocalTime CONFERENCE_START_TIME_NINE_AM = LocalTime.of(9, 0);
	public static final LocalTime NETWORKING_START_TIME_FOUR_PM = LocalTime.of(16, 0);
	

	public static final String ABBR_MINUTE = "min";
	
}
