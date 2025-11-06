package library;

/**
 * 회원 정보 데이터를 담는 클래스 (Data Transfer Object)
 */
public class UserDTO {
    
	// 테이블 컬럼과 1:1로 매칭되는 필드
    private String user_Id;
	private String user_pwd;
    private String user_name;
    private String user_birth; // DTO에서는 우선 문자열(String)로 받습니다.
    private String user_tel;
    private String user_email;
    private String user_address;
 
    public String getUser_Id() {
		return user_Id;
	}
	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}
	public String getUser_pwd() {
		return user_pwd;
	}
	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_birth() {
		return user_birth;
	}
	public void setUser_birth(String user_birth) {
		this.user_birth = user_birth;
	}
	public String getUser_tel() {
		return user_tel;
	}
	public void setUser_tel(String user_tel) {
		this.user_tel = user_tel;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_address() {
		return user_address;
	}
	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}
	
    
    // (user_code는 시퀀스가 자동 생성, loan_renewaldate는 쿼리에서 SYSDATE로 처리할 예정)

    // Getters and Setters (데이터를 넣고 꺼내기 위한 메서드)
    
    
}