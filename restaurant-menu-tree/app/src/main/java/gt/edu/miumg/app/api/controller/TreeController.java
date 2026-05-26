package gt.edu.miumg.app.api.controller;

import gt.edu.miumg.app.mapper.NodeMapper;
import gt.edu.miumg.app.openapi.model.NodeRequest;
import gt.edu.miumg.app.openapi.model.NodeResponse;
import gt.edu.miumg.app.openapi.spec.NodesApi;
import gt.edu.miumg.app.openapi.spec.TreeApi;
import gt.edu.miumg.app.service.TreeService;
import gt.edu.miumg.engine.dto.NodeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Controlador REST que implementa las interfaces TreeApi y NodesApi
 * generadas por OpenAPI Generator. Actúa como puente entre los DTOs
 * internos (NodeDTO) y los modelos de API (NodeResponse/NodeRequest).
 */
@RestController
public class TreeController implements TreeApi, NodesApi {

    private final TreeService treeService;
    private final NodeMapper nodeMapper;

    public TreeController(TreeService treeService, NodeMapper nodeMapper) {
        this.treeService = treeService;
        this.nodeMapper = nodeMapper;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    // ─────────────────────────── MÉTODOS DE NODOS (NodesApi) ──────────────────────────────

    /**
     * POST /nodes/root - Crear nodo raíz
     * Implementa: NodesApi.nodesRootPost(NodeRequest)
     */
    @Override
    public ResponseEntity<NodeResponse> nodesRootPost(NodeRequest nodeRequest) {
        NodeDTO rootDTO = new NodeDTO(UUID.randomUUID().toString(), nodeRequest.getValue(), null);
        NodeDTO saved = treeService.createRoot(nodeRequest.getValue());
        NodeResponse response = nodeMapper.dtoToResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /nodes/{parentId}/children - Agregar hijo a un nodo
     * Implementa: NodesApi.nodesParentIdChildrenPost(String, NodeRequest)
     */
    @Override
    public ResponseEntity<NodeResponse> nodesParentIdChildrenPost(
            String parentId,
            NodeRequest nodeRequest) {
        NodeDTO childDTO = treeService.addChild(parentId, nodeRequest.getValue());
        NodeResponse response = nodeMapper.dtoToResponse(childDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /nodes/{nodeId}/path - Ruta desde raíz a un nodo
     * Implementa: NodesApi.nodesNodeIdPathGet(String)
     */
    @Override
    public ResponseEntity<List<NodeResponse>> nodesNodeIdPathGet(String nodeId) {
        List<NodeDTO> pathDTOs = treeService.pathFromRoot(nodeId);
        List<NodeResponse> responses = nodeMapper.dtoListToResponseList(pathDTOs);
        return ResponseEntity.ok(responses);
    }

    /**
     * GET /nodes/{nodeId}/depth - Profundidad de un nodo
     * Implementa: NodesApi.nodesNodeIdDepthGet(String)
     */
    @Override
    public ResponseEntity<Integer> nodesNodeIdDepthGet(String nodeId) {
        int depth = treeService.depth(nodeId);
        return ResponseEntity.ok(depth);
    }

    /**
     * GET /nodes/{nodeId}/ancestors - Ancestros de un nodo
     * Implementa: NodesApi.nodesNodeIdAncestorsGet(String)
     */
    @Override
    public ResponseEntity<List<NodeResponse>> nodesNodeIdAncestorsGet(String nodeId) {
        List<NodeDTO> ancestorsDTOs = treeService.ancestors(nodeId);
        List<NodeResponse> responses = nodeMapper.dtoListToResponseList(ancestorsDTOs);
        return ResponseEntity.ok(responses);
    }

    // ─────────────────────────── MÉTODOS DE ÁRBOL (TreeApi) ──────────────────────────────

    /**
     * GET /tree - Obtener árbol completo
     * Implementa: TreeApi.treeGet()
     */
    @Override
    public ResponseEntity<NodeResponse> treeGet() {
        NodeDTO treeDTO = treeService.getTree();
        NodeResponse response = nodeMapper.dtoToResponse(treeDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /tree/{nodeId} - Obtener subárbol
     * Implementa: TreeApi.treeNodeIdGet(String)
     */
    @Override
    public ResponseEntity<NodeResponse> treeNodeIdGet(String nodeId) {
        NodeDTO subTreeDTO = treeService.getSubTree(nodeId);
        NodeResponse response = nodeMapper.dtoToResponse(subTreeDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /tree/height - Altura del árbol
     * Implementa: TreeApi.treeHeightGet()
     */
    @Override
    public ResponseEntity<Integer> treeHeightGet() {
        int height = treeService.height();
        return ResponseEntity.ok(height);
    }

    /**
     * GET /tree/traversal - Recorrido DFS o BFS
     * Implementa: TreeApi.treeTraversalGet(String)
     */
    @Override
    public ResponseEntity<List<NodeResponse>> treeTraversalGet(String type) {
        List<NodeDTO> traversalDTOs = treeService.traversal(type);
        List<NodeResponse> responses = nodeMapper.dtoListToResponseList(traversalDTOs);
        return ResponseEntity.ok(responses);
    }

    /**
     * GET /tree/validate - Validar que no haya ciclos
     * Implementa: TreeApi.treeValidateGet()
     */
    @Override
    public ResponseEntity<Boolean> treeValidateGet() {
        boolean isValid = treeService.validate();
        return ResponseEntity.ok(isValid);
    }
}
