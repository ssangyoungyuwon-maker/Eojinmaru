package library;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SincheongUI {

	public void sincheong() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		List<SincheongDTO> list = new ArrayList<SincheongDTO>();
		String title;
		String author;

		while (true) {
			try {
				System.out.print("신청할 도서의 제목을 입력하세요.[종료:q]");
				title = br.readLine();

				if (title.equalsIgnoreCase("q")) {
					System.out.println("이전 화면으로 돌아갑니다.");
					return;

				//System.out.print("신청할 도서의 저자이름을 입력하세요. ");
				//author = br.readLine();
				
				//SincheongDTO dto = new SincheongDTO(title,author);
				//list.add(dto);
				
				//System.out.println(title+"이 신청되었습니다.");

					

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

}

	
	//DAO
}
