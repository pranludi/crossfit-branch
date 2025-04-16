package io.pranludi.crossfit.branch.domain.server;

public record ServerTokenClaims(
    ServerType serverType,
    String serverId
) {

    public ServerTokenClaims {
        if (serverType == null) {
            throw new IllegalArgumentException("Server type cannot be null");
        }
        if (serverId == null || serverId.trim().isEmpty()) {
            throw new IllegalArgumentException("Server ID cannot be null or empty");
        }
        serverId = serverId.trim();
    }
}
