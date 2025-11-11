package library;

/**
 * disposed_book 테이블의 데이터를 담는 DTO
 */
public class DisposedBookDTO {
    private int book_code;
    private String dispose_date;
    private String dispose_reason;
    
    // (Join을 위해 bookname 추가)
    private String bookName; 

    // Getters and Setters
    
    public int getBook_code() {
        return book_code;
    }
    public void setBook_code(int book_code) {
        this.book_code = book_code;
    }
    public String getDispose_date() {
        return dispose_date;
    }
    public void setDispose_date(String dispose_date) {
        this.dispose_date = dispose_date;
    }
    public String getDispose_reason() {
        return dispose_reason;
    }
    public void setDispose_reason(String dispose_reason) {
        this.dispose_reason = dispose_reason;
    }
    public String getBookName() {
        return bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}