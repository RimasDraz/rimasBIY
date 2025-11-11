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
    private String name;
    private String email;
    private String password;

    @Override
    public String toString() {
        return "Myuser{" +
                "keyid=" + keyid +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    public String getid(){
        return id;
    }
    public void setid(int id){
        this.id=id;
    }
}

