package gt.edu.miumg.engine.strategy;

import gt.edu.miumg.engine.dto.NodeDTO;
import java.util.List;
import java.util.Map;

public interface TreeAlgorithmStrategy {

    // Recorre el árbol en profundidad
    // Ejemplo: Bebidas → Calientes → Café → Espresso
    List<NodeDTO> dfs(Map<String, NodeDTO> nodes, String rootId);

    // Recorre el árbol por niveles
    // Ejemplo: Bebidas → Calientes, Fríos → Café, Té, Agua
    List<NodeDTO> bfs(Map<String, NodeDTO> nodes, String rootId);

    // Altura total del árbol
    // Ejemplo: Bebidas→Calientes→Café→Espresso = altura 4
    int height(Map<String, NodeDTO> nodes, String rootId);

    // Profundidad de un nodo específico
    // Ejemplo: Café está a profundidad 3
    int depth(Map<String, NodeDTO> nodes, String nodeId);

    // Todos los ancestros de un nodo
    // Ejemplo: ancestros de Espresso = [Café, Calientes, Bebidas]
    List<NodeDTO> ancestors(Map<String, NodeDTO> nodes, String nodeId);

    // Ruta desde la raíz hasta un nodo
    // Ejemplo: [Bebidas, Calientes, Café, Espresso]
    List<NodeDTO> pathFromRoot(Map<String, NodeDTO> nodes, String nodeId);

    // Construye el árbol jerárquico completo desde datos planos
    NodeDTO buildTree(Map<String, NodeDTO> nodes, String rootId);

    // Valida que no haya ciclos en el árbol
    boolean validateNoCycles(Map<String, NodeDTO> nodes, String rootId);
}