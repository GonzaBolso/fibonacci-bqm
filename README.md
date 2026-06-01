### Pasos
1. Dependencias (pom.xml)
2. Entidades/clases 
3. dto 
4. manejo excepciones 
5. config BD (resources/application.properties / postgresql)
6. repositorio 
7. servicio (logica negocio)
8. controlador
9. Tests de cobertura

### Fibonacci bqm
```properties
El valor de n debe ser un numero positivo entre 1 y 5000
F(-n)= No cumple con la definicion
F(0)= No cumple con la definicion
F(1)=1
F(2)=2
F(50)=20365011074
F(n) = F(n-1) + F(n-2)
```
**Casos:**
> Si n = 0 -> No cumple con la definicion
>
> Si n negativo -> No cumple con la definicion
>
> Si n > 5000 o -> valor fuera de rango
>
> Si n no numerico -> el parametro debe ser entero

###
```properties
Java 17+
Maven 
PostgreSql 14+
```

### Configurar PostgreSQL
```sql
sudo systemctl status postgresql
sudo -u postgres psql
postgres=# CREATE USER testing WITH PASSWORD 'testing';
postgres=# CREATE DATABASE fibonacci_bqm OWNER testing;
postgres=# GRANT ALL PRIVILEGES ON DATABASE fibonacci_bqm TO testing;
```

### En application.properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fibonacci_bqm
spring.datasource.username=testing
spring.datasource.password=testing
```

### Endpoints

#### GET /fibonacci?n=50
#### GET /estadisticas/top?limite=10
#### GET /estadisticas/all


### Ejecutar app
- Verificar puerto en application.properties (server.port=8081)
- Tener postgresql levantado
```properties
mvn spring-boot:run
```