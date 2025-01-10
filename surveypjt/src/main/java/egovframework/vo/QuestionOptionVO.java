package egovframework.vo;

import java.sql.Timestamp;

public class QuestionOptionVO {
	private int optionSerial;
	private int surveySerial;
	private int questionSerial;
	private int optionNo;
	private String optionText;
	private Timestamp createdDt;
	private Timestamp updatedDt;
	
	public int getOptionSerial() {
		return optionSerial;
	}
	public void setOptionSerial(int optionSerial) {
		this.optionSerial = optionSerial;
	}
	public int getSurveySerial() {
		return surveySerial;
	}
	public void setSurveySerial(int surveySerial) {
		this.surveySerial = surveySerial;
	}
	public int getQuestionSerial() {
		return questionSerial;
	}
	public void setQuestionSerial(int questionSerial) {
		this.questionSerial = questionSerial;
	}
	public int getOptionNo() {
		return optionNo;
	}
	public void setOptionNo(int optionNo) {
		this.optionNo = optionNo;
	}
	public String getOptionText() {
		return optionText;
	}
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}
	public Timestamp getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(Timestamp createdDt) {
		this.createdDt = createdDt;
	}
	public Timestamp getUpdatedDt() {
		return updatedDt;
	}
	public void setUpdatedDt(Timestamp updatedDt) {
		this.updatedDt = updatedDt;
	}

}
