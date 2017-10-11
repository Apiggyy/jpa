package com.self.learning.helloworld;

import javax.persistence.*;

@Table(name = "t_department")
@Entity
public class Department {
    private int id;
    private String deptName;
    private Manager mgr;

    @GeneratedValue
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "dept_name")
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @JoinColumn(name = "mgr_id",unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    public Manager getMgr() {
        return mgr;
    }

    public void setMgr(Manager mgr) {
        this.mgr = mgr;
    }
}
