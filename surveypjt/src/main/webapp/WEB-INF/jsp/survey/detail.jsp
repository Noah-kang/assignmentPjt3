<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>설문 상세</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 0;
}

header, footer {
	background-color: #f4f4f4;
	padding: 10px 20px;
	text-align: center;
}

.survey-detail-container {
	width: 90%;
	margin: 20px auto;
}

.question {
	margin-bottom: 20px;
	padding: 10px;
	border: 1px solid #ddd;
	border-radius: 5px;
}

.question img {
	max-width: 100%;
	height: auto;
	margin-bottom: 10px;
	border-radius: 5px;
}

.options {
	margin-top: 10px;
}
</style>
</head>
<body>
	<header>
		<h1>설문 참여</h1>
	</header>
	<main class="survey-detail-container">
		<!-- 설문 기본 정보 -->
		<div>
			<h2>${survey.title}</h2>
			<p>${survey.summary}</p>
			<p>
				<strong>설문 기간:</strong> ${survey.startDt} ~ ${survey.endDt}
			</p>
		</div>
		<form method="post" action="<c:url value='/submit' />">
			<input type="hidden" name="surveySerial"
				value="${survey.surveySerial}"> <input type="hidden"
				name="surveyTitle" value="${survey.title}">

			<!-- 설문 질문 -->
			<c:forEach var="question" items="${questions}" varStatus="status">
				<div class="question">

					<!-- 질문 제목과 필수 여부 -->
					<h3>
						Q. ${question.questionText}
						<c:if test="${question.requiredYn}">
							<span class="required">*</span>
						</c:if>
					</h3>

					<!-- 질문 데이터 -->
					<input type="hidden" name="answers[${status.index}].questionSerial"
						value="${question.questionSerial}"> <input type="hidden"
						name="answers[${status.index}].questionText"
						value="${question.questionText}"> <input type="hidden"
						name="answers[${status.index}].questionType"
						value="${question.questionType}">
					<div>
						<!-- 이미지 표시 -->
						<c:if test="${not empty question.imageFilePath}">
							<img
								src="<c:url value='/questionImage?questionSerial=${question.questionSerial}' />"
								alt="이미지">
						</c:if>
					</div>
					<c:choose>
						<c:when test="${question.questionType == 'SHORT'}">
							<input type="text" name="answers[${status.index}].answerText"
								required="${question.requiredYn}">
						</c:when>
						<c:when test="${question.questionType == 'LONG'}">
							<textarea name="answers[${status.index}].answerText"
								required="${question.requiredYn}"></textarea>
						</c:when>
						<c:when test="${question.questionType == 'RADIO'}">
							<c:forEach var="option" items="${question.options}">
								<input type="radio" name="answers[${status.index}].answers"
									value="${option.optionSerial}">
								<input type="hidden" name="answers[${status.index}].optionTexts"
									value="${option.optionText}">
								<label>${option.optionText}</label>
							</c:forEach>
						</c:when>
						<c:when test="${question.questionType == 'CHECKBOX'}">
							<c:forEach var="option" items="${question.options}">
								<input type="checkbox" name="answers[${status.index}].answers"
									value="${option.optionSerial}">
								<input type="hidden" name="answers[${status.index}].optionTexts"
									value="${option.optionText}">
								<label>${option.optionText}</label>
							</c:forEach>
						</c:when>
						<c:when test="${question.questionType == 'SELECT'}">
							<select name="answers[${status.index}].answers">
								<c:forEach var="option" items="${question.options}">
									<option value="${option.optionSerial}">${option.optionText}</option>
								</c:forEach>
							</select>
							<input type="hidden" name="answers[${status.index}].optionTexts"
								value="${question.options[0].optionText}">
						</c:when>
					</c:choose>
				</div>
			</c:forEach>

			<!-- 버튼 영역 -->
			<div>
				<button onclick="location.href='<c:url value="/"/>'">목록</button>
				<button type="submit" onclick="submitSurvey()">설문 참여</button>
			</div>
		</form>
	</main>
	<script>
		
	</script>
	<footer> &copy; 설문조사 </footer>
</body>
</html>
