package egovframework.vo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class QuestionVO {
	private int questionSerial;
	private int surveySerial;
	private int questionNo;
	private String questionText;
	private String questionType; // short, long, radio,checkbox, select
	private boolean requiredYn;
	private String imageFilePath;
	private String imageFileName;
	private String imageFileUuid;
	private Timestamp createdDt;
	private Timestamp updatedDt;
	
	private List<QuestionOptionVO> options; // 질문에 포함된 옵션 리스트

	private MultipartFile imageFile; // Spring에서 파일 업로드 처리
	
	public int getQuestionSerial() {
		return questionSerial;
	}

	public void setQuestionSerial(int questionSerial) {
		this.questionSerial = questionSerial;
	}

	public int getSurveySerial() {
		return surveySerial;
	}

	public void setSurveySerial(int surveySerial) {
		this.surveySerial = surveySerial;
	}

	public int getQuestionNo() {
		return questionNo;
	}

	public void setQuestionNo(int questionNo) {
		this.questionNo = questionNo;
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

	public boolean isRequiredYn() {
		return requiredYn;
	}

	public void setRequiredYn(boolean requiredYn) {
		this.requiredYn = requiredYn;
	}

	public String getImageFilePath() {
		return imageFilePath;
	}

	public void setImageFilePath(String imagePath) {
		this.imageFilePath = imagePath;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getImageFileUuid() {
		return imageFileUuid;
	}

	public void setImageFileUuid(String imageFileUuid) {
		this.imageFileUuid = imageFileUuid;
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

	public List<QuestionOptionVO> getOptions() {
		return options;
	}

	public void setOptions(List<QuestionOptionVO> options) {
		this.options = options;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
	
}
