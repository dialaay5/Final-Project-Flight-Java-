package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAndAdministratorDto {
    protected User user;
    protected Administrator administrator;

    public UserAndAdministratorDto() {
    }

    public UserAndAdministratorDto(User user, Administrator administrator) {
        this.user = user;
        this.administrator = administrator;
    }

    //Dto Response
    public UserAndAdministratorDto response(User user, Administrator administrator){
        return new UserAndAdministratorDto(user, administrator);
    }
}
