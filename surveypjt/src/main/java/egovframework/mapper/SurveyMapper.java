package egovframework.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import egovframework.vo.AnswerVO;
import egovframework.vo.MemberVO;
import egovframework.vo.QuestionOptionVO;
import egovframework.vo.QuestionVO;
import egovframework.vo.SurveyVO;

@Mapper
public interface SurveyMapper {
	void insertSurvey(SurveyVO surveyVO);
	void insertQuestion(QuestionVO questionVO);
	void insertQuestionOption(QuestionOptionVO optionVO);
	int selectNextSurveySerial();
	int selectNextQuestionSerial();
	int selectNextQuestionOptionSerial();
	int getSurveyCount(@Param("searchKeyword") String searchKeyword);
    List<SurveyVO> getSurveyList(@Param("offset") int offset, @Param("limit") int limit, 
                                  @Param("searchKeyword") String searchKeyword);
    int getActiveSurveyCount(@Param("keyword") String keyword);
    List<SurveyVO> getActiveSurveys(@Param("offset") int offset, @Param("limit") int limit, @Param("keyword") String keyword);
    SurveyVO getSurveyBySerial(int surveySerial);
    List<QuestionVO> getQuestionsBySurveySerial(int surveySerial);
    List<QuestionOptionVO> getOptionsByQuestionSerial(int questionSerial);
    QuestionVO getQuestionByQuestionSerial(int questionSerial);
    void insertAnswer(AnswerVO answer);
    List<Map<String, Object>> getStatisticsBySurveySerial(@Param("surveySerial") int surveySerial, @Param("memberId") String memberId);
    String getSurveyTitle(int surveySerial);
    List<String> getTextResponsesByQuestionSerial(@Param("surveySerial") int surveySerial, @Param("questionSerial") int questionSerial, @Param("memberId") String memberId);
    List<MemberVO> getAllUsersForSurvey(@Param("surveySerial") int surveySerial);
    int getResponseCountBySurveySerial(int surveySerial);
    void updateSurvey(SurveyVO surveyVO);
    void deleteQuestionsBySurveySerial(int surveySerial);
    void deleteOptionsBySurveySerial(int surveySerial);
    void deleteSurvey(int surveySerial);
    void updateSurveyLimitedFields(SurveyVO surveyVO);
    int getParticipationCount(@Param("surveySerial") int surveySerial, @Param("memberSerial") int memberSerial);

}
