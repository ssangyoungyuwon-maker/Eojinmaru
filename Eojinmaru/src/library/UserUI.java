package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class UserUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	// private UserDAO dao = new UserDAOImpl();
	private LoginInfo login = null;
	
	public UserUI(LoginInfo login) {
		this.login = login;
	}
	
	public void menu() {
	
		int ch = 0;
		
		while(true) {
			System.out.println("\n[사용자 화면]");
			
			try {
				System.out.print("1.도서검색 2.대출신청/연장 3.반납신청 4.도서신청 5.마이페이지 6.로그아웃");
				ch = Integer.parseInt(br.readLine());
				
				if(ch == 6) {
					login.logout();
					System.out.println("로그아웃 되었습니다.");
					
					return;
				}
					switch(ch) {
					case 1 : findBybook(); break;
					case 2 : loan(); break;
					case 3 : returnbook(); break;
					case 4 : sincheong(); break;
					case 5 : mypage(); break;					
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
	}
	}
	// 1. 도서 검색
	protected void findBybook() {
		System.out.println("\n[도서검색]");

	}
	// 2. 대출신청/연장
	protected void loan () {
		System.out.println("\n[대출신청 / 연장]");
		
	}
	// 3. 반납신청
	protected void returnbook() {
		System.out.println("\n[반납신청]");
		
	}
	// 4. 도서신청
	protected void sincheong() {
		System.out.println("\n[도서신청]");
		
	}
	// 5. 마이페이지
	protected void mypage() {
		System.out.println("\n[마이페이지]");
		
	}
}
