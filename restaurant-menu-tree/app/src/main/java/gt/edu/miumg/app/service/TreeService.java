package gt.edu.miumg.app.service;

import gt.edu.miumg.app.persistence.TreeRepository;
import gt.edu.miumg.engine.dto.NodeDTO;
import gt.edu.miumg.engine.strategy.TreeAlgorithmStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TreeService {

    private static final Logger log = LoggerFactory.getLogger(TreeService.class);

    private final TreeAlgorithmStrategy strategy;
    private final TreeRepository repository;

    public TreeService(TreeAlgorithmStrategy strategy, TreeRepository repository) {
        this.strategy = strategy;
        this.repository = repository;

        log.info("========================================");
        log.info("  Motor activo  : {}", strategy.getClass().getSimpleName());
        log.info("  Persistencia  : {}", repository.getClass().getSuperclass().getSimpleName());
        log.info("========================================");
    }

    public NodeDTO createRoot(String value) {
        if (repository.existsRoot()) {
            throw new IllegalStateException("Ya existe una raíz");
        }
        NodeDTO root = new NodeDTO(UUID.randomUUID().toString(), value, null);
        return repository.saveRoot(root);
    }

    public NodeDTO addChild(String parentId, String value) {
        Optional<NodeDTO> parent = repository.findById(parentId);
        if (parent.isEmpty()) {
            throw new IllegalArgumentException("Nodo padre no encontrado: " + parentId);
        }
        NodeDTO child = new NodeDTO(UUID.randomUUID().toString(), value, parentId);
        return repository.saveChild(parentId, child);
    }

    public NodeDTO getTree() {
        Map<String, NodeDTO> nodes = repository.findAll();
        String rootId = findRootId(nodes);
        return strategy.buildTree(nodes, rootId);
    }

    public NodeDTO getSubTree(String nodeId) {
        Map<String, NodeDTO> nodes = repository.findAll();
        return strategy.buildTree(nodes, nodeId);
    }

    public List<NodeDTO> pathFromRoot(String nodeId) {
        Map<String, NodeDTO> nodes = repository.findAll();
        return strategy.pathFromRoot(nodes, nodeId);
    }

    public List<NodeDTO> traversal(String type) {
        Map<String, NodeDTO> nodes = repository.findAll();
        String rootId = findRootId(nodes);
        if (type.equalsIgnoreCase("DFS")) {
            return strategy.dfs(nodes, rootId);
        } else {
            return strategy.bfs(nodes, rootId);
        }
    }

    public int height() {
        Map<String, NodeDTO> nodes = repository.findAll();
        String rootId = findRootId(nodes);
        return strategy.height(nodes, rootId);
    }

    public int depth(String nodeId) {
        Map<String, NodeDTO> nodes = repository.findAll();
        return strategy.depth(nodes, nodeId);
    }

    public List<NodeDTO> ancestors(String nodeId) {
        Map<String, NodeDTO> nodes = repository.findAll();
        return strategy.ancestors(nodes, nodeId);
    }

    public boolean validate() {
        Map<String, NodeDTO> nodes = repository.findAll();
        String rootId = findRootId(nodes);
        return strategy.validateNoCycles(nodes, rootId);
    }

    private String findRootId(Map<String, NodeDTO> nodes) {
        for (NodeDTO node : nodes.values()) {
            if (node.getParentId() == null) {
                return node.getId();
            }
        }
        throw new IllegalStateException("No existe raíz en el árbol");
    }
}