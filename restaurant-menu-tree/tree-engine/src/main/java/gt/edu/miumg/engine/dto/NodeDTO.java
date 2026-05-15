package gt.edu.miumg.engine.dto;

import java.util.ArrayList;
import java.util.List;

public class NodeDTO {

    private String id;
    private String value;
    private String parentId;
    private List<NodeDTO> children;

    public NodeDTO() {
        this.children = new ArrayList<>();
    }

    public NodeDTO(String id, String value, String parentId) {
        this.id = id;
        this.value = value;
        this.parentId = parentId;
        this.children = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    public List<NodeDTO> getChildren() { return children; }
    public void setChildren(List<NodeDTO> children) { this.children = children; }
}