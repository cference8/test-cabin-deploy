package com.cf.cabinescaperoom.models;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Contact {
    private String name;
    private String phone;
    private String email;
    private String comments;
    private boolean copy_email;

    public Contact() {
        name = "";
        phone = "";
        email = "";
        comments = "";
        copy_email = false;
    }

}
