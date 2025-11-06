package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import DBUtil.DBConn;

public class UserUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	// private UserDAO dao = new UserDAOImpl();
	
	public void menu() {
		int ch = 0;
		
		while(true) {
			System.out.println("\n[사용자 화면]");
			try {
				System.out.print("1.도서검색 2.대출신청/연장 3.반납신청 4.도서신청 5.마이페이지 6.로그아웃");
				ch = Integer.parseInt(br.readLine());
				
				if(ch == 6) {
					DBConn.close();
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
		
	}
	// 2. 대출신청/연장
	protected void loan () {
		
	}
	// 3. 반납신청
	protected void returnbook() {
		
	}
	// 4. 도서신청
	protected void sincheong() {
		
	}
	// 5. 마이페이지
	protected void mypage() {
		
	}
}
