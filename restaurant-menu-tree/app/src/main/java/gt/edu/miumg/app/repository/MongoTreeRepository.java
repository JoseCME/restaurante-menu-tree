package gt.edu.miumg.app.repository;

import gt.edu.miumg.engine.dto.NodeDTO;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "app.storage", havingValue = "mongo")
public class MongoTreeRepository implements TreeRepository {
 
    private final MongoNodeRepository mongoRepo;
 
    public MongoTreeRepository(MongoNodeRepository mongoRepo) {
        this.mongoRepo = mongoRepo;
    }

    private NodeDTO toDTO(NodeDocument doc) {
        NodeDTO dto = new NodeDTO();
        dto.setId(doc.getId());
        dto.setValue(doc.getValue());
        dto.setParentId(doc.getParentId());
        dto.setChildren(new java.util.ArrayList<>());
        return dto;
    }

    private NodeDocument toDocument(NodeDTO dto) {
        return new NodeDocument(dto.getId(), dto.getValue(), dto.getParentId());
    }

    @Override
    public NodeDTO saveRoot(NodeDTO node) {
        if (existsRoot()) {
            throw new IllegalStateException("Ya existe una raíz en el árbol");
        }
        node.setParentId(null);
        NodeDocument saved = mongoRepo.save(toDocument(node));
        return toDTO(saved);
    }

    @Override
    public NodeDTO saveChild(String parentId, NodeDTO child) {

        if (!mongoRepo.existsById(parentId)) {
            throw new IllegalArgumentException("El nodo padre no existe: " + parentId);
        }

        child.setParentId(parentId);
        NodeDocument saved = mongoRepo.save(toDocument(child));
        return toDTO(saved);
    }

    @Override
    public Optional<NodeDTO> findById(String id) {
        return mongoRepo.findById(id).map(this::toDTO);
    }
 
    @Override
    public Map<String, NodeDTO> findAll() {
        List<NodeDocument> docs = mongoRepo.findAll();
        Map<String, NodeDTO> result = new HashMap<>();
        for (NodeDocument doc : docs) {
            NodeDTO dto = toDTO(doc);
            result.put(dto.getId(), dto);
        }
        return result;
    }
 
    @Override
    public boolean existsRoot() {
        return mongoRepo.findByParentIdIsNull().isPresent();
    }
}