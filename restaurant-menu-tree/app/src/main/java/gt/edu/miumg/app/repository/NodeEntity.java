package gt.edu.miumg.app.repository;

import jakarta.persistence.*;

@Entity
@Table(name = "nodes")
public class NodeEntity {

    @Id
    @Column(nullable = false, updatable = false, length = 36)
    private String id;

    @Column(nullable = false)
    private String value;

    @Column(name = "parent_id", length = 36)
    private String parentId;

    // ─── Constructores ────────────────────────────────────────────────────────

    public NodeEntity() {}

    public NodeEntity(String id, String value, String parentId) {
        this.id = id;
        this.value = value;
        this.parentId = parentId;
    }

    // ─── Getters y Setters ────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
}