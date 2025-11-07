package library;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 도서관 키오스크 - 관리자 메뉴 화면
 * 관리자 로그인 성공 시 호출되는 클래스입니다.
 */

public class AdminUI {
	
	private AdminDAO adminDAO = new AdminDAOImpl();
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
                    System.out.println(">> 잘못된 입력입니다. 1~7 사이의 숫자를 입력해주세요.");
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
            System.out.println("\n--- [회원 관리] ---");
            System.out.println("1. 아이디 검색");
            System.out.println("2. 이름 검색");
            System.out.println("3. 회원 삭제");
            System.out.println("4. 연체 회원");
            System.out.println("5. 전체 리스트");
            System.out.println("6. 뒤로가기"); // [관리자 화면]으로
            System.out.println("--------------------");
            System.out.print("회원 관리 메뉴 선택: ");
            
            String memberChoice = scanner.nextLine();
            switch (memberChoice) {
            
            // 아이디 검색
            case "1":
                System.out.print(">> 검색할 회원의 아이디를 입력하세요: ");
                String id = scanner.nextLine();
                MainDTO user = adminDAO.findUserById(id);
                if (user != null) {
                    System.out.println("--- 검색 결과 (1건) ---");
                    // 1명의 정보만 리스트에 담아서 헬퍼 메서드로 전달
                    List<MainDTO> resultList = new ArrayList<>();
                    resultList.add(user);
                    printUserList(resultList);
                } else {
                    System.out.println(">> 해당 아이디의 회원을 찾을 수 없습니다.");
                }
                break;
                
            // 이름 검색
            case "2":
                System.out.print(">> 검색할 회원의 이름(전체 또는 일부)을 입력하세요: ");
                String name = scanner.nextLine();
                List<MainDTO> nameList = adminDAO.findUserByName(name);
                if (nameList.isEmpty()) {
                    System.out.println(">> 해당 이름의 회원을 찾을 수 없습니다.");
                } else {
                    System.out.println("--- 검색 결과 (" + nameList.size() + "건) ---");
                    printUserList(nameList);
                }
                break;
                
            // 회원 삭제
            case "3":               
            	// 회원 삭제
                System.out.print(">> 삭제할 회원의 아이디를 입력하세요: ");
                String deleteId = scanner.nextLine();
               
               
                // 삭제 전 확인
                MainDTO userToDel = adminDAO.findUserById(deleteId);
                if (userToDel == null) {
                    System.out.println(">> 해당 아이디의 회원이 존재하지 않습니다.");
                    break;
                }
                
                int deleteCode = userToDel.getUser_code();
                
                System.out.print(">> 정말로 '" + userToDel.getUser_name() + "(" + userToDel.getUser_Id() + ")' 님을 삭제하시겠습니까? (Y/N): ");
                String confirm = scanner.nextLine();
                
                if (confirm.equalsIgnoreCase("Y")) {
                    boolean isDeleted = adminDAO.deleteUserByCode(deleteCode);
                    if (isDeleted) {
                        System.out.println(">> 회원 정보가 성공적으로 삭제되었습니다.");
                    } else {
                        System.out.println(">> 회원 삭제에 실패하였습니다. (관리자에게 문의)");
                    }
                } else {
                    System.out.println(">> 회원 삭제를 취소하였습니다.");
                }
                break;
                             
            // 연체 회원
            case "4":
                System.out.println(">> (구현예정) 연체 회원 목록을 조회합니다.");
                break;
                
            // 전체 리스트
            case "5":
                // --- [수정] 4. 전체 리스트 ---
                List<MainDTO> allList = adminDAO.findAllUsers();
                if (allList.isEmpty()) {
                    System.out.println(">> 등록된 회원이 없습니다.");
                } else {
                    System.out.println("--- 전체 회원 목록 (" + allList.size() + "건) ---");
                    printUserList(allList);
                }
                break;
                
            // 뒤로가기
            case "6":
                isMemberMenuRunning = false;
                break;
                
            default:
                System.out.println(">> 잘못된 입력입니다. 1~5 사이의 숫자를 입력해주세요.");
                break;
            }
        }
    }

    /**
     * [신규] 회원 목록을 콘솔에 예쁘게 출력하는 헬퍼(Helper) 메서드
     * @param users : 출력할 회원 목록
     */
    private void printUserList(List<MainDTO> users) {
        if (users == null || users.isEmpty()) {
            return; // 출력할 내용 없음
        }

        // 헤더 출력
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s | %-12s | %-8s | %-12s | %-13s | %-20s | %-15s\n",
                "코드", "아이디", "이름", "생년월일", "전화번호", "이메일", "주소");
        System.out.println("--------------------------------------------------------------------------------------------------");

        // 내용 출력
        for (MainDTO user : users) {
            System.out.printf("%-5d | %-12s | %-8s | %-12s | %-13s | %-20s | %-15s\n",
                    user.getUser_code(),
                    user.getUser_Id(),
                    user.getUser_name(),
                    user.getUser_birth(), // DTO에서 String으로 변환됨
                    user.getUser_tel(),
                    user.getUser_email(),
                    user.getUser_address());
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
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