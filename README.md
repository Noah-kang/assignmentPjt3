# assignmentPjt3
설문조사만들기 3차 과제 [기능 오류 상당함]

- 설문 등록시 에러가 상당합니다.
  - 질문 옵션이 없는 질문의 경우 에러가 발생합니다. 그리고 트랜잭션 롤백도 되지않습니다.
  - 설문 참여도 이상하게 작동합니다. 객관식 질문의 경우 옵션 매핑이 제대로 되지 않습니다.
- 여전히 검색어유지, 드래그앤드랍, 트랜잭션 처리 되지않았습니다.
- 기타 세세한 오류, 유효성검사 등을 컨트롤 하지 못했습니다. 

- 개선사항
  - 버튼과 a태그 혼용을 개선했습니다. 버튼으로 통일
  - 파일을 프로젝트 외부에 저장하고 화면에는 컨트롤러로 IO사용해서 그려줍니다.
  - http리스너 사용으로 중복로그인시 기존의 세션을 해제하도록 했습니다. 
  - coalesce(max(), 0) 사용으로 직접 채번했습니다.
  