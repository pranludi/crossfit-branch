package io.pranludi.crossfit.branch.grpc;

import io.grpc.stub.StreamObserver;
import io.pranludi.crossfit.branch.domain.BranchEntity;
import io.pranludi.crossfit.branch.grpc.interceptor.GrpcRequestInterceptor;
import io.pranludi.crossfit.branch.grpc.interceptor.GrpcResponseInterceptor;
import io.pranludi.crossfit.branch.grpc.mapper.GrpcMapper;
import io.pranludi.crossfit.branch.service.BranchService;
import io.pranludi.crossfit.protobuf.BranchDTO;
import io.pranludi.crossfit.protobuf.branch.AllBranchesRequest;
import io.pranludi.crossfit.protobuf.branch.AllBranchesResponse;
import io.pranludi.crossfit.protobuf.branch.BranchServiceGrpc.BranchServiceImplBase;
import io.pranludi.crossfit.protobuf.branch.GetBranchRequest;
import io.pranludi.crossfit.protobuf.branch.GetBranchResponse;
import io.pranludi.crossfit.protobuf.branch.SaveBranchRequest;
import io.pranludi.crossfit.protobuf.branch.SaveBranchResponse;
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
            .setBranch(GrpcMapper.INSTANCE.branchEntityToProto(branch))
            .build();
        resObserver.onNext(res);
        resObserver.onCompleted();
    }

    @Override
    public void getBranch(GetBranchRequest req, StreamObserver<GetBranchResponse> resObserver) {
        BranchEntity branch = branchService.findById().apply(makeEnvironment.make());
        GetBranchResponse res = GetBranchResponse.newBuilder()
            .setBranch(GrpcMapper.INSTANCE.branchEntityToProto(branch))
            .build();
        resObserver.onNext(res);
        resObserver.onCompleted();
    }

    @Override
    public void allBranches(AllBranchesRequest req, StreamObserver<AllBranchesResponse> resObserver) {
        List<BranchEntity> branches = branchService.allBranches().apply(makeEnvironment.make());
        List<BranchDTO> result = new ArrayList<>();
        for (BranchEntity branch : branches) {
            result.add(GrpcMapper.INSTANCE.branchEntityToProto(branch));
        }

        AllBranchesResponse res = AllBranchesResponse.newBuilder()
            .addAllBranches(result)
            .build();
        resObserver.onNext(res);
        resObserver.onCompleted();
    }
}
