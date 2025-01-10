<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>관리자 페이지</title>
<style>
body {
	font-family: Arial, sans-serif;
}

.container {
	width: 90%;
	margin: 0 auto;
}

.top-bar {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 20px;
}

.logout-button {
	background-color: #007BFF;
	color: white;
	border: none;
	padding: 8px 12px;
	border-radius: 5px;
	cursor: pointer;
	font-size: 14px;
}

.logout-button:hover {
	background-color: #0056b3;
}

.survey-table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

.survey-table th, .survey-table td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: center;
}

.survey-table th {
	background-color: #f4f4f4;
}

.pagination {
	display: flex;
	justify-content: center;
	margin: 20px 0;
}

.pagination button {
	margin: 0 5px;
	padding: 8px 12px;
	border: 1px solid #007BFF;
	color: #007BFF;
	border-radius: 5px;
	background-color: transparent;
	cursor: pointer;
}

.pagination button.active {
	background-color: #007BFF;
	color: #fff;
}

.register-button {
	position: fixed;
	bottom: 20px;
	right: 20px;
	background-color: #007BFF;
	color: #fff;
	padding: 15px 20px;
	border: none;
	border-radius: 50px;
	cursor: pointer;
	box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
	font-size: 16px;
}

.register-button:hover {
	background-color: #0056b3;
}
</style>
</head>
<body>
	<div class="container">
		<!-- 상단바 -->
		<div class="top-bar">
			<h1>설문 관리</h1>
			<!-- 로그아웃 버튼 -->
			<form action="<c:url value='/member/logout' />" method="post">
				<button type="submit" class="logout-button">로그아웃</button>
			</form>
		</div>

		<div class="top-controls">
			<form method="get" action="<c:url value='/admin/list' />">
				<input type="text" name="searchKeyword" placeholder="제목을 입력하세요."
					value="${searchKeyword}">
				<button type="submit">검색</button>
			</form>
		</div>
		<table class="survey-table">
			<thead>
				<tr>
					<th>제목</th>
					<th>사용 여부</th>
					<th>응답 가능 여부</th>
					<th>작성자명</th>
					<th>수정</th>
					<th>통계조회</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="survey" items="${surveyList}">
					<tr>
						<td>${survey.title}</td>
						<td>${survey.usageYn}</td>
						<td><c:choose>
								<c:when
									test="${currentDate.time >= survey.startDt.time && currentDate.time <= survey.endDt.time}">
                                    Y
                                </c:when>
								<c:otherwise>
                                    N
                                </c:otherwise>
							</c:choose></td>
						<td>${survey.memberId}</td>
						<td>
							<button
								onclick="location.href='<c:url value='/admin/edit/${survey.surveySerial}'/>'">수정</button>
						</td>
						<td>
							<button
								onclick="location.href='<c:url value='/admin/statistics/${survey.surveySerial}'/>'">통계조회</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="pagination">
			<button
				onclick="location.href='?page=1&searchKeyword=${searchKeyword}'">&laquo;</button>
			<c:if test="${currentPage > 1}">
				<button
					onclick="location.href='?page=${currentPage - 1}&searchKeyword=${searchKeyword}'">&lt;</button>
			</c:if>
			<c:forEach begin="1" end="${totalPages}" var="page">
				<button class="${currentPage == page ? 'active' : ''}"
					onclick="location.href='?page=${page}&searchKeyword=${searchKeyword}'">${page}</button>
			</c:forEach>
			<c:if test="${currentPage < totalPages}">
				<button
					onclick="location.href='?page=${currentPage + 1}&searchKeyword=${searchKeyword}'">&gt;</button>
			</c:if>
			<button
				onclick="location.href='?page=${totalPages}&searchKeyword=${searchKeyword}'">&raquo;</button>
		</div>
	</div>

	<!-- 등록 버튼 -->
	<button class="register-button"
		onclick="location.href='<c:url value='/admin/create'/>'">등록</button>
	<script>
    function navigateToPage(page) {
        const searchType = '${searchType}';
        const keyword = '${keyword}';
        const url = `?page=${page}&searchType=${searchType}&keyword=${keyword}`;
        window.location.href = url;
    }
</script>
</body>
</html>
