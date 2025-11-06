package library;

public interface MainDAO {
	
	/**
	 * 회원가입 정보를 DB에 저장합니다.
	 * @param user : 회원가입 정보가 담긴 UserDTO 객체
	 * @return : 성공 시 true, 실패 시 false
	 */
	public boolean signUpUser(MainDTO user);
		
	/**
	 * 로그인 정보를 확인합니다.
	 * @param id : 사용자가 입력한 아이디
	 * @param pw : 사용자가 입력한 비밀번호
	 * @return : 로그인 성공 시 해당 유저의 MainDTO, 실패 시 null
	 */
	public MainDTO login(String id, String pw); 
}