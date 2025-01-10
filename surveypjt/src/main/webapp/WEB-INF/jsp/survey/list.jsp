<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>설문조사 목록</title>
<style>
body {
	font-family: Arial, sans-serif;
}

header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 10px;
	background-color: #f4f4f4;
}

header h1 {
	margin: 0;
}

header .auth-buttons button {
	margin-left: 10px;
	border: 1px solid #007BFF;
	padding: 5px 10px;
	background-color: transparent;
	border-radius: 5px;
	cursor: pointer;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

th, td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: center;
}

th {
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
</style>
</head>
<body>
	<c:if test="${not empty errorMessage}">
		<script>
            alert("${errorMessage}");
        </script>
	</c:if>

	<header>
		<h1>설문조사 목록</h1>
		<div class="auth-buttons">
			<c:choose>
				<c:when test="${empty loginMemberId}">
					<button onclick="location.href='<c:url value='/member/login' />'">로그인</button>
				</c:when>
				<c:otherwise>
					<span>${loginMemberId}님 안녕하세요.</span>
					<form action="<c:url value='/member/logout' />" method="post"
						style="display: inline;">
						<button type="submit">로그아웃</button>
					</form>
				</c:otherwise>
			</c:choose>
		</div>
	</header>

	<div class="survey-list-container">
		<form method="get" action="<c:url value='/' />">
			<input type="text" name="keyword" placeholder="제목을 입력하세요."
				value="${keyword}">
			<button type="submit">검색</button>
		</form>

		<table>
			<thead>
				<tr>
					<th>순번</th>
					<th>제목</th>
					<th>개요</th>
					<th>설문기간</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="survey" items="${surveyList}">
					<tr>
						<td>${survey.surveySerial}</td>
						<td><a
							href="<c:url value='/detail/${survey.surveySerial}' />">${survey.title}</a>
						</td>
						<td>${survey.summary}</td>
						<td>${survey.startDt}~ ${survey.endDt}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<div class="pagination">
			<button onclick="location.href='?page=1&keyword=${keyword}'">&laquo;</button>
			<c:if test="${currentPage > 1}">
				<button
					onclick="location.href='?page=${currentPage - 1}&keyword=${keyword}'">&lt;</button>
			</c:if>
			<c:forEach begin="1" end="${totalPages}" var="page">
				<button class="${currentPage == page ? 'active' : ''}"
					onclick="location.href='?page=${page}&keyword=${keyword}'">${page}</button>
			</c:forEach>
			<c:if test="${currentPage < totalPages}">
				<button
					onclick="location.href='?page=${currentPage + 1}&keyword=${keyword}'">&gt;</button>
			</c:if>
			<button
				onclick="location.href='?page=${totalPages}&keyword=${keyword}'">&raquo;</button>
		</div>
	</div>
</body>
</html>
