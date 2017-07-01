package com.enterprises.wayne.iamfine.helper;

import com.enterprises.wayne.iamfine.common.model.TimeFormatter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Ahmed on 2/18/2017.
 */
public class TimeFormatterTest {

    private TimeFormatter timeFormmatter;

    @Before
    public void setup() {
        timeFormmatter = new TimeFormatter();
    }

    @Test
    public void testJustNow() {
        long time = System.currentTimeMillis() - 10 *1000;
        Assert.assertEquals("Just now", timeFormmatter.getDisplayTime(time));
    }

    @Test
    public void testAMinuteAGo() {
        long time = System.currentTimeMillis() - 1*60*1000 - 30;
        Assert.assertEquals("a minute ago", timeFormmatter.getDisplayTime(time));
    }


    @Test
    public void testMinutesAgo() {
        long time = System.currentTimeMillis() - 15*60*1000 - 30;
        Assert.assertEquals("15 minutes ago", timeFormmatter.getDisplayTime(time));
    }

    @Test
    public void testAnHourAgo() {
        long time = System.currentTimeMillis() - 60*60*1000 - 30;
        Assert.assertEquals("an hour ago", timeFormmatter.getDisplayTime(time));
    }

    @Test
    public void testHoursAgo() {
        long time = System.currentTimeMillis() - 15*60*60*1000 - 30;
        Assert.assertEquals("15 hours ago", timeFormmatter.getDisplayTime(time));
    }

    @Test
    public void testADayAgo() {
        long time = System.currentTimeMillis() - 24*60*60*1000 - 30;
        Assert.assertEquals("yesterday", timeFormmatter.getDisplayTime(time));
    }

    @Test
    public void testDaysAgo() {
        long time = System.currentTimeMillis() - 15*24*60*60*1000 - 30;
        Assert.assertEquals("15 days ago", timeFormmatter.getDisplayTime(time));
    }
}