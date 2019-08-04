package com.suixingpay.takin.exception.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.suixingpay.takin.exception.BaseException;
import com.suixingpay.takin.exception.ExceptionInfo;
import com.suixingpay.takin.exception.IExceptionCode;
import com.suixingpay.takin.exception.config.ExceptionProperties;
import com.suixingpay.takin.exception.type.ExceptionCode;

import lombok.extern.slf4j.Slf4j;

/**
 * 异常处理器
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月30日 上午11:33:48
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月30日 上午11:33:48
 */
@Slf4j
public class SuixingPayExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String DEFAULT_MESSAGE = "";

    private static final String ERROR_SPLITER = ";";

    private static final String QUESTION = "?";

    private static final String AND = "&";

    private static final String EQUAL = "=";

    private static final String X_REQUESTED_WITH = "X-Requested-With";

    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    private static final String ACCEPT = "Accept";

    private static final String CHAR_SET = "UTF-8";

    private final ExceptionProperties properties;

    public SuixingPayExceptionHandler(ExceptionProperties properties) {
        this.properties = properties;
    }

    /**
     * 解析异常
     *
     * @param throwable
     * @return
     */
    public ExceptionInfo resolveException(Throwable throwable) {
        IExceptionCode code;
        String message = DEFAULT_MESSAGE;

        if (throwable instanceof UndeclaredThrowableException) {
            UndeclaredThrowableException undeclaredThrowable = (UndeclaredThrowableException) throwable;
            throwable = undeclaredThrowable.getUndeclaredThrowable();
        }
        if (throwable instanceof BaseException) {
            BaseException exception = (BaseException) throwable;
            code = exception.getCode();
            message = exception.getMessage();
        } else if (throwable instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) throwable;
            Set<ConstraintViolation<?>> constraintViolationSet = cve.getConstraintViolations();
            String field;
            StringBuilder errorMsgBuilder = new StringBuilder();
            for (ConstraintViolation<?> constraintViolation : constraintViolationSet) {
                if (properties.isShowErrorFieldName()) {
                    String path = constraintViolation.getPropertyPath().toString();
                    String separator = ".";
                    final int pos = path.lastIndexOf(separator);
                    if (pos == -1 || pos == path.length() - separator.length()) {
                        field = "";
                    }
                    field = path.substring(pos + separator.length());
                    errorMsgBuilder.append(field).append(":");
                }
                errorMsgBuilder.append(constraintViolation.getMessage()).append(ERROR_SPLITER);
            }
            code = ExceptionCode.BAD_REQUEST;
            message = errorMsgBuilder.toString();
        } else if (throwable instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException mException = (MethodArgumentNotValidException) throwable;
            BindingResult bindingResult = mException.getBindingResult();
            List<ObjectError> errors = bindingResult.getAllErrors();
            StringBuilder errorMsgBuilder = new StringBuilder();
            String field;
            String msg;
            for (ObjectError error : errors) {
                if (error instanceof FieldError) {
                    FieldError fError = (FieldError) error;
                    field = fError.getObjectName();
                    msg = fError.getDefaultMessage();
                } else {
                    field = error.getObjectName();
                    msg = error.getDefaultMessage();
                }
                if (properties.isShowErrorFieldName()) {
                    errorMsgBuilder.append(field).append(":");
                }
                errorMsgBuilder.append(msg).append(ERROR_SPLITER);
            }
            code = ExceptionCode.BAD_REQUEST;
            message = errorMsgBuilder.toString();
        } else if (throwable instanceof BindException) {
            BindException bEx = (BindException) throwable;
            List<FieldError> errors = bEx.getFieldErrors();
            StringBuilder errorMsgBuilder = new StringBuilder();
            for (FieldError error : errors) {
                if (properties.isShowErrorFieldName()) {
                    errorMsgBuilder.append(error.getField()).append(":");
                }
                errorMsgBuilder.append(error.getDefaultMessage()).append(ERROR_SPLITER);
            }
            code = ExceptionCode.BAD_REQUEST;
            message = errorMsgBuilder.toString();
        } else if (throwable instanceof org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException) {
            code = ExceptionCode.BAD_REQUEST;
            message = throwable.getMessage();
        } else if (throwable instanceof HttpRequestMethodNotSupportedException) {
            code = ExceptionCode.BAD_REQUEST;
            message = throwable.getMessage();
        } else if (throwable instanceof HttpMediaTypeNotSupportedException) {
            code = ExceptionCode.BAD_REQUEST;
            message = throwable.getMessage();
        } else if (throwable instanceof HttpMediaTypeNotAcceptableException) {
            code = ExceptionCode.BAD_REQUEST;
            message = throwable.getMessage();
        } else if (throwable instanceof MissingPathVariableException) {
            code = ExceptionCode.BAD_REQUEST;
            message = throwable.getMessage();
        } else if (throwable instanceof MissingServletRequestParameterException) {
            code = ExceptionCode.BAD_REQUEST;
            message = throwable.getMessage();
        } else if (throwable instanceof ServletRequestBindingException) {
            code = ExceptionCode.BAD_REQUEST;
            message = throwable.getMessage();
        } else if (throwable instanceof ConversionNotSupportedException) {
            code = ExceptionCode.INTERNAL_SERVER_ERROR;
            message = throwable.getMessage();
        } else if (throwable instanceof TypeMismatchException) {
            code = ExceptionCode.BAD_REQUEST;
            message = throwable.getMessage();
        } else if (throwable instanceof HttpMessageNotReadableException) {
            code = ExceptionCode.BAD_REQUEST;
            message = throwable.getMessage();
        } else if (throwable instanceof HttpMessageNotWritableException) {
            code = ExceptionCode.INTERNAL_SERVER_ERROR;
            message = throwable.getMessage();
        } else if (throwable instanceof MissingServletRequestPartException) {
            code = ExceptionCode.BAD_REQUEST;
            message = throwable.getMessage();
        } else if (throwable instanceof NoHandlerFoundException) {
            code = ExceptionCode.BAD_REQUEST;
            message = throwable.getMessage();
        } else if (throwable instanceof AsyncRequestTimeoutException) {
            code = ExceptionCode.SERVICE_UNAVAILABLE;
            message = throwable.getMessage();
        } else {
            log.error(throwable.getMessage(), throwable);
            code = ExceptionCode.INTERNAL_SERVER_ERROR;
            if (properties.isReturnUnkownExceptionMessage()) {
                String unkownExceptionMessage = properties.getUnkownExceptionMessage();
                if (null != unkownExceptionMessage && unkownExceptionMessage.length() > 0) {
                    message = unkownExceptionMessage;
                } else {
                    message = throwable.getMessage();
                }
            }
        }
        return new ExceptionInfo(code.value(), message);
    }

    /**
     * 判断ajax请求
     *
     * @param request
     * @return
     */
    private boolean isAjax(HttpServletRequest request) {
        String xRequestedWith = request.getHeader(X_REQUESTED_WITH);
        String contentType = request.getHeader(ACCEPT);
        boolean rv = XML_HTTP_REQUEST.equals(xRequestedWith)
                || (null != contentType && contentType.contains(MediaType.APPLICATION_JSON_VALUE));
        return rv;
    }

    /**
     * 将异常信息发送给用户
     *
     * @param request
     * @param response
     * @param info
     * @throws Exception
     */
    public void sendErrorMsg(HttpServletRequest request, HttpServletResponse response, ExceptionInfo info) {
        String errorPageUrl = properties.getErrorPageUrl();
        if (isAjax(request) || null == errorPageUrl || errorPageUrl.length() == 0) {
            writeErrorMsg(response, info.getCode(), info.getMessage());
        } else {
            try {
                switch (properties.getSendErrorType()) {
                    case REDIRECT:
                        if (errorPageUrl.indexOf(QUESTION) == -1) {
                            errorPageUrl = errorPageUrl + QUESTION;
                        }
                        errorPageUrl = errorPageUrl + AND + properties.getErrorMessageName() + EQUAL
                                + URLEncoder.encode(info.getMessage(), properties.getCharSet()) + AND
                                + properties.getCodeName() + EQUAL + info.getCode();
                        response.sendRedirect(errorPageUrl);
                        break;
                    case FORWARD:
                        request.setAttribute(properties.getCodeName(), info.getCode());
                        request.setAttribute(properties.getErrorMessageName(), info.getMessage());
                        request.getRequestDispatcher(errorPageUrl).forward(request, response);
                        break;
                    default:
                        log.error("unkown send error type:{}", properties.getSendErrorType());
                        writeErrorMsg(response, info.getCode(), info.getMessage());
                        break;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * @param response
     * @param code
     * @param message
     * @throws Exception
     */
    private void writeErrorMsg(HttpServletResponse response, int code, String message) {
        if (response.isCommitted()) {
            return;
        }
        response.setStatus(code);
        writeData(response, message);
    }

    private static final String CONTENT_TYPE = MediaType.TEXT_HTML_VALUE + ";charset=UTF-8";

    /**
     * 输出数据
     *
     * @param response
     * @param message 字符串
     */
    private void writeData(HttpServletResponse response, String message) {
        if (null == message || message.length() == 0) {
            return;
        }
        // response.setHeader("Cache-Control", "no-cache");
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHAR_SET);
        try (PrintWriter out = response.getWriter();) {
            out.print(message);
            out.flush();
        } catch (IOException e) {
        }
    }
}
