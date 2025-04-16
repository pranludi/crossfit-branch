package io.pranludi.crossfit.branch.domain.server;

public enum ServerType {

    BRANCH_SERVICE("BRANCH_SERVICE", "지점 서비스");

    private final String code;
    private final String description;

    ServerType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
