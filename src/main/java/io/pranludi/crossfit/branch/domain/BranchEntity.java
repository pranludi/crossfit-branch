package io.pranludi.crossfit.branch.domain;

public record BranchEntity(
    String id,
    String password,
    String name,
    String email,
    String phoneNumber
) {

}
