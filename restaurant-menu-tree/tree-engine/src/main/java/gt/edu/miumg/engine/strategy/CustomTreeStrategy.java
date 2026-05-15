package gt.edu.miumg.engine.strategy;

import gt.edu.miumg.engine.dto.NodeDTO;
import java.util.List;
import java.util.Map;

public class CustomTreeStrategy implements TreeAlgorithmStrategy {

    @Override
    public List<NodeDTO> dfs(Map<String, NodeDTO> nodes, String rootId) {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }

    @Override
    public List<NodeDTO> bfs(Map<String, NodeDTO> nodes, String rootId) {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }

    @Override
    public int height(Map<String, NodeDTO> nodes, String rootId) {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }

    @Override
    public int depth(Map<String, NodeDTO> nodes, String nodeId) {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }

    @Override
    public List<NodeDTO> ancestors(Map<String, NodeDTO> nodes, String nodeId) {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }

    @Override
    public List<NodeDTO> pathFromRoot(Map<String, NodeDTO> nodes, String nodeId) {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }

    @Override
    public NodeDTO buildTree(Map<String, NodeDTO> nodes, String rootId) {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }

    @Override
    public boolean validateNoCycles(Map<String, NodeDTO> nodes, String rootId) {
        throw new UnsupportedOperationException("Por implementar - semana 2");
    }
}