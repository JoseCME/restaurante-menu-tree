# Restaurant Menu Tree API

Backend en Spring Boot que gestiona un menú de restaurante
como árbol jerárquico con dos motores intercambiables
y tres persistencias, todo configurable sin recompilar.

---

## Prerrequisitos

- Java 17
- Maven 3.8+
- MongoDB 7 (instalado localmente)
- PostgreSQL 15 (instalado localmente)

---

## Clonar el repositorio

```bash
git clone https://github.com/JoseCME/restaurante-menu-tree.git
cd restaurante-menu-tree
```

---

## Instalar módulo tree-engine

```bash
cd restaurant-menu-tree/tree-engine
mvn install -DskipTests
cd ..
```

---

## Configurar la combinación deseada

Edita `restaurant-menu-tree/app/src/main/resources/application.properties`:

```properties
app.tree-strategy=custom       # custom | collections
app.storage=memory             # memory | postgres | mongo
```

### Combinaciones disponibles

| tree-strategy | storage  | Requiere BD externa |
|---------------|----------|---------------------|
| custom        | memory   | No                  |
| custom        | postgres | Sí (PostgreSQL)     |
| custom        | mongo    | Sí (MongoDB)        |
| collections   | memory   | No                  |
| collections   | postgres | Sí (PostgreSQL)     |
| collections   | mongo    | Sí (MongoDB)        |

---

## Arrancar el proyecto

Desde Eclipse:
```
Run As → Spring Boot App en AppApplication.java
```

O desde terminal:
```bash
cd restaurant-menu-tree/app
mvn spring-boot:run
```

---

## Swagger UI

```
http://localhost:8080/swagger-ui.html
```

---

## Frontend

```
http://localhost:8080/index.html
```

---

## Persistencia MongoDB

### ¿Cómo funciona?

MongoDB almacena cada nodo del árbol como un documento independiente
en la colección `nodes`. La jerarquía se representa con el campo `parentId`.

Ejemplo de documentos en MongoDB:

```json
{ "_id": "uuid-1", "value": "Menú Principal", "parentId": null     }
{ "_id": "uuid-2", "value": "Bebidas",        "parentId": "uuid-1" }
{ "_id": "uuid-3", "value": "Calientes",      "parentId": "uuid-2" }
{ "_id": "uuid-4", "value": "Café",           "parentId": "uuid-3" }
```

### Clases involucradas

| Clase                  | Responsabilidad                                      |
|------------------------|------------------------------------------------------|
| `NodeDocument.java`    | Entidad MongoDB — mapea un nodo a la colección nodes |
| `MongoNodeRepository`  | Interfaz Spring Data — queries automáticas a MongoDB |
| `MongoTreeRepository`  | Implementa TreeRepository traduciendo NodeDTO ↔ NodeDocument |

### Activar persistencia MongoDB

En `application.properties`:

```properties
app.storage=mongo
```

En `application-mongo.properties`:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/menudb
spring.data.mongodb.database=menudb
```

### Selector condicional

`MongoTreeRepository` se activa automáticamente con:

```java
@ConditionalOnProperty(name = "app.storage", havingValue = "mongo")
```

Si `app.storage` tiene otro valor, Spring ignora este bean y usa
`MemoryTreeRepository` o `PostgresTreeRepository` según corresponda.

### Verificar datos en MongoDB

Abre MongoDB Compass o desde la terminal:

```bash
mongosh
use menudb
db.nodes.find().pretty()
```

---

## Troubleshooting

**Puerto 8080 en uso:**
```bash
netstat -ano | findstr :8080
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

**tree-engine no encontrado:**
```bash
cd restaurant-menu-tree/tree-engine
mvn install -DskipTests
```

---

## Equipo

| Integrante | Rol                                      |
|------------|------------------------------------------|
| A (Jose)   | Custom + Memoria + OpenAPI               |
| B (Kevin)  | Collections + PostgreSQL                 |
| C (Carlo)  | MongoDB + Beans condicionales + Frontend |
