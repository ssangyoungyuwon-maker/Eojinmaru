package library;

import java.util.Scanner;

public class AdminUI2 {
	Scanner sc = new Scanner(System.in);

	public void showAdminmenu() {

		System.out.println("3. 대출 및 반납 관리");
		System.out.println("4. 신청 도서 관리");
		System.out.println("5. 공지사항 등록");
		System.out.println("6. 관리자 로그아웃");
		System.out.println("7. 시스템 종료");
		System.out.println("--------------------");
		System.out.print("회원 관리 메뉴 선택: ");

		String memberChoice = sc.nextLine();
		switch (memberChoice) {
		case "3":
			System.out.println(">> (구현예정) 연체 회원 목록을 조회합니다.");
			break;
		case "4":
			System.out.println(">> (구현예정) 전체 회원 목록을 조회합니다.");
			break;
		case "5":
			System.out.println(">> (구현예정) 전체 회원 목록을 조회합니다.");
			break;
		case "6":
			System.out.println(">> (구현예정) 전체 회원 목록을 조회합니다.");
			break;
		case "7":
			System.out.println("❗정말 시스템을 종료하시겠습니까 ? [Y/N]");
			
			while(true) {
			String shutdown = sc.next();
			sc.nextLine();
			
			if (shutdown.equalsIgnoreCase("y")) {
				System.out.println("시스템을 종료합니다. 🤖");				
				System.exit(0);
				
			} else if (shutdown.equalsIgnoreCase("n")) {
				System.out.println("시스템을 종료하지 않습니다.");
				System.out.println("이전으로 돌아갑니다.");
				
				sc.nextLine();
				
				break;
			} else {
				System.out.print(" 🚨 잘못된 입력입니다. Y 또는 N 을 입력하십시오. -> "); 
			}
			}
		default: System.out.println(">> 잘못된 입력입니다. 1~7 사이의 숫자를 입력해주세요."); break;
		}
	}
}
	