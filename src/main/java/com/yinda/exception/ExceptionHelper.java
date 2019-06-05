package com.yinda.exception;

import com.alibaba.fastjson.JSONObject;
import com.yinda.model.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/6/3 8:52
 * @UpdateUser:
 * @UpdateDate:2019/6/3 8:52
 * @UpdateRemark:
 * @Version:
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHelper {

    @ExceptionHandler(MyException.class)
    public ServerResponse handException(MyException e) {
        log.error("程序异常:{}", hand(e).getMessage());
        String message = e.getMessage();
        return ServerResponse.createByErrorMessage(message);
    }

    @ExceptionHandler(Exception.class)
    public ServerResponse handException(Exception e) {
        log.error("程序异常:{}", hand(e).getMessage());
        return ServerResponse.createByErrorMessage("程序异常");
    }


    public static ServerResponse hand(Exception e) {
        List<Map> collect = Arrays.asList(e.getStackTrace()).stream().map(stackTraceElement -> {
            Map linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("lineNumber", stackTraceElement.getLineNumber());
            linkedHashMap.put("fileNme", stackTraceElement.getFileName());
            linkedHashMap.put("className", stackTraceElement.getClassName());
            linkedHashMap.put("methodName", stackTraceElement.getMethodName());
            return linkedHashMap;
        }).collect(Collectors.toList());
        List throwableList = Arrays.asList(e.getSuppressed()).stream().map(throwable -> throwable.getCause()).collect(Collectors.toList());
        String message = CollectionUtils.isEmpty(throwableList) ? e.getMessage() : throwableList.toString();
        JSONObject json = new JSONObject();
        json.put("message", message);
        json.put("info", collect);
        return ServerResponse.createByErrorMessage(json.toJSONString());
    }
}
