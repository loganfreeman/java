package com.scheng.concurrency.termination;

/**
 * Created by scheng on 7/17/2015.
 */
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;



public class TestAlarmMgrShutdown {
	private AlarmMgr alarmMgr;

	@Before
	public void setUp() throws Exception {
		alarmMgr = AlarmMgr.getInstance();
		alarmMgr.init();

	}


	@Test
	public void testShutdown() {
		AbstractTerminatableThread producer = new AbstractTerminatableThread() {
			private int i = 0;

			@Override
			protected void doRun() throws Exception {
				alarmMgr.sendAlarm(AlarmType.FAULT, "001", "key1=value" + (i++));
				Thread.sleep(30);
			}

			@Override
			protected void doCleanup(Exception cause) {
				System.out.println("Alarms triggered:" + i + ",pending alarm:"
				    + alarmMgr.pendingAlarms());
				alarmMgr.shutdown();
			}

		};

		producer.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		producer.terminate();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		assertEquals(0, alarmMgr.pendingAlarms());
	}



}