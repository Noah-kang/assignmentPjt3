package egovframework.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.mapper.SurveyMapper;
import egovframework.service.SurveyService;
import egovframework.vo.AnswerRequestVO;
import egovframework.vo.AnswerVO;
import egovframework.vo.MemberVO;
import egovframework.vo.OptionStatisticsVO;
import egovframework.vo.QuestionOptionVO;
import egovframework.vo.QuestionStatisticsVO;
import egovframework.vo.QuestionVO;
import egovframework.vo.SurveyVO;

@Service
public class SurveyServiceImpl implements SurveyService {

	private static final Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

	@Autowired
	private SurveyMapper surveyMapper;

	// 설문조사 등록
	@Override
	@Transactional
	public void createSurvey(SurveyVO surveyVO) {
		// SURVEY_SERIAL 채번
		int surveySerial = surveyMapper.selectNextSurveySerial();
		surveyVO.setSurveySerial(surveySerial);
		// SURVEY 데이터 삽입
		surveyMapper.insertSurvey(surveyVO);

		// 질문 리스트 Null 확인
		if (surveyVO.getQuestions() == null || surveyVO.getQuestions().isEmpty()) {
			throw new IllegalArgumentException("질문 데이터가 존재하지 않습니다.");
		}

		int questionNo = 1; // QUESTION_NO는 1부터 시작
		// QUESTION 데이터 삽입
		for (QuestionVO question : surveyVO.getQuestions()) {
			// 질문 필드 확인
			if (question == null || question.getQuestionText() == null) {
				throw new IllegalArgumentException("유효하지 않은 질문 데이터가 포함되어 있습니다.");
			}

			// QUESTION_SERIAL 채번
			int questionSerial = surveyMapper.selectNextQuestionSerial();
			question.setQuestionSerial(questionSerial);
			question.setSurveySerial(surveySerial);

			// QUESTION_NO 설정
			question.setQuestionNo(questionNo++);

			// QUESTION 삽입
			surveyMapper.insertQuestion(question);

			int optionNo = 1; // OPTION_NO는 1부터 시작
			// 질문 옵션 삽입
			if (question.getOptions() != null) {
				for (QuestionOptionVO option : question.getOptions()) {

					// OPTION_SERIAL 채번
					int optionSerial = surveyMapper.selectNextQuestionOptionSerial();
					option.setOptionSerial(optionSerial);
					option.setQuestionSerial(questionSerial);
					option.setSurveySerial(surveySerial);

					// OPTION_NO 설정
					option.setOptionNo(optionNo++);

					// 옵션 삽입
					surveyMapper.insertQuestionOption(option);
				}
			}
		}
	}

	// 설문 개수 가져오기
	@Override
	public int getSurveyCount(String searchType, String searchKeyword) {
		return surveyMapper.getSurveyCount(searchKeyword);
	}

	// 설문 목록 가져오기
	@Override
	public List<SurveyVO> getSurveyList(int page, int pageSize, String searchType, String searchKeyword) {
		int offset = (page - 1) * pageSize;
		return surveyMapper.getSurveyList(offset, pageSize, searchKeyword);
	}

	// 설문 개수 가져오기(일반사용자 Y)
	@Override
	public int getActiveSurveyCount(String keyword) {
		return surveyMapper.getActiveSurveyCount(keyword);
	}

	// 설문 목록 가져오기(일반사용자 Y)
	@Override
	public List<SurveyVO> getActiveSurveys(int offset, int limit, String keyword) {
		return surveyMapper.getActiveSurveys(offset, limit, keyword);
	}

	// 설문 정보 가져오기
	@Override
	public SurveyVO getSurveyBySerial(int surveySerial) {
		return surveyMapper.getSurveyBySerial(surveySerial);
	}

