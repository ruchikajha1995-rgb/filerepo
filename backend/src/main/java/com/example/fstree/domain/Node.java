
package com.example.fstree.domain;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter; import lombok.EqualsAndHashCode;
import java.util.*;
@Entity @Table(name="nodes")
@Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Node {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;
  @Version private Long version;
  @Column(nullable=false) private String name;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private NodeType type;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="parent_id") private Node parent;
  @OneToMany(mappedBy="parent", cascade=CascadeType.ALL, orphanRemoval=true)
  @OrderBy("position ASC") private List<Node> children = new ArrayList<>();
  @Column(nullable=false) private int position;
  @OneToMany(mappedBy="node", cascade=CascadeType.ALL, orphanRemoval=true) private List<NodeTag> tags = new ArrayList<>();
  public boolean isFolder(){ return type==NodeType.FOLDER; }
}
