package egovframework.vo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class SurveyVO {
	private int surveySerial;
	private String title;
	private String summary;
	private boolean usageYn;
	private Date startDt;
	private Date endDt;
	private String memberId;
	private String editId;
	private Timestamp createdDt;
	private Timestamp updatedDt;
	
	private List<QuestionVO> questions; // 설문에 포함된 질문 리스트
	private boolean hasResponses; // 응답자 존재여부 확인용

	public int getSurveySerial() {
		return surveySerial;
	}

	public void setSurveySerial(int surveySerial) {
		this.surveySerial = surveySerial;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public boolean isUsageYn() {
		return usageYn;
	}

	public void setUsageYn(boolean usageYn) {
		this.usageYn = usageYn;
	}

	public Date getStartDt() {
		return startDt;
	}

	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}

	public Date getEndDt() {
		return endDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
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

	public List<QuestionVO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionVO> questions) {
		this.questions = questions;
	}

	public boolean isHasResponses() {
		return hasResponses;
	}

	public void setHasResponses(boolean hasResponses) {
		this.hasResponses = hasResponses;
	} 
}
