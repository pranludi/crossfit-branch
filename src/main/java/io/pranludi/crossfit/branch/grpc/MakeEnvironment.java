package io.pranludi.crossfit.branch.grpc;

import io.pranludi.crossfit.branch.domain.EnvironmentData;
import io.pranludi.crossfit.branch.grpc.interceptor.InterceptorConstant;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class MakeEnvironment {

    public EnvironmentData make() {
        String branchId = InterceptorConstant.CTX_BRANCH_ID.get();

        EnvironmentData environmentData = new EnvironmentData(branchId, LocalDateTime.now());

        return environmentData;
    }
}
