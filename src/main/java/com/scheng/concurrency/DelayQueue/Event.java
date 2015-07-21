package com.scheng.concurrency.DelayQueue;

/**
 * Created by scheng on 7/21/2015.
 */
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


public class Event implements Delayed {

	private Date startDate;

	public Event(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public int compareTo(Delayed o) {
		long result = this.getDelay(TimeUnit.NANOSECONDS) -o.getDelay(TimeUnit.NANOSECONDS);
		if (result<0) {
			return -1;
		} else if (result>0) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public long getDelay(TimeUnit timeUnit) {
		Date now = new Date();
		long diff = startDate.getTime() - now.getTime();
		return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
	}

}