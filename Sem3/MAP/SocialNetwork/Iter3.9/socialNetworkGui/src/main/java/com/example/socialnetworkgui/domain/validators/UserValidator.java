package com.example.socialnetworkgui.domain.validators;


import com.example.socialnetworkgui.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String errorMsg = "";
        if (entity.getFirstName().isEmpty())
            errorMsg += "First name must be a non-empty string.\n";
        if (entity.getLastName().isEmpty())
            errorMsg += "Last name must be a non-empty string.\n";
        if (entity.getTag().isEmpty())
            errorMsg += "Tag must be a non-empty string.\n";
        if (!errorMsg.isEmpty())
            throw new ValidationException(errorMsg);
    }
}

