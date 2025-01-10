<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>설문조사 수정</title>
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
	<h1>설문조사 수정</h1>
	<form onsubmit="return validateForm()" method="post"
		action="<c:url value='/admin/edit'/>" enctype="multipart/form-data">
		<!-- 설문 기본 정보 -->
		<div class="form-section">
		
        	<input type="hidden" name="surveySerial" value="${survey.surveySerial}">
			
			<label for="title">제목:</label> <input type="text" id="title"
				name="title" value="${survey.title}"
				${survey.hasResponses ? 'disabled' : ''}><br> <label
				for="summary">개요:</label>
			<textarea id="summary" name="summary"
				${survey.hasResponses ? 'disabled' : ''}>${survey.summary}</textarea>
			<br> <label for="startDt">설문 시작일:</label> <input type="date"
				id="startDt" name="startDt" value="${survey.startDt}" required><br>

			<label for="endDt">설문 종료일:</label> <input type="date" id="endDt"
				name="endDt" value="${survey.endDt}" required><br> <label
				for="usageYn">사용 여부:</label> <input type="radio" id="usageYnYes"
				name="usageYn" value="true" ${survey.usageYn ? 'checked' : ''}>
			<label for="usageYnYes">사용</label> <input type="radio" id="usageYnNo"
				name="usageYn" value="false" ${!survey.usageYn ? 'checked' : ''}>
			<label for="usageYnNo">미사용</label>
		</div>

		<!-- 질문 목록 -->
		<div class="form-section">
			<h3>질문 목록</h3>
			<div id="questions">
				<c:forEach var="question" items="${questions}" varStatus="status">
					<div class="question-container" id="question-${status.index}">
						<label>질문 텍스트:</label> <input type="text"
							name="questions[${status.index}].questionText"
							value="${question.questionText}"
							${survey.hasResponses ? 'disabled' : ''}><br> <label>질문
							타입:</label> <select name="questions[${status.index}].questionType"
							onchange="toggleOptionVisibility(${status.index}, this)"
							${survey.hasResponses ? 'disabled' : ''}>
							<option value="SHORT"
								${question.questionType == 'SHORT' ? 'selected' : ''}>단답형</option>
							<option value="LONG"
								${question.questionType == 'LONG' ? 'selected' : ''}>장문형</option>
							<option value="RADIO"
								${question.questionType == 'RADIO' ? 'selected' : ''}>라디오박스</option>
							<option value="CHECKBOX"
								${question.questionType == 'CHECKBOX' ? 'selected' : ''}>체크박스</option>
							<option value="SELECT"
								${question.questionType == 'SELECT' ? 'selected' : ''}>셀렉트박스</option>
						</select><br> <label>필수 여부:</label> <input type="checkbox"
							name="questions[${status.index}].requiredYn" value="true"
							${question.requiredYn ? 'checked' : ''}
							${survey.hasResponses ? 'disabled' : ''}><br> <label>이미지파일:</label>
						<c:if test="${not empty question.imageFileName}">
							<span>현재 파일: ${question.imageFileName}</span>
							<!-- 질문 고유 식별자 전달 -->
    						<input type="hidden" name="questions[${status.index}].questionSerial" value="${question.questionSerial}">
    						<input type="hidden" name="questions[${status.index}].index" value="${status.index}">
							<input type="checkbox" name="questions[${status.index}].fileDeleted" value="true"> 파일 삭제
							<br>
							<input type="hidden"
								name="questions[${status.index}].imageFileName"
								value="${question.imageFileName}">
							<input type="hidden"
								name="questions[${status.index}].imageFileUuid"
								value="${question.imageFileUuid}">
							<input type="hidden"
								name="questions[${status.index}].imageFilePath"
								value="${question.imageFilePath}">
						</c:if>
						<input type="file" name="questions[${status.index}].imageFile" ${survey.hasResponses ? 'disabled' : ''}><br>

						<!-- 옵션 목록 -->
						<c:if test="${not empty question.options}">
							<div class="options-container" id="options-${status.index}">
								<h4>옵션</h4>
								<c:forEach var="option" items="${question.options}"
									varStatus="optionStatus">
									<div id="option-${status.index}-${optionStatus.index}">
										<label>옵션 텍스트:</label> <input type="text"
											name="questions[${status.index}].options[${optionStatus.index}].optionText"
											value="${option.optionText}" ${survey.hasResponses ? 'disabled' : ''}>
										<button type="button"
											onclick="removeOption(${status.index}, ${optionStatus.index})" ${survey.hasResponses ? 'disabled' : ''}>옵션
											삭제</button>
									</div>
								</c:forEach>
							</div>
						<!-- Add Option button -->
						<div>
    					<button type="button" id="addOptionBtn-${status.index}" 
            					onclick="addOption(${status.index})" ${survey.hasResponses ? 'disabled' : ''}>+ 옵션 추가</button>
            					</div>
						</c:if>
						<div>
						
						<button type="button" onclick="removeQuestion(${status.index})" 
                        ${survey.hasResponses ? 'disabled' : ''}>질문 삭제</button>
                        </div>
						
					</div>
				</c:forEach>
			</div>
			<button type="button" onclick="addQuestion()"
				${survey.hasResponses ? 'disabled' : ''}>+ 질문 추가</button>
		</div>

		<button type="submit">수정</button>
		<button type="submit" formaction="<c:url value='/admin/delete'/>" 
        onclick="return confirm('정말로 삭제하시겠습니까?');">
    삭제
