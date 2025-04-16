package io.pranludi.crossfit.branch.grpc.interceptor;

import io.grpc.Metadata;

public class ExceptionConstant {

  public static final Metadata.Key<String> MD_ERROR_CODE = Metadata.Key.of("error-code", Metadata.ASCII_STRING_MARSHALLER);
  public static final Metadata.Key<String> MD_MESSAGE = Metadata.Key.of("message", Metadata.ASCII_STRING_MARSHALLER);

}
