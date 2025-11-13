package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class UserUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private UserDAOImpl1 dao = new UserDAOImpl1();

	private LoginInfo login = null;
	private ReturnUI returnUI = null;
	private MypageUI mypageUI = null;

	public UserUI(LoginInfo login) {
		this.login = login;

		returnUI = new ReturnUI(login);
		mypageUI = new MypageUI(login);
	}

	public void menu() {
		
		int ch = 0;

		while (true) {
			System.out.println("\n[사용자 화면]");

			try {
				System.out.print("1.도서검색 2.대출 3.반납신청 4.도서신청 5.마이페이지 6.로그아웃  =>  ");
				ch = Integer.parseInt(br.readLine());
				
				if (ch == 6) {
					login.logout();
					System.out.println("로그아웃 되었습니다.");
					return;				
				}
				switch (ch) {
				case 1: findBybook(); break;
				case 2: loan(); break;
				case 3: returnUI.start(); break;
				case 4: sincheong(); break;
				case 5: mypageUI.menu(); break;
				}
			} catch (NumberFormatException e) {
				System.out.println("숫자만 입력해주세요");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 1. 도서 검색[제목], [저자] / (도서번호, isbn, 도서이름, 저자, 출판사, 발행일를 출력)
	protected void findBybook() {
		System.out.println("\n[도서검색]");
		
		String search;
		List<BookInfoDTO1> list = null;

		try {
			System.out.print("도서 제목 또는 저자 ? ");
			search = br.readLine();
			System.out.println();
			
			list = dao.listBook(search);
			
			if (list.size() == 0) {
				System.out.println("우리 도서관에 등록된 도서가 아닙니다.");
				return;
			}
			
			for (BookInfoDTO1 dto : list) {
				System.out.print(dto.getBook_code() + "\t");
				System.out.print(dto.getIsbn() + "\t");
				System.out.print(dto.getBookName() + "\t");
				System.out.print(dto.getAuthor_name() + "\t");
				System.out.print(dto.getPublisher_name() + "\t");
				System.out.println(dto.getPublish_date());
			}
			System.out.println();
			
			int ch3 = 0;
			
			System.out.print("1.대출신청 2.사용자화면  =>  ");
			ch3 = Integer.parseInt(br.readLine());
			
			switch(ch3) {
			case 1: insertloan(); break;
			case 2: menu(); break;
			}
				
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	// 대출 신청 시 연체된 회원이면 대출불가 날짜 출력
	// 대출 연장 시 연장 가능 회기 출력
	protected void insertloan() {
	    System.out.println("\n[대출신청]");

	    int bookcode;
	    
	    try {
	    	// 패널티 조회
	    	
	    	// 연체 조회
	    	
	    	// 도서 대출 개수(5이하)
	    	
	    	
	    	
	        System.out.print("도서 코드 ? ");
	        bookcode = Integer.parseInt(br.readLine());
	        	        
	        List<BookInfoDTO1> list = dao.loanBook(bookcode);
	        
	        if(list.size() == 0) {
	        	System.out.println("도서코드는 잘못되었습니다. 다시 입력하세요..");
	        	return;
           }
	        
	        LoanDTO dto = new LoanDTO();
	        dto.setBook_code(bookcode);
	        dto.setUser_code(login.loginUser().getUser_code());
	        
	        dao.insertloan(dto);

	        System.out.println("대출이 완료 되었습니다.");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    System.out.println();
	}


	
	// 2. 대출신청/연장/예약신청
	public void loan() {
		System.out.println("\n[대출 리스트]");
		
		int usercode;

		try {
			System.out.print("회원 번호 ? ");
			usercode = Integer.parseInt(br.readLine());
			
			List<LoanDTO> list = dao.listloan(usercode);

			if (list.size() == 0) {
				System.out.println("대출신청한 도서가 없습니다.");
				return;
			}
			for (LoanDTO dto : list) {
				System.out.println("회원님의 대출 도서 목록입니다.");
				System.out.print("도서 제목 : " + dto.getBookname() + "\t");
				System.out.print("도서 코드 : " + dto.getBook_code() + "\t");
				System.out.print("대출 일자 : " + dto.getCheckout_date() + "\t");
				System.out.print("반납 예정일자 : " + dto.getDue_date() + "\t");
				System.out.print("반납 일자 : " + dto.getDue_date() + "\t");
				System.out.println("대출연장 가능 여부 : " + dto.getDue_date() + "\t");
			}
		
		    System.out.println();

		    int ch2 = 0;

		       System.out.println("1.대출연장 2.대출예약");
		       ch2 = Integer.parseInt(br.readLine());

		   switch (ch2) {
		   case 1: renewloan(); break;
		   case 2: loanreservation(); break;
		   }
		   } catch (Exception e) {
			e.printStackTrace();
        	}
	}
	
	protected void renewloan() {
		System.out.println("\n[대출연장 신청]");
		
		int usercode;

		try {
			System.out.print("회원 번호 ? ");
			usercode = Integer.parseInt(br.readLine());
			
			List<LoanDTO> list = dao.listloan(usercode);

			if (list.size() == 0) {
				System.out.println("대출신청한 도서가 없습니다.");
				return;
			}
			for (LoanDTO dto : list) {
				System.out.println("회원님의 대출 도서 목록입니다.");
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

	// 대출 예약(대출 중인 도서)
	protected void loanreservation() {
		System.out.println("\n[대출 예약 신청]");
		
		
	}
	
	// 4. 도서신청
	protected void sincheong() {
		System.out.println("\n[도서신청]");

	}
}
