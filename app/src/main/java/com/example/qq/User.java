package com.example.qq;


public class User {
    private int code,id;
    private String status,nickname;
    private Object obj;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {

        this.code = code;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status=status;
    }
    public Object getObj(){

        return obj;
    }
    public void setObj(Object obj) {

        this.obj=obj;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
