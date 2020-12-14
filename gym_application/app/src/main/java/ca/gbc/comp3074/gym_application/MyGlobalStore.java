package ca.gbc.comp3074.gym_application;

import android.app.Application;

public class MyGlobalStore extends Application {
    private String user;
    private String userRole;
    private boolean loginStatus=false;

    public MyGlobalStore() {
    }

    public MyGlobalStore(String user, String userRole, boolean loginStatus) {
        this.user = user;
        this.userRole = userRole;
        this.loginStatus = loginStatus;
    }

    public boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
