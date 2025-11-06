package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MypageUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public void menu() {
		int ch;
		while (true) {
			try {
				System.out.println("1.내정보확인 2.내정보수정 3.탈퇴 4.로그아웃 5.홈으로");
				ch = Integer.parseInt(br.readLine());

				switch (ch) {
				case 1:
					chkmyinfo(); break;
				case 2:
					update(); break;
				case 3:
					delete(); break;
				case 4:
					logout(); break;
				// case 5 : UserUI.menu(); break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void chkmyinfo() {
		System.out.println("\n[내정보확인]");
		
	}

	public void update() {
		System.out.println("\n[내정보수정]");
		
	}

	public void delete() {
		System.out.println("\n[탈퇴]");
		
	}

	public void logout() {
		System.out.println("\n[로그아웃]");
		
	}
}