	// 질문과 옵션 가져오기
	@Override
	public List<QuestionVO> getQuestionsWithOptions(int surveySerial) {
		List<QuestionVO> questions = surveyMapper.getQuestionsBySurveySerial(surveySerial);
		for (QuestionVO question : questions) {
			List<QuestionOptionVO> options = surveyMapper.getOptionsByQuestionSerial(question.getQuestionSerial());
			question.setOptions(options);
			
			// 로깅 추가
            logger.debug("Question: {}, Options: {}", question, options);
		}
		return questions;
	}

	// 질문하나만 가져오기 (이미지출력용)
	@Override
	public QuestionVO getQuestionBySerial(int questionSerial) {
		return surveyMapper.getQuestionByQuestionSerial(questionSerial);
	}

	// 설문 응답 저장
	@Override
	public void saveAnswers(AnswerVO answerVO) {
		Integer memberSerial = answerVO.getMemberSerial(); // MemberSerial 가져오기
		String memberId = answerVO.getMemberId();

		for (AnswerRequestVO answer : answerVO.getAnswers()) {
			switch (answer.getQuestionType()) {
			case "CHECKBOX":
				for (int i = 0; i < answer.getAnswers().size(); i++) {
					saveAnswer(memberSerial, memberId, answerVO.getSurveySerial(), answer.getQuestionSerial(),
							answer.getQuestionText(), answerVO.getSurveyTitle(), answer.getAnswers().get(i), // optionSerial
							null, answer.getOptionTexts().get(i), // 대응되는 optionText
							answer.getQuestionType());
				}
				break;

			case "RADIO":
			case "SELECT":
				saveAnswer(memberSerial, memberId, answerVO.getSurveySerial(), answer.getQuestionSerial(),
						answer.getQuestionText(), answerVO.getSurveyTitle(), answer.getAnswers().get(0), // optionSerial
						null, answer.getOptionTexts().get(0), // 선택된 optionText
						answer.getQuestionType());
				break;

			case "SHORT":
			case "LONG":
				saveAnswer(memberSerial, memberId, answerVO.getSurveySerial(), answer.getQuestionSerial(),
						answer.getQuestionText(), answerVO.getSurveyTitle(), null, answer.getAnswerText(), // Text 응답
						null, answer.getQuestionType());
				break;

			default:
				throw new IllegalArgumentException("지원하지 않는 질문 타입: " + answer.getQuestionType());
			}
		}
	}

	private void saveAnswer(Integer memberSerial, String memberId, int surveySerial, int questionSerial,
			String questionText, String surveyTitle, Integer optionSerial, String answerText, String optionText,
			String questionType) {
		AnswerVO newAnswer = new AnswerVO();
		newAnswer.setMemberSerial(memberSerial); // MemberSerial 설정
		newAnswer.setMemberId(memberId);
		newAnswer.setSurveySerial(surveySerial);
		newAnswer.setQuestionSerial(questionSerial);
		newAnswer.setQuestionText(questionText);
		newAnswer.setSurveyTitle(surveyTitle);
		newAnswer.setOptionSerial(optionSerial);
		newAnswer.setAnswerText(answerText);
		newAnswer.setOptionText(optionText);
		newAnswer.setQuestionType(questionType);

		logger.info("답변 저장: {}", newAnswer);
		surveyMapper.insertAnswer(newAnswer);
	}

