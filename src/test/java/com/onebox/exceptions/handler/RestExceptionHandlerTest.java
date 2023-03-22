package com.onebox.exceptions.handler;


import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.WebRequest;

import com.onebox.constants.ErrorCodes;
import com.onebox.exceptions.NotFoundException;


@ExtendWith(SpringExtension.class)
class RestExceptionHandlerTest {

    @Mock
    private WebRequest request;

    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("error");
        Assert.assertEquals(HttpStatus.BAD_REQUEST,
                restExceptionHandler.handleIllegalArgumentException(illegalArgumentException).getStatusCode());
    }

    @Test
    void handleAllException() {
        Exception exception = new Exception();
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
                restExceptionHandler.handleAllExceptions(exception, request).getStatusCode());
    }

    @Test
    void handleNotFoundException() {
    	NotFoundException exception = new NotFoundException(ErrorCodes.NOT_FOUND,"");
        Assert.assertEquals(HttpStatus.NOT_FOUND,
                restExceptionHandler.handleNotFoundException(exception).getStatusCode());
    }
  
}