package com.onthehouse.details;

/**
 * Created by Shannu on 22-Sep-17.
 */

public class Competition {
    private int event_id;
    private int member_id;
    private String competition_answer;

    public void setEvent_id(int event_id){this.event_id = event_id;}
    public int getEvent_id(){return this.event_id;}

    public void setMember_id(int member_id){this.member_id = member_id;}
    public int getMember_id(){return this.member_id;}

    public void setCompetition_answer(String competition_answer){this.competition_answer = competition_answer;}
    public String getCompetition_answer(){return this.competition_answer;}
}
