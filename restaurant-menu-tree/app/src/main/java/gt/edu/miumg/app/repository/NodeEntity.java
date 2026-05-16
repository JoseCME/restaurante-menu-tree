package gt.edu.miumg.app.repository;

import jakarta.persistence.*;

@Entity
@Table(name = "nodes")
public class NodeEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String value;

    @Column(name = "parent_id")
    private String parentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}