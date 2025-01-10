<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>설문조사 생성</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 20px;
}

.form-section {
	border: 1px solid #ddd;
	border-radius: 8px;
	padding: 16px;
	margin-bottom: 16px;
	background-color: #f9f9f9;
}

.form-section h3 {
	margin-top: 0;
}

.question-container {
	border: 1px solid #ccc;
	padding: 16px;
	border-radius: 8px;
	margin-bottom: 16px;
	background-color: #fff;
}

.options-container {
	margin-top: 8px;
	margin-left: 16px;
	display: none; /* 기본적으로 옵션 영역 숨기기 */
}

.question-actions, .option-actions {
	margin-top: 8px;
}
</style>
</head>
<body>
	<h1>설문조사 생성</h1>
	<form onsubmit="return validateForm()" method="post"
		action="<c:url value='/admin/create'/>" enctype="multipart/form-data">

		<!-- 설문 기본 정보 -->
		<div class="form-section">
			<label for="title">제목:</label> <input type="text" id="title"
				name="title" required><br> <label for="summary">개요:</label>
			<textarea id="summary" name="summary" required></textarea>
			<br> <label for="startDt">설문 시작일:</label> <input type="date"
				id="startDt" name="startDt" required> <label for="endDt">설문
				종료일:</label> <input type="date" id="endDt" name="endDt" required><br>

			<label for="usageYn">사용 여부:</label> <input type="radio"
				id="usageYnYes" name="usageYn" value="true" checked> <label
				for="usageYnYes">사용</label> <input type="radio" id="usageYnNo"
				name="usageYn" value="false"> <label for="usageYnNo">미사용</label>
		</div>

		<!-- 질문 추가 영역 -->
		<div class="form-section">
			<h3>질문 추가</h3>
			<div id="questions">
				<!-- 질문 추가 영역 -->
			</div>
			<button type="button" onclick="addQuestion()">+ 질문 추가</button>
		</div>

		<button type="submit">설문 생성</button>
		<button type="button"
			onclick="location.href='<c:url value="/admin/list"/>'">
			목록으로</button>
	</form>

	<script>
        let questionIndex = 0; // 질문 인덱스
        let optionIndices = {}; // 옵션 인덱스 관리 객체

        function addQuestion() {
            const questionsDiv = document.getElementById("questions");
            const questionHtml = `
                <div class="question-container" id="question-\${questionIndex}">
                    <label>질문 텍스트:</label>
                    <input type="text" name="questions[\${questionIndex}].questionText" required><br>

                    <label>질문 타입:</label>
                    <select name="questions[\${questionIndex}].questionType" onchange="toggleOptionVisibility(\${questionIndex}, this)">
                        <option value="SHORT">단답형</option>
                        <option value="LONG">장문형</option>
                        <option value="RADIO">라디오박스</option>
                        <option value="CHECKBOX">체크박스</option>
                        <option value="SELECT">셀렉트박스</option>
                    </select><br>

                    <label>필수 여부:</label>
                    <input type="checkbox" name="questions[\${questionIndex}].requiredYn" value="true"><br>

                    <label>이미지 업로드:</label>
                    <input type="file" name="questions[\${questionIndex}].imageFile"><br>

                    <!-- 옵션 추가 -->
                    <div class="options-container" id="options-\${questionIndex}">
                        <h4>옵션</h4>
                        <button type="button" id="addOptionBtn-\${questionIndex}" onclick="addOption(\${questionIndex})">+ 옵션 추가</button>
                    </div>

                    <div class="question-actions">
                        <button type="button" onclick="removeQuestion(\${questionIndex})">질문 삭제</button>
                    </div>
                </div>
            `;
            questionsDiv.insertAdjacentHTML("beforeend", questionHtml);

            // 옵션 인덱스 초기화
            optionIndices[questionIndex] = 0;

            // 질문 인덱스 증가
            questionIndex++;
        }

        function addOption(questionIndex) {
            const optionsDiv = document.getElementById(`options-\${questionIndex}`);
            const optionIndex = optionIndices[questionIndex];
            const optionHtml = `
                <div id="option-\${questionIndex}-\${optionIndex}">
                    <label>옵션 텍스트:</label>
                    <input type="text" name="questions[\${questionIndex}].options[\${optionIndex}].optionText" required>
                    <button type="button" onclick="removeOption(\${questionIndex}, \${optionIndex})">옵션 삭제</button>
                </div>
            `;
            optionsDiv.insertAdjacentHTML("beforeend", optionHtml);

            // 옵션 인덱스 증가
            optionIndices[questionIndex]++;
        }

        function removeQuestion(index) {
            const questionElement = document.getElementById(`question-\${index}`);
            questionElement.remove();
            delete optionIndices[index];
        }

        function removeOption(questionIndex, optionIndex) {
            const optionElement = document.getElementById(`option-\${questionIndex}-\${optionIndex}`);
            optionElement.remove();
        }

        function toggleOptionVisibility(questionIndex, selectElement) {
            const optionsContainer = document.getElementById(`options-\${questionIndex}`);
            const addOptionBtn = document.getElementById(`addOptionBtn-\${questionIndex}`);

            // 라디오박스, 체크박스, 드롭다운일 경우 옵션 영역 보이기
            if (selectElement.value === "RADIO" || selectElement.value === "CHECKBOX" || selectElement.value === "SELECT") {
                optionsContainer.style.display = "block";
                addOptionBtn.style.display = "inline-block";
            } else {
                optionsContainer.style.display = "none";
                addOptionBtn.style.display = "none";
            }
        }

        function validateForm() {
            const questions = document.querySelectorAll('.question-container');
            if (questions.length === 0) {
                alert("최소 하나 이상의 질문을 추가해야 합니다.");
                return false;
            }
            return true;
        }
    </script>
</body>
</html>
