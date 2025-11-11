package library;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdminUI2 {

	private AdminDAO2 dao = new AdminDAOImpl2();

	Scanner sc = new Scanner(System.in);

	private List<AdminDTO2> list;

	public void showAdminmenu() {

		boolean isAdminRunning = true;

		while (isAdminRunning) {
			System.out.println("3. 대출 및 반납 관리");
			System.out.println("4. 신청 도서 관리");
			System.out.println("5. 공지사항 등록");
			System.out.println("6. 관리자 로그아웃");
			System.out.println("7. 시스템 종료");
			System.out.println("--------------------");
			System.out.print("회원 관리 메뉴 선택: ");

			String memberChoice = sc.nextLine();

			switch (memberChoice) {
			case "3":
				System.out.println(" 🤖 도서 대출/반납 관리 페이지로 이동합니다.");
				System.out.println();
				this.showLoanBookandMemberInfo();
				break;
			case "4":
				System.out.println(" 📚 신청 도서 목록으로 이동합니다.");
				System.out.println();
				this.showsincheongmanage();
				break;
			case "5":
				System.out.println();
				System.out.println("📢 공지사항 관리 페이지로 이동합니다.");
				System.out.println();
				this.noticeadmin();
				break;
			case "6":
				System.out.println(" 📋 메인 화면으로 돌아갑니다. ");
				isAdminRunning = false;
				break;
			case "7": // 시스템 종료
				System.out.println("❗정말 시스템을 종료하시겠습니까 ? [Y/N]");

				String shutdown;
				do {
					shutdown = sc.next();
					sc.nextLine();

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

				this.showAdminmenu();
				break;

			default:
				System.out.println(">> 잘못된 입력입니다. 1~7 사이의 숫자를 입력해주세요.");
				System.out.println();
				this.showAdminmenu();
				break;
			}
		}
	}

	public void showLoanBookandMemberInfo() { // 3.대출/반납 관리
		List<AdminDTO2> list = dao.loanbooklist();

		String LINE = "====================================================================================================================================================";

		System.out.println("\n\t\t\t📚 [ 대출/반납 관리 메뉴 ] 📚\t\t\t\t\t");
		System.out.println(LINE);
		System.out.printf("\t\t\t💡 대출중인 도서 수: %d 개\n", list.size());
		System.out.println(LINE);

		System.out.printf("| %-6s | %-4s | %-4s | %-40s| %-10s| %-10s| %-10s| %-8s | %-6s |\n", "유저이름", "대출번호", "북코드",
				"         책이름", "대출일", "반납예정일", "실제반납일", "도서상태", "연체일수");
		System.out.println(LINE);

		// 5. 데이터 출력
		if (list.isEmpty()) {
			// 테이블 포맷에 맞추어 중앙에 메시지 출력
			System.out.println(String.format("| %-145s |", "     대출중인 도서가 없습니다."));
		} else {
			for (AdminDTO2 dto : list) {
				String returnDateDisplay = dto.getReturn_date();
				if (returnDateDisplay == null || returnDateDisplay.isEmpty()) {
					returnDateDisplay = "          "; // 10칸 공백
				}
				System.out.printf("| %-6s | %-5d | %-5d | %-40s| %-10s | %-10s | %-10s | %-8s | %-6d |\n",
						dto.getUsername(), dto.getLoancode(), dto.getBookcode(),
						dao.truncateString(dto.getBookname(), 10), dto.getCheckout_date(), dto.getDue_date(),
						returnDateDisplay, dto.getBook_condition(), dto.getOverdue_date());
			}
		}
		System.out.println(LINE);

		System.out.println();
		System.out.println(" 1.대출된 도서검색  2.회원별 대출도서검색  3.연체된도서검색 ");
		System.out.println("  ' < ' 이전페이지📚             📚다음페이지 ' > ' ");
		System.out.println(" 입력 =>  ");

		String memberChoice = sc.nextLine();

		switch (memberChoice) {
		case "1":
			System.out.println(" 🤖 대출 도서 검색 페이지로 이동합니다.");
			System.out.println();
			this.loanbooksearchbybookcode(list);
			break;
		case "2":
			System.out.println(" 🤖 회원별 대출도서 검색 페이지로 이동합니다.");
			System.out.println();
			break;
		case "3":
			System.out.println(" 🤖 연체된 도서 확인 페이지로 이동합니다.");
			System.out.println();
			break;
		default:
			System.out.println("잘못된 입력입니다. 메뉴로 돌아갑니다.");
			System.out.println();
			break;
		}
	}

//	 배가업무용...
//	String childrenWithCart = 
//            // 빨간색 수레와 책
//              "    O         ."+ "📚책📚" + ".\n"  
//            + "   /|\\--------/\u2500\u2500\u2500\u2500\u2500\u2500\\ "+ " 끌고가는중... " + "\n" 
//            + "    |        |\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500| + " 도서정리중... " + \n" 
//            + "   / \\       `O------O` \n";
//        
//        System.out.println(childrenWithCart);	

	public void loanbooksearchbybookcode(List<AdminDTO2> currentList) {
		System.out.println("\n🔢 검색할 책번호(bookcode)를 입력하세요. ('0' 입력 시 이전 메뉴로 돌아갑니다.) => ");

		String inputLine = sc.nextLine().trim();
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

		AdminDTO2 selectedDto = dao.loanbooksearchbybookcode(s);

		if (selectedDto == null) {
			System.out.println("⛔ 유효하지 않은 북코드입니다. 다시 입력해주세요.");
			this.loanbooksearchbybookcode(currentList);
			return;
		}

		String LINE = "====================================================================================================================================================";
		System.out.println("\n\t\t\t🔎 [ 검색 결과 : Bookcode " + s + "  ] \t\t\t\t\t");
		System.out.println(LINE);
		System.out.printf("| %-6s | %-4s | %-4s | %-40s| %-10s| %-10s| %-10s| %-8s | %-6s |\n", "유저이름", "대출번호", "북코드",
				"         책이름", "대출일", "반납예정일", "실제반납일", "도서상태", "연체일수");
		System.out.println(LINE);
		String returnDateDisplay = selectedDto.getReturn_date();
		if (returnDateDisplay == null || returnDateDisplay.isEmpty()) {
			returnDateDisplay = "          "; // 10칸 공백
		}
		System.out.printf("| %-6s | %-5d | %-5d | %-40s| %-10s | %-10s | %-10s | %-8s | %-6d |\n",
				selectedDto.getUsername(), selectedDto.getLoancode(), selectedDto.getBookcode(),
				dao.truncateString(selectedDto.getBookname(), 40), selectedDto.getCheckout_date(), selectedDto.getDue_date(),
				returnDateDisplay, selectedDto.getBook_condition(), selectedDto.getOverdue_date());
		System.out.println(LINE);
		
		if ("대출중".equals(selectedDto.getBook_condition())) {
	        System.out.println("\n✨ 이 도서는 현재 **'대출중'** 상태입니다.");
	        System.out.print("해당 도서를 바로 반납 처리하시겠습니까? [반납] 입력 (다른 키 입력 시 취소) => ");
	        String confirmReturn = sc.nextLine().trim();
	        
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
	    String nextAction = sc.nextLine().trim();
	    
	    if(nextAction.equals("1")) {
	        this.loanbooksearchbybookcode(currentList); 
	    } else {
	        this.showLoanBookandMemberInfo();
	    }

	}

	public void loanbookreturn(AdminDTO2 loanInfo) {
	    
	    AdminDTO2 updateDto = new AdminDTO2();
	    updateDto.setBookcode(loanInfo.getBookcode());
	    updateDto.setBook_condition("반납"); 
	    
	    try {
	        int result = dao.loanbookreturn(updateDto);
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

	public void showsincheongmanage() {
		List<AdminDTO2> list = dao.sinchoengdaegidoseo();

		String LINE = "=========================================================================";

		System.out.println("\n\t\t\t📚 [ 도서 신청 관리 메뉴 ] 📚\t\t\t\t\t");
		System.out.println(LINE);
		System.out.printf("\t\t\t💡 현재까지 총 신청 도서 수: %d 건\n", list.size());
		System.out.println(LINE);

		System.out.println(String.format("|%-4s |\t\t\t%-30s| %-4s |", "신청번호", "신청 도서", "상태"));
		System.out.println(LINE);

		if (list.isEmpty()) {
			System.out.println(String.format("| %-79s |", "     신청 내역이 없습니다."));
		} else {
			for (AdminDTO2 dto : list) {
				System.out.println(String.format("|  %-4s| %-30 \t| %-4s |", dto.getSincheongcode(),
						dao.truncateString(dto.getSincheongbook(), 10), dto.getSincheongstatus()));
			}
		}
		System.out.println(LINE);

		this.sujeongsincheongstatus(list);

	}

	public void sujeongsincheongstatus(List<AdminDTO2> currentList) {
		System.out.println("\n🔢 처리할 신청 번호를 입력하세요. ('0' 입력 시 이전 메뉴로 돌아갑니다.) => ");

		String inputLine = sc.nextLine().trim();
		int s = 0;

		try {
			s = Integer.parseInt(inputLine);
		} catch (NumberFormatException e) {
			System.out.println("\n⛔ 잘못된 입력 형식입니다. 메뉴로 돌아갑니다.\n");
			this.showAdminmenu();
			return;
		}
		if (s == 0) {
			System.out.println("\n⬅️ 이전 메뉴로 돌아갑니다. \n");
			this.showAdminmenu();
			return;
		}

		AdminDTO2 selectedDto = null;
		for (AdminDTO2 dto : currentList) {
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

		String confirm = sc.nextLine().trim();

		if (confirm.equalsIgnoreCase("Y")) {
			newStatus = "승인";
		} else if (confirm.equalsIgnoreCase("N")) {
			newStatus = "반려";
		} else {
			System.out.println("⚠️ Y 또는 N만 입력해야 합니다. 상태 변경이 취소되었습니다.");
			this.sujeongsincheongstatus(currentList);
			return;
		}

		AdminDTO2 updateDto = new AdminDTO2();
		updateDto.setSincheongcode(s);
		updateDto.setSincheongstatus(newStatus);

		try {
			int result = dao.sujeongsincheongstatus(updateDto);
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
		list = dao.notice();

		System.out.println();
		String LINE = "================================================================================";

		System.out.printf("\t\t\t\t💡 공지사항 목록\n");

		System.out.println(LINE);

		System.out.println(String.format("|%-4s|\t\t\t%-25s\t\t| %-4s|", " 번호", "    공지사항", "    일자    "));
		System.out.println("--------------------------------------------------------------------------------");

		if (list.isEmpty()) {
			System.out.println(String.format("|%-26s\t\t|", "\t\t\t등록된 공지사항이 없습니다.\t\t\t"));
		} else {
			for (AdminDTO2 dto : list) {
				System.out.println(String.format("| %-3s| %-45s\t| %-4s |", dto.getNoticeId(),
						dao.truncateString(dto.getNoticeTitle(), 25), dto.getNoticeDate()));
			}
		}
		System.out.println(LINE);

		while (true) {
			System.out.println("📔 메뉴 선택: [ 등록 ] 공지 등록, [ 공지번호 ] 확인 및 수정/삭제, [ 0 ] 이전 메뉴로 돌아가기");
			System.out.print("선택 입력 > ");

			String input = sc.nextLine().trim();

			if (input.equalsIgnoreCase("등록")) {
				System.out.println("\n📢 공지사항 등록 화면으로 이동합니다.");
				this.noticeinsert();
				break;

			} else if (input.equals("0")) {
				System.out.println("\n⬅️ 이전 메뉴로 돌아갑니다.");
				System.out.println();
				return;

			} else {
				try {
					int noticeId = Integer.parseInt(input);

					boolean isValidId = false;

					for (AdminDTO2 dto : list) {
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
		this.noticeadmin();
	}

	public void noticeinsert() { // '등록'으로 들어와 공지사항 등록하기

		System.out.println("등록할 공지 제목을 입력해주세요 \n 제목 : ");
		String newTitle = sc.nextLine().trim();

		System.out.println("등록할 공지 내용을 입력해주세요 \n 내용 : ");
		String newContent = sc.nextLine().trim();

		if (newTitle.isEmpty() && newContent.isEmpty()) {
			System.out.println("\n✅ 입력된 내용이 없어 공지사항 등록이 취소되었습니다.");
			this.noticeadmin();
			return;
		}

		AdminDTO2 insertdto = new AdminDTO2();
		insertdto.setNoticeTitle(newTitle);
		insertdto.setNoticeContent(newContent);

		try {
			int result = dao.noticeInsert(insertdto);

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
		AdminDTO2 selectedNotice = dao.selectNoticeById(noticeId);

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

			String choice = sc.nextLine().trim();

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
		AdminDTO2 selectedNotice = dao.selectNoticeById(noticeId);

		if (selectedNotice == null) {
			System.out.println("🚨 오류: 해당 번호의 공지사항 정보를 찾을 수 없습니다.");
		}

		System.out.println("수정할 공지 제목을 입력해주세요");
		System.out.println("제목 : ");
		String newTitle = sc.nextLine().trim();

		System.out.println("수정할 공지 내용을 입력해주세요");
		System.out.println("내용 : ");
		String newContent = sc.nextLine().trim();

		if (newTitle.isEmpty() && newContent.isEmpty()) {
			System.out.println("\n✅ 입력된 내용이 없어 공지사항 수정이 취소되었습니다.");
			this.noticeadmin();
			return;
		}

		AdminDTO2 updatedto = new AdminDTO2();
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
			int result = dao.noticeUpdate(updatedto);

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

		AdminDTO2 selectedNotice = dao.selectNoticeById(noticeId);

		System.out.println("\n 🚨 정말로 > " + selectedNotice.getNoticeTitle() + " < 공지를 삭제하시겠습니까 ? ⚠️");
		System.out.print(" 삭제하려면 [Y]를 입력하세요. (다른 키 입력 시 취소) : ");

		String confirmDelete = sc.nextLine().trim();

		if (!confirmDelete.equalsIgnoreCase("y")) {
			System.out.println("✅ 공지사항 삭제가 취소되었습니다.");
			return;
		}

		try {
			int result = dao.noticeDelete(noticeId);
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
