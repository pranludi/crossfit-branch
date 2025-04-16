package io.pranludi.crossfit.branch.grpc.interceptor;

import io.grpc.Context;
import io.grpc.Metadata;

public class InterceptorConstant {

    public static final Metadata.Key<String> MD_BRANCH_ID = Metadata.Key.of("branch-id", Metadata.ASCII_STRING_MARSHALLER);
    public static final Context.Key<String> CTX_BRANCH_ID = Context.key("branch-id");

}