	// 통계조회
	@Override
	public List<QuestionStatisticsVO> getSurveyStatistics(int surveySerial, String memberId) {
	    // 1. 통합 데이터를 가져오기
	    List<Map<String, Object>> rawData = surveyMapper.getStatisticsBySurveySerial(surveySerial, memberId);
	    logger.debug("Raw data from mapper: {}", rawData);

	    // 2. 데이터 가공 및 VO 생성
	    Map<Integer, QuestionStatisticsVO> questionStatisticsMap = new LinkedHashMap<>();
	    for (Map<String, Object> row : rawData) {
	        try {
	            Integer questionSerial = (Integer) row.get("questionserial");
	            String questionText = (String) row.get("questiontext");
	            String questionType = (String) row.get("questiontype");
	            String optionText = (String) row.get("optiontext");
	            Long responseCountRaw = (Long) row.get("responsecount");
	            Long totalResponsesRaw = (Long) row.get("totalresponses");

	            // Null 체크 및 기본값 설정
	            Integer responseCount = responseCountRaw != null ? responseCountRaw.intValue() : 0;
	            Integer totalResponses = totalResponsesRaw != null ? totalResponsesRaw.intValue() : 0;

	            logger.debug("Processing row: questionSerial={}, questionText={}, questionType={}, optionText={}, responseCount={}, totalResponses={}",
	                    questionSerial, questionText, questionType, optionText, responseCount, totalResponses);

	            // 질문 VO 생성 또는 가져오기
	            QuestionStatisticsVO questionStat = questionStatisticsMap.getOrDefault(questionSerial, new QuestionStatisticsVO());
	            questionStat.setQuestionText(questionText);
	            questionStat.setQuestionType(questionType);
	            questionStat.setTotalResponses(totalResponses);
	            questionStat.setQuestionSerial(questionSerial); // questionSerial 설정

	            // 객관식 옵션별 통계 추가
	            if (!"SHORT".equals(questionType) && !"LONG".equals(questionType)) {
	                OptionStatisticsVO optionStat = new OptionStatisticsVO();
	                optionStat.setOptionText(optionText != null ? optionText : ""); // Null 처리
	                optionStat.setResponseCount(responseCount);

	                // 퍼센티지 계산
	                if (totalResponses > 0) {
	                    optionStat.setPercentage((responseCount * 100.0) / totalResponses);
	                } else {
	                    optionStat.setPercentage(0.0);
	                }

	                if (questionStat.getOptionStatistics() == null) {
	                    questionStat.setOptionStatistics(new ArrayList<>());
	                }
	                questionStat.getOptionStatistics().add(optionStat);
	            }

	            questionStatisticsMap.put(questionSerial, questionStat);
	        } catch (Exception e) {
	            logger.error("Error processing row: {}", row, e);
	        }
	    }

	    // 3. 주관식 데이터 추가
	    for (Integer questionSerial : questionStatisticsMap.keySet()) {
	        try {
	            QuestionStatisticsVO questionStat = questionStatisticsMap.get(questionSerial);

	            // 단답형 및 장문형에만 응답 추가
	            if ("SHORT".equals(questionStat.getQuestionType()) || "LONG".equals(questionStat.getQuestionType())) {
	                List<String> textResponses = surveyMapper.getTextResponsesByQuestionSerial(surveySerial, questionSerial, memberId);
	                logger.debug("Text responses for questionSerial {}: {}", questionSerial, textResponses);
	                questionStat.setTextAnswers(textResponses);
	            }
	        } catch (Exception e) {
	            logger.error("Error processing text responses for questionSerial {}", questionSerial, e);
	        }
	    }

	    // 4. 결과 반환
	    logger.debug("Final question statistics: {}", questionStatisticsMap.values());
	    return new ArrayList<>(questionStatisticsMap.values());
	}

	// 설문제목을 가져오는 코드 - 하나만 가져오면되서 이게 더 적합합니다.
	@Override
	public String getSurveyTitle(int surveySerial) {
		return surveyMapper.getSurveyTitle(surveySerial);
	}

	// 설문 참여한 사용자들 가져오는 코드
	@Override
	public List<MemberVO> getAllUsersForSurvey(int surveySerial) {
	    return surveyMapper.getAllUsersForSurvey(surveySerial);
	}
	
