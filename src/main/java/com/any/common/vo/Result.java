package com.any.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private T data;


    /**
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(){
        return new Result<>(20000, "success", null);
    }

    /**
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(String message){
        return new Result<>(20000, message, null);
    }

    /**
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data){
        return new Result<>(20000, "success", data);
    }

    public static <T> Result<T> success(String message, T data){
        return new Result<>(20000, message, data);
    }


    /**
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(){
        return new Result<>(20001, "fail", null);
    }

    /**
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(String message){
        return new Result<>(20001, message, null);
    }

    /**
     *
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(Integer code, String message){
        return new Result<>(code, message, null);
    }

}
