package egovframework.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import egovframework.service.SurveyService;
import egovframework.vo.MemberVO;
import egovframework.vo.QuestionStatisticsVO;
import egovframework.vo.QuestionVO;
import egovframework.vo.SurveyVO;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	private static final String UPLOAD_DIR = "C:/Users/winitech/Desktop/surveyfile/"; // 절대경로 파일저장

	@Autowired
	private SurveyService surveyService;

	// 설문조사폼 겟
	@GetMapping("/create")
	public String showCreateForm() {
		return "survey/create";
	}

	// 설문조사 등록
	@PostMapping("/create")
	public String createSurvey(SurveyVO surveyVO, HttpSession session, MultipartFile[] imageFiles, Model model)
			throws Exception {
		// 질문유무 확인
		if (surveyVO == null || surveyVO.getQuestions() == null) {
			model.addAttribute("error", "설문 또는 질문 데이터가 누락되었습니다.");
			return "survey/create";
		}

		// 설문조사는 로그인한(관리자만) 등록할수있음
		String memberId = (String) session.getAttribute("loginMemberId");
		surveyVO.setMemberId(memberId);
		surveyVO.setEditId(memberId); // 최초등록시 수정자도 본인임

		// 날짜 형식 처리
		if (surveyVO.getStartDt() == null || surveyVO.getEndDt() == null) {
			throw new IllegalArgumentException("시작일 및 종료일은 필수 입력 항목입니다.");
		}

		// 파일처리
		for (QuestionVO question : surveyVO.getQuestions()) {
			MultipartFile imageFile = question.getImageFile(); // Get the image file for the question
			if (imageFile != null && !imageFile.isEmpty()) {
				String originalFileName = imageFile.getOriginalFilename();
				String uuid = UUID.randomUUID().toString();
				String savedFileName = uuid + "_" + originalFileName;
				File saveFile = new File(UPLOAD_DIR, savedFileName);
				imageFile.transferTo(saveFile);

				// VO에 파일정보 넣기
				question.setImageFilePath(UPLOAD_DIR + savedFileName); // 상대 경로 저장
				question.setImageFileName(originalFileName);
				question.setImageFileUuid(uuid);
			}
		}

		surveyService.createSurvey(surveyVO);
		return "redirect:/admin/list";
	}

	// 관리자페이지 홈
	@GetMapping("/list")
	public String adminList(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "searchKeyword", required = false) String searchKeyword, Model model) {

		// 오늘 날짜를 java.util.Date로 가져오기
		Date currentDate = new Date();

		int pageSize = 10;
		int totalSurveys = surveyService.getSurveyCount("title", searchKeyword);
		List<SurveyVO> surveyList = surveyService.getSurveyList(page, pageSize, "title", searchKeyword);

		int totalPages = (int) Math.ceil((double) totalSurveys / pageSize);

		model.addAttribute("currentDate", currentDate);
		model.addAttribute("surveyList", surveyList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("searchKeyword", searchKeyword);
		return "survey/admin";
	}

	// 통계조회 겟
	@GetMapping("/statistics/{surveySerial}")
	public String getStatistics(@PathVariable int surveySerial, @RequestParam(required = false) String memberId,
			Model model) {
		// 전체 사용자 또는 특정 사용자 통계 조회
		List<QuestionStatisticsVO> statistics = surveyService.getSurveyStatistics(surveySerial, memberId);

		// 설문 제목 조회
		String surveyTitle = surveyService.getSurveyTitle(surveySerial);

		// 사용자 목록 조회 (예시)
		List<MemberVO> userList = surveyService.getAllUsersForSurvey(surveySerial);

		model.addAttribute("statistics", statistics);
		model.addAttribute("surveyTitle", surveyTitle);
		model.addAttribute("userList", userList);
		model.addAttribute("selectedMemberId", memberId); // 선택된 사용자
		model.addAttribute("surveySerial", surveySerial);

		return "survey/statistics";
	}

	// 설문 수정화면 겟
	@GetMapping("/edit/{surveySerial}")
	public String showEditForm(@PathVariable int surveySerial, Model model) {
		// 설문 기본 정보 가져오기
		SurveyVO survey = surveyService.getSurveyBySerial(surveySerial);
		if (survey == null) {
			model.addAttribute("error", "존재하지 않는 설문입니다.");
			return "error";
		}

		// 설문 참여 여부 확인
		boolean hasResponses = surveyService.hasSurveyResponses(surveySerial);
		survey.setHasResponses(hasResponses); // 참여 여부를 VO에 설정

		// 질문 및 옵션 가져오기
		List<QuestionVO> questions = surveyService.getQuestionsWithOptions(surveySerial);
		
		// 로깅 추가
	    logger.debug("Survey: {}", survey);
	    logger.debug("Questions with Options: {}", questions);

		// 데이터 모델에 추가
		model.addAttribute("survey", survey);
		model.addAttribute("questions", questions);

		return "survey/edit"; // JSP 파일명
	}

	// 설문 수정
	@PostMapping("/edit")
	public String updateSurvey(SurveyVO surveyVO, HttpSession session, HttpServletRequest request, Model model) throws Exception {
	    try {
	        // 로그인 사용자 ID 설정
	        String memberId = (String) session.getAttribute("loginMemberId");
	        surveyVO.setEditId(memberId);
	        
	        // 참여 여부 확인
	        boolean hasResponses = surveyService.hasSurveyResponses(surveyVO.getSurveySerial());

	        if (hasResponses) {
	            // 참여자가 있으면 제한된 필드만 업데이트
	            surveyService.updateSurveyLimitedFields(surveyVO);
	        } else {
	        // 질문 처리 및 파일 업로드
	        int index = 0; // 인덱스 변수 생성
	        for (QuestionVO question : surveyVO.getQuestions()) {
	            // 파일부터 처리 하고 
	        	handleFileUploadForEdit(question, request, index);
	        	index++;
	        }
	        // 설문 업데이트
	        surveyService.updateSurvey(surveyVO);
	        }

	        return "redirect:/admin/list";
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", "설문 수정 중 오류가 발생했습니다.");
	        return "survey/edit";
	    }
	}
	
	private void handleFileUploadForEdit(QuestionVO question, HttpServletRequest request, int index) throws IOException {
		
	    // 기존 파일 삭제 여부 확인
	    if ("true".equals(request.getParameter("questions[" + index + "].fileDeleted"))) {
	        // 기존 파일 삭제
	        if (question.getImageFilePath() != null) {
	            File oldFile = new File(question.getImageFilePath());
	            if (oldFile.exists()) {
	                oldFile.delete();
	            }
	        }
	        // 기존 파일 정보 제거
	        question.setImageFileName(null);
	        question.setImageFilePath(null);
	        question.setImageFileUuid(null);
	    } else if (question.getImageFilePath() != null) {
	        // 파일 삭제되지 않은 경우 기존 파일 정보 유지
	        return;
	    }

	    // 새 파일 업로드 처리
	    if (question.getImageFile() != null && !question.getImageFile().isEmpty()) {
	        String originalFileName = question.getImageFile().getOriginalFilename();
	        String uuid = UUID.randomUUID().toString();
	        String savedFileName = uuid + "_" + originalFileName;
	        File saveFile = new File(UPLOAD_DIR, savedFileName);
	        question.getImageFile().transferTo(saveFile);

	        question.setImageFilePath(saveFile.getAbsolutePath());
	        question.setImageFileName(originalFileName);
	        question.setImageFileUuid(uuid);
	    }
	}
	
	// 설문조사 삭제
	@PostMapping("/delete")
	public String deleteSurvey(@RequestParam("surveySerial") int surveySerial, HttpServletRequest request, Model model) throws Exception{
	    try {
	        // 설문조사 삭제 서비스 호출
	        surveyService.deleteSurvey(surveySerial);

	        // 성공적으로 삭제한 후 목록 페이지로 리다이렉트
	        return "redirect:/admin/list";
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", "설문 삭제 중 오류가 발생했습니다.");
	        return "survey/edit";
	    }
	}
}
