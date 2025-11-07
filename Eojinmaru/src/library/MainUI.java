package library;

import java.util.Scanner;

public class MainUI {

    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PW = "admin";
    
    private Scanner scanner = new Scanner(System.in);
    
    private AdminUI admin = new AdminUI();
    private MainDAO mainDAO = new MainDAOImpl();
    private LoginInfo login = new LoginInfo(); // <-- [수정] 로그인 정보(세션) 객체 추가

    public void menu() {
        while (true) {
            System.out.println("\n===== [메인 화면] =====");
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("3. 도서 검색");
            System.out.println("4. 도서 신청");
            System.out.println("5. 공지사항");
            System.out.println("=========================");
            System.out.print("메뉴를 선택하세요: ");

            String mainChoice = scanner.nextLine();

            switch (mainChoice) {
            	// 1. 로그인
                case "1": signin(); break;
                // 2. 회원가입
                case "2": signup(); break;
                // 3. 도서 검색
                case "3":
                    System.out.println(">> (구현예정) 도서 검색 페이지로 이동합니다.");
                    break;
                // 4. 도서 신청
                case "4":
                    System.out.println(">> (구현예정) 도서 신청 페이지로 이동합니다.");
                    break;
                // 5. 공지사항 조회
                case "5":
                    System.out.println(">> (구현예정) 공지사항 목록을 조회합니다.");
                    break;
                default:
                    System.out.println(">> 잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요.");
                    break;
            }
        }
        
    }
    
    private void signin() {
    	System.out.println("\n--- [로그인] ---");
        System.out.print("아이디: ");
        String inputId = scanner.nextLine();
        
        System.out.print("비밀번호: ");
        String inputPw = scanner.nextLine();

        // 1. 관리자 로그인 확인
        if (ADMIN_ID.equals(inputId) && ADMIN_PW.equals(inputPw)) {
            System.out.println(">> 관리자님, 환영합니다. [관리자 화면]으로 이동합니다.");
            admin.showMenu(scanner);
        }
        // 2. 관리자가 아니면, 사용자 로그인 시도
        else {
            // [수정] MainDAO를 통해 사용자 로그인 시도
            MainDTO user = mainDAO.login(inputId, inputPw);
            
            if(user != null) {
                // [수정] 로그인 성공 -> LoginInfo에 정보 저장
                login.login(user); 
                System.out.println(">> " + user.getUser_name() + "님, 환영합니다. [사용자 화면]으로 이동합니다.");
                
                // [수정] UserUI 생성자에 login 객체 전달
                UserUI userUI = new UserUI(login); 
                // [수정] UserUI.menu에 scanner 전달 (BufferedReader 충돌 방지)
                //userUI.menu(scanner); 
            } else {
                // 로그인 실패
                System.out.println(">> 아이디 또는 비밀번호가 일치하지 않습니다.");
            }
        }
    }
    
    private void signup() {
    	// (회원가입 로직은 동일)
        System.out.println("\n--- [회원가입] ---");
        MainDTO newUser = new MainDTO();
        System.out.print("아이디: ");
        newUser.setUser_Id(scanner.nextLine());
        System.out.print("비밀번호: ");
        newUser.setUser_pwd(scanner.nextLine());
        System.out.print("이름: ");
        newUser.setUser_name(scanner.nextLine());
        System.out.print("생년월일 (YYYY-MM-DD): ");
        newUser.setUser_birth(scanner.nextLine());
        System.out.print("전화번호 (010-XXXX-XXXX): ");
        newUser.setUser_tel(scanner.nextLine());
        System.out.print("이메일: ");
        newUser.setUser_email(scanner.nextLine());
        System.out.print("주소: ");
        newUser.setUser_address(scanner.nextLine());
        boolean isSuccess = mainDAO.signUpUser(newUser);
        if (isSuccess) {
            System.out.println(">> 회원가입이 성공적으로 완료되었습니다.");
        } else {
            System.out.println(">> 회원가입에 실패하였습니다.");
        }
    }
    
    
}