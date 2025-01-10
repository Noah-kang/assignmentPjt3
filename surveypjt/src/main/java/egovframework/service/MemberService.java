package egovframework.service;

import egovframework.vo.MemberVO;

public interface MemberService {
	// 회원가입
	void registerMember(MemberVO member);
	
	// 로그인
	MemberVO login(String memberId, String rawPassword);
	
	// 아이디 중복확인
	boolean isMemberIdExists(String memberId);
}
