package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



public class UserUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private UserDAOImpl1 dao = new UserDAOImpl1();
	private LoginInfo login = null;
	
	public UserUI(LoginInfo login) {
		this.login = login;
	}
	
	public void menu() {
	
		int ch = 0;
		
		while(true) {
			System.out.println("\n[사용자 화면]");
			
			try {
				System.out.print("1.도서검색 2.대출 3.반납신청 4.도서신청 5.마이페이지 6.로그아웃");
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
	
	
	// 1. 도서 검색[제목], [저자] / (도서번호, isbn, 도서이름, 저자, 출판사, 발행일를 출력)
	protected void findBybook() {
		System.out.println("\n[도서검색]");
		
		String bookname;
		String authorname;
		
		try {
			System.out.print("도서 제목 ? ");
			bookname = br.readLine();
			authorname = br.readLine();
			
			List<BookInfoDTO1> list = dao.listBook(bookname, authorname);
			
			if(list.size() == 0) {
				System.out.println("우리 도서관에 등록된 도서가 아닙니다.");
				return;
			}
			for(BookInfoDTO1 dto : list) {
				System.out.print("도서번호" + dto.getBook_code() + "\t");
				System.out.print("ISBN번호" + dto.getIsbn() + "\t");
				System.out.print("도서제목" + dto.getBookName() + "\t");
				System.out.print("도서저자" + dto.getAuthor_name() + "\t");
				System.out.print("출판사" + dto.getPublisher_name() + "\t");
				System.out.println("도서 발행일" + dto.getPublish_date());
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	
	// 2. 대출신청/연장
	  // 대출 신청 시 연체된 회원이면 대출불가 날짜 출력
	  // 대출 연장 시 연장 가능 회기 출력
	public void loan() {
	    int ch2 = 0;
	
	while(true) {
		System.out.println("\n[대출]");
		try {
			System.out.println("1.대출신청 2.대출연장");
			ch2 = Integer.parseInt(br.readLine());
			
			switch(ch2) {
			case 1 : insertloan(); break;
			case 2 : renewloan(); break;
			}
			
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	}
	
	protected void insertloan() {
		System.out.println("\n[대출신청]");

		List<BookInfoDTO1> list = new ArrayList<BookInfoDTO1>();
		/*
		LoanDTO dto = new LoanDTO();
		String bookname;
		String bookcondition;
		*/
		
		try {
			System.out.print("도서 제목 ? ");
			// bookname = br.readLine();
			
			if(list.size() == 0) {
				System.out.println("등록된 도서가 아닙니다.");
				return;
			
			} 
				


		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
		

	protected void renewloan() {
		System.out.println("\n[대출연장 신청]");
		String usercode;

		try {
			System.out.print("회원 번호 ? ");
			usercode = br.readLine();
			
			List<LoanDTO> list = dao.listloan(usercode);
			
			if(list.size() == 0) {
				System.out.println("대출신청한 도서가 없습니다.");
				return;
			}
			for(LoanDTO dto : list) {
				System.out.println(usercode + "회원님의 대출 도서 목록입니다.");
				System.out.print("도서 제목 : " + dto.getBookname() + "\t");
				System.out.print("도서 코드 : " + dto.getBook_code() + "\t");
				System.out.print("대출 일자 : " + dto.getCheckout_date() + "\t");
				System.out.print("반납 예정일자 : " + dto.getDue_date() + "\t");
				System.out.print("반납 일자 : " + dto.getDue_date() + "\t");
				System.out.println("대출연장 가능 여부 : " + dto.getDue_date() + "\t");
			}
			
			
				
		} catch (Exception e) {
			e.printStackTrace();
		} 
		System.out.println();
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
