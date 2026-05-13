package gt.edu.miumg.app.repository;

import gt.edu.miumg.engine.dto.NodeDTO;
import java.util.Map;
import java.util.Optional;

public interface TreeRepository {

    // Guarda el nodo raíz
    NodeDTO saveRoot(NodeDTO node);

    // Agrega un nodo hijo a un padre existente
    NodeDTO saveChild(String parentId, NodeDTO child);

    // Busca un nodo por su id
    Optional<NodeDTO> findById(String id);

    // Devuelve todos los nodos del árbol
    Map<String, NodeDTO> findAll();

    // Verifica si ya existe una raíz
    boolean existsRoot();
}