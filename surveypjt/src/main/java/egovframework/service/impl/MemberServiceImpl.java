package egovframework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.mapper.MemberMapper;
import egovframework.service.MemberService;
import egovframework.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private MemberMapper memberMapper;

    @Transactional
    @Override
    public void registerMember(MemberVO member) {
    	if(memberMapper.isMemberIdExists(member.getMemberId())) {
    		throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
    	}
    	//pk 가져오기
    	int nextVal = memberMapper.selectNextMemberSerial();
    	
    	// vo에 세팅
    	member.setMemberSerial(nextVal);
        // 평문 비번 그대로 저장
        memberMapper.insertMember(member);
    }
    
    @Override
    public boolean isMemberIdExists(String memberId) {
    	return memberMapper.isMemberIdExists(memberId);
    }
    
    @Override
    public MemberVO login(String memberId, String rawPassword) {
    	MemberVO member = memberMapper.getMemberByMemberId(memberId);
    	if(member != null) {
    		if(member.getPassword().equals(rawPassword)) {
    			return member;
    		}
    	}
    	return null; // 로그인 실패
    }
}
