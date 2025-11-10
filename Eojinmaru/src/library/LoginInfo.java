package library;

public class LoginInfo {
	private MemberDTO loginUser = null;

	public MemberDTO loginUser() {
		return loginUser;
	}

	public void login(MemberDTO loginUser) {
		this.loginUser = loginUser;
	}

	public void logout() {
		loginUser = null;
	}

}