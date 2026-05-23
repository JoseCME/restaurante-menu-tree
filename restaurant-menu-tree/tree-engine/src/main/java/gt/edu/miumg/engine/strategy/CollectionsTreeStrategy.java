package gt.edu.miumg.engine.strategy;

import gt.edu.miumg.engine.dto.NodeDTO;

import java.util.*;

public class CollectionsTreeStrategy implements TreeAlgorithmStrategy {

    // ─── DFS (usa ArrayDeque como pila) ───────────────────────────────────────

    @Override
    public List<NodeDTO> dfs(Map<String, NodeDTO> nodes, String rootId) {
        List<NodeDTO> result = new ArrayList<>();
        if (rootId == null || !nodes.containsKey(rootId)) return result;

        ArrayDeque<String> stack = new ArrayDeque<>();
        stack.push(rootId);

        while (!stack.isEmpty()) {
            String currentId = stack.pop();
            NodeDTO current = nodes.get(currentId);
            if (current == null) continue;
            result.add(current);

            // Hijos: buscar nodos cuyo parentId == currentId
            List<String> childIds = new ArrayList<>();
            for (NodeDTO node : nodes.values()) {
                if (currentId.equals(node.getParentId())) {
                    childIds.add(node.getId());
                }
            }
            // Invertir para mantener orden natural (izquierda primero)
            Collections.reverse(childIds);
            for (String childId : childIds) {
                stack.push(childId);
            }
        }
        return result;
    }

    // ─── BFS (usa ArrayDeque como cola) ──────────────────────────────────────

    @Override
    public List<NodeDTO> bfs(Map<String, NodeDTO> nodes, String rootId) {
        List<NodeDTO> result = new ArrayList<>();
        if (rootId == null || !nodes.containsKey(rootId)) return result;

        ArrayDeque<String> queue = new ArrayDeque<>();
        queue.offer(rootId);

        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            NodeDTO current = nodes.get(currentId);
            if (current == null) continue;
            result.add(current);

            for (NodeDTO node : nodes.values()) {
                if (currentId.equals(node.getParentId())) {
                    queue.offer(node.getId());
                }
            }
        }
        return result;
    }

    // ─── HEIGHT (BFS nivel por nivel) ────────────────────────────────────────

    @Override
    public int height(Map<String, NodeDTO> nodes, String rootId) {
        if (rootId == null || !nodes.containsKey(rootId)) return -1;

        ArrayDeque<String> queue = new ArrayDeque<>();
        queue.offer(rootId);
        int height = -1;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            height++;
            for (int i = 0; i < levelSize; i++) {
                String currentId = queue.poll();
                for (NodeDTO node : nodes.values()) {
                    if (currentId.equals(node.getParentId())) {
                        queue.offer(node.getId());
                    }
                }
            }
        }
        return height;
    }

    // ─── DEPTH (subir por parentId contando pasos) ────────────────────────────

    @Override
    public int depth(Map<String, NodeDTO> nodes, String nodeId) {
        if (nodeId == null || !nodes.containsKey(nodeId)) return -1;

        int depth = 0;
        String currentId = nodeId;

        while (true) {
            NodeDTO current = nodes.get(currentId);
            if (current == null || current.getParentId() == null) break;
            depth++;
            currentId = current.getParentId();
        }
        return depth;
    }

    // ─── ANCESTORS ───────────────────────────────────────────────────────────

    @Override
    public List<NodeDTO> ancestors(Map<String, NodeDTO> nodes, String nodeId) {
        List<NodeDTO> ancestors = new ArrayList<>();
        if (nodeId == null || !nodes.containsKey(nodeId)) return ancestors;

        String currentId = nodes.get(nodeId).getParentId();
        while (currentId != null) {
            NodeDTO current = nodes.get(currentId);
            if (current == null) break;
            ancestors.add(current);
            currentId = current.getParentId();
        }
        return ancestors;
    }

    // ─── PATH FROM ROOT ──────────────────────────────────────────────────────

    @Override
    public List<NodeDTO> pathFromRoot(Map<String, NodeDTO> nodes, String nodeId) {
        List<NodeDTO> path = new ArrayList<>();
        if (nodeId == null || !nodes.containsKey(nodeId)) return path;

        String currentId = nodeId;
        while (currentId != null) {
            NodeDTO current = nodes.get(currentId);
            if (current == null) break;
            path.add(0, current); // insertar al inicio = orden raíz → nodo
            currentId = current.getParentId();
        }
        return path;
    }

    // ─── BUILD TREE (árbol anidado con hijos) ────────────────────────────────

    @Override
    public NodeDTO buildTree(Map<String, NodeDTO> nodes, String rootId) {
        if (rootId == null || !nodes.containsKey(rootId)) return null;

        // Pre-construir mapa de hijos para eficiencia
        Map<String, List<NodeDTO>> childrenMap = new HashMap<>();
        for (NodeDTO node : nodes.values()) {
            childrenMap.put(node.getId(), new ArrayList<>());
        }
        for (NodeDTO node : nodes.values()) {
            if (node.getParentId() != null) {
                List<NodeDTO> siblings = childrenMap.get(node.getParentId());
                if (siblings != null) siblings.add(node);
            }
        }

        return buildRecursive(nodes, childrenMap, rootId);
    }

    private NodeDTO buildRecursive(Map<String, NodeDTO> nodes,
                                   Map<String, List<NodeDTO>> childrenMap,
                                   String nodeId) {
        NodeDTO node = nodes.get(nodeId);
        if (node == null) return null;

        NodeDTO result = new NodeDTO();
        result.setId(node.getId());
        result.setValue(node.getValue());
        result.setParentId(node.getParentId());

        List<NodeDTO> children = new ArrayList<>();
        for (NodeDTO child : childrenMap.getOrDefault(nodeId, new ArrayList<>())) {
            NodeDTO childTree = buildRecursive(nodes, childrenMap, child.getId());
            if (childTree != null) children.add(childTree);
        }
        result.setChildren(children);
        return result;
    }

    // ─── VALIDATE NO CYCLES (DFS con HashSet de visitados) ───────────────────

    @Override
    public boolean validateNoCycles(Map<String, NodeDTO> nodes, String rootId) {
        Set<String> visited = new HashSet<>();
        ArrayDeque<String> stack = new ArrayDeque<>();
        stack.push(rootId);

        while (!stack.isEmpty()) {
            String currentId = stack.pop();
            if (visited.contains(currentId)) return false; // ciclo detectado
            visited.add(currentId);

            for (NodeDTO node : nodes.values()) {
                if (currentId.equals(node.getParentId())) {
                    stack.push(node.getId());
                }
            }
        }
        return true;
    }
}