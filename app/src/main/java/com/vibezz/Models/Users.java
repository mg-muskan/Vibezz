package com.vibezz.Models;

public class Users {

    String profilePic, user, email, password, userId, lastMessage, about;

    public Users(String profilePic, String user, String email, String password, String userId, String lastMessage) {

        this.profilePic = profilePic;
        this.user = user;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;

    }

    public Users(){}

    //SignUp Constructor
    public Users(String user, String email, String password) {

        this.user = user;
        this.email = email;
        this.password = password;

    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

}
