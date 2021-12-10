package org.beyond.rabc.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;

import org.beyond.rabc.constant.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * flash correct
 *
 * @author Gent Liu
 * @date 2019/6/4 18:34
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    private static final String DEBUG_PROPERTY_KEY = "app.debug";
    private static final String STRING_TRUE = "true";
    private static final String GENERIC_ERROR_MESSAGE = "服务端发生未知错误";

    private final boolean debug;

    public ExceptionControllerAdvice(final Environment environment) {
        this.debug = (STRING_TRUE.equals(environment.getProperty(DEBUG_PROPERTY_KEY)));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RuntimeException.class)
    public Object handleException(final RuntimeException e) {
        LOGGER.error(e.getMessage(), e);
        return this.formatException(e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Object handleException(final Exception e) {
        LOGGER.error(e.getMessage(), e);
        return this.formatException(e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object bindExceptionHandler(final MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        return this.formatException(errors.get(0));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleException(final ConstraintViolationException e) {
        return this.formatException(e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public Object bindExceptionHandler(final BindException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
//        LOGGER.error(e.getMessage(), e);
        return this.formatException(errors.get(0));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UnexpectedTypeException.class)
    public Object handleException(final UnexpectedTypeException e) {
        LOGGER.error(e.getMessage(), e);
        return this.formatException(e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServletException.class)
    public Object handleException(final ServletException e) {
        LOGGER.error(e.getMessage(), e);
        return this.formatException(e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleException(final HttpMessageNotReadableException e) {
        LOGGER.error(e.getMessage(), e);
        return this.formatException(e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleException(final IllegalArgumentException e) {
        LOGGER.error(e.getMessage(), e);
        return this.formatException(e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object handleException(final MethodArgumentTypeMismatchException e) {
        LOGGER.error(e.getMessage(), e);
        return this.formatException(e);
    }



    private Map formatException(final Exception e) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", Code.FAILED);
        data.put("message", StringUtils.isEmpty(e.getMessage()) ? GENERIC_ERROR_MESSAGE : e.getMessage());
        if (this.debug) {
            this.appendException(data, e);
        }
        return data;
    }

    private Map formatException(final ObjectError e) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", Code.FAILED);
        data.put("message", StringUtils.isEmpty(e.getDefaultMessage()) ? GENERIC_ERROR_MESSAGE : e.getDefaultMessage());
        return data;
    }

    private void appendException(final Map<String, Object> node, final Throwable e) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("message", e.getMessage());
        data.put("class", e.getClass().getName());
        data.put("stackTrace", e.getStackTrace());
        if (e.getCause() != null) {
            this.appendException(data, e.getCause());
        }
        node.put("exception", data);
    }

}
