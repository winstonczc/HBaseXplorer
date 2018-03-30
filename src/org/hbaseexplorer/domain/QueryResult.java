/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hbaseexplorer.domain;

/**
 * @description
 * @author <a href="winstonczc@126.com">winston</a>
 * @since 2018年3月30日
 * @version 1.0.0
 */
public class QueryResult {

    public static final QueryResult OK = new QueryResult(0, "");

    private Integer code;

    private String msg;

    public QueryResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSucc() {
        return this.code != null && this.code.intValue() == 0;
    }

    public static QueryResult succ(String msg) {
        return new QueryResult(0, msg);
    }

    public static QueryResult error(Integer code, String msg) {
        return new QueryResult(code, msg);
    }

}
