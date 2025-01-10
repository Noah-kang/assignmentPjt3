package egovframework.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.service.SurveyService;
import egovframework.vo.AnswerVO;
import egovframework.vo.QuestionVO;
import egovframework.vo.SurveyVO;

@Controller
@RequestMapping("/")
public class SurveyController {

	private static final Logger logger = LoggerFactory.getLogger(SurveyController.class);
	
	@Autowired
	private SurveyService surveyService;

	// 일반사용자 설문 목록 조회 홈
	@RequestMapping("/")
	public String surveyList(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String keyword,
			HttpSession session, Model model) {
		int pageSize = 10;
		int totalSurveys = surveyService.getActiveSurveyCount(keyword);
		int totalPages = (int) Math.ceil((double) totalSurveys / pageSize);
		int offset = (page - 1) * pageSize;

		List<SurveyVO> surveyList = surveyService.getActiveSurveys(offset, pageSize, keyword);

		model.addAttribute("surveyList", surveyList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("keyword", keyword);
		model.addAttribute("loginMemberId", session.getAttribute("loginMemberId"));

		return "survey/list";
	}

	// 일반사용자 설문 get
	@GetMapping("/detail/{surveySerial}")
	public String getSurveyDetail(@PathVariable int surveySerial, HttpSession session,Model model, RedirectAttributes redirectAttributes) {
		// 설문 정보 가져오기
		SurveyVO survey = surveyService.getSurveyBySerial(surveySerial);
		if (survey == null) {
			model.addAttribute("error", "존재하지 않는 설문입니다.");
			return "error";
		}

		// 현재 날짜 가져오기
	    Date today = new Date();

	    // 설문 기간 확인
	    if (survey.getStartDt().after(today) || survey.getEndDt().before(today)) {
	        redirectAttributes.addFlashAttribute("errorMessage", "설문 가능 기간이 아닙니다.");
	        return "redirect:/";
	    }
		
	    // 이미 참여한 설문인지 확인
	    Integer memberSerial = (Integer) session.getAttribute("loginMemberSerial");
	    if (memberSerial != null && surveyService.hasMemberParticipated(surveySerial, memberSerial)) {
	        redirectAttributes.addFlashAttribute("errorMessage", "이미 참여한 설문입니다.");
	        return "redirect:/";
	    }
	    
		// 설문 질문 가져오기 - impl에서 옵션이 존재하면 질문안에 넣어서 옵니다.
		List<QuestionVO> questions = surveyService.getQuestionsWithOptions(surveySerial);

		// 데이터 모델에 추가
		model.addAttribute("survey", survey);
		model.addAttribute("questions", questions);

		return "survey/detail"; // JSP 파일명
	}

	// 이미지별 질문정보 조회해서 파일 그려주기
	@GetMapping("/questionImage")
	public void getQuestionImage(@RequestParam("questionSerial") int questionSerial, HttpServletResponse response) {
		try {
			// 질문 정보 조회
			QuestionVO question = surveyService.getQuestionBySerial(questionSerial);
			if (question == null || question.getImageFilePath() == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			// 실제 파일 경로
			String uploadPath = question.getImageFilePath();
			File file = new File(uploadPath);
			if (!file.exists()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			// MIME 타입 설정
			String ext = question.getImageFileName().substring(question.getImageFileName().lastIndexOf('.'))
					.toLowerCase();
			switch (ext) {
			case ".png":
				response.setContentType(MediaType.IMAGE_PNG_VALUE);
				break;
			case ".gif":
				response.setContentType(MediaType.IMAGE_GIF_VALUE);
				break;
			case ".jpg":
			case ".jpeg":
				response.setContentType(MediaType.IMAGE_JPEG_VALUE);
				break;
			default:
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				break;
			}

			// 파일 스트림 전송
			try (FileInputStream fis = new FileInputStream(file); OutputStream out = response.getOutputStream()) {

				byte[] buffer = new byte[2048];
				int bytesRead;
				while ((bytesRead = fis.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				out.flush();
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	// 설문 참여
	@PostMapping("/submit")
	public String submitSurvey(
	        @ModelAttribute AnswerVO answerVO,
	        HttpSession session,
	        RedirectAttributes redirectAttributes) {

	    try {
	    	String memberId = (String) session.getAttribute("loginMemberId");
	        Integer memberSerial = (Integer) session.getAttribute("loginMemberSerial");
	        if (memberSerial == null) {
	            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
	            return "redirect:/member/login";
	        }
	        
	        answerVO.setMemberId(memberId);
	        answerVO.setMemberSerial(memberSerial); // 세션에서 회원 정보 설정
	        surveyService.saveAnswers(answerVO); // VO를 그대로 전달
	        redirectAttributes.addFlashAttribute("successMessage", "설문이 성공적으로 저장되었습니다.");
	    } catch (Exception e) {
	        logger.error("설문 저장 중 오류 발생", e);
	        redirectAttributes.addFlashAttribute("errorMessage", "설문 저장 중 문제가 발생했습니다.");
	    }

	    return "redirect:/";
	}



}
