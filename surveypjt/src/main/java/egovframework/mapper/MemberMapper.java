package egovframework.mapper;

import org.apache.ibatis.annotations.Mapper;

import egovframework.vo.MemberVO;

@Mapper
public interface MemberMapper {
	
	//serial 구하기
	int selectNextMemberSerial();
	
	//아이디 존재 여부
	boolean isMemberIdExists(String memberId);
	
	//회원 가입(INSERT)
	void insertMember(MemberVO member);
	
	//아이디로 회원 조회 
	MemberVO getMemberByMemberId(String memberId);
}
