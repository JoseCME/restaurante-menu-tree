package gt.edu.miumg.app.persistence;

import gt.edu.miumg.engine.dto.NodeDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "app.storage", havingValue = "postgres")
public class PostgresTreeRepository implements TreeRepository {

    @PersistenceContext
    private EntityManager em;

    // ─── Guardar raíz ─────────────────────────────────────────────────────────

    @Override
    @Transactional
    public NodeDTO saveRoot(NodeDTO node) {
        NodeEntity entity = new NodeEntity(node.getId(), node.getValue(), null);
        em.persist(entity);
        return toDTO(entity);
    }

    // ─── Guardar hijo ─────────────────────────────────────────────────────────

    @Override
    @Transactional
    public NodeDTO saveChild(String parentId, NodeDTO child) {
        NodeEntity entity = new NodeEntity(child.getId(), child.getValue(), parentId);
        em.persist(entity);
        return toDTO(entity);
    }

    // ─── Buscar por id ────────────────────────────────────────────────────────

    @Override
    public Optional<NodeDTO> findById(String id) {
        NodeEntity entity = em.find(NodeEntity.class, id);
        return Optional.ofNullable(entity).map(this::toDTO);
    }

    // ─── Traer todos como Map ─────────────────────────────────────────────────

    @Override
    public Map<String, NodeDTO> findAll() {
        List<NodeEntity> entities = em
                .createQuery("SELECT n FROM NodeEntity n", NodeEntity.class)
                .getResultList();

        Map<String, NodeDTO> map = new HashMap<>();
        for (NodeEntity e : entities) {
            map.put(e.getId(), toDTO(e));
        }
        return map;
    }

    // ─── Verificar si existe raíz ─────────────────────────────────────────────

    @Override
    public boolean existsRoot() {
        Long count = em
                .createQuery(
                    "SELECT COUNT(n) FROM NodeEntity n WHERE n.parentId IS NULL",
                    Long.class)
                .getSingleResult();
        return count > 0;
    }

    // ─── Conversión Entity → DTO ──────────────────────────────────────────────

    private NodeDTO toDTO(NodeEntity entity) {
        NodeDTO dto = new NodeDTO();
        dto.setId(entity.getId());
        dto.setValue(entity.getValue());
        dto.setParentId(entity.getParentId());
        dto.setChildren(new ArrayList<>());
        return dto;
    }
}
