package com.example.fstree.service;

import com.example.fstree.domain.*;
import com.example.fstree.dto.NodeDtos.*;
import com.example.fstree.repo.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NodeService {

  private final NodeRepository nodes;
  private final NodeTagRepository tags;

  public NodeService(NodeRepository nodes, NodeTagRepository tags) {
    this.nodes = nodes;
    this.tags = tags;
  }

  public List<NodeResponse> getTree() {
    return nodes.findByParentIsNullOrderByPositionAsc()
        .stream()
        .map(this::toResponseTree)
        .toList();
  }

  public NodeResponse getOne(Long id) {
    var n = nodes.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Node not found"));
    return toResponse(n, false);
  }

  @Transactional
  public NodeResponse create(CreateNodeRequest req) {
    Node parent = null;
    int pos;

    if (req.parentId() != null) {
      parent = nodes.findById(req.parentId())
          .orElseThrow(() -> new EntityNotFoundException("Parent not found"));
      if (!parent.isFolder())
        throw new IllegalArgumentException("Files cannot have children");

      var sibs = nodes.findByParentIdOrderByPositionAsc(parent.getId());
      int computedPos = req.position() != null
          ? Math.max(0, Math.min(req.position(), sibs.size()))
          : sibs.size();

      final Long parentId = parent.getId();
      final int finalPos = computedPos;
      nodes.shiftRight(parentId, finalPos);
      pos = finalPos;
    } else {
      var roots = nodes.findByParentIsNullOrderByPositionAsc();
      int computedPos = req.position() != null
          ? Math.max(0, Math.min(req.position(), roots.size()))
          : roots.size();

      final int finalPos = computedPos;
      nodes.shiftRight(null, finalPos);
      pos = finalPos;
    }

    var n = new Node();
    n.setName(req.name());
    n.setType(req.type());
    n.setParent(parent);
    n.setPosition(pos);
    n = nodes.save(n);
    applyTags(n, req.tags());
    return toResponse(n, false);
  }

  @Transactional
  public NodeResponse update(Long id, UpdateNodeRequest req) {
    var n = nodes.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Node not found"));
    n.setName(req.name());
    applyTagsReplace(n, req.tags());
    return toResponse(n, false);
  }

  @Transactional
  public void delete(Long id) {
    var n = nodes.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Node not found"));
    nodes.delete(n);
  }

  @Transactional
  public NodeResponse move(Long id, MoveNodeRequest req) {
    var n = nodes.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Node not found"));

    Node newParent = null;
    if (req.newParentId() != null) {
      newParent = nodes.findById(req.newParentId())
          .orElseThrow(() -> new EntityNotFoundException("Parent not found"));
      if (!newParent.isFolder())
        throw new IllegalArgumentException("Files cannot have children");
    }

    if (n.getParent() != null) {
      var oldSibs = nodes.findByParentIdOrderByPositionAsc(n.getParent().getId());
      int oldPos = n.getPosition();
      oldSibs.forEach(s -> {
        if (s.getPosition() > oldPos)
          s.setPosition(s.getPosition() - 1);
      });
    } else {
      var roots = nodes.findByParentIsNullOrderByPositionAsc();
      int oldPos = n.getPosition();
      roots.forEach(r -> {
        if (r.getPosition() > oldPos)
          r.setPosition(r.getPosition() - 1);
      });
    }

    int newPos = Math.max(0, req.newPosition());
    if (newParent != null) {
      var newSibs = nodes.findByParentIdOrderByPositionAsc(newParent.getId());
      newPos = Math.min(newPos, newSibs.size());
      final int posFinal = newPos;
      newSibs.forEach(s -> {
        if (s.getPosition() >= posFinal)
          s.setPosition(s.getPosition() + 1);
      });
    } else {
      var roots = nodes.findByParentIsNullOrderByPositionAsc();
      newPos = Math.min(newPos, roots.size());
      final int posFinal = newPos;
      roots.forEach(r -> {
        if (r.getPosition() >= posFinal)
          r.setPosition(r.getPosition() + 1);
      });
    }

    n.setParent(newParent);
    n.setPosition(newPos);
    return toResponse(n, false);
  }

  private void applyTags(Node n, Map<String, String> map) {
    if (map == null)
      return;
    map.forEach((k, v) -> {
      var t = new NodeTag();
      t.setNode(n);
      t.setKey(k);
      t.setValue(v);
      n.getTags().add(t);
    });
  }

  private void applyTagsReplace(Node n, Map<String, String> map) {
    n.getTags().clear();
    applyTags(n, map);
  }

  private NodeResponse toResponse(Node n, boolean includeChildren) {
    Map<String, String> tagMap = n.getTags().stream()
        .collect(Collectors.toMap(NodeTag::getKey, NodeTag::getValue, (a, b) -> b, LinkedHashMap::new));

    List<NodeResponse> kids = includeChildren
        ? n.getChildren().stream().map(c -> toResponse(c, true)).toList()
        : List.of();

    return new NodeResponse(
        n.getId(),
        n.getName(),
        n.getType(),
        n.getPosition(),
        n.getParent() == null ? null : n.getParent().getId(),
        tagMap,
        kids);
  }

  private NodeResponse toResponseTree(Node n) {
    return toResponse(n, true);
  }
}
