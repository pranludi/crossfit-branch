package io.pranludi.crossfit.branch.exception;

import java.util.Arrays;

public class ServerError extends RuntimeException {

    public static final ServerError INVALID_AUTH_SERVER = new ServerError("인증 에러가 발생하였습니다. (20001)", 20001);
    public static final ServerError GENERAL_ERROR = new ServerError("에러가 발생하였습니다. (50000)", 50000);

    public static ServerError BRANCH_NOT_FOUND(String branchId) {
        return new ServerError("지점(" + branchId + ")을 찾을 수 없습니다. (40000)", 40000);
    }

    private String message;
    private int code;
    private Object[] arguments;

    public ServerError(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public ServerError(String message, Object... arguments) {
        this.message = message;
        this.arguments = arguments;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        if (message.indexOf("\"") > 0) {
            message = message.replace("\"", "'");
        }
        if (arguments != null) {
            return "ServerException," + message + "," + code + "," + Arrays.toString(arguments);
        }
        return "ServerException," + message + "," + code;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
