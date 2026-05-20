package gt.edu.miumg.app.repository;

import gt.edu.miumg.engine.dto.NodeDTO;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "app.storage", havingValue = "mongo")
public class MongoTreeRepository implements TreeRepository {

    @Override
    public NodeDTO saveRoot(NodeDTO node) {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }

    @Override
    public NodeDTO saveChild(String parentId, NodeDTO child) {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }

    @Override
    public Optional<NodeDTO> findById(String id) {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }

    @Override
    public Map<String, NodeDTO> findAll() {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }

    @Override
    public boolean existsRoot() {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }
}