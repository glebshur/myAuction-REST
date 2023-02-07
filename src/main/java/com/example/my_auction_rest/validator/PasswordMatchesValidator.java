package com.example.my_auction_rest.validator;


import com.example.my_auction_rest.annotation.PasswordMatches;
import com.example.my_auction_rest.payload.request.SignUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// Validator checks if the confirm password is the same as the password
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
   @Override
   public void initialize(PasswordMatches constraint) {
   }

   @Override
   public boolean isValid(Object obj, ConstraintValidatorContext context) {
      SignUpRequest signUpRequest = (SignUpRequest) obj;
      return signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword());
   }
}
