package Domain.Validators;

import Domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String errorMsg = "";
        if (entity.getId() < 0)
            errorMsg += "Id must be a positive integer.\n";
        if (entity.getFirstName().isEmpty())
            errorMsg += "First name must be a non-empty string.\n";
        if (entity.getLastName().isEmpty())
            errorMsg += "Last name must be a non-empty string.\n";
        if (!errorMsg.isEmpty())
            throw new ValidationException(errorMsg);
    }
}