</button>
		<button type="button"
			onclick="location.href='<c:url value="/admin/list"/>'">목록으로</button>
	</form>

	<script>
	document.addEventListener('DOMContentLoaded', () => {
	    console.log('Initializing question containers...');
	    document.querySelectorAll('.question-container').forEach((container, index) => {
	        const selectElement = container.querySelector(`select[name^="questions[\${index}]"]`);
	        console.log(`Processing questionIndex: \${index}`);
	        console.log('Select element:', selectElement);

	        if (selectElement) {
	            toggleOptionVisibility(index, selectElement);
	        } else {
	            console.warn(`Select element not found for questionIndex: \${index}`);
	        }
	    });
	});
	
	
		let questionIndex = document.querySelectorAll('.question-container').length; // 초기 질문 개수로 questionIndex 동기화
        let optionIndices = {}; // 옵션 인덱스 관리 객체

     	// 기존 질문에 대해 초기 옵션 인덱스를 설정
        document.querySelectorAll('.question-container').forEach((container, index) => {
            const options = container.querySelectorAll('.options-container > div');
            optionIndices[index] = options.length; // 기존 옵션 개수
        });
        
     	// 옵션 컨테이너의 가시성 설정 함수
        function toggleOptionVisibility(questionIndex, selectElement) {
            const optionsContainer = document.getElementById(`options-\${questionIndex}`);
            const addOptionBtn = document.getElementById(`addOptionBtn-\${questionIndex}`);
            
         // 디버깅용 로그 추가
            console.log(`Checking visibility for questionIndex: \${questionIndex}`);
            console.log(`Options container id: options-\${questionIndex}`);
            console.log('Options container:', optionsContainer);
            console.log(`Add Option button id: addOptionBtn-\${questionIndex}`);
            console.log('Add Option button:', addOptionBtn);
            
            
         // 요소가 없는 경우 안전하게 종료
            if (!optionsContainer || !addOptionBtn) {
                console.warn(`Options container or Add Option button not found for questionIndex: \${questionIndex}`);
                return;
            }

            // 라디오박스, 체크박스, 드롭다운일 경우 옵션 영역 보이기
            if (selectElement.value === "RADIO" || selectElement.value === "CHECKBOX" || selectElement.value === "SELECT") {
                optionsContainer.style.display = "block";
                addOptionBtn.style.display = "inline-block";
            } else {
                optionsContainer.style.display = "none";
                addOptionBtn.style.display = "none";
            }
        }
     	
     	// 페이지 로드 시 모든 질문 초기화
        document.querySelectorAll('.question-container').forEach((container, index) => {
            const selectElement = container.querySelector(`select[name="questions[\${index}].questionType"]`);
            
         // selectElement가 없는 경우 로그 출력 후 무시
            if (!selectElement) {
                console.warn(`Question type select not found for question index: ${index}`);
                return;
            }
            
            toggleOptionVisibility(index, selectElement);
        });
        
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
            if (!optionsDiv) {
                console.error(`Options container not found for questionIndex: \${questionIndex}`);
                return;
            }

            // 새로운 옵션 인덱스 계산
            if (!optionIndices[questionIndex]) {
                optionIndices[questionIndex] = 0;
            }
            const optionIndex = optionIndices[questionIndex]++;

            // 새로운 옵션 HTML 생성
            const optionHtml = `
                <div id="option-\${questionIndex}-\${optionIndex}">
                    <label>옵션 텍스트:</label>
                    <input type="text" name="questions[\${questionIndex}].options[\${optionIndex}].optionText" required>
                    <button type="button" onclick="removeOption(\${questionIndex}, \${optionIndex})">옵션 삭제</button>
                </div>
            `;

            // 옵션 추가
            optionsDiv.insertAdjacentHTML('beforeend', optionHtml);
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
