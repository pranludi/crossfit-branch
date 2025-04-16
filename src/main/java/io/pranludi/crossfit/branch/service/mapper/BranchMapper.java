package io.pranludi.crossfit.branch.service.mapper;

import io.pranludi.crossfit.branch.domain.BranchEntity;
import io.pranludi.crossfit.branch.mapper.EntityMapper;
import io.pranludi.crossfit.branch.repository.dto.BranchDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BranchMapper extends EntityMapper<BranchDTO, BranchEntity> {

}
