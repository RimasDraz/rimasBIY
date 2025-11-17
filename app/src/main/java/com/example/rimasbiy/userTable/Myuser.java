package com.example.rimasbiy.userTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Myuser {
    @PrimaryKey(autoGenerate = true)
    public long keyid;
    @ColumnInfo(name = "id")
    private int id;
    private String phone;
    private String email;
    private String password;

    @Override
    public String toString() {
        return "Myuser{" +
                "keyid=" + keyid +
                ", id=" + id +
                ", name='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    public int getId() {
        return id;
    }
    public void setid(int id){
        this.id=id;
    }
    public long getKeyid() {
        return keyid;
    }

    public void setKeyid(long keyid) {
        this.keyid = keyid;
    }

    public String getPhone() {
        return phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
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


}

