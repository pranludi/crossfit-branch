package io.pranludi.crossfit.branch.repository;

import io.pranludi.crossfit.branch.repository.dto.BranchDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<BranchDTO, String> {

}
