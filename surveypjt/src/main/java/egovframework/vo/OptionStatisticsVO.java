package egovframework.vo;

public class OptionStatisticsVO {
	// 통계처리를 위한 옵션VO
	private String optionText; // 옵션 내용
    private int responseCount; // 응답 수
    private double percentage; // 응답 비율
	
    // 게터 세터
    public String getOptionText() {
		return optionText;
	}
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}
	public int getResponseCount() {
		return responseCount;
	}
	public void setResponseCount(int responseCount) {
		this.responseCount = responseCount;
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
    
}
