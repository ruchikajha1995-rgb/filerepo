
package com.example.fstree.repo;
import com.example.fstree.domain.NodeTag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface NodeTagRepository extends JpaRepository<NodeTag, Long> {
  List<NodeTag> findByNodeId(Long nodeId);
}