	// 설문참여자 존재 여부 가져오기
	@Override
    public boolean hasSurveyResponses(int surveySerial) {
        return surveyMapper.getResponseCountBySurveySerial(surveySerial) > 0;
    }
	
	
	// 설문 업데이트
	@Override
	@Transactional
	public void updateSurvey(SurveyVO surveyVO) {
	    // SURVEY 업데이트
	    surveyMapper.updateSurvey(surveyVO);

	    // 기존 질문과 옵션 삭제
	    surveyMapper.deleteQuestionsBySurveySerial(surveyVO.getSurveySerial());
	    surveyMapper.deleteOptionsBySurveySerial(surveyVO.getSurveySerial());

	    // 질문과 옵션 저장 (등록과 유사)
	    if (surveyVO.getQuestions() == null || surveyVO.getQuestions().isEmpty()) {
	        throw new IllegalArgumentException("질문 데이터가 존재하지 않습니다.");
	    }

	    int questionNo = 1; // QUESTION_NO는 1부터 시작
	    for (QuestionVO question : surveyVO.getQuestions()) {
	        if (question == null || question.getQuestionText() == null) {
	            throw new IllegalArgumentException("유효하지 않은 질문 데이터가 포함되어 있습니다.");
	        }

	        // QUESTION_SERIAL 채번
	        int questionSerial = surveyMapper.selectNextQuestionSerial();
	        question.setQuestionSerial(questionSerial);
	        question.setSurveySerial(surveyVO.getSurveySerial());

	        // QUESTION_NO 설정
	        question.setQuestionNo(questionNo++);

	        // QUESTION 삽입
	        surveyMapper.insertQuestion(question);

	        int optionNo = 1; // OPTION_NO는 1부터 시작
	        if (question.getOptions() != null) {
	            for (QuestionOptionVO option : question.getOptions()) {
	                // OPTION_SERIAL 채번
	                int optionSerial = surveyMapper.selectNextQuestionOptionSerial();
	                option.setOptionSerial(optionSerial);
	                option.setQuestionSerial(questionSerial);
	                option.setSurveySerial(surveyVO.getSurveySerial());

	                // OPTION_NO 설정
	                option.setOptionNo(optionNo++);

	                // 옵션 삽입
	                surveyMapper.insertQuestionOption(option);
	            }
	        }
	    }
	}
	
	// 설문 업데이트 (참여자 존재 제한 업데이트)
	@Override
    @Transactional
    public void updateSurveyLimitedFields(SurveyVO surveyVO) {
        logger.info("참여자가 있는 설문조사의 제한된 필드만 업데이트: surveySerial={}", surveyVO.getSurveySerial());

        // 제한된 필드만 업데이트
        surveyMapper.updateSurveyLimitedFields(surveyVO);
    }
	
	// 설문 삭제
	@Override
    @Transactional
    public void deleteSurvey(int surveySerial) {
        // 질문 목록 가져오기
        List<QuestionVO> questions = surveyMapper.getQuestionsBySurveySerial(surveySerial);

        for (QuestionVO question : questions) {
            // 질문에 연결된 파일 삭제
            if (question.getImageFilePath() != null) {
                File file = new File(question.getImageFilePath());
                if (file.exists()) {
                    if (file.delete()) {
                        logger.info("파일 삭제 성공: {}", question.getImageFilePath());
                    } else {
                        logger.warn("파일 삭제 실패: {}", question.getImageFilePath());
                    }
                } else {
                    logger.warn("파일이 존재하지 않음: {}", question.getImageFilePath());
                }
            }

            // 질문 옵션 삭제
            surveyMapper.deleteOptionsBySurveySerial(surveySerial);
        }

        // 질문 삭제
        surveyMapper.deleteQuestionsBySurveySerial(surveySerial);

        // 설문 삭제
        surveyMapper.deleteSurvey(surveySerial);

        logger.info("설문조사 (surveySerial: {}) 삭제 성공", surveySerial);
    }
	
	// 사용자별 설문 유무 가져오기
	@Override
	public boolean hasMemberParticipated(int surveySerial, int memberSerial) {
	    return surveyMapper.getParticipationCount(surveySerial, memberSerial) > 0;
	}

	
}
