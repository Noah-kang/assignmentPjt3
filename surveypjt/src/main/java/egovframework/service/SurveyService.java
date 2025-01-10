package egovframework.service;

import java.util.List;

import egovframework.vo.AnswerVO;
import egovframework.vo.MemberVO;
import egovframework.vo.QuestionStatisticsVO;
import egovframework.vo.QuestionVO;
import egovframework.vo.SurveyVO;

public interface SurveyService {
	// 설문조사 생성
	void createSurvey(SurveyVO surveyVO);
	// 설문조사 개수가져오기
	int getSurveyCount(String searchType, String searchKeyword);
	// 설문 목록 가져오기
    List<SurveyVO> getSurveyList(int page, int pageSize, String searchType, String searchKeyword);
    // 일반사용자용 설문 개수 가져오기 (사용여부 Y)
    int getActiveSurveyCount(String keyword);
    // 일반사용자용 설문 목록 가져오기 (사용여부 Y)
    List<SurveyVO> getActiveSurveys(int offset, int limit, String keyword);
    // 설문 가져오기
    SurveyVO getSurveyBySerial(int surveySerial);
    // 질문과 옵션을 가져옵니다.
    List<QuestionVO> getQuestionsWithOptions(int surveySerial);
    // 질문하나만 가져옵니다. 우선은 이미지 정보를 위해서
    QuestionVO getQuestionBySerial(int questionSerial);
    // 설문응답 저장
    void saveAnswers(AnswerVO answerVO);
    // 통계조회
    List<QuestionStatisticsVO> getSurveyStatistics(int surveySerial, String memberId);
    // 설문조사 제목 조회
    String getSurveyTitle(int surveySerial);
    // 설문 참여한 사용자들 조회
    List<MemberVO> getAllUsersForSurvey(int surveySerial);
    // 설문 참여자 존재 여부 조회
    boolean hasSurveyResponses(int surveySerial);
    // 설문 업데이트
    void updateSurvey(SurveyVO surveyVO);
    // 설문 삭제
    void deleteSurvey(int surveySerial);
    // 설문 참여자 존재시 제한된 업데이트
    void updateSurveyLimitedFields(SurveyVO surveyVO);
    // 사용자별 설문 참여여부 확인 쿼리
    boolean hasMemberParticipated(int surveySerial, int memberSerial);
    
}
