/*
 *
 * Copyright (C) 2014
 *
 */

package de.htw.sdf.photoplatform.exception.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

/**
 * @author <a href="mailto:s0531603@htw-berlin.de">Daniil Tomilow</a>
 */
public class ResponseError implements Serializable
{

    private static final String API_URL = "http://wiki-sdf.f4.htw-berlin.de/wiki/API";

    private final int status; // Http status
    private final int code;
    private final String message;
    private final String developerInfo;

    private final List<InputError> errors;

    /**
     * ResponseError constructor for binding errors.
     *
     * @param status
     *         the status
     * @param code
     *         the code
     * @param message
     *         the message
     * @param errors
     *         the errors
     */
    public ResponseError(int status, int code, String message, Errors errors)
    {
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerInfo = "More info: " + API_URL + "/" + code;

        List<InputError> inputErrors = new ArrayList<>();

        if (errors != null)
        {
            for (FieldError error : errors.getFieldErrors())
            {
                inputErrors.add(new InputError(error.getField(), error.getDefaultMessage()));
            }

        }

        this.errors = inputErrors;
    }

    /**
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @return the code
     */
    public int getCode()
    {
        return code;
    }

    /**
     * @return the developerInfo
     */
    public String getDeveloperInfo()
    {
        return developerInfo;
    }

    /**
     * @return the developerInfo
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * @return the errors
     */
    public List<InputError> getErrors()
    {
        return errors;
    }
}
