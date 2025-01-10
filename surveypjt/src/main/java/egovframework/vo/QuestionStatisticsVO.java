package egovframework.vo;

import java.util.List;

public class QuestionStatisticsVO {
	// 통계조회를 위한 질문 VO
	private String questionText;
	private String questionType;
	private Integer questionSerial;
	private Integer totalResponses;
	private List<String> textAnswers; // 단답형/장문형 응답 리스트
	private List<OptionStatisticsVO> optionStatistics; // 객관식 응답 리스트
	
	// 게터 세터
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
	public Integer getQuestionSerial() {
		return questionSerial;
	}
	public void setQuestionSerial(Integer questionSerial) {
		this.questionSerial = questionSerial;
	}
	public Integer getTotalResponses() {
		return totalResponses;
	}
	public void setTotalResponses(Integer totalResponses) {
		this.totalResponses = totalResponses;
	}
	public List<String> getTextAnswers() {
		return textAnswers;
	}
	public void setTextAnswers(List<String> textAnswers) {
		this.textAnswers = textAnswers;
	}
	public List<OptionStatisticsVO> getOptionStatistics() {
		return optionStatistics;
	}
	public void setOptionStatistics(List<OptionStatisticsVO> optionStatistics) {
		this.optionStatistics = optionStatistics;
	}

}
