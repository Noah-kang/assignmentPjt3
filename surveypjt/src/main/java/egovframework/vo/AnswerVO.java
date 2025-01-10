package egovframework.vo;

import java.sql.Timestamp;
import java.util.List;

public class AnswerVO {
	private int answerSerial;
    private int surveySerial;
    private int questionSerial;
    private int memberSerial;
    private Integer optionSerial; // 선택 옵션 (라디오, 체크박스 등)
    private String answerText;   // 자유형 텍스트 응답 (단답형, 장문형)
    private Timestamp createdDt;
    private String surveyTitle;   // 설문조사 제목
    private String questionText; // 질문 내용
    private String optionText;   // 옵션 내용
    private String memberId;
    private String questionType;
	
    private List<AnswerRequestVO> answers; // 응답 받을때 사용할 VO (다중 체크박스를 리스트에 담기위해서)
    
    // 게터세터
    public int getAnswerSerial() {
		return answerSerial;
	}
	public void setAnswerSerial(int answerSerial) {
		this.answerSerial = answerSerial;
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
	public int getMemberSerial() {
		return memberSerial;
	}
	public void setMemberSerial(int memberSerial) {
		this.memberSerial = memberSerial;
	}
	public Integer getOptionSerial() {
		return optionSerial;
	}
	public void setOptionSerial(Integer optionSerial) {
		this.optionSerial = optionSerial;
	}
	public String getAnswerText() {
		return answerText;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	public Timestamp getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(Timestamp createdDt) {
		this.createdDt = createdDt;
	}
	public List<AnswerRequestVO> getAnswers() {
		return answers;
	}
	public void setAnswers(List<AnswerRequestVO> answers) {
		this.answers = answers;
	}
	public String getSurveyTitle() {
		return surveyTitle;
	}
	public void setSurveyTitle(String surveyTitle) {
		this.surveyTitle = surveyTitle;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public String getOptionText() {
		return optionText;
	}
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
}
