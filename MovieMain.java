//첫 화면
public class MovieMain {
    
   public static void main(String[] args) {
      MovieSystem.initializeMovie();

      int menu = 0;
      while (menu != 3) {
         System.out.println("영화");
         System.out.println("1. 영화 조회");
         System.out.println("2. 로그인");
         System.out.println("3. 종료");
         System.out.println("작업 선택 : ");

         menu = SkScanner.getInt();

         try {
            switch (menu) {
            case 1:
               movieCheck(); // 영화 조회
               break;

            case 2:
               login(); // 로그인
               break;
               
            case 3:
               break;

            default:
               System.out.println("\n오류 잘못된 선택");
            }
         } catch (Exception e) {
            System.out.println("\n 오류:" + e.getMessage() + "\n");
         }
      }

   }

   // 영화 조회
   static void movieCheck() {

      System.out.println("영화 조회");

      int menu = 0;
      while (menu != 5) {
         System.out.println("1. 영화 전체 조회");
         System.out.println("2. 영화 제목으로 조회");
         System.out.println("3. 상영관로 조회");
         System.out.println("4. 상영날짜으로 조회");
         System.out.println("5. 뒤로가기");

         System.out.println("작업 선택 : ");

         menu = SkScanner.getInt();
         try {
            MovieSystem.movieMenu(menu);
         } catch (Exception e) {
            System.out.println("\n 오류:" + e.getMessage() + "\n");
         }

      }

   }

   // 로그인
   public static void login() throws Exception {

      System.out.println("로그인");

      int loginMenu = 0;
      while (loginMenu != 4) {
         System.out.println("1. 회원");
         System.out.println("2. 비회원");
         System.out.println("3. 관리자");
         System.out.println("4. 뒤로가기");

         System.out.println("작업 선택 : ");

         loginMenu = SkScanner.getInt();

         try {
            switch (loginMenu) {
            case 1:
               memberInfo();// 회원
               break;

            case 2:
               noneMemberInfo(); // 비회원
               break;

            case 3:
               managerInfo();   // 관리자
               break;
               
            case 4:
               break;

            default:
               System.out.println("\n 오류 잘못된 선택");
            }
         } catch (Exception e) {
            System.out.println("\n 오류:" + e.getMessage() + "\n");
         }
      }
   }

   // 회원의 입력정보를 받아 영화를 예약하는 부분
   public static void memberInfo() {
      int paymentMethod;

      System.out.println("\n---- 회원 ----");
      String id = SkScanner.getString("\n 회원님의 아이디를 적어주세요 : ");
      int password = SkScanner.getInt("\n 회원님의 비밀번호를 적어주세요 : ");
      String name = SkScanner.getString("\n 회원님의 이름을 적어주세요: ");
      System.out.println("\n 영화목록 :  ");
      MovieSystem.reservationMovies();
      int movienamenum = SkScanner.getInt("\n 예약하려는 영화를 선택해주세요 : ");

      int countHuman = SkScanner.getInt("\n 인원수를 입력해주세요 : ");
      do {
         System.out.println("\n 결제 방법을 선택해주세요  (1. 현장결제 2. 선결제) : ");
         paymentMethod = SkScanner.getInt();
      } while (paymentMethod != 1 && paymentMethod != 2);

      MovieMember newMember = MovieSystem.openMember(id, password, name, movienamenum, countHuman); // 영화 객체 생성

      System.out.println("\n ---- 회원 목록 ----");
      newMember.output();
   }

   // 비회원의 입력정보를 받아 영화를 예약하는 부분

   public static void noneMemberInfo() {
      int paymentMethod2;
      System.out.println("\n---- 비회원 ----");
      String name2 = SkScanner.getString("\n 회원님의 이름을 적어주세요 : ");
      int phoneNum2 = SkScanner.getInt("\n 회원님의 전화번호를 적어주세요(00012345678) : ");
      System.out.println("\n 영화 목록 : ");
      MovieSystem.reservationMovies();
      int movienamenum2 = SkScanner.getInt("\n 예약하려는 영화를 선택해주세요 : ");

      int countHuman2 = SkScanner.getInt("\n 인원수를 입력해주세요 : ");
      do {
         System.out.println("\n 결제방법을 선택해주세요 ( 1.현장결제 2.선결제) : ");
         paymentMethod2 = SkScanner.getInt();
      } while (paymentMethod2 != 1 && paymentMethod2 != 2);

      MovieNoneMember newNoneMember = MovieSystem.openNoneMember(name2, phoneNum2, movienamenum2, countHuman2); // 영화 객체 생성

      System.out.println("\n ---비회원 목록---");
      newNoneMember.output();

   }
   
   static void managerInfo() throws Exception {
      
      System.out.println("관리자");
      String name = SkScanner.getString("\n 관리자명을 적어주세요 : ");
      String id = SkScanner.getString("\n 관리자의 아이디를 적어주세요 : ");
      int password = SkScanner.getInt("\n 관리자의 비밀번호를 적어주세요 : ");
      
      MovieSystem.managerLogin(name, id, password);
      
      int ManagerMenu = 0;
      while (ManagerMenu != 4) {
         System.out.println("1. 영화 추가");
         System.out.println("2. 영화 정보 수정");
         System.out.println("3. 영화 삭제");
         System.out.println("4. 뒤로가기");

         System.out.println("작업 선택 : ");

         ManagerMenu = SkScanner.getInt();

         try {
            switch (ManagerMenu) {
            case 1:
               addMovieByManager();      // 영화 추가
               break;

            case 2:
               modifyMovieByManager();      // 영화 수정
               break;
               
            case 3:
               deleteMovieByManager();      // 영화 삭제
               break;
               
            case 4:
               break;

            default:
               System.out.println("\n 오류 잘못된 선택");
            }
         } catch (Exception e) {
            System.out.println("\n 오류:" + e.getMessage() + "\n");
         }
      }   
   }
   
   //관리자 메뉴 1 : 영화 정보 추가 메소드
   static Movie addMovieByManager() {
      
      String title = SkScanner.getString("\n 영화 제목 입력 : ");
      String genre = SkScanner.getString("\n 영화 장르 입력 : ");
      String director = SkScanner.getString("\n 영화 감독 입력 : ");
      String theater = SkScanner.getString("\n 상영관 입력 : ");
      String date = SkScanner.getString("\n 상영날짜 입력 : ");
      
      Movie m = MovieSystem.createAddMovie(title, genre, director, theater, date);
      
      return m;
   }
   
   static Movie modifyMovieByManager() {
      System.out.println("영화 목록");
      MovieSystem.showAllMovies();
      
      int movieNo = SkScanner.getInt("\n 수정할 영화를 선택해주세요 : ");
      System.out.println("\n 영화 제목 : " + MovieSystem.movies.get(movieNo-1).title);
      String genre = SkScanner.getString("\n 영화 장르 입력 : ");
      String director = SkScanner.getString("\n 영화 감독 입력 : ");
      String theater = SkScanner.getString("\n 상영관 입력 : ");
      String date = SkScanner.getString("\n 상영날짜 입력 : ");
      
      Movie m = MovieSystem.modifyMovie(movieNo, genre, director, theater, date);
      
      return m;
   }
   
   //관리자 메뉴 3 : 영화 정보 삭제 메소드
   static void deleteMovieByManager() {
      System.out.println("영화 목록");
      MovieSystem.showAllMovies();
      int movieNo = SkScanner.getInt("\n 삭제할 영화를 선택해주세요 : ");
      
      MovieSystem.deletMovie(movieNo);
   }

}