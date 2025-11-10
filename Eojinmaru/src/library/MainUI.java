package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import DBUtil.DBConn;

public class MainUI {

    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PW = "admin";
    
    private Connection conn = DBConn.getConnection();
    
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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
                case "5": notice(); break;
                default:
                    System.out.println(">> 잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요.");
                    break;
            }
        }
        
    }
    
    // 로그인
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
                userUI.menu(); 
            } else {
                // 로그인 실패
                System.out.println(">> 아이디 또는 비밀번호가 일치하지 않습니다.");
            }
        }
    }
    
    // 회원 가입
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
    
    // 공지사항 목록 보기
    private void notice() {
    	final int MaxNumInPage = 5;
    	int pages = 1;
    	
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	String sql;
    	
    	System.out.println("\n[공지사항]");
    	System.out.println("번호\t제목\t게시날짜");
    	
    	
    	try {
			while(true) {
				// 게시글 갯수 확인하고 최대 페이지 계산
				sql = "SELECT COUNT(*) cnt FROM notice";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				rs.next();
				int maxPage = (int)Math.ceil(rs.getInt("cnt") / (double)MaxNumInPage);
				
				// 해당하는 페이지의 공지글 목록 가져오기
	    		sql = "SELECT notice_id, notice_title, TO_CHAR(notice_date, 'YY-MM-DD') notice_date FROM notice ORDER BY notice_id DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, (pages-1)*MaxNumInPage);
				pstmt.setInt(2, MaxNumInPage);
				rs = pstmt.executeQuery();
				
				// 출력
				System.out.println("글번호\t제목\t게시날짜");
				while(rs.next()) {
					System.out.println(rs.getString("notice_id") + "\t" + rs.getString("notice_title") + "\t" + rs.getString("notice_date"));
				}
				
				
				System.out.print("1.왼쪽 2.오른쪽 3.게시글 보기 4.이전 => ");
				
				int ch = Integer.parseInt(br.readLine());
				switch(ch) {
				case 1: if(pages > 1) pages -= 1; break;
				case 2: if(pages < maxPage) pages += 1; break;
				case 3: break;
				case 4: return;
				default: throw new Exception("부적절한 입력");
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    
    
    
}