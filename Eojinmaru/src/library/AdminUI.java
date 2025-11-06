package library;

import java.util.Scanner;

/**
 * 도서관 키오스크 - 관리자 메뉴 화면
 * 관리자 로그인 성공 시 호출되는 클래스입니다.
 */

public class AdminUI {

    /**
     * [관리자 화면]을 표시하는 메서드
     * @param scanner : 메인 클래스에서 생성한 Scanner 객체를 전달받음
     */
    public void showMenu(Scanner scanner) {
        
        // [관리자 루프] isAdminRunning 플래그가 true 인 동안 관리자 메뉴 반복
        boolean isAdminRunning = true; 

        while (isAdminRunning) {
            // 관리자 메인 메뉴 출력
            System.out.println("\n===== [관리자 화면] =====");
            System.out.println("1. 회원관리");
            System.out.println("2. 도서관리");
            System.out.println("3. 대출/반납");
            System.out.println("4. 신청 도서");
            System.out.println("5. 공지사항 등록");
            System.out.println("6. 로그아웃"); // 메인 화면으로 돌아가기
            System.out.println("7. 프로그램 종료"); // 프로그램 종료하기 - only 관리자
            System.out.println("=========================");
            System.out.print("메뉴를 선택하세요: ");

            String adminChoice = scanner.nextLine();

            switch (adminChoice) {
                case "1":
                    // --- 1. 회원 관리 서브메뉴 ---
                    this.showMemberMenu(scanner); // 회원관리 메뉴 메서드 호출
                    break;

                case "2":
                    // --- 2. 도서 관리 서브메뉴 ---
                    this.showBookMenu(scanner); // 도서관리 메뉴 메서드 호출
                    break;

                case "3" :   
                	
                    System.out.println(">> (구현예정) 대출/반납 관리 페이지로 이동합니다."); 
                    break;
                case "4": 
                    System.out.println(">> (구현예정) 신청 도서 관리 페이지로 이동합니다."); 
                    break;
                case "5": 
                    System.out.println(">> (구현예정) 공지사항 등록 페이지로 이동합니다."); 
                    break;
                case "6":
                    isAdminRunning = false; // 관리자 루프 종료 -> 메인 화면으로 돌아감
                    break;
                case "7":
                	
                	System.out.println("❗ 시스템을 종료 하시겠습니까 ? [ Y / N ] ");                	
                	Scanner sc = new Scanner(System.in);
                	
                	while (true) {
                		String s = sc.nextLine();
	
                		if (s.equalsIgnoreCase("y")) {
                			System.out.println("🤖 시스템을 종료하겠습니다 ... ");
                			System.exit(0);                		
                		}  else if (s.equalsIgnoreCase("n")){ 
                			System.out.println("메인화면으로 돌아갑니다.");
                			break;
                		} else {  
                			System.out.println(" 🚨 [Y 또는 N 만 입력해주세요] -> ");
                		}
                		
                	} break;
                default:
                    System.out.println(">> 잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요.");
                    break;
            }
        }
        System.out.println(">> 로그아웃 되었습니다. [메인 화면]으로 돌아갑니다.");
    }

    /**
     * [1. 회원 관리] 서브 메뉴를 표시하는 private 메서드
     */
    private void showMemberMenu(Scanner scanner) {
        boolean isMemberMenuRunning = true;
        while (isMemberMenuRunning) {
            System.out.println("\n--- [1. 회원 관리] ---");
            System.out.println("1. 아이디 검색");
            System.out.println("2. 이름 검색");
            System.out.println("3. 연체 회원");
            System.out.println("4. 전체 리스트");
            System.out.println("5. 뒤로가기"); // [관리자 화면]으로
            System.out.println("--------------------");
            System.out.print("회원 관리 메뉴 선택: ");
            
            String memberChoice = scanner.nextLine();
            switch (memberChoice) {
                case "1": System.out.println(">> (구현예정) 아이디로 회원을 검색합니다."); break;
                case "2": System.out.println(">> (구현예정) 이름으로 회원을 검색합니다."); break;
                case "3": System.out.println(">> (구현예정) 연체 회원 목록을 조회합니다."); break;
                case "4": System.out.println(">> (구현예정) 전체 회원 목록을 조회합니다."); break;
                case "5": isMemberMenuRunning = false; break; // 회원관리 루프 종료
                default: System.out.println(">> 잘못된 입력입니다. 1~5 사이의 숫자를 입력해주세요."); break;
            }
        }
    }

    /**
     * [2. 도서 관리] 서브 메뉴를 표시하는 private 메서드
     */
    private void showBookMenu(Scanner scanner) {
        boolean isBookMenuRunning = true;
        while (isBookMenuRunning) {
            System.out.println("\n--- [2. 도서 관리] ---");
            System.out.println("1. 도서 전체 리스트");
            System.out.println("2. 도서 검색");
            System.out.println("3. 파손 도서 확인");
            System.out.println("4. 폐기 도서 등록");
            System.out.println("5. 신상 도서 등록");
            System.out.println("6. 뒤로가기"); // [관리자 화면]으로
            System.out.println("--------------------");
            System.out.print("도서 관리 메뉴 선택: ");

            String bookChoice = scanner.nextLine();
            switch (bookChoice) {
                case "1": System.out.println(">> (구현예정) 도서 전체 리스트를 조회합니다."); break;
                case "2": System.out.println(">> (구현예정) 도서를 검색합니다."); break;
                case "3": System.out.println(">> (구현예정) 파손 도서 목록을 확인합니다."); break;
                case "4": System.out.println(">> (구현예정) 폐기 도서를 등록합니다."); break;
                case "5": System.out.println(">> (구현예정) 신상 도서를 등록합니다."); break;
                case "6": isBookMenuRunning = false; break; // 도서관리 루프 종료
                default: System.out.println(">> 잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요."); break;
            }
        }
    }
}