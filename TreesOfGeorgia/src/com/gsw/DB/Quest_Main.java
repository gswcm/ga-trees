package com.gsw.DB;

public class Quest_Main
{
	private int quest_id;
	private String qText;
	private int yesNav;
	private int noNav;
	private String yesText;
	private String noText;
	
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
	public int getNoNav() {
		return noNav;
	}
	public void setNoNav(int noNav) {
		this.noNav = noNav;
	}
	public void setYesText(String text){
		this.yesText = text;
	}
	public String getYesText(){
		return yesText;
	}
	public void setNoText(String text){
		this.noText = text;
	}
	public String getNoText(){
		return noText;
	}
}