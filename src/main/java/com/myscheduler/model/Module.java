package com.myscheduler.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Pierre on 2016-04-27.
 */
@Entity
@Table(name = "modules")
public class Module {
    @Id
    private Integer id;
    private String name;
    private String type;
    private Integer addressnum;
    private String addresslet;
    private String status;
    private String all_lights_on;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAddressnum() {
        return addressnum;
    }

    public void setAddressnum(Integer addressnum) {
        this.addressnum = addressnum;
    }

    public String getAddresslet() {
        return addresslet;
    }

    public void setAddresslet(String addresslet) {
        this.addresslet = addresslet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAll_lights_on() {
        return all_lights_on;
    }

    public void setAll_lights_on(String all_lights_on) {
        this.all_lights_on = all_lights_on;
    }
}
