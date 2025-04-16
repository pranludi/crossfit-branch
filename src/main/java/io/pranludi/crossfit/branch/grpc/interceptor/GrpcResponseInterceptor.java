package io.pranludi.crossfit.branch.grpc.interceptor;

import io.grpc.ForwardingServerCall;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.pranludi.crossfit.branch.exception.ServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(10)
public class GrpcResponseInterceptor implements ServerInterceptor {

    final Logger log = LoggerFactory.getLogger(GrpcResponseInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> serverCall,
        Metadata metadata,
        ServerCallHandler<ReqT, RespT> serverCallHandler
    ) {

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(
            serverCallHandler.startCall(new ExceptionHandlingServerCall<>(serverCall), metadata)) {

            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception e) {
                    handleException(serverCall, metadata, e);
                }
            }
        };
    }

    private <RespT> void handleException(ServerCall<RespT, ?> call, Metadata metadata, Exception e) {
        Status status;
        if (e instanceof IllegalArgumentException) {
            status = Status.INVALID_ARGUMENT.withDescription(e.getMessage());
            metadata.put(ExceptionConstant.MD_ERROR_CODE, "INVALID_ARGUMENT");
            metadata.put(ExceptionConstant.MD_MESSAGE, e.getMessage());
        } else if (e instanceof ServerError) {
            status = Status.UNKNOWN.withDescription(e.getMessage()).withCause(e);
            metadata.put(ExceptionConstant.MD_ERROR_CODE, ((ServerError) e).getCode() + "");
            metadata.put(ExceptionConstant.MD_MESSAGE, e.getMessage());
        } else {
            status = Status.UNKNOWN.withDescription("Unknown error occurred").withCause(e);
            metadata.put(ExceptionConstant.MD_ERROR_CODE, "UNKNOWN");
            metadata.put(ExceptionConstant.MD_MESSAGE, e.getMessage());
        }
        log.error("Exception: ", e);
        call.close(status, metadata);
    }

    private static class ExceptionHandlingServerCall<ReqT, RespT>
        extends ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT> {

        protected ExceptionHandlingServerCall(ServerCall<ReqT, RespT> delegate) {
            super(delegate);
        }
    }

}
