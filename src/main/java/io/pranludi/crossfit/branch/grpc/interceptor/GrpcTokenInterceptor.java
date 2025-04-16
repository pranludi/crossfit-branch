package io.pranludi.crossfit.branch.grpc.interceptor;

import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.pranludi.crossfit.branch.domain.server.ServerTokenClaims;
import io.pranludi.crossfit.branch.common.ServerTokenUtil;
import io.pranludi.crossfit.branch.domain.server.ServerProperties;
import io.pranludi.crossfit.branch.exception.ServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class GrpcTokenInterceptor implements ServerInterceptor {

    final Logger logger = LoggerFactory.getLogger(GrpcTokenInterceptor.class);
    final ServerTokenUtil serverTokenUtil;
    final ServerProperties serverProperties;

    public GrpcTokenInterceptor(ServerTokenUtil serverTokenUtil, ServerProperties serverProperties) {
        this.serverTokenUtil = serverTokenUtil;
        this.serverProperties = serverProperties;
    }

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall
        , Metadata metadata
        , ServerCallHandler<ReqT, RespT> serverCallHandler) {
        return new SimpleForwardingServerCallListener<ReqT>(serverCallHandler.startCall(serverCall, metadata)) {
            @Override
            public void onMessage(ReqT message) {
                String token = metadata.get(InterceptorConstant.MD_TOKEN);
                if (token.isEmpty() || token.isBlank()) {
                    throw ServerError.INVALID_AUTH_SERVER;
                }
                ServerTokenClaims serverTokenClaims = serverTokenUtil.validateAndGetClaims(token);
                if (serverTokenClaims.serverType() != serverProperties.getType()) {
                    throw ServerError.INVALID_AUTH_SERVER;
                }
                logger.info("token = {}", token);

                super.onMessage(message);
            }
        };
    }

}
