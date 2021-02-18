package userpackage;

public class User {
    public static final String GENDER_MALE ="M";
    public static final String GENDER_FEMALE = "F";
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String userName;
    private String gender;
    private String password;
    private String age;

    public User(String userName, String password, String gender, String age){
        this.age=age;
        this.gender=gender;
        this.password=password;
        this.userName=userName;
    }


    public User() {

    }
    public String getAge(){
        return age;
    }
    public void setAge(String age){
        this.age=age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    }


