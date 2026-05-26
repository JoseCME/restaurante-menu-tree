package gt.edu.miumg.app.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "nodes")
public class NodeDocument {

    @Id
    private String id;
    private String value;
    private String parentId;

    public NodeDocument() {}

    public NodeDocument(String id, String value, String parentId) {
        this.id = id;
        this.value = value;
        this.parentId = parentId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
}
