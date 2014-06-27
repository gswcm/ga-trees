package com.gsw.DB;

public class Quest_Main
{
	private int quest_id;
	private String qText;
	private int yesNav;
	private int noNav;
	
	public String getqText() {
		return qText;
	}
	public void setqText(String text) {
		this.qText = text;
	}
	public int getQuest_id() {
		return quest_id;
	}
	public void setQuest_id(int quest_id) {
		this.quest_id = quest_id;
	}
	
	public int getYesNav() {
		return yesNav;
	}
	public void setYesNav(int yesNav) {
		this.yesNav = yesNav;
	}
	public int getGroup_id() {
		return noNav;
	}
	public void setNoNav(int noNav) {
		this.noNav = noNav;
	}
}