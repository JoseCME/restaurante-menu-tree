# Restaurant Menu Tree API

Backend en Spring Boot que gestiona un menú de restaurante como árbol N-ario
jerárquico, con **dos motores de algoritmos intercambiables** y **tres
persistencias intercambiables**, todo configurable sin recompilar.

Proyecto final del curso **Programación III** — Universidad Mariano Gálvez de
Guatemala (MIUMG).

---

## Tabla de contenidos

1. [Equipo](#equipo)
2. [Arquitectura](#arquitectura)
3. [Stack técnico](#stack-técnico)
4. [Prerrequisitos](#prerrequisitos)
5. [Cómo ejecutar](#cómo-ejecutar)
6. [Endpoints del API](#endpoints-del-api)
7. [Motor de algoritmos](#motor-de-algoritmos)
8. [Persistencias](#persistencias)
9. [Validación cruzada entre motores](#validación-cruzada-entre-motores)
10. [Decisiones de diseño](#decisiones-de-diseño)
11. [Documentación por integrante](#documentación-por-integrante)
12. [Troubleshooting](#troubleshooting)
13. [Estructura del repositorio](#estructura-del-repositorio)

---

## Equipo

| Integrante | Rol |
|------------|-----|
| **A — Jose Carlos Morataya** | Custom + Memoria + OpenAPI contract-first + esqueleto multimódulo + DataLoader |
| **B — Kevin** | Collections + PostgreSQL + modelo ER + scripts SQL |
| **C — Carlo** | MongoDB + beans condicionales + selectores Spring + Frontend |

---

## Arquitectura

```
Controller (API REST)
    │
    ▼
TreeService (orquesta)
    │
    ├──► TreeAlgorithmStrategy (interfaz, módulo tree-engine)
    │         │
    │         ├── CustomTreeStrategy       ← app.tree-strategy=custom
    │         └── CollectionsTreeStrategy  ← app.tree-strategy=collections
    │
    └──► TreeRepository (interfaz, módulo app)
              │
              ├── MemoryTreeRepository    ← app.storage=memory
              ├── PostgresTreeRepository  ← app.storage=postgres
              └── MongoTreeRepository     ← app.storage=mongo
```

El proyecto es un **multimódulo Maven** con dos módulos:

- **`tree-engine`** — Java puro. Contiene la interfaz `TreeAlgorithmStrategy`,
  las dos implementaciones del motor (custom y collections), y el DTO
  `NodeDTO`. No depende de Spring ni de ninguna base de datos.
- **`app`** — Spring Boot. Consume `tree-engine` como dependencia. Contiene
  los controladores, servicios, repositorios, configuración, mapper, frontend
  y bootstrap.

---

## Stack técnico

- **Java 17**
- **Spring Boot 3.5**
- **Maven** (multimódulo)
- **OpenAPI 3** con `openapi-generator-maven-plugin` 7.6.0 (contract-first)
- **SpringDoc** para Swagger UI
- **Spring Data JPA** + PostgreSQL 15+
- **Spring Data MongoDB** + MongoDB 7
- **JUnit 5**

---

## Prerrequisitos

- Java 17
- Maven 3.8+
- PostgreSQL 15+ (solo si se usa `app.storage=postgres`)
- MongoDB 7 (solo si se usa `app.storage=mongo`)

---

## Cómo ejecutar

### 1. Clonar el repositorio

```bash
git clone https://github.com/JoseCME/restaurante-menu-tree.git
cd restaurante-menu-tree
```

### 2. Instalar el módulo `tree-engine`

```bash
cd restaurant-menu-tree/tree-engine
mvn install -DskipTests
cd ..
```

### 3. Configurar la combinación deseada

Edita `restaurant-menu-tree/app/src/main/resources/application.properties`:

```properties
app.tree-strategy=custom       # custom | collections
app.storage=memory             # memory | postgres | mongo
spring.profiles.active=memory  # debe coincidir con app.storage
```

#### Combinaciones disponibles

| tree-strategy | storage | Requiere BD externa |
|---------------|---------|---------------------|
| custom | memory | No |
| custom | postgres | Sí (PostgreSQL) |
| custom | mongo | Sí (MongoDB) |
| collections | memory | No |
| collections | postgres | Sí (PostgreSQL) |
| collections | mongo | Sí (MongoDB) |

### 4. Credenciales de Postgres

Por seguridad, la contraseña de Postgres **no se almacena en el repositorio**.
Se lee de la variable de entorno `DB_PASSWORD`. Para definirla en Eclipse:

> Run Configurations → pestaña Environment → Add → `DB_PASSWORD` = tu contraseña local.

Si no se define, se usa `postgres` como valor por defecto.

### 5. Arrancar

**Desde Eclipse:**
```
Run As → Spring Boot App  en  AppApplication.java
```

**Desde terminal:**
```bash
cd restaurant-menu-tree/app
mvn spring-boot:run
```

Por defecto el servidor arranca en `http://localhost:8082`. Al iniciar, el
[DataLoader](#decisiones-de-diseño) carga automáticamente un menú demo de 14
nodos con 4 niveles de profundidad (solo en `memory` y `mongo`; en `postgres`
los datos se cargan vía scripts SQL).

### 6. Acceder

- **Swagger UI:** http://localhost:8082/swagger-ui.html
- **Frontend:** http://localhost:8082/index.html

---

## Endpoints del API

Todos definidos en `app/src/main/resources/openapi.yaml` y generados como
interfaces Java por el plugin `openapi-generator-maven-plugin`.

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/nodes/root` | Crear raíz del árbol |
| POST | `/nodes/{parentId}/children` | Agregar hijo a un nodo |
| GET | `/tree` | Obtener árbol completo |
| GET | `/tree/{nodeId}` | Obtener subárbol desde un nodo |
| GET | `/nodes/{nodeId}/path` | Ruta raíz → nodo |
| GET | `/tree/traversal?type=DFS` | Recorrido en profundidad |
| GET | `/tree/traversal?type=BFS` | Recorrido en anchura |
| GET | `/tree/height` | Altura del árbol |
| GET | `/nodes/{nodeId}/depth` | Profundidad de un nodo |
| GET | `/nodes/{nodeId}/ancestors` | Ancestros de un nodo |
| GET | `/tree/validate` | Validar que no haya ciclos |

---

## Motor de algoritmos

El motor vive en el módulo `tree-engine` y expone la interfaz
`TreeAlgorithmStrategy` con 8 métodos. Existen dos implementaciones
intercambiables.

### Estrategia Custom (`app.tree-strategy=custom`)

Implementación con estructuras propias, sin librerías externas para el dominio
del árbol. Solo usa JDK como auxiliar.

Componentes propios en `tree-engine/src/main/java/.../strategy/custom/`:

- **`TreeNode`** — nodo del árbol con id, valor, parentId y arreglo manual de hijos.
- **`MyStack`** — pila propia (LIFO) implementada con arreglo redimensionable.
  Se usa en DFS.
- **`MyQueue`** — cola propia (FIFO) implementada con arreglo redimensionable.
  Se usa en BFS.

El método `height` cuenta el camino más largo desde la raíz hasta una hoja en
**aristas** (un árbol con un solo nodo tiene altura 0).

### Estrategia Collections (`app.tree-strategy=collections`)

Implementación basada en estructuras del JDK:

- `ArrayDeque` como pila en DFS y como cola en BFS
- `HashMap<String, List<NodeDTO>>` en `buildTree` (mapa de hijos pre-computado)
- `HashSet<String>` para detección de ciclos
- `ArrayList` para resultados y listas de hijos por nodo

Ver el cuadro comparativo en
[Validación cruzada entre motores](#validación-cruzada-entre-motores).

---

## Persistencias

Las tres persistencias implementan la interfaz `TreeRepository` con 5 métodos
(`saveRoot`, `saveChild`, `findById`, `findAll`, `existsRoot`). El selector
`app.storage` decide cuál se inyecta vía `@ConditionalOnProperty`.

### Memory

`MemoryTreeRepository` mantiene un `Map<String, NodeDTO>` en memoria. Los datos
se pierden al apagar la JVM. Ideal para demos rápidas y pruebas.

### PostgreSQL

`PostgresTreeRepository` usa JPA (`EntityManager`) contra una tabla `nodes`
autorreferenciada. Persistencia durable. Detalles en la sección
[Integrante B](#integrante-b--kevin).

### MongoDB

`MongoTreeRepository` usa Spring Data MongoDB sobre la colección `nodes` con
campo `parentId`. Persistencia durable, sin esquema rígido. Detalles en la
sección [Integrante C](#integrante-c--carlo).

---

## Validación cruzada entre motores

Para verificar que las dos implementaciones del motor son **equivalentes**, se
ejecutaron las operaciones de consulta sobre el mismo menú demo (14 nodos, 4
niveles) con `app.storage=memory`, cambiando únicamente `app.tree-strategy`
entre `custom` y `collections`.

### Resultados

| Operación | Custom | Collections | ¿Coinciden? |
|-----------|--------|-------------|-------------|
| height | 3 | 3 | ✅ Idéntico |
| validate (sin ciclos) | true | true | ✅ Idéntico |
| depth(Café) | 3 | 3 | ✅ Idéntico |
| ancestors(Café) | [Calientes, Bebidas, Menu] | [Calientes, Bebidas, Menu] | ✅ Idéntico |
| path(Café) | [Menu, Bebidas, Calientes, Cafe] | [Menu, Bebidas, Calientes, Cafe] | ✅ Idéntico |
| DFS | 14 nodos | 14 nodos | ✅ Equivalente* |
| BFS | 14 nodos, mismos niveles | 14 nodos, mismos niveles | ✅ Equivalente* |

(*) DFS y BFS visitan los mismos nodos y respetan las propiedades del recorrido
(profundidad-primero / amplitud-primero). El orden entre nodos hermanos puede
variar porque cada motor almacena los hijos en una estructura distinta
(`TreeNode` interno en Custom, `HashMap` en Collections). Como el modelo de
datos no define un orden explícito entre hermanos, ambos órdenes son válidos.

---

## Decisiones de diseño

### Contract-first con OpenAPI

El contrato del API (`openapi.yaml`) es la fuente única de verdad. El plugin
`openapi-generator-maven-plugin` genera automáticamente las interfaces
`TreeApi` y `NodesApi`, que el `TreeController` implementa. Esto garantiza
que cualquier cambio en el contrato se refleja como error de compilación si
el controlador no lo soporta.

### NodeMapper

Se separó el DTO interno (`NodeDTO`, usado por el motor) del DTO público
(`NodeResponse`, generado por OpenAPI). `NodeMapper` convierte entre ambos.
Esto permite evolucionar el contrato externo sin afectar la lógica interna.

### Profiles de Spring por persistencia

Cada persistencia tiene su propio `application-{profile}.properties` y
excluye las auto-configuraciones que no necesita. Por ejemplo, el profile
`memory` excluye `DataSourceAutoConfiguration` y `MongoAutoConfiguration`
para que Spring no intente conectarse a bases que no se usan.

### DataLoader idempotente

`DataLoader` implementa `CommandLineRunner` y carga el menú demo al arrancar.
Es idempotente:

1. Omite la carga si `app.storage=postgres` (carga delegada a scripts SQL).
2. Omite la carga si el repositorio ya contiene datos (evita duplicación
   en mongo y postgres entre reinicios).
3. `TreeService.createRoot()` rechaza una segunda raíz como tercera línea
   de defensa.

### Externalización de credenciales

La contraseña de Postgres se externaliza mediante `${DB_PASSWORD:postgres}`.
Ningún secreto vive en el repositorio. Cada integrante define la variable en
su entorno local.

### `ArrayDeque` en `CollectionsTreeStrategy`

`ArrayDeque` es la implementación recomendada por Java para pilas y colas.
`Stack` tiene sincronización innecesaria y `LinkedList` tiene mayor overhead
de memoria.

### `EntityManager` en `PostgresTreeRepository`

Se eligió usar `EntityManager` con JPQL en lugar de Spring Data JPA. Esto
permite control explícito de las queries sin depender de interfaces
generadas, lo que facilita la portabilidad entre proveedores JPA.

### Beans condicionales con `@ConditionalOnProperty`

Permite que Spring active únicamente el bean correcto según la
configuración, sin necesidad de perfiles complejos o lógica de instanciación
manual.

---

## Documentación por integrante

### Integrante A — Jose Carlos

**Componentes:**

- Interfaz `TreeAlgorithmStrategy` (módulo `tree-engine`)
- `CustomTreeStrategy` con `TreeNode`, `MyStack`, `MyQueue`
- `MemoryTreeRepository`
- `openapi.yaml` y configuración del generador
- `TreeController`, `TreeService`, `NodeMapper`
- `DataLoader` (paquete `bootstrap`)
- Profiles de Spring y exclusiones de auto-configuración

**Decisiones clave:**

- Contract-first sobre code-first para garantizar consistencia entre
  contrato y controlador.
- Convención de altura por aristas (estándar académica, CLRS).
- DataLoader con triple guarda para idempotencia.
- Logging del motor y persistencia activos al arrancar, con
  `getSuperclass()` para extraer el nombre real del bean detrás del proxy
  CGLIB de Spring.

---

### Integrante B — Kevin

#### CollectionsTreeStrategy

Implementación de `TreeAlgorithmStrategy` que usa únicamente colecciones
estándar de Java (`ArrayDeque`, `HashMap`, `HashSet`, `ArrayList`). No
depende de ninguna estructura de datos propia.

**Estructuras de datos utilizadas:**

| Colección | Uso |
|-----------|-----|
| `ArrayDeque` como **pila** (`push`/`pop`) | DFS, validateNoCycles |
| `ArrayDeque` como **cola** (`offer`/`poll`) | BFS, height |
| `HashMap<String, List<NodeDTO>>` | buildTree — mapa de hijos pre-computado |
| `HashSet<String>` | validateNoCycles — nodos ya visitados |
| `ArrayList` | Resultados, lista de hijos por nodo |

**Algoritmos clave:**

- **DFS** — Empuja la raíz en la pila. En cada iteración: saca el nodo
  actual, lo agrega al resultado, busca sus hijos (nodos donde
  `parentId == currentId`), los invierte y los empuja. La inversión
  garantiza que el primer hijo declarado se procese primero.
- **BFS** — Encola la raíz. En cada iteración: desencola el nodo actual, lo
  agrega al resultado, busca sus hijos y los encola sin invertir.
- **height** — BFS nivel a nivel. En cada iteración procesa exactamente
  `levelSize` nodos antes de incrementar el contador. Retorna `-1` para
  árbol vacío, `0` para un árbol de un solo nodo (solo raíz).
- **depth** — Sube por `parentId` contando pasos hasta llegar a la raíz.
- **ancestors** — Retorna todos los ancestros desde el padre inmediato hasta
  la raíz (orden ascendente).
- **pathFromRoot** — Igual que ancestors pero incluye el nodo mismo y el
  orden es de raíz hacia el nodo. Usa `path.add(0, current)` para invertir
  el recorrido ascendente.
- **buildTree** — Pre-computa un `Map<String, List<NodeDTO>>` en una sola
  pasada antes de la recursión, reduciendo la complejidad de O(n²) a O(n).
- **validateNoCycles** — DFS con `HashSet<String>` de visitados. Si se
  intenta visitar un nodo ya visitado, retorna `false`.

#### PostgresTreeRepository

Implementación de `TreeRepository` que persiste los nodos en PostgreSQL
usando JPA (`EntityManager`). Se activa cuando `app.storage=postgres`.

**Configuración (`application-postgres.properties`):**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/menudb
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD:postgres}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.sql.init.mode=always
app.storage=postgres
```

**Schema SQL (`schema.sql`):**
```sql
CREATE TABLE IF NOT EXISTS nodes (
    id        VARCHAR(36) PRIMARY KEY,
    value     VARCHAR(255) NOT NULL,
    parent_id VARCHAR(36),
    CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES nodes(id)
);
```

La tabla se auto-referencia: `parent_id` apunta al `id` del nodo padre. La
raíz tiene `parent_id = NULL`.

**Métodos:**

| Método | Descripción | Transaccional |
|--------|-------------|---------------|
| `saveRoot(NodeDTO)` | Persiste el nodo raíz (`parentId = null`) | Sí |
| `saveChild(parentId, NodeDTO)` | Persiste un nodo hijo referenciando a su padre | Sí |
| `findById(String)` | Busca por PK con `EntityManager.find()` | No |
| `findAll()` | JPQL `SELECT n FROM NodeEntity n` → Map | No |
| `existsRoot()` | Cuenta nodos con `parent_id IS NULL` | No |

**Conversión de tipos:**

El método privado `toDTO(NodeEntity)` convierte la entidad JPA al DTO del
motor. Los hijos no se cargan desde la base de datos — el motor los
resuelve en memoria a partir del `Map<String, NodeDTO>` completo que
devuelve `findAll()`.

#### Cómo activar la configuración B

En `application.properties`:

```properties
app.tree-strategy=collections
app.storage=postgres
spring.profiles.active=postgres
```

Para Postgres el `DataLoader` está desactivado: los datos se cargan
mediante scripts SQL.

---

### Integrante C — Carlo

#### MongoTreeRepository

Persistencia en MongoDB. Almacena cada nodo del árbol como un documento
independiente en la colección `nodes`. La jerarquía se representa con el
campo `parentId`.

**Ejemplo de documentos en MongoDB:**

```json
{ "_id": "uuid-1", "value": "Menú Principal", "parentId": null     }
{ "_id": "uuid-2", "value": "Bebidas",        "parentId": "uuid-1" }
{ "_id": "uuid-3", "value": "Calientes",      "parentId": "uuid-2" }
{ "_id": "uuid-4", "value": "Café",           "parentId": "uuid-3" }
```

**Clases involucradas:**

| Clase | Responsabilidad |
|-------|------------------|
| `NodeDocument.java` | Entidad MongoDB — mapea un nodo a la colección `nodes` |
| `MongoNodeRepository` | Interfaz Spring Data — queries automáticas a MongoDB |
| `MongoTreeRepository` | Implementa `TreeRepository` traduciendo `NodeDTO ↔ NodeDocument` |

**Configuración (`application-mongo.properties`):**

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/menudb
spring.data.mongodb.database=menudb
```

**Selector condicional:**

`MongoTreeRepository` se activa automáticamente con:

```java
@ConditionalOnProperty(name = "app.storage", havingValue = "mongo")
```

Si `app.storage` tiene otro valor, Spring ignora este bean y usa
`MemoryTreeRepository` o `PostgresTreeRepository` según corresponda.

**Verificar datos en MongoDB:**

```bash
mongosh
use menudb
db.nodes.find().pretty()
```

#### Frontend

Frontend HTML/CSS/JS en `app/src/main/resources/static/`, servido
directamente por Spring Boot. Consume los 11 endpoints del API. Accesible
en `http://localhost:8082/index.html` cuando la aplicación está corriendo.

---

## Troubleshooting

**Puerto 8082 en uso:**
```bash
netstat -ano | findstr :8082
taskkill /PID <numero> /F
```

**MongoDB no conecta:**
Verifica que el servicio de MongoDB esté corriendo en Windows:
```
Servicios → MongoDB Server → Iniciar
```

**PostgreSQL no conecta:**
Verifica que el servicio de PostgreSQL esté corriendo en Windows:
```
Servicios → postgresql-x64-15 → Iniciar
```

Y que la base `menudb` exista. Para crearla:
```sql
CREATE DATABASE menudb;
```

**`tree-engine` no encontrado al compilar:**
```bash
cd restaurant-menu-tree/tree-engine
mvn install -DskipTests
```

**Falla `BeanDefinitionOverrideException` al arrancar con mongo:**
Asegúrate de que `MongoTreeRepository` esté anotado con `@Repository` y que
no haya un `@Bean` duplicado en `TreeConfig`.

**Nombre de persistencia se ve como `MongoTreeRepository$$SpringCGLIB$$0`:**
Spring envuelve los repositorios en proxies CGLIB. El log usa
`.getSuperclass().getSimpleName()` para mostrar el nombre real de la clase.

---

## Estructura del repositorio

```
restaurant-menu-tree/
├── pom.xml                           # POM padre
├── README.md
│
├── tree-engine/                      # Módulo motor (Java puro)
│   ├── pom.xml
│   └── src/main/java/gt/edu/miumg/engine/
│       ├── dto/NodeDTO.java
│       └── strategy/
│           ├── TreeAlgorithmStrategy.java
│           ├── CustomTreeStrategy.java
│           ├── CollectionsTreeStrategy.java
│           └── custom/
│               ├── TreeNode.java
│               ├── MyStack.java
│               └── MyQueue.java
│
└── app/                              # Módulo Spring Boot
    ├── pom.xml
    └── src/main/
        ├── java/gt/edu/miumg/app/
        │   ├── AppApplication.java
        │   ├── api/controller/TreeController.java
        │   ├── bootstrap/DataLoader.java
        │   ├── config/TreeConfig.java
        │   ├── mapper/NodeMapper.java
        │   ├── persistence/
        │   │   ├── TreeRepository.java
        │   │   ├── MemoryTreeRepository.java
        │   │   ├── PostgresTreeRepository.java
        │   │   ├── NodeEntity.java
        │   │   ├── MongoTreeRepository.java
        │   │   ├── MongoNodeRepository.java
        │   │   └── NodeDocument.java
        │   └── service/TreeService.java
        └── resources/
            ├── application.properties
            ├── application-memory.properties
            ├── application-postgres.properties
            ├── application-mongo.properties
            ├── openapi.yaml
            ├── schema.sql
            └── static/                # Frontend
                ├── index.html
                ├── styles.css
                ├── app.js
                └── tree-viz.js
```