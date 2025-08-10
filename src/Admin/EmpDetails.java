package Admin;

import java.time.LocalDate;

public class EmpDetails {
    private int empid;
    private String empname;
    private String gender;
    private String phoneno;
    private String role;
    private LocalDate joindate;
    private String username;
    private String password;

    public EmpDetails(int empid, String empname, String gender, String phoneno,
                      String role, LocalDate joindate, String username, String password) {
        this.empid = empid;
        this.empname = empname;
        this.gender = gender;
        this.phoneno = phoneno;
        this.role = role;
        this.joindate = joindate;
        this.username = username;
        this.password = password;
    }

    // getters
    public int getEmpid() { return empid; }
    public String getEmpname() { return empname; }
    public String getGender() { return gender; }
    public String getPhoneno() { return phoneno; }
    public String getRole() { return role; }
    public LocalDate getJoindate() { return joindate; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    // setters
    public void setEmpid(int empid) { this.empid = empid; }
    public void setEmpname(String empname) { this.empname = empname; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPhoneno(String phoneno) { this.phoneno = phoneno; } // <- fixed
    public void setRole(String role) { this.role = role; }
    public void setJoindate(LocalDate joindate) { this.joindate = joindate; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}
