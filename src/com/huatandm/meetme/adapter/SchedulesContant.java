package com.huatandm.meetme.adapter;

public class SchedulesContant implements Comparable<SchedulesContant> {

	String schedulestime, schedulestopic, schedulesroom, schedulescontent,
			schedulesdate, Schedulesshowtime, Schedulesplace;
	int schedulesid;
	Object schedulesspeakers;
	Object schedule_host;
	String map;
	Object schedule_subschedules;
	Boolean isShow = true;
	Boolean isShowTime = true;

	public Boolean getIsShowTime() {
		return isShowTime;
	}

	public void setIsShowTime(Boolean isShowTime) {
		this.isShowTime = isShowTime;
	}

	public Object getSchedule_host() {
		return schedule_host;
	}

	public void setSchedule_host(Object schedule_host) {
		this.schedule_host = schedule_host;
	}

	public Object getSchedule_subschedules() {
		return schedule_subschedules;
	}

	public void setSchedule_subschedules(Object schedule_subschedules) {
		this.schedule_subschedules = schedule_subschedules;
	}

	public String getSchedulesplace() {
		return Schedulesplace;
	}

	public void setSchedulesplace(String schedulesplace) {
		Schedulesplace = schedulesplace;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public Object getSchedulesspeakers() {
		return schedulesspeakers;
	}

	public void setSchedulesspeakers(Object schedulesspeakers) {
		this.schedulesspeakers = schedulesspeakers;
	}

	public int getSchedulesid() {
		return schedulesid;
	}

	public void setSchedulesid(int schedulesid) {
		this.schedulesid = schedulesid;
	}

	public String getSchedulestime() {
		return schedulestime;
	}

	public void setSchedulestime(String schedulestime) {
		this.schedulestime = schedulestime;
	}

	public String getSchedulestopic() {
		return schedulestopic;
	}

	public void setSchedulestopic(String schedulestopic) {
		this.schedulestopic = schedulestopic;
	}

	public String getSchedulesroom() {
		return schedulesroom;
	}

	public void setSchedulesroom(String schedulesroom) {
		this.schedulesroom = schedulesroom;
	}

	public String getSchedulescontent() {
		return schedulescontent;
	}

	public void setSchedulescontent(String schedulescontent) {
		this.schedulescontent = schedulescontent;
	}

	@Override
	public int compareTo(SchedulesContant another) {
		// TODO Auto-generated method stub
		int compareName = this.schedulestime.compareTo(another
				.getSchedulestime());
		if (compareName == 0) {
			System.out.println("有相同的数据" + schedulestime + ","
					+ another.getSchedulestime());
		}
		return compareName;
	}

	public String getSchedulesdate() {
		return schedulesdate;
	}

	public void setSchedulesdate(String schedulesdate) {
		this.schedulesdate = schedulesdate;
	}

	public String getSchedulesshowtime() {
		return Schedulesshowtime;
	}

	public void setSchedulesshowtime(String schedulesshowtime) {
		Schedulesshowtime = schedulesshowtime;
	}
}
