<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>설문조사 통계</title>
<script src="<c:url value='/resources/js/chart.umd.min.js' />"></script>
<style>
    body {
        font-family: Arial, sans-serif;
    }
    .chart-container {
        width: 50%;
        margin: 0 auto;
        margin-bottom: 30px;
    }
</style>
</head>
<body>
	<div style="margin-bottom: 20px;">
		<h2>${surveyTitle} 통계</h2>
		<!-- 사용자 선택 -->
		<form method="get" action="">
			<label for="memberId">사용자 선택:</label> <select name="memberId"
				id="memberId" onchange="updateAction(this.value)">
				<option value="">전체</option>
				<c:forEach var="user" items="${userList}">
					<option value="${user.memberId}"
						${user.memberId == selectedMemberId ? 'selected' : ''}>
						${user.memberId}</option>
				</c:forEach>
			</select>
		</form>
	</div>

	<c:forEach var="question" items="${statistics}">
		<div style="border: 1px solid #ccc; margin-bottom: 20px; padding: 15px; border-radius: 8px;">
			<h3>${question.questionText}</h3>

			<!-- 객관식 -->
			<c:if test="${not empty question.optionStatistics}">
				<div class="chart-container">
					<canvas id="chart-${question.questionSerial}"></canvas>
				</div>
				<script>
					const ctx${question.questionSerial} = document.getElementById('chart-${question.questionSerial}').getContext('2d');
					new Chart(ctx${question.questionSerial}, {
						type: 'pie',
						data: {
							labels: [
								<c:forEach var="option" items="${question.optionStatistics}">
									"${option.optionText}"<c:if test="${!status.last}">,</c:if>
								</c:forEach>
							],
							datasets: [{
								data: [
									<c:forEach var="option" items="${question.optionStatistics}">
										${option.responseCount}<c:if test="${!status.last}">,</c:if>
									</c:forEach>
								],
								backgroundColor: [
									'#FF6384', '#36A2EB', '#FFCE56', '#4CAF50', '#FF9F40', '#9966FF'
								]
							}]
						},
						options: {
							responsive: true,
							plugins: {
								legend: {
									position: 'top'
								}
							}
						}
					});
				</script>
			</c:if>

			<!-- 주관식 -->
			<c:if test="${not empty question.textAnswers}">
				<h4>주관식 응답:</h4>
				<ul>
					<c:forEach var="answer" items="${question.textAnswers}">
						<li>${answer}</li>
					</c:forEach>
				</ul>
			</c:if>
		</div>
	</c:forEach>

	<!-- 목록으로 가는 버튼 -->
	<div style="text-align: center; margin-top: 20px;">
		<button onclick="location.href='<c:url value="/admin/list"/>'"
			style="background-color: #4caf50; color: white; border: none; padding: 10px 20px; font-size: 16px; border-radius: 5px; cursor: pointer;">
			목록으로 돌아가기</button>
	</div>

	<script>
	function updateAction(memberId) {
	    const form = document.querySelector('form');
	    if (memberId === "") {
	        // 전체를 선택하면 URL에 memberId 없이 전송
	        form.action = '<c:url value="/admin/statistics/${surveySerial}"/>';
	        form.memberId.disabled = true; // memberId 필드를 비활성화하여 전송되지 않게 함
	    } else {
	        // 특정 사용자를 선택한 경우
	        form.action = `<c:url value="/admin/statistics/${surveySerial}" />?memberId=${memberId}`;
	        form.memberId.disabled = false; // memberId 필드를 활성화
	    }
	    form.submit();
	}
	</script>
</body>
</html>
