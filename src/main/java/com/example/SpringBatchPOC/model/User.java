package com.example.SpringBatchPOC.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="GURU_USER")
public class User {

    @Id
    private Long id;
    private String name;
    private String dept;
    private Integer salary;
    private Date createdDate;

    public User(Long id, String name, String dept, Integer salary, Date createdDate) {
        this.id = id;
        this.name = name;
        this.dept = dept;
        this.salary = salary;
        this.createdDate = createdDate;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dept='" + dept + '\'' +
                ", salary=" + salary +
                ", createdDate=" + createdDate +
                '}';
    }
}
