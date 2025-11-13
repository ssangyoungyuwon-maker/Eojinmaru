package library;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AdminUI {
	Scanner scanner = new Scanner(System.in);
	
	private AdminDAO adminDAO = new AdminDAOImpl();
	private AdminDAOImpl adminDAOImpl = new AdminDAOImpl();
    
    public void showMenu() {
        
        boolean isAdminRunning = true; 

        while (isAdminRunning) {
           
            System.out.println("\n===== [관리자 화면] =====");
            System.out.println("1. 회원관리");
            System.out.println("2. 도서관리");
            System.out.println("3. 대출/반납");
            System.out.println("4. 신청 도서");
            System.out.println("5. 공지사항 등록");
            System.out.println("6. 로그아웃"); 
            System.out.println("7. 프로그램 종료");
            System.out.println("=========================");
            System.out.print("메뉴를 선택하세요: ");

            String adminChoice = scanner.nextLine();

            switch (adminChoice) {
                case "1":
                    this.showMemberMenu();
                    break;

                case "2":
                    this.showBookMenu(); 
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
                    isAdminRunning = false; 
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

    private void showMemberMenu() {
        boolean isMemberMenuRunning = true;
        while (isMemberMenuRunning) {
            System.out.println("\n--- [1. 회원 관리] ---");
            System.out.println("1. 아이디 검색");
            System.out.println("2. 이름 검색");
            System.out.println("3. 회원 삭제");
            System.out.println("4. 연체 회원");
            System.out.println("5. 전체 리스트");
            System.out.println("6. 뒤로가기"); 
            System.out.println("--------------------");
            System.out.print("회원 관리 메뉴 선택: ");
            
            String memberChoice = scanner.nextLine();
            switch (memberChoice) {
            
            // 아이디 검색
            case "1":
                System.out.print(">> 검색할 회원의 아이디를 입력하세요: ");
                String id = scanner.nextLine();
                MemberDTO user = adminDAO.findUserById(id);
                if (user != null) {
                    System.out.println("--- 검색 결과 (1건) ---");
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
                    System.out.println("--- 검색 결과 (" + nameList.size() + "건) ---");
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
                
                System.out.print(">> 정말로 '" + userToDel.getUser_name() + "(" + userToDel.getUser_Id() + ")' 님을 삭제하시겠습니까? (Y/N): ");
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
                             
            // 연체 회원
            case "4":
                System.out.println(">> (구현예정) 연체 회원 목록을 조회합니다.");
                break;
                
            // 전체 리스트
            case "5":
                List<MemberDTO> allList = adminDAO.findAllUsers();
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
                System.out.println(">> 잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요.");
                break;
            }
        }
    }

    private void printUserList(List<MemberDTO> users) {
        if (users == null || users.isEmpty()) {
            return; 
        }

        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s | %-12s | %-8s | %-12s | %-13s | %-20s | %-15s\n",
                "코드", "아이디", "이름", "생년월일", "전화번호", "이메일", "주소");
        System.out.println("--------------------------------------------------------------------------------------------------");

        for (MemberDTO user : users) {
            System.out.printf("%-5d | %-12s | %-8s | %-12s | %-13s | %-20s | %-15s\n",
                    user.getUser_code(),
                    user.getUser_Id(),
                    user.getUser_name(),
                    user.getUser_birth(), 
                    user.getUser_tel(),
                    user.getUser_email(),
                    user.getUser_address());
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }
    
    private void showBookMenu() {
        boolean isBookMenuRunning = true;
        while (isBookMenuRunning) {
            System.out.println("\n--- [2. 도서 관리] ---");
            System.out.println("1. 도서 전체 리스트");
            System.out.println("2. 도서 검색");
            System.out.println("3. 폐기 도서 확인");
            System.out.println("4. 폐기 도서 등록");
            System.out.println("5. 신상 도서 등록"); 
            System.out.println("6. 도서 삭제"); 
            System.out.println("7. 뒤로가기");
            System.out.println("--------------------");
            System.out.print("도서 관리 메뉴 선택: ");

            String bookChoice = scanner.nextLine();
            switch (bookChoice) {
            
            	// 도서 전체 리스트
             case "1": {
                    List<BookInfoDTO1> allBooks = adminDAO.findAllBooks();
                    if (allBooks.isEmpty()) {
                        System.out.println(">> 등록된 도서가 없습니다.");
                    } else {
                        System.out.println("--- 전체 도서 목록 (" + allBooks.size() + "건) ---");
                        printBookList(allBooks);
                    }
                    break; }
                	
                // 도서 검색
                case "2": {
                    System.out.print(">> 검색할 도서명(전체 또는 일부)을 입력하세요: ");
                    String bookName = scanner.nextLine();
                    List<BookInfoDTO1> bookList = adminDAO.findBooksByName(bookName);
                    if (bookList.isEmpty()) {
                        System.out.println(">> 해당 도서명의 도서를 찾을 수 없습니다.");
                    } else {
                        System.out.println("--- 도서 검색 결과 (" + bookList.size() + "건) ---");
                        printBookList(bookList);
                    }
                    break; }
                	
                // 폐기 도서 확인
                case "3": {
                    List<DisposedBookDTO> disposedList = adminDAO.findAllDisposedBooks();
                    if (disposedList.isEmpty()) {
                        System.out.println(">> 폐기 등록된 도서가 없습니다.");
                    } else {
                        System.out.println("--- 폐기 도서 목록 (" + disposedList.size() + "건) ---");
                        printDisposedBookList(disposedList); // <-- 새로운 헬퍼 메서드 호출
                    }
                    break; }
                
                // 폐기 도서 등록
                case "4": {
                	System.out.println("\n--- [4. 폐기 도서 등록] ---");
                    System.out.print(">> 폐기할 도서의 **코드(BOOK_CODE)**를 입력하세요: ");
                    String disposeCodeInput = scanner.nextLine();
                    int disposeBookCode;
                    
                    try {
                        disposeBookCode = Integer.parseInt(disposeCodeInput);
                    } catch (NumberFormatException e) {
                        System.out.println(">> 잘못된 입력입니다. 도서 코드는 숫자만 입력할 수 있습니다.");
                        break;
                    } 
                    
                    // 폐기 등록 전, 도서가 book (재고) 테이블에 존재하는지 확인
                    BookInfoDTO1 bookToDispose = adminDAO.findBookByCode(disposeBookCode);
                    
                    if (bookToDispose == null) {
                        System.out.println(">> 해당 도서 코드(" + disposeCodeInput + ")의 도서가 'book' 테이블에 존재하지 않습니다.");
                        break;
                    }

                    System.out.print(">> 도서 '" + bookToDispose.getBookName() + "'의 폐기 사유를 입력하세요 (파손, 분실 등): ");
                    String reason = scanner.nextLine();

                    System.out.print(">> 정말로 '" + bookToDispose.getBookName() + "' 도서를 폐기 등록하시겠습니까? (y/n): ");
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
                	break; }
                	
                // 신상 도서 등록
                case "5": {
                	System.out.println("\n--- [5. 신상 도서 등록] ---");
                    BookInfoDTO1 newBook = new BookInfoDTO1();
                   
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
                	break; }
                
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
                    BookInfoDTO1 bookToDelete = adminDAO.findBookByCode(deleteBookCode);
                    
				
                    // 2. 폐기 기록 테이블(disposedbook)에서 검색
                    DisposedBookDTO disposedToDelete = adminDAOImpl.findDisposedBookByCode(deleteBookCode);
                    
                    // --- 삭제 대상 결정 로직 ---
                    if (bookToDelete != null) {
                        // 1. 재고(book)에 있는 경우 -> 재고 삭제 (기존 case 6 로직)
                        System.out.print(">> (재고) 정말로 도서 '" + bookToDelete.getBookName() + "(" + deleteBookCode + ")'를 영구 삭제하시겠습니까? (y/n): ");
                        String confirmDel = scanner.nextLine();
                        
                        if (confirmDel.equalsIgnoreCase("y")) {
                            boolean isDeleted = adminDAO.deleteBookByCode(deleteBookCode);
                            if (isDeleted) {
                                System.out.println(">> 재고 도서 정보가 성공적으로 삭제되었습니다.");
                                break;
                            } else {
                                System.out.println(">> 재고 도서 삭제에 실패하였습니다.");
                                return;
                            }
                        } else {
                            System.out.println(">> 도서 삭제를 취소하였습니다.");
                            return;
                        }
                    
                    } else if (disposedToDelete != null) {
                        // 2. 재고에 없고 폐기 기록(disposedbook)에 있는 경우 -> 폐기 기록 삭제
                        String bookName = (disposedToDelete.getBookName() == null ? "제목 없음" : disposedToDelete.getBookName());
                        
                        System.out.print(">> (폐기 기록) 정말로 도서 '" + bookName + "(" + deleteBookCode + ")'의 기록을 영구 삭제하시겠습니까? (y/n): ");
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
                	break; }
                 
                	
                case "7": {
                	isBookMenuRunning = false; 
                	break; }
                    
                default: {
                	System.out.println(">> 잘못된 입력입니다. 1~7 사이의 숫자를 입력해주세요."); 
                	break; }
            
            }
        }
    }
    
    
    
    
    private void printBookList(List<BookInfoDTO1> books) {
        if (books == null || books.isEmpty()) {
            return;
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-19s | %-8s | %-8s | %-8s | %-30s | %-12s\n", 
                            "ISBN", "BookCode", "Cat_ID", "Pub_ID", "도서명", "출판일");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");

        for (BookInfoDTO1 book : books) {
            System.out.printf("%-19s | %-8s | %-8s | %-8s | %-30s | %-12s\n",
            		book.getIsbn(), 
                   (book.getBook_code() == 0 ? "-" : String.valueOf(book.getBook_code())),
                    book.getCategory_id(), 
                    book.getPublisher_id(), 
                    book.getBookName(),
                    book.getPublish_date());
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
    }
    
    private void printDisposedBookList(List<DisposedBookDTO> books) {
        if (books == null || books.isEmpty()) {
            return;
        }

        System.out.println("--------------------------------------------------------------------");
        System.out.printf("%-10s | %-30s | %-12s | %-30s\n", 
                            "BookCode", "책 제목", "폐기일자", "폐기 사유");
        System.out.println("--------------------------------------------------------------------");

        for (DisposedBookDTO book : books) {
            System.out.printf("%-10d | %-30s | %-12s | %-30s\n",
                    book.getBook_code(),
                    book.getBookName(),
                    book.getDispose_date(),
                    book.getDispose_reason());
        }
        System.out.println("--------------------------------------------------------------------");
    }
}