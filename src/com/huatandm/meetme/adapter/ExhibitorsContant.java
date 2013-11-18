package com.huatandm.meetme.adapter;

public class ExhibitorsContant implements Comparable<ExhibitorsContant> {

	String name;
	String number;
	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public int compareTo(ExhibitorsContant another) {
		// TODO Auto-generated method stub
		int compareName = this.name.compareTo(another.getName());
		if (compareName == 0) {
			System.out.println("有相同的数据" + name + "," + another.getName());
		}
		return compareName;
	}
}
