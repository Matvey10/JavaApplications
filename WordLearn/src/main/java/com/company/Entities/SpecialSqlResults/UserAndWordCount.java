package com.company.Entities.SpecialSqlResults;

public class UserAndWordCount {
    String userLogin;
    Long count;

    public UserAndWordCount(String userLogin, Long count) {
        this.userLogin = userLogin;
        this.count = count;
    }

    public UserAndWordCount() {
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
