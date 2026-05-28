package gt.edu.miumg.app.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Expone la configuración activa del sistema.
 *
 * ¿Por qué existe este endpoint?
 * El frontend necesita saber qué motor (strategy) y qué
 * persistencia (storage) están activos para mostrarlo en
 * los badges del header, sin que el usuario tenga que
 * revisar application.properties manualmente.
 *
 * @Value inyecta el valor de la propiedad directamente
 * desde application.properties en tiempo de arranque.
 * Si la propiedad no existe, Spring lanza error al iniciar.
 */
@RestController
public class ConfigController {

    @Value("${app.tree-strategy}")
    private String treeStrategy;

    @Value("${app.storage}")
    private String storage;

    /**
     * GET /config
     * Devuelve la strategy y storage activos.
     *
     * Ejemplo de respuesta:
     * {
     *   "strategy": "custom",
     *   "storage": "mongo"
     * }
     */
    @GetMapping("/config")
    public Map<String, String> getConfig() {
        return Map.of(
            "strategy", treeStrategy,
            "storage",  storage
        );
    }
}
