package com.lab.common.util;

import com.lab.common.exception.ParamErrorException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class DtoUtil {


    public static void checkDtoParams(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            throw new ParamErrorException(fieldError.getField() + ", " + fieldError.getDefaultMessage());
        }
    }
}
