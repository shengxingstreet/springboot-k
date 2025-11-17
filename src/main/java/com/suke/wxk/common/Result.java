package com.suke.wxk.common;



import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    private Result() {
    }

    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result<Void> success(String message) {
        return success(null, message);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    // 新增：只接收 data，默认 message 为 "操作成功"
    public static <T> Result<T> success(T data) {
        return success(data, "操作成功");
    }

}
