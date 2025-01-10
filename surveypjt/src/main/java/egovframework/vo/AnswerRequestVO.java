package egovframework.vo;

import java.util.List;

public class AnswerRequestVO {
	// 답변을 파람으로 받을때 체크박스가 리스트라서 사용할 VO입니다.
	private int questionSerial;
    private String questionText; // 질문 내용
    private String questionType;
    private List<Integer> answers; // 선택된 옵션 시리얼 (다중 응답)
    private List<String> optionTexts; // 선택된 옵션 텍스트 (다중 응답)
    private String answerText; // 주관식 답변
    private String surveyTitle; // 설문 제목
    // 게터 세터
	public int getQuestionSerial() {
		return questionSerial;
	}
	public void setQuestionSerial(int questionSerial) {
		this.questionSerial = questionSerial;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public List<Integer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Integer> answers) {
		this.answers = answers;
	}
	public List<String> getOptionTexts() {
		return optionTexts;
	}
	public void setOptionTexts(List<String> optionTexts) {
		this.optionTexts = optionTexts;
	}
	public String getAnswerText() {
		return answerText;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	public String getSurveyTitle() {
		return surveyTitle;
	}
	public void setSurveyTitle(String surveyTitle) {
		this.surveyTitle = surveyTitle;
	}
    
}
