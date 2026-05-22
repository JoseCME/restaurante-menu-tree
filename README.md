# \# Restaurant Menu Tree API

# 

# Backend en Spring Boot que gestiona un menú de restaurante

# como árbol jerárquico con dos motores intercambiables

# y tres persistencias, todo configurable sin recompilar.

# 

# \---

# 

# \## Prerrequisitos

# 

# \- Java 17

# \- Maven 3.8+

# \- Docker Desktop

# 

# \---

# 

# \## Clonar el repositorio

# 

# git clone https://github.com/JoseCME/restaurante-menu-tree.git

# cd restaurante-menu-tree

# 

# \---

# 

# \## Instalar módulo tree-engine

# 

# cd restaurant-menu-tree/tree-engine

# mvn install

# cd ..

# 

# \---

# 

# \## Arrancar el proyecto

# 

# Edita application.properties con la combinación deseada:

# 

# app.tree-strategy=custom

# app.storage=memory

# 

# Luego desde Eclipse:

# Run As → Spring Boot App

# 

# \---

# 

# \## Swagger

# 

# http://localhost:8082/swagger-ui.html

# 

# \---

# 

# \## Equipo

# 

# | Integrante | Rol |

# |------------|-----|

# | A (Jose) | Custom + Memoria + OpenAPI |

# | B (Kevin) | Collections + PostgreSQL |

# | C (Carlo) | Beans + MongoDB |

# 

# \---

# 

# \## Pendiente de documentar

# 

# \- Persona A: validación custom completa

# \- Persona B: instrucciones PostgreSQL + Collections

# \- Persona C: instrucciones MongoDB + selectores

