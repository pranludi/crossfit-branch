package io.pranludi.crossfit.branch.grpc.interceptor;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.pranludi.crossfit.branch.exception.CommonException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(10)
public class GrpcResponseInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall
        , Metadata metadata
        , ServerCallHandler<ReqT, RespT> serverCallHandler) {
        ServerCall.Listener<ReqT> delegate;
        try {
            delegate = serverCallHandler.startCall(serverCall, metadata);
        } catch (Exception ex) {
            return handleInterceptorException(ex, serverCall);
        }
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception ex) {
                    handleEndpointException(ex, serverCall, metadata);
                }
            }
        };
    }

    private static <ReqT, RespT> void handleEndpointException(Exception ex, ServerCall<ReqT, RespT> serverCall, Metadata metadata) {
        if (ex instanceof CommonException) {
            metadata.put(ExceptionConstant.MD_ERROR_CODE, "COMMON_ERROR");
            metadata.put(ExceptionConstant.MD_MESSAGE, ex.getMessage());
        }
        serverCall.close(Status.INTERNAL.withCause(ex).withDescription(ex.getMessage()), metadata);
    }

    private <ReqT, RespT> ServerCall.Listener<ReqT> handleInterceptorException(Throwable t, ServerCall<ReqT, RespT> serverCall) {
        serverCall.close(Status.INTERNAL
            .withCause(t)
            .withDescription("An exception occurred in a **subsequent** interceptor"), new Metadata());

        return new ServerCall.Listener<>() {
            // no-op
        };
    }

}
