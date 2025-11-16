package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private BookDAOImpl dao = new BookDAOImpl();

	private LoginInfo login = null;
	private ReturnUI returnUI = null;
	private MypageUI mypageUI = null;
	private SincheongUI sincheong = new SincheongUI();

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
				System.out.print("1.도서검색/대출신청 2.대출 3.반납신청 4.도서신청 5.마이페이지 6.로그아웃  =>  ");
				ch = Integer.parseInt(br.readLine());

				if (ch == 1) {
					int usercode = login.loginUser().getUser_code();

					List<LoanDTO> overduelist = dao.overdue(usercode);
					List<LoanDTO> renewallist = dao.renwaldate(usercode);
					// int countlist = dao.loancount(usercode);
					
					boolean overdue = !overduelist.isEmpty();
					boolean restricted = !renewallist.isEmpty() && renewallist.get(0).getLoan_renewaldate() != null;
					// boolean maxcount = countlist >= 5;


						if(overdue  || restricted) {
							if(overdue) {
							System.out.println("\n연체한 도서가 존재합니다. 연체된 도서 반납 후 다시 이용해주세요.");
						} if(restricted) {
							System.out.println("\n도서 반납 연체로 " + renewallist.get(0).getLoan_renewaldate() + " 까지 대출이 불가합니다.");
						} 			
						} else {
							findBybook();
						}
		
						}
								
					if (ch == 6) {
						login.logout();
						System.out.println("로그아웃 되었습니다.");
						return;
					}
					switch (ch) {
					case 2:
						loan();
						break;
					case 3:
						returnUI.start();
						break;
					case 4:
						sincheong.sincheongUI();
						break;
					case 5:
						mypageUI.menu();
						break;
					} 
				    } catch (NumberFormatException e) {
				    System.out.println("숫자만 입력해주세요");
			        } catch (Exception e) {
			        	e.printStackTrace();
			        }
		}
	}

	// 1. 도서 검색[제목], [저자] / (도서번호, isbn, 도서이름, 저자, 출판사, 발행일를 출력)
	protected void findBybook() {
		System.out.println("\n[도서검색/대출신청]");

		String search;
		List<BookInfoDTO> list = null;

		while (true) {
			try {
				System.out.print("도서 제목 또는 저자(공백 입력시 이전 메뉴) => ");
				search = br.readLine();
				System.out.println();

				// 공백 입력시 뒤로가기
				if (search.isBlank())
					return;

				list = dao.listBook(search);

				// 결과 출력
				String LINE = "==================================================================================";
				System.out.println();
				System.out.println(LINE);
				System.out.println(String.format("| %-4s|\t\t%-20s\t| %-10s\t| %-6s\t| %-6s|", "번호", "책 제목", "저자",
						"출판사", "대출가능여부"));
				if (list.size() == 0) {
					System.out.println("우리 도서관에 등록된 도서가 아닙니다.");
					return;

				} else {
					for (BookInfoDTO dto : list) {
						System.out.println(
								"----------------------------------------------------------------------------------");
						System.out.println(String.format("| %-3s| %-20s\t\t| %-10s\t| %-6s\t| %-6s|",
								dto.getBook_code(), truncateString(dto.getBookName(), 20),
								truncateString(dto.getAuthor_name(), 10), truncateString(dto.getPublisher_name(), 6),
								(dto.getBook_condition().equals("대출가능") ? "대출가능" : "대출불가")));
					}
				}
				System.out.println(LINE);

				int ch3 = 0;

				System.out.print("1.대출신청 2.도서검색 3.사용자화면  =>  ");
				ch3 = Integer.parseInt(br.readLine());

				switch (ch3) {
				case 1:
					insertloan();
					return;
				case 2:
					break;
				case 3:
					return;
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

// 대출 구현  대출 -> 대출 중인 리스트 -> 대출 연장 or 대출예약 -> 2개 다 하기 싫으면 사용자화면 
	// * 대출 신청
	// * 대출 신청 -> 도서 "대출 가능"을 "대출 불가"로 변경(완)
	// * 대출 신청 시 연체된 회원이면 대출불가 날짜 출력

	// * 대출 연장
	// * 대출연장 신청 -> 반납 예정일 + 7일 날짜 출력(완)
	// * 대출 연장 시 연장 사용했을시 사용 불가 함 출력(완)

	// * 대출 중 도서 예약

	protected void insertloan() {
		System.out.println("\n[대출 신청]");

		int bookcode;

		try {
			// 패널티 조회

			// 연체 조회

			// 도서 대출 개수(5이하)

			System.out.print("도서 코드 ? ");
			bookcode = Integer.parseInt(br.readLine());

			List<BookInfoDTO> list = dao.loanBook(bookcode);
			List<LoanDTO> loanlist = dao.loanlistbook(bookcode);

			if (list.size() == 0) {
				System.out.println("도서코드는 잘못되었습니다. 다시 입력하세요..");
				return;
			}

			for (LoanDTO dto1 : loanlist) {
				if (dto1.getBook_code() == bookcode) {

					LoanDTO dto = new LoanDTO();
					dto.setBook_code(bookcode);
					dto.setUser_code(login.loginUser().getUser_code());

					// 대출저장 및 해당책은 대출중으로 변경
					dao.insertloan(dto);

					System.out.println("대출이 완료 되었습니다.");

					return;
				}

			}
			System.out.println("대출 중인 도서이므로 대출이 불가능합니다.");

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	// 2. 대출
	public void loan() {
		System.out.println("\n[대출 리스트]");
		int usercode;

		try {
			System.out.print("회원 번호 ? ");
			usercode = Integer.parseInt(br.readLine());

			List<LoanDTO> list = dao.listloaning(usercode);

			if (list.size() == 0) {
				System.out.println("대출신청한 도서가 없습니다.");
			} else {

				System.out.println();
				System.out.println("[회원님의 대출 중인 도서 목록입니다.]");
				String LINE = "=====================================================================================";
				System.out.println();
				System.out.println(LINE);
				System.out.println(String.format("| %-4s|%-4s\t|\t\t%-11s\t| %-11s\t| %-10s|", "대출번호", "도서코드", "책 제목",
						"대출일자", "반납예정일자"));
				for (LoanDTO dto : list) {
					System.out.println("----------------------------------------------------------------------------");
					System.out.println(String.format("| %-5s| %-6s | %-10s\t | %-10s\t| %-12s|", dto.getLoan_code(),
							dto.getBook_code(), truncateString(dto.getBookname(), 20),
							truncateString(dto.getCheckout_date(), 10), truncateString(dto.getDue_date(), 10)));
				}
				System.out.println(LINE);
				System.out.println();
			}

			int ch2 = 0;

			System.out.print("1.대출연장 2.대출예약 3.도서검색/대출신청  =>  ");
			ch2 = Integer.parseInt(br.readLine());

			if (ch2 == 1) {
				System.out.println();
				System.out.print("연장할 대출 번호 ? ");
				int loan_code = Integer.parseInt(br.readLine());
				boolean b = false;

				for (LoanDTO dto : list) {
					if (dto.getLoan_code() == loan_code && dto.getIsExtended() == 0) {
						b = true;
						break;
					}
				}

				if (b) {
					dao.extendloan(loan_code);
					System.out.println("신청하신 도서가 연장되었습니다.");
				} else {
					for (LoanDTO dto : list) {
						if (dto.getIsExtended() == 1) {
							System.out.println("연장신청을 1회 사용하였기에 연장 신청이 불가합니다.");
						}
					}
				}
			}
			switch (ch2) {
			case 2:
				loanreservation();
				break;
			case 3:
				findBybook();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 대출 예약(대출 중인 도서)
	protected void loanreservation() {
		System.out.println("\n[대출 예약 신청]");

		int bookcode;

		try {
			System.out.print("예약신청할 도서 번호 ? ");
			bookcode = Integer.parseInt(br.readLine());

			List<LoanDTO> list = dao.loanlistall(bookcode);

			if (list.size() == 0) {
				System.out.println("대출 중인 도서가 없습니다.");
				return;
			}
			for (LoanDTO dto : list) {
				if (dto.getBook_code() == bookcode) {

					LoanDTO dto1 = new LoanDTO();
					dto1.setBook_code(bookcode);
					dto1.setUser_code(login.loginUser().getUser_code());

					dao.loanreservation(dto1);

					System.out.println("대출 예약이 완료 되었습니다.");

					return;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 말줄임 함수(책제목, 저자 이름 자르는데 사용)
	private String truncateString(String text, int maxLength) {
		if (text == null) {
			text = "";
		}
		if (text.length() > maxLength) {
			if (maxLength < 10) {
				return text.substring(0, maxLength);
			}
			return text.substring(0, maxLength - 10) + "...";
		}
		if (text.length() < maxLength) {
			StringBuilder sb = new StringBuilder(text);
			int paddingLength = maxLength - text.length();
			for (int i = 0; i < paddingLength; i++) {
				sb.append(" ");
			}
			return sb.toString();
		}
		return text;
	}
}
