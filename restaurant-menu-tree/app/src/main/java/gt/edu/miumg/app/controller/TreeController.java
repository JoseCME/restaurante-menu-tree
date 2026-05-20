package gt.edu.miumg.app.controller;

import gt.edu.miumg.app.service.TreeService;
import gt.edu.miumg.engine.dto.NodeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TreeController {

    private final TreeService treeService;

    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    // 1. Crear raíz
    @PostMapping("/nodes/root")
    public ResponseEntity<NodeDTO> createRoot(@RequestBody Map<String, String> body) {
        NodeDTO root = treeService.createRoot(body.get("value"));
        return ResponseEntity.status(HttpStatus.CREATED).body(root);
    }

    // 2. Agregar hijo
    @PostMapping("/nodes/{parentId}/children")
    public ResponseEntity<NodeDTO> addChild(
            @PathVariable String parentId,
            @RequestBody Map<String, String> body) {
        NodeDTO child = treeService.addChild(parentId, body.get("value"));
        return ResponseEntity.status(HttpStatus.CREATED).body(child);
    }

    // 3. Árbol completo
    @GetMapping("/tree")
    public ResponseEntity<NodeDTO> getTree() {
        return ResponseEntity.ok(treeService.getTree());
    }

    // 4. Subárbol
    @GetMapping("/tree/{nodeId}")
    public ResponseEntity<NodeDTO> getSubTree(@PathVariable String nodeId) {
        return ResponseEntity.ok(treeService.getSubTree(nodeId));
    }

    // 5. Ruta desde raíz
    @GetMapping("/nodes/{nodeId}/path")
    public ResponseEntity<List<NodeDTO>> pathFromRoot(@PathVariable String nodeId) {
        return ResponseEntity.ok(treeService.pathFromRoot(nodeId));
    }

    // 6 y 7. DFS y BFS
    @GetMapping("/tree/traversal")
    public ResponseEntity<List<NodeDTO>> traversal(@RequestParam String type) {
        return ResponseEntity.ok(treeService.traversal(type));
    }

    // 8. Altura
    @GetMapping("/tree/height")
    public ResponseEntity<Integer> height() {
        return ResponseEntity.ok(treeService.height());
    }

    // 9. Profundidad
    @GetMapping("/nodes/{nodeId}/depth")
    public ResponseEntity<Integer> depth(@PathVariable String nodeId) {
        return ResponseEntity.ok(treeService.depth(nodeId));
    }

    // 10. Ancestros
    @GetMapping("/nodes/{nodeId}/ancestors")
    public ResponseEntity<List<NodeDTO>> ancestors(@PathVariable String nodeId) {
        return ResponseEntity.ok(treeService.ancestors(nodeId));
    }

    // 11. Validar ciclos
    @GetMapping("/tree/validate")
    public ResponseEntity<Boolean> validate() {
        return ResponseEntity.ok(treeService.validate());
    }
}