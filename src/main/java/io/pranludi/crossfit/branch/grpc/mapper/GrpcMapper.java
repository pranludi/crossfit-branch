package io.pranludi.crossfit.branch.grpc.mapper;

import com.google.protobuf.Timestamp;
import io.pranludi.crossfit.branch.domain.BranchEntity;
import io.pranludi.crossfit.branch.protobuf.BranchDTO;
import java.time.Instant;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GrpcMapper {

    GrpcMapper INSTANCE = Mappers.getMapper(GrpcMapper.class);

    default Timestamp localDateTimeToTimestamp(LocalDateTime localDateTime) {
        Instant instant = java.sql.Timestamp.valueOf(localDateTime).toInstant();
        return Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).build();
    }

    BranchDTO branchEntityToProto(BranchEntity branch);

}
