//영화 관리자
public class MovieManager {
   String name;   //관리자 이름
   String id;      //관리자 아이디
   int password;   //관리자 비밀번호
   int managerNo;   //관리자 번호
   
   MovieManager() {
   }
   
   MovieManager(String name, String id, int password, int managerNo) {
      this.name = name;
      this.id = id;
      this.password = password;
   }
   
   public String getName() {
      return name;
   }
   
   public void setName(String name) {
      this.name = name;
   }
   
   public String getId() {
      return id;
   }
   
   public void setId(String id) {   
      this.id = id;
   }
   
   public int getPassword() {
      return password;
   }
   
   public void setPassword(int password) {
      this.password = password;
   }
   
   public int getManagerNo() {
      return managerNo;
   }
   
   public void setManagerNo(int managerNo) {
      this.managerNo = managerNo;
   }
   
   public String toString() {
      return "  관리자명 : " + name + ", 관리자 아이디 : " + id + ", 비밀번호 : " + password;
   }
   
   public void output() {
      System.out.println(this.toString() + "\n");
   }
   
}