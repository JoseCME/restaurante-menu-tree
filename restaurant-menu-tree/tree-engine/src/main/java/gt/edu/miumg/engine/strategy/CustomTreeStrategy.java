package gt.edu.miumg.engine.strategy;

import gt.edu.miumg.engine.dto.NodeDTO;
import gt.edu.miumg.engine.strategy.custom.MyQueue;
import gt.edu.miumg.engine.strategy.custom.MyStack;
import gt.edu.miumg.engine.strategy.custom.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomTreeStrategy implements TreeAlgorithmStrategy {

    // Construye árbol interno desde datos planos
    private TreeNode buildInternalTree(Map<String, NodeDTO> nodes, String rootId) {
        TreeNode[] treeNodes = new TreeNode[nodes.size()];
        int index = 0;

        for (NodeDTO dto : nodes.values()) {
            treeNodes[index] = new TreeNode(dto.getId(), dto.getValue(), dto.getParentId());
            index++;
        }

        TreeNode root = null;
        for (int i = 0; i < treeNodes.length; i++) {
            if (treeNodes[i].getId().equals(rootId)) {
                root = treeNodes[i];
            }
        }

        for (int i = 0; i < treeNodes.length; i++) {
            String parentId = treeNodes[i].getParentId();
            if (parentId != null) {
                for (int j = 0; j < treeNodes.length; j++) {
                    if (treeNodes[j].getId().equals(parentId)) {
                        treeNodes[j].addChild(treeNodes[i]);
                        break;
                    }
                }
            }
        }

        return root;
    }

    // Convierte TreeNode a NodeDTO
    private NodeDTO toDTO(TreeNode node) {
        NodeDTO dto = new NodeDTO(node.getId(), node.getValue(), node.getParentId());
        return dto;
    }

    @Override
    public List<NodeDTO> dfs(Map<String, NodeDTO> nodes, String rootId) {
        List<NodeDTO> result = new ArrayList<>();
        TreeNode root = buildInternalTree(nodes, rootId);
        if (root == null) return result;

        MyStack stack = new MyStack();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode current = (TreeNode) stack.pop();
            result.add(toDTO(current));

            for (int i = current.getChildCount() - 1; i >= 0; i--) {
                stack.push(current.getChildren()[i]);
            }
        }
        return result;
    }

    @Override
    public List<NodeDTO> bfs(Map<String, NodeDTO> nodes, String rootId) {
        List<NodeDTO> result = new ArrayList<>();
        TreeNode root = buildInternalTree(nodes, rootId);
        if (root == null) return result;

        MyQueue queue = new MyQueue();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            TreeNode current = (TreeNode) queue.dequeue();
            result.add(toDTO(current));

            for (int i = 0; i < current.getChildCount(); i++) {
                queue.enqueue(current.getChildren()[i]);
            }
        }
        return result;
    }

    @Override
    public int height(Map<String, NodeDTO> nodes, String rootId) {
        TreeNode root = buildInternalTree(nodes, rootId);
        if (root == null) return 0;
        return calculateHeight(root);
    }

    private int calculateHeight(TreeNode node) {
        if (node.getChildCount() == 0) return 0;
        int maxHeight = 0;
        for (int i = 0; i < node.getChildCount(); i++) {
            int childHeight = calculateHeight(node.getChildren()[i]);
            if (childHeight > maxHeight) {
                maxHeight = childHeight;
            }
        }
        return maxHeight + 1;
    }

    @Override
    public int depth(Map<String, NodeDTO> nodes, String nodeId) {
        int depth = 0;
        String currentId = nodeId;

        while (currentId != null) {
            NodeDTO current = nodes.get(currentId);
            if (current == null) break;
            currentId = current.getParentId();
            if (currentId != null) depth++;
        }
        return depth;
    }

    @Override
    public List<NodeDTO> ancestors(Map<String, NodeDTO> nodes, String nodeId) {
        List<NodeDTO> result = new ArrayList<>();
        NodeDTO current = nodes.get(nodeId);

        if (current == null) return result;
        String parentId = current.getParentId();

        while (parentId != null) {
            NodeDTO parent = nodes.get(parentId);
            if (parent == null) break;
            result.add(parent);
            parentId = parent.getParentId();
        }
        return result;
    }

    @Override
    public List<NodeDTO> pathFromRoot(Map<String, NodeDTO> nodes, String nodeId) {
        List<NodeDTO> ancestors = ancestors(nodes, nodeId);
        List<NodeDTO> path = new ArrayList<>();

        for (int i = ancestors.size() - 1; i >= 0; i--) {
            path.add(ancestors.get(i));
        }

        NodeDTO node = nodes.get(nodeId);
        if (node != null) path.add(node);

        return path;
    }

    @Override
    public NodeDTO buildTree(Map<String, NodeDTO> nodes, String rootId) {
        TreeNode root = buildInternalTree(nodes, rootId);
        if (root == null) return null;
        return buildDTOTree(root, nodes);
    }

    private NodeDTO buildDTOTree(TreeNode node, Map<String, NodeDTO> nodes) {
        NodeDTO dto = new NodeDTO(node.getId(), node.getValue(), node.getParentId());
        for (int i = 0; i < node.getChildCount(); i++) {
            dto.getChildren().add(buildDTOTree(node.getChildren()[i], nodes));
        }
        return dto;
    }

    @Override
    public boolean validateNoCycles(Map<String, NodeDTO> nodes, String rootId) {
        List<NodeDTO> visited = new ArrayList<>();
        return dfsValidate(nodes, rootId, visited);
    }

    private boolean dfsValidate(Map<String, NodeDTO> nodes, String nodeId, List<NodeDTO> visited) {
        NodeDTO current = nodes.get(nodeId);
        if (current == null) return true;

        for (NodeDTO v : visited) {
            if (v.getId().equals(nodeId)) return false;
        }

        visited.add(current);
        NodeDTO parent = nodes.get(current.getParentId());
        if (parent != null) {
            return dfsValidate(nodes, parent.getId(), visited);
        }
        return true;
    }
}