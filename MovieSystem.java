import java.util.Vector;

public class MovieSystem {
   static Vector<Movie> movies = new Vector<Movie>();
   static Vector<MovieMember> members = new Vector<MovieMember>();
   static Vector<MovieNoneMember> nonemembers = new Vector<MovieNoneMember>();
   static Vector<MovieManager> managers = new Vector<MovieManager>();

   // 기본 영화 및 관리자 정보
   public static void initializeMovie() {
      try {
         createManager("배성규", "BAE", 1234, 001);
         createManager("신동현", "SHIN", 4321, 002);
         
         MovieSystem.createAddMovie("삼진그룹 영어토익반", "드라마", "이종필", "4관", "11월18일");
         MovieSystem.createAddMovie("담보", "드라마", "강대규", "5관", "11월21일");
         MovieSystem.createAddMovie("해리포터와 아즈카반의 죄수", "판타지", "알폰소 쿠아론", "2관", "7월16일");
         MovieSystem.createAddMovie("더 퍼지 심판의 날", "액션", "제임스 드모나코", "1관", "3월 16일");
         MovieSystem.createAddMovie("너의 이름은", "애니메이션", "신카이 마코토", "7관", "1월4일");
         MovieSystem.createAddMovie("인셉션", "액션", "크리스토퍼 놀란", "6관", "7월21일");                
      }
      catch(Exception e) {
         System.out.println("에러" + e.getMessage());
      }
   }
   
   // 영화 정보가 주어지면 영화 객체 생성하여 반환
   static Movie createMovie(String title, String genre, String director, String theater, String date) {
      Movie m = new Movie(title, genre, director, theater, date);

      return m;
   }
   
   // 영화 정보가 주어지면 영화 객체 생성하고 이를 주어진 벡터 movies 객체에 크기순으로 저장하는 메소드
   static Movie createAddMovie(String title, String genre, String director, String theater, String date) {
      Movie m = createMovie(title, genre, director, theater, date);
      int pos = getPosToAddByTitle(m);
      movies.add(pos, m);

      return m;
   }
   
   //movies에서 title을 이름 순으로 저장할 위치를 탐색하여 반환하는 메소드
   static int getPosToAddByTitle(Movie m) {
      int i;
      for(i = 0; i <movies.size(); i++)
         if (movies.get(i).title.compareTo(m.title) > 0 )
            return i;
      return i;
   }
   
   // movies에서 movieNo-1에 해당하는 값을 수정하는 메소드
   static Movie modifyMovie(int movieNo, String genre, String director, String theater, String date) {
      
      Movie m = new Movie(movies.get(movieNo-1).title, genre, director, theater, date);
      movies.set(movieNo-1, m);
      
      return m;
   }
   
   //movies에서 movieNo-1에 해당하는 값을 삭제하는 메소드
   static void deletMovie(int movieNo) {
      
      movies.remove(movieNo-1);
   }

   /* ***************************************************************************************** */
   // 영화 조회 메뉴
   static void movieMenu(int menu) {

      switch (menu) {
      case 1:
         showAllMovies(); // 영화 전체 조회
         break;

      case 2:
         showMovieByTitle(); // 영화 제목 검색 조회
         break;

      case 3:
         showMovieTheater(); // 상영관 검색 조회
         break;

      case 4:
         showMovieByDate(); // 상영날짜 검색 조회
         break;

      case 5:
         break;      //뒤로가기

      default:
         System.out.println("\n 오류 잘못된 선택");
      }
   }

   // 영화 조회 메뉴 1 : 영화 전체 조회
   static void showAllMovies() {
      MovieSystem.outputMovies(movies, "오류");
   }

   // 벡터 movies에 저장된 객체들을 모두 출력
   // 저장된 객체가 하나도 없으면 입력한 에러메세지 출력
   static void outputMovies(Vector<Movie> movies, String errorMSG) {

      if (movies.get(0) == null) {
         System.out.println(errorMSG);
      }
      else
         for (int i = 0; i < movies.size(); i++)
            movies.get(i).output((i + 1) + "번째 영화");
   }

   // 영화 조회 메뉴 2 : 영화 제목 검색 조회
   static void showMovieByTitle() {

      if (movies.get(0) == null) {
         System.out.println("\n 검색 결과 없음\n");
      }
      else {
         String titleToSearch = getStringWithPrompt("검색할 제목");

         Vector<Movie> movieSearched = searchMovieByTitle(titleToSearch);

         MovieSystem.outputMovies(movieSearched, "영화 제목에 '" + titleToSearch + "'을 포함한 영화가 없습니다.");
      }
   }

   // 벡터 movies의 영화 중에서 제목이 titleToSearch을 포함하는 영화를 찾아 모두 반환
   static Vector<Movie> searchMovieByTitle(String titleToSearch) {
      int noMovies = movies.size(); // 원소 개수

      Vector<Movie> moviesSearched = new Vector<Movie>(noMovies);

      for (int cnt = 0; cnt < noMovies && movies.get(cnt) != null; cnt++)
         if (movies.get(cnt).title.toLowerCase().contains(titleToSearch.toLowerCase()))
            moviesSearched.add(movies.get(cnt));

      return moviesSearched;
   }

   // 영화 조회 메뉴 3 : 상영관 검색 조회
   static void showMovieTheater() {

      if (movies.get(0) == null) {
         System.out.println("\n 검색 결과 없음\n");
      }
      else {
         String theaterToSearch = getStringWithPrompt("검색할 상영관");

         Vector<Movie> moviesSearched = searchTheater(theaterToSearch);

         MovieSystem.outputMovies(moviesSearched, "상영관이 없습니다.");
      }
   }

