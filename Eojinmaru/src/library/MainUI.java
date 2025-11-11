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
    
    private MemberDAO memberDAO = new MemberDAOImpl();
    private LoginInfo login = new LoginInfo(); // 로그인 정보 저장
    private AdminUI adminUI = new AdminUI();
    private UserUI userUI = new UserUI(login);

    public void menu() {
        while (true) {
        	try {
        		System.out.println("\n===== [메인 화면] =====");
        		System.out.println("1. 로그인");
        		System.out.println("2. 회원가입");
        		System.out.println("3. 도서 검색");
        		System.out.println("4. 도서 신청");
        		System.out.println("5. 공지사항");
        		System.out.println("=========================");
        		System.out.print("메뉴를 선택하세요: ");
        		
        		int mainChoice = Integer.parseInt(br.readLine());
        		
        		switch (mainChoice) {
        		// 1. 로그인
        		case 1: signin(); break;
        		// 2. 회원가입
        		case 2: signup(); break;
        		// 3. 도서 검색
        		case 3:
        			System.out.println(">> (구현예정) 도서 검색 페이지로 이동합니다.");
        			break;
        			// 4. 도서 신청
        		case 4:
        			System.out.println(">> (구현예정) 도서 신청 페이지로 이동합니다.");
        			break;
        			// 5. 공지사항 조회
        		case 5: notice(); break;
        		default:
        			System.out.println(">> 잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요.");
        			break;
        		}
        	} catch (NumberFormatException e) {
        		System.out.println(">> 잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요.");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
    }
    
    // 로그인
    private void signin() {
    	try {
    		System.out.println("\n--- [로그인] ---");
    		System.out.print("아이디: ");
    		String inputId = br.readLine();
    		System.out.print("비밀번호: ");
    		String inputPw = br.readLine();
    		
    		// 1. 관리자 로그인 확인 및 관리자 메뉴 호출
    		if (ADMIN_ID.equals(inputId) && ADMIN_PW.equals(inputPw)) {
    			System.out.println(">> 관리자님, 환영합니다. [관리자 화면]으로 이동합니다.");
    			adminUI.showMenu(scanner);
    		}
    		// 2. 관리자가 아니면, 사용자 로그인 시도
    		else {
    			// MemberDAO를 통해 사용자 로그인 시도
    			MemberDTO user = memberDAO.login(inputId, inputPw);
    			
    			if(user != null) {
    				// 로그인 성공 -> LoginInfo에 정보 저장
    				login.login(user); 
    				System.out.println(">> " + user.getUser_name() + "님, 환영합니다. [사용자 화면]으로 이동합니다.");
    				// 사용자 메뉴 호출
    				userUI.menu();
    			} else {
    				// 로그인 실패
    				System.out.println(">> 아이디 또는 비밀번호가 일치하지 않습니다.");
    			}
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    
    // 회원 가입
    private void signup() {
    	System.out.println("\n--- [회원가입] ---");
    	
    	try {
    		MemberDTO newUser = new MemberDTO();
    		System.out.print("아이디: ");
    		newUser.setUser_Id(br.readLine());
    		System.out.print("비밀번호: ");
    		newUser.setUser_pwd(br.readLine());
    		System.out.print("이름: ");
    		newUser.setUser_name(br.readLine());
    		System.out.print("생년월일 (YYYY-MM-DD): ");
    		newUser.setUser_birth(br.readLine());
    		System.out.print("전화번호 (010-XXXX-XXXX): ");
    		newUser.setUser_tel(br.readLine());
    		System.out.print("이메일: ");
    		newUser.setUser_email(br.readLine());
    		System.out.print("주소: ");
    		newUser.setUser_address(br.readLine());
    		boolean isSuccess = memberDAO.signUpUser(newUser);
    		if (isSuccess) {
    			System.out.println(">> 회원가입이 성공적으로 완료되었습니다.");
    			// 회원가입 성공시 바로 로그인까지
    			// 로그인 성공 -> LoginInfo에 정보 저장
				login.login(newUser); 
				System.out.println(">> " + newUser.getUser_name() + "님, 환영합니다. [사용자 화면]으로 이동합니다.");
				// 사용자 메뉴 호출
				userUI.menu();
    		} else {
    			System.out.println(">> 회원가입에 실패하였습니다.");
    		}
		} catch (Exception e) {
			e.printStackTrace();
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