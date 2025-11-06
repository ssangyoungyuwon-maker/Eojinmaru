package library;

import java.util.Scanner;

/**
 * 도서관 키오스크 - 메인 화면
 * 프로그램의 주 진입점(Entry Point)입니다.
 */
public class Main {

	// 관리자 계정 정보를 상수로 정의 (final static)
    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PW = "admin";
    
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        
        // AdminMenu 클래스의 인스턴스(객체)를 생성합니다.
        Admin admin = new Admin();

        // [메인 루프] isMainRunning 플래그가 true인 동안 메인 화면 반복
        boolean isMainRunning = true; 

        while (isMainRunning) {
            System.out.println("\n===== [메인 화면] =====");
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("3. 도서 검색");
            System.out.println("4. 도서 신청");
            System.out.println("5. 공지사항");
            System.out.println("6. 프로그램 종료");
            System.out.println("=========================");
            System.out.print("메뉴를 선택하세요: ");

            String mainChoice = scanner.nextLine();

            switch (mainChoice) {
                case "1":
                    System.out.println(">> 로그인 페이지로 이동합니다.");
                    
                    // --- 로그인 로직 시작 ---
                    System.out.println("\n--- [로그인] ---");
                    System.out.print("아이디: ");
                    String inputId = scanner.nextLine();
                    
                    System.out.print("비밀번호: ");
                    String inputPw = scanner.nextLine();

                    // (가정) 1. 관리자 로그인 확인
                    // 입력받은 ID와 PW가 모두 'admin'과 일치하는지 확인
                    if (ADMIN_ID.equals(inputId) && ADMIN_PW.equals(inputPw)) {
                        System.out.println(">> 관리자님, 환영합니다. [관리자 화면]으로 이동합니다.");
                        // 관리자 메뉴 호출
                        admin.showMenu(scanner); 
                    } 
                    // (가정) 2. 향후 사용자 로그인 확인 (추가 가능)
                    // else if ( memberDAO.checkLogin(inputId, inputPw) ) {
                    //    System.out.println(">> " + inputId + "님, 환영합니다. [사용자 화면]으로 이동합니다.");
                    //    userMenu.showMenu(scanner);
                    // }
                    else {
                        // 로그인 실패
                        System.out.println(">> 아이디 또는 비밀번호가 일치하지 않습니다.");
                        System.out.println(">> [메인 화면]으로 돌아갑니다.");
                    }
                    // --- 로그인 로직 종료 ---
                    break;
                case "2":
                    System.out.println(">> (구현예정) 회원가입 페이지로 이동합니다.");
                    // TODO: 회원가입 기능 구현
                    break;
                case "3":
                    System.out.println(">> (구현예정) 도서 검색 페이지로 이동합니다.");
                    // TODO: 도서 검색 기능 구현
                    break;
                case "4":
                    System.out.println(">> (구현예정) 도서 신청 페이지로 이동합니다.");
                    // TODO: 도서 신청 기능 구현
                    break;
                case "5":
                    System.out.println(">> (구현예정) 공지사항 목록을 조회합니다.");
                    // TODO: 공지사항 조회 기능 구현
                    break;
                case "6":
                    isMainRunning = false; // 메인 루프 종료 (프로그램 종료)
                    break;
                default:
                    System.out.println(">> 잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요.");
                    break;
            }
        }
        
        System.out.println("도서관 키오스크 프로그램을 종료합니다.");
        scanner.close(); // 프로그램이 완전히 종료될 때 Scanner를 닫습니다.
    }
}