   // 벡터 movies의 영화 중에서 상영관이 theaterToSearch를 포함하는 영화를 찾아 모두 반환
   static Vector<Movie> searchTheater(String theaterToSearch) {
      int noMovies = movies.size();

      Vector<Movie> moviesSearched = new Vector<Movie>(noMovies);

      for (int cnt = 0; cnt < noMovies && movies.get(cnt) != null; cnt++)
         if (movies.get(cnt).theater.toLowerCase().contains(theaterToSearch.toLowerCase())) {
            int pos = getPosToAddByTheater(movies.get(cnt), moviesSearched);
            moviesSearched.add(pos, movies.get(cnt));
         }

      return moviesSearched;
   }
   
   //movies에서 theater을 이름 순으로 저장할 위치를 탐색하여 반환하는 메소드
   static int getPosToAddByTheater(Movie m, Vector<Movie> moviesSearched) {
      int i;
      for(i = 0; i <moviesSearched.size(); i++)
         if (moviesSearched.get(i).theater.compareTo(m.theater) > 0 )
            return i;
      return i;
   }
   

   // 영화 조회 메뉴 4 : 상영날짜 검색 조회
   static void showMovieByDate() {

      if (movies.get(0) == null) {
         System.out.println("\n 검색 결과 없음\n");
      }
      else {
         String dateToSearch = getStringWithPrompt("검색할 상영날짜");
         
         Vector<Movie> moivesSearched = searchDate(dateToSearch);
         
         MovieSystem.outputMovies(moivesSearched, "상영날짜가 없습니다.");
      }
   }
   
   // 벡터 movies의 영화 중에서 상영날짜가 dateToSearch를 포함하는 영화를 찾아 모두 반환
   static Vector<Movie> searchDate(String dateToSearch) {
      int noMovies = movies.size();
      
      Vector<Movie> moviesSearched = new Vector<Movie>(noMovies);
      
      for (int cnt = 0; cnt < noMovies && movies.get(cnt) != null; cnt++)
         if (movies.get(cnt).date.toLowerCase().contains(dateToSearch.toLowerCase())) {
            int pos = getPosToAddByDate(movies.get(cnt), moviesSearched);
            moviesSearched.add(pos, movies.get(cnt));
         }
      
      return moviesSearched;
   }
   
   //movies에서 date을 이름 순으로 저장할 위치를 탐색하여 반환하는 메소드
   static int getPosToAddByDate(Movie m, Vector<Movie> moviesSearched) {
      int i;
      for(i = 0; i <moviesSearched.size(); i++)
         if (moviesSearched.get(i).date.compareTo(m.date) > 0 )
            return i;
      return i;
   }
   
   
   /* ***************************************************************************************** */
   
   
   // 로그인 메뉴 2 : 로그인 => 로그인 메뉴 2/3 : 회원/비회원 => 예약하려는 영화 선택
   // 영화 제목만 조회
   static void reservationMovies() {
      MovieSystem.outputMovietitles(movies, "오류");
   }

   // 벡터 movies에 저장된 객체 중 제목(title)을 모두 출력
   // 저장된 객체가 하나도 없으면 입력한 에러메세지 출력
   static void outputMovietitles(Vector<Movie> movies, String errorMSG) {

      if (movies.get(0) == null) {
         System.out.println(errorMSG);
      } else
         for (int i = 0; i < movies.size(); i++)
            movies.get(i).outputTitle((i + 1) + "번째 영화");
   }
   
   

   // MovieMember
   static String setMovieNum(int i) {
      return movies.get(i - 1).title;
   }

   static String setMovieNum2(int i) {
      return movies.get(i - 1).title;
   }

   static void addToMember(MovieMember MemberId) {
      members.add(MemberId);
   }

   // openMember객체 저장
   static MovieMember openMember(String id, int password, String name, int movienamenum, int countHuman) {
      String moviename = setMovieNum(movienamenum);
      MovieMember newMember = new MovieMember(id, password, name, moviename, countHuman);
      MovieSystem.addToMember(newMember);

      return newMember;
   }

   static void addToNoneMember(MovieNoneMember NoneMember) {
      nonemembers.add(NoneMember);
   }

   // openNoneMember 객체 저장
   static MovieNoneMember openNoneMember(String name2, int phoneNum2, int movienamenum2, int countHuman2) {
      String moviename2 = setMovieNum2(movienamenum2);
      MovieNoneMember newNoneMember = new MovieNoneMember(name2, phoneNum2, moviename2, countHuman2);
      MovieSystem.addToNoneMember(newNoneMember);

      return newNoneMember;
   }
   
   
   /* ***************************************************************************************** */

   // 관리자 정보가 주어지면 관리자 객체 생성하고 이를 주어진 벡터 managers 객체에 크기순으로 저장하는 메소드
   static MovieManager createManager(String name, String id, int password, int managerNo) {
      MovieManager newManager = new MovieManager(name, id, password, managerNo);
      managers.add(newManager);
      
      return newManager;
   }
   
   // 관리자 정보가 주어지면 managers에 있는 정보랑 비교 후 일치하면 로그인 성공하는 메소드
   static void managerLogin(String name, String id, int password) throws Exception {
      int i;
      
      for (i = 0; i < managers.size(); i++) {
         if(managers.get(i).name.equals(name) && managers.get(i).id.equals(id) && managers.get(i).password == password) {
            System.out.println("\n 관리자 " + managers.get(i).name + " 로그인 성공했습니다. \n");
            i = -1;
            break;
         }
      }
      
      if(i != -1)
         throw new Exception("관리자 로그인 실패했습니다.");
      
   }
   

   
   /* ***************************************************************************************** */
   // 프롬프트 메시지를 출력한 후 문자열을 읽어 반환
   static String getStringWithPrompt(String prompt) {
      System.out.print("\n  * " + prompt + " > ");

      return SkScanner.getString();
   }
}