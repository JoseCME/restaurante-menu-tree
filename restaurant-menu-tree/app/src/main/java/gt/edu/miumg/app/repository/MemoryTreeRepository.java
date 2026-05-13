package gt.edu.miumg.app.repository;

import gt.edu.miumg.engine.dto.NodeDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MemoryTreeRepository implements TreeRepository {

    private final Map<String, NodeDTO> store = new HashMap<>();

    @Override
    public NodeDTO saveRoot(NodeDTO node) {
        node.setId(UUID.randomUUID().toString());
        store.put(node.getId(), node);
        return node;
    }

    @Override
    public NodeDTO saveChild(String parentId, NodeDTO child) {
        child.setId(UUID.randomUUID().toString());
        child.setParentId(parentId);
        store.put(child.getId(), child);
        return child;
    }

    @Override
    public Optional<NodeDTO> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Map<String, NodeDTO> findAll() {
        return store;
    }

    @Override
    public boolean existsRoot() {
        return store.values().stream()
                .anyMatch(node -> node.getParentId() == null);
    }
}