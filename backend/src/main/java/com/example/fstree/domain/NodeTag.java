
package com.example.fstree.domain;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter; import lombok.EqualsAndHashCode;
@Entity
@Table(name="node_tags", uniqueConstraints=@UniqueConstraint(columnNames={"node_id","k"}))
@Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NodeTag {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="node_id", nullable=false) private Node node;
  @Column(name="k", nullable=false) private String key;
  @Column(name="v", nullable=false) private String value;
}
