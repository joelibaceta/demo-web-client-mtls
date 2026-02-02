package com.demo.webclient.model;

public class LoginRequest {
    private String username;
    private String clientname;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String clientname, String password) {
        this.username = username;
        this.clientname = clientname;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", clientname='" + clientname + '\'' +
                ", password='***'" +
                '}';
    }
}
