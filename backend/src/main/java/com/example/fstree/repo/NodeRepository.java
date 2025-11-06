package com.example.fstree.repo;

import com.example.fstree.domain.Node;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface NodeRepository extends JpaRepository<Node, Long> {

    List<Node> findByParentIdOrderByPositionAsc(Long parentId);

    List<Node> findByParentIsNullOrderByPositionAsc();

    Optional<Node> findById(Long id);

    @Transactional
    @Modifying
    @Query("update Node n set n.position = n.position + 1 " +
           "where ( ( :parentId is null and n.parent is null ) or (n.parent.id = :parentId) ) " +
           "and n.position >= :fromPos")
    void shiftRight(@Param("parentId") Long parentId, @Param("fromPos") int fromPos);
}
