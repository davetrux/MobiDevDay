package com.mdd.service;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 3/7/13
 * Time: 9:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Person {
    private String FirstName;
    private String LastName;
    private String Gender;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String _FirstName) {
        this.FirstName = _FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
