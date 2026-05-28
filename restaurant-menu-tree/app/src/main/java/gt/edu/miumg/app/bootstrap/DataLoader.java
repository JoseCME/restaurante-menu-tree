package gt.edu.miumg.app.bootstrap;

import gt.edu.miumg.app.persistence.TreeRepository;
import gt.edu.miumg.app.service.TreeService;
import gt.edu.miumg.engine.dto.NodeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final TreeService treeService;
    private final TreeRepository repository;

    private final String storageType;

    public DataLoader(TreeService treeService,
                      TreeRepository repository,
                      @Value("${app.storage}") String storageType) {
        this.treeService = treeService;
        this.repository = repository;
        this.storageType = storageType;
    }

    @Override
    public void run(String... args) {
        // Postgres carga datos via data.sql (Persona B), no via DataLoader
        if ("postgres".equalsIgnoreCase(storageType)) {
            log.info("Persistencia=postgres -> carga de datos delegada a scripts SQL (Persona B). DataLoader omitido.");
            return;
        }

        // Idempotencia: no recargar si ya existen datos (protege mongo de duplicar)
        if (!repository.findAll().isEmpty()) {
            log.info("El repositorio ya contiene datos. DataLoader omitido.");
            return;
        }

        log.info("Repositorio vacio. Cargando menu de demostracion...");
        loadDemoMenu();
        log.info("Menu de demostracion cargado correctamente.");
    }

   
    private void loadDemoMenu() {
        // Nivel 1 - raiz
        NodeDTO menu = treeService.createRoot("Menu");

        // Nivel 2
        NodeDTO bebidas = treeService.addChild(menu.getId(), "Bebidas");
        NodeDTO comidas = treeService.addChild(menu.getId(), "Comidas");
        NodeDTO postres = treeService.addChild(menu.getId(), "Postres");

        // Nivel 3 - bajo Bebidas
        NodeDTO calientes = treeService.addChild(bebidas.getId(), "Calientes");
        NodeDTO frias = treeService.addChild(bebidas.getId(), "Frias");

        // Nivel 3 - bajo Comidas
        NodeDTO entradas = treeService.addChild(comidas.getId(), "Entradas");
        NodeDTO platosFuertes = treeService.addChild(comidas.getId(), "Platos fuertes");

        // Nivel 4 - bajo Calientes
        treeService.addChild(calientes.getId(), "Cafe");
        treeService.addChild(calientes.getId(), "Te");

        // Nivel 4 - bajo Frias
        treeService.addChild(frias.getId(), "Jugos");

        // Nivel 4 - bajo Entradas
        treeService.addChild(entradas.getId(), "Ensaladas");

        // Nivel 4 - bajo Platos fuertes
        treeService.addChild(platosFuertes.getId(), "Pastas");

        // Nivel 3 - bajo Postres
        treeService.addChild(postres.getId(), "Helados");
    }
}