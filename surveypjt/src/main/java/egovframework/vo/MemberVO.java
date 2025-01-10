package egovframework.vo;

import java.sql.Timestamp;

public class MemberVO {
	private int memberSerial;
	private String memberId;
	private String password;
	private String name;
	private String memberLevel; //DB에는 enum (admin or user) - 실제로 어플리케이션에서는 user로만 가입되게사용
	private Timestamp createdDt;
	private Timestamp updatedDt;
	
	// 게터 세터
	public int getMemberSerial() {
		return memberSerial;
	}
	public void setMemberSerial(int memberSerial) {
		this.memberSerial = memberSerial;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemberLevel() {
		return memberLevel;
	}
	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}
	public Timestamp  getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(Timestamp  createdDt) {
		this.createdDt = createdDt;
	}
	public Timestamp  getUpdatedDt() {
		return updatedDt;
	}
	public void setUpdatedDt(Timestamp  updatedDt) {
		this.updatedDt = updatedDt;
	}
	
	
}
