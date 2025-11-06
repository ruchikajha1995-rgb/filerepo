package com.example.fstree.web;

import com.example.fstree.dto.NodeDtos.*;
import com.example.fstree.service.NodeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class NodeController {

  private final NodeService svc;

  public NodeController(NodeService svc) {
    this.svc = svc;
  }

  // 👇 NEW ENDPOINT
  @GetMapping("/nodes")
  public List<NodeResponse> getAllNodes() {
    return svc.getTree(); // returns the full tree structure
  }

  @GetMapping("/nodes/tree")
  public List<NodeResponse> tree() {
    return svc.getTree();
  }

  @GetMapping("/nodes/{id}")
  public NodeResponse one(@PathVariable Long id) {
    return svc.getOne(id);
  }

  @PostMapping("/nodes")
  public NodeResponse create(@Valid @RequestBody CreateNodeRequest req) {
    return svc.create(req);
  }

  @PutMapping("/nodes/{id}")
  public NodeResponse update(@PathVariable Long id, @Valid @RequestBody UpdateNodeRequest req) {
    return svc.update(id, req);
  }

  @DeleteMapping("/nodes/{id}")
  public void delete(@PathVariable Long id) {
    svc.delete(id);
  }

  @PostMapping("/nodes/{id}/move")
  public NodeResponse move(@PathVariable Long id, @Valid @RequestBody MoveNodeRequest req) {
    return svc.move(id, req);
  }
}
