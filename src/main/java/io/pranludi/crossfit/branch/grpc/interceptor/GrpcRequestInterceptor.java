package io.pranludi.crossfit.branch.grpc.interceptor;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class GrpcRequestInterceptor implements ServerInterceptor {

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall
    , Metadata metadata
    , ServerCallHandler<ReqT, RespT> serverCallHandler) {
    String branchId = metadata.get(InterceptorConstant.MD_BRANCH_ID);
    Context context = Context.current().withValue(InterceptorConstant.CTX_BRANCH_ID, branchId);
    return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
  }

}
