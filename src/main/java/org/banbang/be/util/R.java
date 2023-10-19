package org.banbang.be.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> implements Serializable {

    private static final long serialVersionUID = 5187687995319002219L;

    private Integer code;
    private Boolean success;
    private String message;
    private T data;

    public static <T> R<T> definition(int code, String message, Boolean success, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(success);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static <T> R<T> definition(HttpServletResponse resp, int code, String message, Boolean success, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(success);
        r.setMessage(message);
        r.setData(data);
        resp.setStatus(code);
        return r;
    }

    public static <T> R<T> ok(String message, T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMessage(message);
        r.setSuccess(true);
        r.setData(data);
        return r;
    }

    public static <T> R<T> ok(int code, String message, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        r.setSuccess(true);
        r.setData(data);
        return r;
    }
    public static <T> R<T> ok(HttpServletResponse resp, int code, String message, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        r.setSuccess(true);
        r.setData(data);
        resp.setStatus(code);
        return r;
    }
    public static <T> R<T> ok(HttpServletResponse resp, String message, T data) {
        R<T> r = new R<>();
        r.setCode(HttpServletResponse.SC_OK);
        r.setMessage(message);
        r.setSuccess(true);
        r.setData(data);
        resp.setStatus(HttpServletResponse.SC_OK);
        return r;
    }

    public static <T> R<T> ok(HttpServletResponse resp, String message) {
        R<T> r = new R<>();
        r.setCode(HttpServletResponse.SC_OK);
        r.setMessage(message);
        r.setSuccess(true);
        r.setData(null);
        resp.setStatus(HttpServletResponse.SC_OK);
        return r;
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMessage("操作成功");
        r.setSuccess(true);
        r.setData(data);
        return r;
    }

    public static <T> R<T> ok(String message) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setSuccess(true);
        r.setMessage(message);
        return r;
    }

    public static <T> R<T> ok(int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(true);
        r.setMessage(message);
        return r;
    }

    public static <T> R<T> ok() {
        R<T> r = new R<>();
        r.setCode(200);
        r.setSuccess(true);
        r.setMessage("操作成功");
        return r;
    }

    public static <T> R<T> error() {
        R<T> r = new R<>();
        r.setCode(500);
        r.setSuccess(false);
        r.setMessage("操作失败");
        return r;
    }

    public static <T> R<T> error(String message) {
        R<T> r = new R<>();
        r.setCode(500);
        r.setSuccess(false);
        r.setMessage(message);
        return r;
    }

    public static <T> R<T> error(int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(false);
        r.setMessage(message);
        return r;
    }

    public static <T> R<T> error(HttpServletResponse resp, int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(false);
        r.setMessage(message);
        resp.setStatus(code);
        return r;
    }

    public static <T> R<T> error(int code, String message, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(false);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static <T> R<T> error(HttpServletResponse resp, int code, String message, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(false);
        r.setMessage(message);
        r.setData(data);
        resp.setStatus(code);
        return r;
    }

    public static <T> R<T> error(HttpServletResponse resp, String message, T data) {
        R<T> r = new R<>();
        r.setCode(HttpServletResponse.SC_BAD_REQUEST);
        r.setSuccess(false);
        r.setMessage(message);
        r.setData(data);
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return r;
    }

    public static <T> R<T> error(HttpServletResponse resp, String message) {
        R<T> r = new R<>();
        r.setCode(HttpServletResponse.SC_BAD_REQUEST);
        r.setSuccess(false);
        r.setMessage(message);
        r.setData(null);
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return r;
    }


    public static <T> R<T> ok(HttpServletResponse resp, int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(true);
        r.setMessage(message);
        r.setData(null);
        resp.setStatus(code);
        return r;
    }
}
