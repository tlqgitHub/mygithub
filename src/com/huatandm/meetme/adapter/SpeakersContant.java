package com.huatandm.meetme.adapter;

public class SpeakersContant implements Comparable<SpeakersContant> {

	String speaker_image, speaker_name, speaker_complay, speaker_title,
			speaker_introduction;
	String speaker_username;
	Object speaker_schedule;
	String totalName;

	String speaker_compare;// 排序字段

	public String getSpeaker_compare() {
		return speaker_compare;
	}

	public void setSpeaker_compare(String speaker_compare) {
		this.speaker_compare = speaker_compare;
	}

	public String getTotalName() {
		return totalName;
	}

	public void setTotalName(String totalName) {
		this.totalName = totalName;
	}

	public String getSpeaker_username() {
		return speaker_username;
	}

	public void setSpeaker_username(String speaker_username) {
		this.speaker_username = speaker_username;
	}

	int speakerid;

	public int getSpeakerid() {
		return speakerid;
	}

	public void setSpeakerid(int speakerid) {
		this.speakerid = speakerid;
	}

	public String getSpeaker_image() {
		return speaker_image;
	}

	public void setSpeaker_image(String speaker_image) {
		this.speaker_image = speaker_image;
	}

	public String getSpeaker_name() {
		return speaker_name;
	}

	public void setSpeaker_name(String speaker_name) {
		this.speaker_name = speaker_name;
	}

	public String getSpeaker_complay() {
		return speaker_complay;
	}

	public void setSpeaker_complay(String speaker_complay) {
		this.speaker_complay = speaker_complay;
	}

	public String getSpeaker_title() {
		return speaker_title;
	}

	public void setSpeaker_title(String speaker_title) {
		this.speaker_title = speaker_title;
	}

	public Object getSpeaker_schedule() {
		return speaker_schedule;
	}

	public void setSpeaker_schedule(Object speaker_schedule) {
		this.speaker_schedule = speaker_schedule;
	}

	public String getSpeaker_introduction() {
		return speaker_introduction;
	}

	public void setSpeaker_introduction(String speaker_introduction) {
		this.speaker_introduction = speaker_introduction;
	}

	@Override
	public int compareTo(SpeakersContant another) {
		// TODO Auto-generated method stub
		int compareName = this.speaker_compare.compareTo(another
				.getSpeaker_compare());
		if (compareName == 0) {
			System.out.println("有相同的数据" + speaker_compare + ","
					+ another.getSpeaker_compare());
		}
		return compareName;
	}

}
