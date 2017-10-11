package com.self.learning.helloworld;

import javax.persistence.*;

@Table(name = "t_manager")
@Entity
public class Manager {
    private int id;
    private String mgrName;
    private Department dept;

    @GeneratedValue
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "mgr_name")
    public String getMgrName() {
        return mgrName;
    }

    public void setMgrName(String mgrName) {
        this.mgrName = mgrName;
    }

    @OneToOne(mappedBy = "mgr")
    public Department getDept() {
        return dept;
    }

    public void setDept(Department dept) {
        this.dept = dept;
    }
}
