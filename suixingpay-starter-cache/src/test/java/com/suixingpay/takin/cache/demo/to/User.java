package com.suixingpay.takin.cache.demo.to;

import java.io.Serializable;

import com.suixingpay.takin.cache.demo.type.Sex;
import com.suixingpay.takin.cache.demo.type.State;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:35:34
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:35:34
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1932703849895844645L;

    private Integer id;

    private String name;

    private Integer age;

    private Sex sex;

    private State state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return the sex
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(Sex sex) {
        this.sex = sex;
    }

    /**
     * @return the state
     */
    public State getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * TODO
     * 
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", age=" + age + ", sex=" + sex + ", state=" + state + "]";
    }

}
