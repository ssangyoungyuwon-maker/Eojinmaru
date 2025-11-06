package library;

public class LoginInfo {
private MainDTO loginUser = null;

public MainDTO loginUser() {
	return loginUser;
}
public void login(MainDTO loginUser) {
	this.loginUser = loginUser;
}
public void logout() {
	loginUser = null;
}

}