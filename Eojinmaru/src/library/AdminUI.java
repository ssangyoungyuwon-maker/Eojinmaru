package library;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AdminUI {
	Scanner scanner = new Scanner(System.in);

	private AdminDAO adminDAO = new AdminDAOImpl();
	private AdminDAOImpl adminDAOImpl = new AdminDAOImpl();
	private List<AdminDTO> list;

	public void showMenu() {

		boolean isAdminRunning = true;

		while (isAdminRunning) {

			System.out.println("\n\t\t\t\t\t🔒 [ 관 리 자 ] \t\t\t\t\t");
			System.out.println(
					"====================================================================================================");
			System.out.println("\t\t\t\t\t 1. 회원 관리");
			System.out.println("\t\t\t\t\t 2. 도서 관리");
			System.out.println("\t\t\t\t\t 3. 대출 및 반납 관리");
			System.out.println("\t\t\t\t\t 4. 신청 도서 관리");
			System.out.println("\t\t\t\t\t 5. 공지사항 관리");
			System.out.println("\t\t\t\t\t 6. 관리자 로그아웃");
			System.out.println("\t\t\t\t\t 7. 시스템 종료");
			System.out.println(
					"====================================================================================================\n");

			System.out.print("회원 관리 메뉴 선택: ");

			String adminChoice = scanner.nextLine();

			switch (adminChoice) {
			case "1":
				System.out.println(" 🤖 [회원 관리] 페이지로 이동합니다.");
				System.out.println();
				this.showMemberMenu();
				break;

			case "2":
				System.out.println(" 🤖 [도서 관리] 페이지로 이동합니다.");
				System.out.println();
				this.showBookMenu();
				break;

			case "3":
				System.out.println(" 🤖 [대출 및 반납 관리] 페이지로 이동합니다.");
				System.out.println();
				this.showLoanBookandMemberInfo();
				break;
			case "4":
				System.out.println(" 📚 [신청 도서 관리] 페이지로 이동합니다.");
				System.out.println();
				this.showsincheongmanage();
				break;
			case "5":
				System.out.println();
				System.out.println("📢 [공지사항 관리] 페이지로 이동합니다.");
				System.out.println();
				this.noticeadmin();
				break;
			case "6":
				System.out.println(" 📋 [메인 화면] 으로 돌아갑니다. ");
				isAdminRunning = false;
				break;
			case "7": // 시스템 종료
				System.out.println("❗정말 시스템을 종료하시겠습니까 ? [Y/N]");

				String shutdown;
				do {
					shutdown = scanner.next();
					scanner.nextLine();

					if (shutdown.equalsIgnoreCase("y")) {
						System.out.println("시스템을 종료합니다. 🤖");
						System.exit(0);

					} else if (shutdown.equalsIgnoreCase("n")) {
						System.out.println("시스템을 종료하지 않습니다.");
						System.out.println("이전으로 돌아갑니다.");
						System.out.println();

					} else {
						System.out.print(" 🚨 잘못된 입력입니다. Y 또는 N 을 입력하십시오. -> ");
					}
				} while (!shutdown.equalsIgnoreCase("y") && !shutdown.equalsIgnoreCase("n"));

				this.showMenu();
				break;

			default:
				System.out.println(">> 잘못된 입력입니다. 1~7 사이의 숫자를 입력해주세요.");
				System.out.println();
				this.showMenu();
				break;
			}
		}
	}

	private void showMemberMenu() {
		boolean isMemberMenuRunning = true;
		while (isMemberMenuRunning) {
			System.out.println("\n\t\t\t\t\t🔒 [ 회원 관리 ] \t\t\t\t\t");
			System.out.println(
					"====================================================================================================");
			System.out.println("\t\t\t\t\t1. 아이디 검색");
			System.out.println("\t\t\t\t\t2. 이름 검색");
			System.out.println("\t\t\t\t\t3. 회원 삭제");
			System.out.println("\t\t\t\t\t4. 전체 리스트");
			System.out.println("\t\t\t\t\t5. 뒤로가기");
			System.out.println(
					"====================================================================================================\n");
			System.out.print("회원 관리 메뉴 선택: ");

			String memberChoice = scanner.nextLine();
			switch (memberChoice) {

			// 아이디 검색
			case "1":
				System.out.print(">> 검색할 회원의 아이디를 입력하세요: ");
				String id = scanner.nextLine();
				MemberDTO user = adminDAO.findUserById(id);
				if (user != null) {

					System.out.println("\n\t\t\t\t\t🔎 [ 검색 결과 ] \t\t\t\t\t");
					List<MemberDTO> resultList = new ArrayList<>();
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
				List<MemberDTO> nameList = adminDAO.findUserByName(name);
				if (nameList.isEmpty()) {
					System.out.println(">> 해당 이름의 회원을 찾을 수 없습니다.");
				} else {
					System.out.println("\n\t\t\t\t\t🔎 [ 검색 결과 : " + nameList.size() + "건 ] \t\t\t\t\t");
					printUserList(nameList);
				}
				break;

			// 회원 삭제
			case "3":
				System.out.print(">> 삭제할 회원의 코드를 입력하세요: ");
				String inputCode = scanner.nextLine();
				int deleteCode;

				try {
					deleteCode = Integer.parseInt(inputCode);
				} catch (NumberFormatException e) {
					System.out.println(">> 잘못된 입력입니다. 회원 코드는 숫자만 입력할 수 있습니다.");
					break;
				}

				MemberDTO userToDel = adminDAO.findUserByCode(deleteCode);
				if (userToDel == null) {
					System.out.println(">> 해당 코드의 회원이 존재하지 않습니다.");
					break;
				}

				System.out.print(">> 정말로 '" + userToDel.getUser_name() + "(" + userToDel.getUser_Id()
						+ ")' 님을 삭제하시겠습니까? (Y/N): ");
				String confirm = scanner.nextLine();

				if (confirm.equalsIgnoreCase("Y")) {
					boolean isDeleted = adminDAO.deleteUserByCode(deleteCode);
					if (isDeleted) {
						System.out.println(">> 회원 정보가 성공적으로 삭제되었습니다.");
					} else {
						System.out.println(">> 회원 삭제에 실패하였습니다.");
					}
				} else {
					System.out.println(">> 회원 삭제를 취소하였습니다.");
				}
				break;

			
			// 전체 리스트
			case "4":
				List<MemberDTO> allList = adminDAO.findAllUsers();
				if (allList.isEmpty()) {
					System.out.println(">> 등록된 회원이 없습니다.");
					break;
				}

				final int pageSize = 10;
				int currentPage = 1;
				int totalItems = allList.size();
				int totalPages = (totalItems + pageSize - 1) / pageSize; // 전체 페이지 수 계산

				while (true) {
					int startIdx = (currentPage - 1) * pageSize;
					int endIdx = Math.min(startIdx + pageSize, totalItems);

					List<MemberDTO> pageList = allList.subList(startIdx, endIdx);

					System.out.println("\n\t\t\t\t\t🔎 [ 전체 유저수 : " + allList.size() + "건 ] \t\t\t\t\t");
					printUserList(pageList); // 현재 페이지 리스트 출력

					String s = String.format("페이지 %d / %d", currentPage, totalPages);
					System.out.println("\t'<' 이전 페이지\t\t\t" + s + "\t\t\t' >' 다음 페이지");
					System.out.println("0. 뒤로가기 / 페이지 번호 입력: ");

					String pageChoice = scanner.nextLine().trim();

					if (pageChoice.equals("0")) {
						break;
					} else if (pageChoice.equals("<")) {
						if (currentPage > 1) {
							currentPage--;
						} else {
							System.out.println("⚠️ 첫 번째 페이지입니다.");
						}
					} else if (pageChoice.equals(">")) {
						if (currentPage < totalPages) {
							currentPage++;
						} else {
							System.out.println("⚠️ 마지막 페이지입니다.");
						}
					} else {
						try {
							int pageNum = Integer.parseInt(pageChoice);
							if (pageNum >= 1 && pageNum <= totalPages) {
								currentPage = pageNum;
							} else {
								System.out.println("🚨 유효하지 않은 페이지 번호입니다.");
							}
						} catch (NumberFormatException e) {
							System.out.println("🚨 잘못된 입력입니다. '<', '>', '0', 또는 페이지 번호를 입력해주세요.");
						}
					}
				}
				break;

			// 뒤로가기
			case "5":
				isMemberMenuRunning = false;
				break;

			default:
				System.out.println(">> 잘못된 입력입니다. 1~5 사이의 숫자를 입력해주세요.");
				break;
			}
		}
	}

	private void showBookMenu() {
		boolean isBookMenuRunning = true;
		while (isBookMenuRunning) {
			String Line = "====================================================================================================";
			System.out.println("\n\t\t\t\t\t🔒 [ 도서 관리 ] \t\t\t\t\t");
			System.out.println(Line);
			System.out.println("\t\t\t\t\t1. 도서 전체 리스트");
			System.out.println("\t\t\t\t\t2. 도서 검색");
			System.out.println("\t\t\t\t\t3. 폐기 도서 확인");
			System.out.println("\t\t\t\t\t4. 폐기 도서 등록");
			System.out.println("\t\t\t\t\t5. 신상 도서 등록");
			System.out.println("\t\t\t\t\t6. 도서 삭제");
			System.out.println("\t\t\t\t\t7. 뒤로가기");
			System.out.println(Line);
			System.out.print("도서 관리 메뉴 선택: ");

			String bookChoice = scanner.nextLine();
			switch (bookChoice) {

			// 도서 전체 리스트
			case "1": {
				List<BookInfoDTO> allBooks = adminDAO.findAllBooks();
				if (allBooks.isEmpty()) {
					System.out.println(">> 등록된 도서가 없습니다.");
					break;
				}

				final int pageSize = 10;
				int currentPage = 1;
				int totalItems = allBooks.size();
				int totalPages = (totalItems + pageSize - 1) / pageSize;

				while (true) {
					int startIdx = (currentPage - 1) * pageSize;
					int endIdx = Math.min(startIdx + pageSize, totalItems);

					List<BookInfoDTO> pageList = allBooks.subList(startIdx, endIdx);

					System.out.println("\n\t\t\t\t\t🔎 [ 전체 도서 권수 : " + allBooks.size() + "건 ] \t\t\t\t\t");

					printBookList(pageList);

					String s = String.format("페이지 %d / %d", currentPage, totalPages);
					System.out.println("\t'<' 이전 페이지\t\t\t" + s + "\t\t\t' >' 다음 페이지");
					System.out.println("0. 뒤로가기 / 페이지 번호 입력: ");

					String pageChoice = scanner.nextLine().trim();

					// 사용자 입력 처리
					if (pageChoice.equals("0")) {
						break;
					} else if (pageChoice.equals("<")) {
						if (currentPage > 1) {
							currentPage--;
						} else {
							System.out.println("⚠️ 첫 번째 페이지입니다.");
						}
					} else if (pageChoice.equals(">")) {
						if (currentPage < totalPages) {
							currentPage++;
						} else {
							System.out.println("⚠️ 마지막 페이지입니다.");
						}
					} else {
						try {
							int pageNum = Integer.parseInt(pageChoice);
							if (pageNum >= 1 && pageNum <= totalPages) {
								currentPage = pageNum;
							} else {
								System.out.println("🚨 유효하지 않은 페이지 번호입니다.");
							}
						} catch (NumberFormatException e) {
							System.out.println("🚨 잘못된 입력입니다. '<', '>', '0', 또는 페이지 번호를 입력해주세요.");
						}
					}
				}
				break;
			}

			// 도서 검색
			case "2": {
				System.out.print(">> 검색할 도서명(전체 또는 일부)을 입력하세요: ");
				String bookName = scanner.nextLine();
				List<BookInfoDTO> bookList = adminDAO.findBooksByName(bookName);
				if (bookList.isEmpty()) {
					System.out.println(">> 해당 도서명의 도서를 찾을 수 없습니다.");
				} else {
					System.out.println("\n\t\t\t\t\t🔎 [ 도서 검색 결과 : " + bookList.size() + "건 ] \t\t\t\t\t");
					printBookList(bookList);
				}
				break;
			}

			// 폐기 도서 확인
			case "3": {
				List<DisposedBookDTO> disposedList = adminDAO.findAllDisposedBooks();
				if (disposedList.isEmpty()) {
					System.out.println(">> 폐기 등록된 도서가 없습니다.");
				} else {
					System.out.println("\n\t\t\t🔎 [ 폐기 도서 목록 : " + disposedList.size() + "건 ] \t\t\t\t\t");
					printDisposedBookList(disposedList); // <-- 새로운 헬퍼 메서드 호출
				}
				break;
			}

			// 폐기 도서 등록
			case "4": {
				System.out.println("\n\t\t\t\t\t\t📚 [ 폐기 도서 등록 ] 📚\t\t\t\t\t");
				System.out.print(">> 폐기할 도서의 코드(BOOK_CODE)를 입력하세요: ");
				String disposeCodeInput = scanner.nextLine();
				int disposeBookCode;

				try {
					disposeBookCode = Integer.parseInt(disposeCodeInput);
				} catch (NumberFormatException e) {
					System.out.println(">> 잘못된 입력입니다. 도서 코드는 숫자만 입력할 수 있습니다.");
					break;
				}

				// 폐기 등록 전, 도서가 book (재고) 테이블에 존재하는지 확인
				BookInfoDTO bookToDispose = adminDAO.findBookByCode(disposeBookCode);

				if (bookToDispose == null) {
					System.out.println(">> 해당 도서 코드(" + disposeCodeInput + ")의 도서가 'book' 테이블에 존재하지 않습니다.");
					break;
				}

				System.out.print(">> 도서 '" + bookToDispose.getBookName() + "'의 폐기 사유를 입력하세요: (파손, 분실 등): ");
				String reason = scanner.nextLine();

				System.out.print(">> 정말로 '" + bookToDispose.getBookName() + "' 도서를 폐기 등록하시겠습니까? (Y/N): ");
				String confirmDispose = scanner.nextLine();

				if (confirmDispose.equalsIgnoreCase("y")) {

					boolean registerSuccess = adminDAO.registerDisposedBook(disposeBookCode, reason);

					if (registerSuccess) {
						System.out.println(">> 폐기 등록이 완료되었으며, book (재고) 테이블에서 삭제되었습니다.");
					} else {
						System.out.println(">> 폐기 등록에 실패했습니다. (DB 오류 또는 코드 형식 오류)");
					}
				} else {
					System.out.println(">> 폐기 등록을 취소하였습니다.");
				}
				break;
			}

			// 신상 도서 등록
			case "5": {
				System.out.println("\n\t\t\t\t\t\t📚 [ 신상 도서 등록 ] 📚\t\t\t\t\t");
				BookInfoDTO newBook = new BookInfoDTO();

				System.out.print("ISBN (예: 123-45-678-9123-4): ");
				String isbnInput = scanner.nextLine();

				String isbnPattern = "^\\d{3}-\\d{2}-\\d{3}-\\d{4}-\\d{1}$";

				if (!Pattern.matches(isbnPattern, isbnInput)) {
					System.out.println(">> 유효하지 않은 ISBN 형식입니다. (숫자3-숫자2-숫자3-숫자4-숫자1 형식으로 입력하세요)");
					break;
				}

				newBook.setIsbn(isbnInput);

				System.out.print("Category ID: ");
				newBook.setCategory_id(scanner.nextInt());
				scanner.nextLine();

				System.out.print("Publisher ID: ");
				newBook.setPublisher_id(scanner.nextLine());

				System.out.print("도서명: ");
				newBook.setBookName(scanner.nextLine());

				System.out.print("출판일 (YYYY-MM-DD): ");
				String dateInput = scanner.nextLine();

				if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", dateInput)) {
					System.out.println(">> 날짜 형식이 잘못되었습니다. (YYYY-MM-DD 형식으로 입력하세요)");
					break;
				}
				newBook.setPublish_date(dateInput);

				System.out.print("도서 코드 (BOOK_CODE): ");
				newBook.setBook_code(scanner.nextInt());
				scanner.nextLine();

				boolean insertSuccess = adminDAO.insertBook(newBook);
				if (insertSuccess) {
					System.out.println(">> 신상 도서가 성공적으로 등록되었습니다.");
				} else {
					System.out.println(">> 도서 등록에 실패하였습니다. (DB 오류 또는 날짜 형식 오류)");
				}
				break;
			}

			// 도서 삭제
			case "6": {
				System.out.print(">> 삭제할 도서의 **코드(BOOK_CODE)**를 입력하세요: ");
				String deleteCodeInput = scanner.nextLine();
				int deleteBookCode;

				try {
					deleteBookCode = Integer.parseInt(deleteCodeInput);
				} catch (NumberFormatException e) {
					System.out.println(">> 잘못된 입력입니다. 도서 코드는 숫자만 입력할 수 있습니다.");
					break;
				}

				// 1. 재고 테이블(book)에서 검색
				BookInfoDTO bookToDelete = adminDAO.findBookByCode(deleteBookCode);

				// 2. 폐기 기록 테이블(disposedbook)에서 검색
				DisposedBookDTO disposedToDelete = adminDAOImpl.findDisposedBookByCode(deleteBookCode);

				// --- 삭제 대상 결정 로직 ---
				if (bookToDelete != null) {
					// 1. 재고(book)에 있는 경우 -> 재고 삭제 (기존 case 6 로직)
					System.out.print(">> (재고) 정말로 도서 '" + bookToDelete.getBookName() + "(" + deleteBookCode
							+ ")'를 영구 삭제하시겠습니까? (y/n): ");
					String confirmDel = scanner.nextLine();

					if (confirmDel.equalsIgnoreCase("y")) {
						boolean isDeleted = adminDAO.deleteBookByCode(deleteBookCode);
						if (isDeleted) {
							System.out.println(">> 재고 도서 정보가 성공적으로 삭제되었습니다.");
						} else {
							System.out.println(">> 재고 도서 삭제에 실패하였습니다.");
						}
					} else {
						System.out.println(">> 도서 삭제를 취소하였습니다.");
					}

				} else if (disposedToDelete != null) {
					// 2. 재고에 없고 폐기 기록(disposedbook)에 있는 경우 -> 폐기 기록 삭제
					String bookName = (disposedToDelete.getBookName() == null ? "제목 없음"
							: disposedToDelete.getBookName());

					System.out.print(
							">> (폐기 기록) 정말로 도서 '" + bookName + "(" + deleteBookCode + ")'의 기록을 영구 삭제하시겠습니까? (y/n): ");
					String confirmDel = scanner.nextLine();

					if (confirmDel.equalsIgnoreCase("y")) {
						// 폐기 기록 삭제 DAO 호출
						boolean isDeleted = adminDAO.deleteDisposedBook(deleteBookCode);
						if (isDeleted) {
							System.out.println(">> 폐기 도서 기록이 성공적으로 삭제되었습니다.");
						} else {
							System.out.println(">> 폐기 기록 삭제에 실패하였습니다. (DB 오류)");
						}
					} else {
						System.out.println(">> 폐기 기록 삭제를 취소하였습니다.");
					}

				} else {
					// 3. 재고에도, 폐기 기록에도 없는 경우
					System.out.println(">> 해당 도서 코드(" + deleteBookCode + ")의 도서 (재고/폐기 기록)가 존재하지 않습니다.");
				}
				break;
			}

			case "7":
				isBookMenuRunning = false;
				break;

			default: {
				System.out.println(">> 잘못된 입력입니다. 1~7 사이의 숫자를 입력해주세요.");
				break;
			}

			}
		}
	}

	private void printUserList(List<MemberDTO> users) {
		if (users == null || users.isEmpty()) {
			return;
		}

		String Line = "==================================================================================================================================";

		System.out.println(Line);

		System.out.printf("| %-4s | %-15s\t | %-4s | %-10s\t | %-10s | %-22s\t | %-15s\t |\n", "유저코드", "아이디", "이름",
				"생년월일", "전화번호", "이메일", "주소");

		System.out.println(Line);

		for (MemberDTO user : users) {
			System.out.printf("| %-4s  | %-15s\t | %-4s | %-10s\t| %-12s\t| %-22s\t | %-15s\t |\n",
					// "유저코드", "아이디", "이름", "생년월일", "전화번호", "이메일", "주소"
					user.getUser_code(), user.getUser_Id(), adminDAOImpl.truncateString(user.getUser_name(), 4),
					user.getUser_birth(), user.getUser_tel(), user.getUser_email(), user.getUser_address());
		}
		System.out.println(Line);
	}

	private void printBookList(List<BookInfoDTO> books) {
		if (books == null || books.isEmpty()) {
			return;
		}

		System.out.println(
				"====================================================================================================");
		System.out.printf("%-19s | %-8s | %-8s | %-8s | %-30s | %-12s\n", "ISBN", "BookCode", "Cat_ID", "Pub_ID", "도서명",
				"출판일");

		System.out.println(
				"====================================================================================================\n");
		for (BookInfoDTO book : books) {
			System.out.printf("%-19s | %-8s | %-8s | %-8s | %-30s | %-12s\n", book.getIsbn(),
					(book.getBook_code() == 0 ? "-" : String.valueOf(book.getBook_code())), book.getCategory_id(),
					book.getPublisher_id(), book.getBookName(), book.getPublish_date());
		}
		System.out.println(
				"====================================================================================================\n");

	}

	private void printDisposedBookList(List<DisposedBookDTO> books) {
		if (books == null || books.isEmpty()) {
			return;
		}

		System.out.println(
				"====================================================================================================");
		System.out.printf("%-10s | %-30s | %-12s | %-30s\n", "BookCode", "책 제목", "폐기일자", "폐기 사유");
		System.out.println(
				"====================================================================================================\n");

		for (DisposedBookDTO book : books) {
			System.out.printf("%-10d | %-30s | %-12s | %-30s\n", book.getBook_code(), book.getBookName(),
					book.getDispose_date(), book.getDispose_reason());
		}
		System.out.println(
				"====================================================================================================\n");
	}

	public void showLoanBookandMemberInfo() { // 3.대출/반납 관리
		List<AdminDTO> list = adminDAO.loanbooklist();

		final int pageSize = 10;
		int currentPage = 1;

		int totalItems = list.size();
		int totalPages = (totalItems + pageSize - 1) / pageSize;

		String LINE = "======================================================================================================================";

		while (true) {

			int startIdx = (currentPage - 1) * pageSize;
			int endIdx = Math.min(startIdx + pageSize, totalItems);

			System.out.println("\n\t\t\t\t\t\t📚 [ 대출/반납 관리 메뉴 ] 📚\t\t\t\t\t");
			System.out.println(LINE);
			System.out.printf("\t\t\t\t\t\t💡 대출중인 도서 수: %d 개\n", list.size());
			System.out.println(LINE);

			System.out.printf("| %-2s\t|%-5s| %-5s| %-15s\t\t|    %-7s|   %-4s  |  %-8s |  %-2s\t| %-5s |\n", "유저이름",
					"대출번호", "북코드", "\t  책이름", "대출일", "반납예정일", "실제반납일", "도서상태", "연체일수");
			System.out.println(LINE);

			if (list.isEmpty()) {
				System.out.println(String.format("| %-145s |", "     대출중인 도서가 없습니다."));
			} else {
				for (int i = startIdx; i < endIdx; i++) {
					AdminDTO dto = list.get(i);
					String returnDateDisplay = dto.getReturn_date();

					if (returnDateDisplay == null || returnDateDisplay.isEmpty()) {
						returnDateDisplay = "          "; // 10칸 공백
					}

					System.out.printf("| %-2s\t| %-5d | %-5d | %-20s\t| %-10s | %-10s | %-10s | %-2s\t| %-6d |\n",
							dto.getUsername(), dto.getLoancode(), dto.getBookcode(),
							adminDAO.truncateString(dto.getBookname(), 15), dto.getCheckout_date(), dto.getDue_date(),
							returnDateDisplay, dto.getBook_condition(), dto.getOverdue_date());
				}
			}
			System.out.println(LINE);

			String prevArrow = " ' < ' 이전페이지📚";
			String s = String.format("페이지 %d / %d", currentPage, totalPages);
			String nextArrow = "📚다음페이지 ' > '";

			System.out.println("\t" + prevArrow + "\t\t\t\t\t" + s + "\t\t\t\t" + nextArrow);
			System.out.println("🔎1.대출된 도서검색  \n🔎2.회원별 대출도서검색  \n🔎3.연체된도서검색 \n🔎4.도서반납관리(배가) \n (그 외 입력: 메뉴 종료) ");
			System.out.print(" 입력 : ");

			String memberChoice = scanner.nextLine();

			switch (memberChoice) {

			case "<":
				if (currentPage > 1) {
					currentPage--; // 이전 페이지로 이동
				} else {
					System.out.println("⚠️ 첫 번째 페이지입니다.");
				}
				break;
			case ">":
				if (currentPage < totalPages) {
					currentPage++; // 다음 페이지로 이동
				} else {
					System.out.println("⚠️ 마지막 페이지입니다.");
				}
				break;

			case "1":
				System.out.println(" 🤖 대출 도서 검색 페이지로 이동합니다.");
				System.out.println();
				this.loanbooksearchbybookcode(list);
				break;
			case "2":
				System.out.println(" 🤖 회원별 대출도서 검색 페이지로 이동합니다.");
				System.out.println();
				this.loanbooksearchbyusername(list);
				break;
			case "3":
				System.out.println(" 🤖 연체된 도서 확인 페이지로 이동합니다.");
				System.out.println();
				this.overdueloanbooklist();
				break;
			case "4":
				System.out.println(" 🤖 도서 반납 관리 페이지로 이동합니다.");
				System.out.println();
				this.loanbookbaega();
				break;
			default:
				System.out.println("📋 메뉴로 돌아갑니다. 📋");
				System.out.println();
				return;
			}

		}
	}

	public void loanbooksearchbybookcode(List<AdminDTO> currentList) {
		System.out.println("\n🔢 검색할 책번호(bookcode)를 입력하세요. ('0' 입력 시 이전 메뉴로 돌아갑니다.) => ");

		String inputLine = scanner.nextLine().trim();
		int s = 0;

		try {
			s = Integer.parseInt(inputLine);
		} catch (NumberFormatException e) {
			System.out.println("\n⛔ 잘못된 입력 형식입니다. 메뉴로 돌아갑니다.\n");
			this.showLoanBookandMemberInfo();
			return;
		}
		if (s == 0) {
			System.out.println("\n⬅️ 이전 메뉴로 돌아갑니다. \n");
			this.showLoanBookandMemberInfo();
			return;
		}

		AdminDTO selectedDto = adminDAO.loanbooksearchbybookcode(s);

		if (selectedDto == null) {
			System.out.println("⛔ 유효하지 않은 북코드입니다. 다시 입력해주세요.");
			this.loanbooksearchbybookcode(currentList);
			return;
		}

		String LINE = "====================================================================================================================================================";
		System.out.println("\n\t\t\t🔎 [ 검색 결과 : Bookcode " + s + "  ] \t\t\t\t\t");
		System.out.println(LINE);
		System.out.printf("| %-6s | %-4s | %-4s | %-28s\t| %-10s| %-8s| %-8s| %-8s | %-6s |\n", "유저이름", "대출번호", "북코드",
				"         책이름", "대출일", "반납예정일", "실제반납일", "도서상태", "연체일수");
		System.out.println(LINE);
		String returnDateDisplay = selectedDto.getReturn_date();
		if (returnDateDisplay == null || returnDateDisplay.isEmpty()) {
			returnDateDisplay = "　　　　　　　　　　";
		}
		System.out.printf("| %-6s | %-5d | %-5d | %-25s\t| %-10s | %-13s  | %-10s | %-8s | %-6d |\n",
				selectedDto.getUsername(), selectedDto.getLoancode(), selectedDto.getBookcode(),
				adminDAO.truncateString(selectedDto.getBookname(), 25), selectedDto.getCheckout_date(),
				selectedDto.getDue_date(), returnDateDisplay, selectedDto.getBook_condition(),
				selectedDto.getOverdue_date());
		System.out.println(LINE);

		if ("대출중".equals(selectedDto.getBook_condition())) {
			System.out.println("\n✨ 이 도서는 현재 **'대출중'** 상태입니다.");
			System.out.print("해당 도서를 바로 반납 처리하시겠습니까? [반납] 입력 (다른 키 입력 시 취소) => ");
			String confirmReturn = scanner.nextLine().trim();

			if (confirmReturn.equals("반납")) {
				this.loanbookreturn(selectedDto);
			} else {
				System.out.println("✅ 도서 반납 처리가 취소되었습니다.");
			}
		} else {
			System.out.println("\nℹ️ 이 도서는 현재 **'" + selectedDto.getBook_condition() + "'** 상태이므로 반납 처리를 할 수 없습니다.");
		}

		System.out.println("\n1. 다시 검색  0. 이전 메뉴로 돌아가기");
		System.out.print("입력 => ");
		String nextAction = scanner.nextLine().trim();

		if (nextAction.equals("1")) {
			this.loanbooksearchbybookcode(currentList);
		} else {
			this.showLoanBookandMemberInfo();
		}
	}

	public void loanbookreturn(AdminDTO loanInfo) {

		AdminDTO updateDto = new AdminDTO();
		updateDto.setBookcode(loanInfo.getBookcode());
		updateDto.setBook_condition("반납");

		try {
			int result = adminDAO.loanbookreturn(updateDto);
			if (result > 0) {
				System.out.println("🎉 북코드 " + loanInfo.getBookcode() + "번 도서의 반납 처리가 성공적으로 완료되었습니다.");

				if (loanInfo.getOverdue_date() > 0) {
					System.out.println("❗ 경고: 해당 도서는 " + loanInfo.getOverdue_date() + "일 연체 상태였습니다.");
				}
			} else {
				System.out.println("❌ 반납 처리에 실패했습니다.");
			}
		} catch (Exception e) {
			System.out.println("❌ 오류 발생: 반납 처리 중 문제가 발생했습니다. " + e.getMessage());
		}
	}

	public void loanbooksearchbyusername(List<AdminDTO> currentList) {
		System.out.println("\n🔢 검색할 유저의 이름을 입력하세요. ('0' 입력 시 이전 메뉴로 돌아갑니다.) => ");

		String inputLine = scanner.nextLine().trim();

		if ("0".equals(inputLine) || inputLine.isEmpty()) {
			System.out.println("이전 메뉴로 돌아갑니다.");
			return;
		}

		List<AdminDTO> list = adminDAO.loanbooksearchbyname(inputLine);

		if (list == null) {
			System.out.println("⛔ 유효하지 않은 유저입니다. 다시 입력해주세요.");
			this.loanbooksearchbyusername(currentList);
			return;
		}

		final int pageSize = 10;
		int currentPage = 1;

		int totalItems = list.size();
		int totalPages = (totalItems + pageSize - 1) / pageSize;

		while (true) {

			int startIdx = (currentPage - 1) * pageSize;
			int endIdx = Math.min(startIdx + pageSize, totalItems);

			String LINE = "============================================================================================================";
			System.out.println("\n\t\t\t\t🔎 [ 검색 결과 : 유저이름 ▶ " + inputLine + " ◀  ] \t\t\t\t\t");
			System.out.println(LINE);
			System.out.printf("| %-6s| %-5s| %-5s| %-20s\t| %-10s| %-8s | %-10s| %-8s|\n", "유저이름", "대출번호", "북코드",
					"         책이름", "대출일", "반납예정일", "도서상태", "연체일수");
			System.out.println(LINE);

			for (int i = startIdx; i < endIdx; i++) {
				AdminDTO dto = list.get(i);
				System.out.printf("| %-6s | %-5d | %-5d | %-20s\t| %-10s | %-10s | %-8s\t| %-8s |\n", dto.getUsername(),
						dto.getLoancode(), dto.getBookcode(), adminDAO.truncateString(dto.getBookname(), 15),
						dto.getCheckout_date(), dto.getDue_date(), dto.getBook_condition(), dto.getOverdue_date());
			}
			System.out.println(LINE);

			String prevArrow = " ' < ' 이전페이지📚";
			String s = String.format("페이지 %d / %d", currentPage, totalPages);
			String nextArrow = "📚다음페이지 ' > '";

			System.out.println("\t" + prevArrow + "\t\t\t\t\t" + s + "\t\t\t\t" + nextArrow);
			System.out.println("🔎1.대출된 도서검색  \n🔎2.회원별 대출도서검색  \n🔎3.연체된도서검색 \n🔎4.도서반납관리(배가) \n (그 외 입력: 메뉴 종료) ");
			System.out.print(" 입력 : ");

			String memberChoice = scanner.nextLine();

			switch (memberChoice) {

			case "<":
				if (currentPage > 1) {
					currentPage--; // 이전 페이지로 이동
				} else {
					System.out.println("⚠️ 첫 번째 페이지입니다.");
				}
				break;
			case ">":
				if (currentPage < totalPages) {
					currentPage++; // 다음 페이지로 이동
				} else {
					System.out.println("⚠️ 마지막 페이지입니다.");
				}
				break;
			default:
				System.out.println("📋 메뉴로 돌아갑니다. 📋");
				System.out.println();
				return;
			}
		}
	}

	public void loanbookbaega() {

		List<AdminDTO> list = adminDAO.returnbooklist();

		final int pageSize = 10;
		int currentPage = 1;

		int totalItems = list.size();
		int totalPages = (totalItems + pageSize - 1) / pageSize;

		String LINE = "=========================================================================================================";

		while (true) {

			int startIdx = (currentPage - 1) * pageSize;
			int endIdx = Math.min(startIdx + pageSize, totalItems);

			System.out.println("\n\t\t\t\t\t  📚 [ 반납 도서 관리 ] 📚\t\t\t\t\t");
			System.out.println(LINE);
			System.out.printf("\t\t\t\t\t  💡 반납된 도서 수: %d 개\n", list.size());
			System.out.println(LINE);

			System.out.printf("| %-2s\t |%-5s| %-5s| %-20s\t\t| %-10s| %-10s| %-6s | %-6s|\n", "유저이름", "대출번호", "북코드",
					"         책이름", "대출일", "실제반납일", "도서상태", "연체일수");
			System.out.println(LINE);

			if (list.isEmpty()) {
				System.out.println(String.format("| %-145s |", "     반납된 도서가 없습니다."));
			} else {
				for (int i = startIdx; i < endIdx; i++) {
					AdminDTO dto = list.get(i);
					String returnDateDisplay = dto.getReturn_date();

					if (returnDateDisplay == null || returnDateDisplay.isEmpty()) {
						returnDateDisplay = "          "; // 10칸 공백
					}

					System.out.printf("| %-2s\t| %-5d | %-5d | %-20s\t| %-10s | %-10s | %-8s | %-6d |\n",
							adminDAO.truncateString(dto.getUsername(), 3), dto.getLoancode(), dto.getBookcode(),
							adminDAO.truncateString(dto.getBookname(), 20), dto.getCheckout_date(), returnDateDisplay,
							dto.getBook_condition(), dto.getOverdue_date());
				}
			}
			System.out.println(LINE);

			String prevArrow = " ' < ' 이전페이지📚";
			String s = String.format("페이지 %d / %d", currentPage, totalPages);
			String nextArrow = "📚다음페이지 ' > '";

			System.out.println("  " + prevArrow + "\t\t\t\t" + s + "\t\t\t  " + nextArrow);
			System.out.println("🔎1.일괄배가  \n🔎2.도서별 배가 \n (그 외 입력: 메뉴 종료) ");
			System.out.print(" 입력 : ");

			String memberChoice = scanner.nextLine();

			switch (memberChoice) {

			case "<":
				if (currentPage > 1) {
					currentPage--; // 이전 페이지로 이동
				} else {
					System.out.println("⚠️ 첫 번째 페이지입니다.");
				}
				break;
			case ">":
				if (currentPage < totalPages) {
					currentPage++; // 다음 페이지로 이동
				} else {
					System.out.println("⚠️ 마지막 페이지입니다.");
				}
				break;

			case "1":
				System.out.println();
				this.returnbook_baega_all(null);
				break;
			case "2":
				System.out.println(" 🤖 도서별 배가를 실시합니다..");
				System.out.println();
				this.returnbook_baega();
				break;
			default:
				System.out.println("📋 메뉴로 돌아갑니다. 📋");
				System.out.println();
				return;
			}

		}

	}

	public void returnbook_baega_all(AdminDTO returnbook) {
		System.out.println(" 🤖 반납된 모든 도서의 배가를 실시합니다..");
		System.out.println(" ❗ 정말로 모든 도서의 배가를 진행하시겠습니까 ?");
		System.out.println(" '배가완료' 를 입력해주세요. (그 외 입력시 이전 메뉴로 돌아갑니다.) ");

		String adminchoice = scanner.nextLine().trim();

		if (adminchoice.equals("배가완료")) {
			try {
				adminDAO.returnbook_baega_all(returnbook);

				System.out.println();
				String childrenWithCart =
						// 빨간색 수레와 책
						"    O         ." + "📚책📚" + ".\n" + "   /|\\--------/\u2500\u2500\u2500\u2500\u2500\u2500\\ "
								+ " 끌고가는중... " + "\n"
								+ "    |        |\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500|   도서정리중...  \n"
								+ "   / \\       `O------O` \n";

				System.out.println(childrenWithCart);
				System.out.println("☑️ 모든 반납된 도서에 대한 배가 처리가 성공적으로 완료되었습니다.");
				System.out.println();

			} catch (Exception e) {
				System.out.println("오류가 발생하였습니다. 배가 업무가 취소되었습니다." + e.getMessage());
				return;
			}
		} else {
			System.out.println("배가 업무가 취소되었습니다.이전 메뉴로 돌아갑니다.");
			return;
		}

	}

	public void returnbook_baega() { // 도서코드를 입력받아 배가하기 (리스트 노출필요)

		System.out.println(" 🤖 반납된 모든 도서의 배가를 실시합니다..");
		System.out.println(" 🔢 배가를 진행 할 도서의 북코드를 입력해주세요. ");
		System.out.print(" 입력 : ");

		String inputLine = scanner.nextLine().trim();

		if (inputLine.isEmpty()) {
			System.out.println("\n⬅️ 이전 메뉴로 돌아갑니다. \n");
			return;
		}

		int bookCode;
		int result = 0;

		try {
			bookCode = Integer.parseInt(inputLine);
			result = adminDAO.returnbook_baega(bookCode);

			if (result > 0) {
				System.out.println("\n✅ [Book Code: " + bookCode + "] 도서의 배가 처리가 완료되었습니다. \n");
			} else {
				System.out.println(
						"\n⚠️ [Book Code: " + bookCode + "] 해당 북코드가 존재하지 않거나, 현재 배가 처리할 수 있는 '반납' 상태가 아닙니다.\n");
			}

		} catch (NumberFormatException e) {
			System.out.println("\n⛔ 잘못된 입력 형식입니다. 유효한 도서 코드를 숫자로 입력해주세요.\n");
			return;
		} catch (Exception e) {
			System.out.println("\n❌ 시스템 오류가 발생했습니다. 배가 업무가 취소되었습니다.\n");
			e.printStackTrace();
			return;
		}
		this.showLoanBookandMemberInfo();
	}

	public void overdueloanbooklist() {

		List<AdminDTO> list = adminDAO.overdueloanbooklist();

		int itemsPerPage = 10; // 10 개당 1페이지로 할당
		int totalItems = list.size();

		int totalPages = (totalItems == 0) ? 1 : (int) Math.ceil((double) totalItems / itemsPerPage);

		int currentPage = 1;

		while (true) {

			String LINE = "========================================================================================================";

			System.out.println("\n\t\t\t\t\t📚 [ 연체도서 관리 메뉴 ] 📚\t\t\t\t\t");
			System.out.println(LINE);
			System.out.printf("\t\t\t\t\t💡 연체중인 도서 수: %d 개\n", list.size());
			System.out.println(LINE);

			System.out.printf("| %-6s |%-4s | %-4s | %-20s | %-10s|%-10s| %-7s |%-6s|\n", "유저이름", "대출번호", "북코드",
					"     \t책이름", "대출일", "반납예정일", "도서상태", "연체일수");
			System.out.println(LINE);

			if (list.isEmpty()) {
				System.out.println(String.format("| %-145s |", "     연체중인 도서가 없습니다."));
			} else {

				int startIndex = (currentPage - 1) * itemsPerPage;
				int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

				List<AdminDTO> pageList = list.subList(startIndex, endIndex);

				for (AdminDTO dto : pageList) {
					System.out.printf("| %-6s | %-5d | %-5d | %-20s\t| %-10s | %-10s | %-8s| %-6d |\n",
							dto.getUsername(), dto.getLoancode(), dto.getBookcode(),
							adminDAO.truncateString(dto.getBookname(), 16), dto.getCheckout_date(), dto.getDue_date(),
							dto.getBook_condition(), dto.getOverdue_date());
				}
			}
			System.out.println(LINE);

			String s = String.format("페이지 %d / %d", currentPage, totalPages);

			System.out.println("  '<<' 처음으로  ' < ' 이전페이지📚           \t" + s + " \t        📚다음페이지 ' > '  마지막 '>>' ");
			System.out.println("  '0' 이전메뉴");
			System.out.print("  입력 : ");

			String pagechoice = scanner.nextLine().trim();

			switch (pagechoice) {
			case "<<": // 처음 페이지
				currentPage = 1;
				break;
			case ">>": // 마지막 페이지
				currentPage = totalPages;
				break;
			case "<": // 이전 페이지
				if (currentPage > 1) {
					currentPage--;
				}
				break;
			case ">": // 다음 페이지
				if (currentPage < totalPages) {
					currentPage++;
				}
				break;
			case "0":
				return;
			default:
				try {
					int pageNum = Integer.parseInt(pagechoice);
					if (pageNum >= 1 && pageNum <= totalPages) {
						currentPage = pageNum;
					} else {
						System.out.println("! 유효하지 않은 페이지 번호입니다. 잠시 후 다시 시도하세요.");
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
						}
					}
				} catch (NumberFormatException e) {
					System.out.println("! 잘못된 입력입니다. <<, <, >, >>, 0 또는 페이지 번호를 입력하세요.");
					try {
						Thread.sleep(1000);
					} catch (Exception e2) {
					}
				}
				break;
			}
		}

	}

	public void showsincheongmanage() {
		List<AdminDTO> list = adminDAO.sinchoengdaegidoseo();

		String LINE = "=========================================================================";

		System.out.println("\n\t\t\t📚 [ 도서 신청 관리 메뉴 ] 📚\t\t\t\t\t");
		System.out.println(LINE);
		System.out.printf("\t\t\t💡 현재까지 총 신청 도서 수: %d 건\n", list.size());
		System.out.println(LINE);

		System.out.println(String.format("|%-4s |\t\t       %-30s\t| %-4s |", "신청번호", "신청 도서", "상태"));
		System.out.println(LINE);

		if (list.isEmpty()) {
			System.out.println(String.format("| %-79s |", "     신청 내역이 없습니다."));
		} else {
			for (AdminDTO dto : list) {
				System.out.println(String.format("|  %-4s| %-35s \t| %-4s |", dto.getSincheongcode(),
						adminDAO.truncateString(dto.getSincheongbook(), 40), dto.getSincheongstatus()));
			}
		}
		System.out.println(LINE);

		this.sujeongsincheongstatus(list);

	}

	public void sujeongsincheongstatus(List<AdminDTO> currentList) {
		System.out.println("\n🔢 처리할 신청 번호를 입력하세요. ('0' 입력 시 이전 메뉴로 돌아갑니다.) => ");

		String inputLine = scanner.nextLine().trim();
		int s = 0;

		try {
			s = Integer.parseInt(inputLine);
		} catch (NumberFormatException e) {
			System.out.println("\n⛔ 잘못된 입력 형식입니다. 메뉴로 돌아갑니다.\n");
			this.showMenu();
			return;
		}
		if (s == 0) {
			System.out.println("\n⬅️ 이전 메뉴로 돌아갑니다. \n");
			this.showMenu();
			return;
		}

		AdminDTO selectedDto = null;
		for (AdminDTO dto : currentList) {
			if (s == dto.getSincheongcode()) {
				selectedDto = dto;
				break;
			}
		}

		if (selectedDto == null) {
			System.out.println("⛔ 유효하지 않은 신청 번호입니다. 다시 입력해주세요.");
			this.sujeongsincheongstatus(currentList);
			return;
		}

		String newStatus = "";

		System.out.println("\n[신청 도서: " + selectedDto.getSincheongbook() + "]");
		System.out.print("선택하신 ▶ " + selectedDto.getSincheongbook() + " ◀ 도서의 상태를 변경하시겠습니까? [Y = 승인, N = 반려] => ");

		String confirm = scanner.nextLine().trim();

		if (confirm.equalsIgnoreCase("Y")) {
			newStatus = "승인";
		} else if (confirm.equalsIgnoreCase("N")) {
			newStatus = "반려";
		} else {
			System.out.println("⚠️ Y 또는 N만 입력해야 합니다. 상태 변경이 취소되었습니다.");
			this.sujeongsincheongstatus(currentList);
			return;
		}

		AdminDTO updateDto = new AdminDTO();
		updateDto.setSincheongcode(s);
		updateDto.setSincheongstatus(newStatus);

		try {
			int result = adminDAO.sujeongsincheongstatus(updateDto);
			if (result > 0) {
				System.out.println("🎉 신청 번호 " + s + "번의 상태가 '" + newStatus + "'(으)로 성공적으로 변경되었습니다.");
			} else {
				System.out.println("❌ 상태 변경에 실패했습니다.");
			}
		} catch (SQLException e) {
			System.out.println("❌ 오류 발생: " + e.getMessage().split("\n")[0]);
		}

		System.out.println("\n🔄 변경된 신청 목록을 다시 표시합니다.");
		this.showsincheongmanage();
	}

	public void noticeadmin() {
		list = adminDAO.notice();

		final int pageSize = 10;
		int currentPage = 1;

		int totalItems = list.size();
		int totalPages = (totalItems == 0) ? 1 : (totalItems + pageSize - 1) / pageSize;

		System.out.println();
		String LINE = "================================================================================";

		while (true) {

			if (currentPage < 1)
				currentPage = 1;
			if (currentPage > totalPages)
				currentPage = totalPages;

			int startIdx = (currentPage - 1) * pageSize;
			int endIdx = Math.min(startIdx + pageSize, totalItems);

			System.out.printf("\t\t\t\t💡 공지사항 목록\n");
			System.out.println(LINE);
			System.out.println(String.format("|%-4s|\t\t%-30s\t\t\t| %-4s|", "번호", "공지사항", "    일자    "));
			System.out.println("--------------------------------------------------------------------------------");

			if (list.isEmpty()) {
				System.out.println(String.format("|%-26s\t\t|", "\t\t\t등록된 공지사항이 없습니다.\t\t\t"));
			} else {
				for (int i = startIdx; i < endIdx; i++) {
					AdminDTO dto = list.get(i);
					System.out.println(String.format("| %-3s| %-40s\t| %-4s |", dto.getNoticeId(),
							adminDAO.truncateString(dto.getNoticeTitle(), 40), dto.getNoticeDate()));
				}
			}
			System.out.println(LINE);

			String prevArrow = " ' < ' 이전페이지📚";
			String s = String.format("페이지 %d / %d", currentPage, totalPages);
			String nextArrow = "📚다음페이지 ' > '";

			System.out.println(" " + prevArrow + "\t\t" + s + "\t\t\t" + nextArrow);
			System.out.println();
			System.out.println("📔 메뉴 선택: \n[ 등록 ] 공지 등록 \n[ 공지번호 ] 확인 및 수정/삭제 \n[ 0 ] 이전 메뉴로 돌아가기 ");
			System.out.print(" 입력 : ");

			while (true) {

				String memberChoice = scanner.nextLine().trim();

				if (memberChoice.equals("<")) {
					if (currentPage > 1) {
						currentPage--;
						break;
					} else {
						System.out.println("⚠️ 첫 번째 페이지입니다.");
						continue;
					}
				} else if (memberChoice.equals(">")) {
					if (currentPage < totalPages) {
						currentPage++;
						break;
					} else {
						System.out.println("⚠️ 마지막 페이지입니다.");
						continue;
					}
				} else if (memberChoice.equalsIgnoreCase("등록")) {
					System.out.println("\n📢 공지사항 등록 화면으로 이동합니다.");
					this.noticeinsert();

					list = adminDAO.notice();
					totalItems = list.size();
					totalPages = (totalItems == 0) ? 1 : (totalItems + pageSize - 1) / pageSize;
					break;

				} else if (memberChoice.equals("0")) {
					System.out.println("\n⬅️ 이전 메뉴로 돌아갑니다.");
					System.out.println();
					return;

				} else {
					try {
						int noticeId = Integer.parseInt(memberChoice);

						boolean isValidId = false;

						for (AdminDTO dto : list) {
							if (dto.getNoticeId() == noticeId) {
								isValidId = true;
								break;
							}
						}
						if (isValidId) {
							System.out.println("\n✏️ 공지 번호 " + noticeId + "번 수정/삭제 화면으로 이동합니다.");
							this.noticeUpdate(noticeId);
							break;
						} else {
							System.out.println("🚨 유효하지 않은 공지 번호입니다. 다시 입력해주세요.");
						}
					} catch (NumberFormatException e) {
						System.out.println("🚨 잘못된 입력입니다. '등록', 공지번호, 또는 '0'을 입력해주세요.");
					}
				}
			}
		}
	}

	public void noticeinsert() { // '등록'으로 들어와 공지사항 등록하기

		System.out.println("등록할 공지 제목을 입력해주세요 \n 제목 : ");
		String newTitle = scanner.nextLine().trim();

		System.out.println("등록할 공지 내용을 입력해주세요 \n 내용 : ");
		String newContent = scanner.nextLine().trim();

		if (newTitle.isEmpty() && newContent.isEmpty()) {
			System.out.println("\n✅ 입력된 내용이 없어 공지사항 등록이 취소되었습니다.");
			this.noticeadmin();
			return;
		}

		AdminDTO insertdto = new AdminDTO();
		insertdto.setNoticeTitle(newTitle);
		insertdto.setNoticeContent(newContent);

		try {
			int result = adminDAO.noticeInsert(insertdto);

			if (result > 0) {
				System.out.println("🎉 성공적으로 " + insertdto.getNoticeTitle() + " 공지가 \n\t 등록되었습니다.");
			} else {
				System.out.println("❌ 공지사항 등록에 실패했습니다.");
			}
		} catch (Exception e) {
			System.out.println("❌ 오류 발생: 공지사항 등록 중 문제가 발생했습니다. " + e.getMessage());
		} finally {
			this.noticeadmin();
		}

	}

	public void noticeUpdate(int noticeId) { // 공지사항 수정 UI진입
		AdminDTO selectedNotice = adminDAO.selectNoticeById(noticeId);

		if (selectedNotice == null) {
			System.out.println("🚨 오류: 해당 번호의 공지사항 정보를 찾을 수 없습니다.");
			return;
		}

		System.out.println("\n=======================================================");
		System.out.printf("\t\t📢 공지사항 상세 정보 (No. %d)\n", selectedNotice.getNoticeId());
		System.out.println("=======================================================");
		System.out.printf("\t\t\t\t       작성일: %s\n", selectedNotice.getNoticeDate());
		System.out.println("-------------------------------------------------------");
		System.out.printf("제목: %s\n", selectedNotice.getNoticeTitle());
		System.out.println("-------------------------------------------------------");
		System.out.println("내용:");
		System.out.println(selectedNotice.getNoticeContent());
		System.out.println("=======================================================");

		while (true) {
			System.out.println("✅ 메뉴 선택: [ 1 ] 수정, [ 2 ] 삭제, [ 0 ] 목록으로 돌아가기");
			System.out.print("선택 입력 > ");

			String choice = scanner.nextLine().trim();

			if (choice.equals("1")) {
				System.out.println("\n✏️ 공지사항 수정 화면으로 이동합니다.");
				this.NoticeModify(noticeId);
				return;

			} else if (choice.equals("2")) {
				System.out.println("\n❌ 공지사항 삭제를 진행합니다.");
				this.NoticeDelete(noticeId);
				return;

			} else if (choice.equals("0")) {
				System.out.println("\n⬅️ 공지사항 목록으로 돌아갑니다.");
				return;

			} else {
				System.out.println("🚨 잘못된 입력입니다. 0, 1, 2 중 하나를 입력해주세요.");
			}
		}
	}

	private void NoticeModify(int noticeId) { // 공지사항 수정
		AdminDTO selectedNotice = adminDAO.selectNoticeById(noticeId);

		if (selectedNotice == null) {
			System.out.println("🚨 오류: 해당 번호의 공지사항 정보를 찾을 수 없습니다.");
		}

		System.out.println("수정할 공지 제목을 입력해주세요");
		System.out.println("제목 : ");
		String newTitle = scanner.nextLine().trim();

		System.out.println("수정할 공지 내용을 입력해주세요");
		System.out.println("내용 : ");
		String newContent = scanner.nextLine().trim();

		if (newTitle.isEmpty() && newContent.isEmpty()) {
			System.out.println("\n✅ 입력된 내용이 없어 공지사항 수정이 취소되었습니다.");
			this.noticeadmin();
			return;
		}

		AdminDTO updatedto = new AdminDTO();
		updatedto.setNoticeId(noticeId);

		if (newTitle.isEmpty()) {
			updatedto.setNoticeTitle(selectedNotice.getNoticeTitle());
		} else {
			updatedto.setNoticeTitle(newTitle);
		}

		if (newContent.isEmpty()) {
			updatedto.setNoticeContent(selectedNotice.getNoticeContent());
		} else {
			updatedto.setNoticeContent(newContent);
		}

		try {
			int result = adminDAO.noticeUpdate(updatedto);

			if (result > 0) {
				System.out.println("🎉 성공적으로 " + selectedNotice.getNoticeTitle() + " 공지가 \n\t 수정되었습니다.");
			} else {
				System.out.println("❌ 공지사항 수정에 실패했습니다.");
			}
		} catch (Exception e) {
			System.out.println("❌ 오류 발생: 공지사항 수정 중 문제가 발생했습니다. " + e.getMessage());

		}

	}

	public void NoticeDelete(int noticeId) { // 공지사항 삭제

		AdminDTO selectedNotice = adminDAO.selectNoticeById(noticeId);

		System.out.println("\n 🚨 정말로 > " + selectedNotice.getNoticeTitle() + " < 공지를 삭제하시겠습니까 ? ⚠️");
		System.out.print(" 삭제하려면 [Y]를 입력하세요. (다른 키 입력 시 취소) : ");

		String confirmDelete = scanner.nextLine().trim();

		if (!confirmDelete.equalsIgnoreCase("y")) {
			System.out.println("✅ 공지사항 삭제가 취소되었습니다.");
			return;
		}

		try {
			int result = adminDAO.noticeDelete(noticeId);
			if (result > 0) {
				System.out.println("🎉 성공적으로 " + selectedNotice.getNoticeTitle() + " 공지가 삭제되었습니다.");
			} else {
				System.out.println("❌ 공지사항 삭제에 실패했습니다.");
			}
		} catch (Exception e) {
			System.out.println("❌ 오류 발생: 공지사항 삭제 중 문제가 발생했습니다. " + e.getMessage());
		}
	}

}
