package io.pranludi.crossfit.branch.domain;

import java.time.LocalDateTime;

public record EnvironmentData(
    String id,
    LocalDateTime currentDateTime
) {

}
