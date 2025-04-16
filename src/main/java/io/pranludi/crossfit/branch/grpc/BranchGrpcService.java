package io.pranludi.crossfit.branch.grpc;

import io.grpc.stub.StreamObserver;
import io.pranludi.crossfit.branch.domain.BranchEntity;
import io.pranludi.crossfit.branch.grpc.interceptor.GrpcRequestInterceptor;
import io.pranludi.crossfit.branch.grpc.interceptor.GrpcResponseInterceptor;
import io.pranludi.crossfit.branch.protobuf.AllBranchesRequest;
import io.pranludi.crossfit.branch.protobuf.AllBranchesResponse;
import io.pranludi.crossfit.branch.protobuf.BranchDTO;
import io.pranludi.crossfit.branch.protobuf.BranchServiceGrpc.BranchServiceImplBase;
import io.pranludi.crossfit.branch.protobuf.GetBranchRequest;
import io.pranludi.crossfit.branch.protobuf.GetBranchResponse;
import io.pranludi.crossfit.branch.protobuf.SaveBranchRequest;
import io.pranludi.crossfit.branch.protobuf.SaveBranchResponse;
import io.pranludi.crossfit.branch.service.BranchService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService(interceptors = {GrpcRequestInterceptor.class, GrpcResponseInterceptor.class})
public class BranchGrpcService extends BranchServiceImplBase {

    final BranchService branchService;
    final MakeEnvironment makeEnvironment;

    public BranchGrpcService(BranchService branchService, MakeEnvironment makeEnvironment) {
        this.branchService = branchService;
        this.makeEnvironment = makeEnvironment;
    }

    @Override
    public void saveBranch(SaveBranchRequest req, StreamObserver<SaveBranchResponse> resObserver) {
        BranchEntity branchEntity = new BranchEntity(
            "id",
            req.getPassword(),
            req.getName(),
            req.getEmail(),
            req.getPhoneNumber()
        );

        BranchEntity branch = branchService.save(branchEntity).apply(makeEnvironment.make());
        SaveBranchResponse res = SaveBranchResponse.newBuilder()
            .setBranch(
                BranchDTO.newBuilder()
                    .setId(branch.id())
                    .setPassword(branch.password())
                    .setName(branch.name())
                    .setEmail(branch.email())
                    .setPhoneNumber(branch.phoneNumber())
                    .build()
            )
            .build();
        resObserver.onNext(res);
        resObserver.onCompleted();
    }

    @Override
    public void getBranch(GetBranchRequest req, StreamObserver<GetBranchResponse> resObserver) {
        BranchEntity branch = branchService.findById().apply(makeEnvironment.make());
        GetBranchResponse res = GetBranchResponse.newBuilder()
            .setBranch(
                BranchDTO.newBuilder()
                    .setId(branch.id())
                    .setPassword(branch.password())
                    .setName(branch.name())
                    .setEmail(branch.email())
                    .setPhoneNumber(branch.phoneNumber())
                    .build()
            )
            .build();
        resObserver.onNext(res);
        resObserver.onCompleted();
    }

    @Override
    public void allBranches(AllBranchesRequest req, StreamObserver<AllBranchesResponse> resObserver) {
        List<BranchEntity> branches = branchService.allBranches().apply(makeEnvironment.make());
        List<BranchDTO> result = new ArrayList<>();
        for (BranchEntity branch : branches) {
            result.add(
                BranchDTO.newBuilder()
                    .setId(branch.id())
                    .setPassword(branch.password())
                    .setName(branch.name())
                    .setEmail(branch.email())
                    .setPhoneNumber(branch.phoneNumber())
                    .build()
            );
        }

        AllBranchesResponse res = AllBranchesResponse.newBuilder()
            .addAllBranches(result)
            .build();
        resObserver.onNext(res);
        resObserver.onCompleted();
    }
}
