
package com.example.fstree.dto;
import com.example.fstree.domain.NodeType;
import jakarta.validation.constraints.*;
import java.util.*;
public class NodeDtos {
  public record NodeResponse(
      Long id, String name, NodeType type, int position, Long parentId,
      Map<String,String> tags, List<NodeResponse> children) {}
  public record CreateNodeRequest(
      @NotBlank String name, @NotNull NodeType type,
      Long parentId, Integer position,
      Map<@NotBlank String,@NotBlank String> tags) {}
  public record UpdateNodeRequest(
      @NotBlank String name,
      Map<@NotBlank String,@NotBlank String> tags) {}
  public record MoveNodeRequest(Long newParentId, @NotNull Integer newPosition) {}
}
