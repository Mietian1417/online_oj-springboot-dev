package com.example.demo.model;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 20:05
 */

@Data
public class UserSubmitCode {
    private int userID;
    private int problemID;
    private String submitCode;
}