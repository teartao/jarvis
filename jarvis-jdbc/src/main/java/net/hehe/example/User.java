package net.hehe.example;

/**
 * @Author neo·tao
 * @Date 2017/11/22
 * @Desc
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String sex;

    //setter和getter方法省略……

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
