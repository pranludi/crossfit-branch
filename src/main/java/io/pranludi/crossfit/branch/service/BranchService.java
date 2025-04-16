package io.pranludi.crossfit.branch.service;

import io.pranludi.crossfit.branch.domain.BranchEntity;
import io.pranludi.crossfit.branch.domain.EnvironmentData;
import io.pranludi.crossfit.branch.repository.BranchRepository;
import io.pranludi.crossfit.branch.repository.dto.BranchDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BranchService {

    final BranchRepository branchRepository;

    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    // 지점 등록
    @Transactional
    public Function<EnvironmentData, BranchEntity> save(BranchEntity branchEntity) {
        return (EnvironmentData env) -> {
            BranchDTO branchDTO = new BranchDTO(
                env.id(),
                branchEntity.password(),
                branchEntity.name(),
                branchEntity.email(),
                branchEntity.phoneNumber()
            );
            branchRepository.save(branchDTO);
            return branchEntity;
        };
    }

    // 지점 조회
    public Function<EnvironmentData, BranchEntity> findById() {
        return (EnvironmentData env) -> {
            BranchDTO branchDTO = branchRepository.findById(env.id()).orElseThrow();
            return new BranchEntity(
                branchDTO.getId(),
                branchDTO.getPassword(),
                branchDTO.getName(),
                branchDTO.getEmail(),
                branchDTO.getPhoneNumber()
            );
        };
    }

    // 모든 지점
    public Function<EnvironmentData, List<BranchEntity>> allBranches() {
        return (EnvironmentData env) -> {
            List<BranchDTO> branchDTOs = branchRepository.findAll();
            List<BranchEntity> entities = new ArrayList<>();
            for (BranchDTO branchDTO : branchDTOs) {
                entities.add(
                    new BranchEntity(
                        branchDTO.getId(),
                        branchDTO.getPassword(),
                        branchDTO.getName(),
                        branchDTO.getEmail(),
                        branchDTO.getPhoneNumber()
                    )
                );
            }
            return entities;
        };
    }
}
