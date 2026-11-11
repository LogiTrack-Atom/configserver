# configserver

Servidor de configuración de LogiTrack Atom. Java 25, Spring Boot 4.1.1 y Spring Cloud 2025.1.4; puerto interno 8085. Responsable principal: [DiazGab](https://github.com/DiazGab).

Lee [configserver-ms](https://github.com/LogiTrack-Atom/configserver-ms) mediante Git. No necesita MySQL ni incorpora Config Client. La clase de arranque mantiene su nombre original `DemoApplication` para conservar la estructura existente.

## Compilar y comprobar

```bash
./mvnw -B -ntp verify javadoc:javadoc
# JAR independiente:
java -jar target/configserver-0.0.1-SNAPSHOT.jar
# Imagen construida íntegramente desde fuentes:
docker build -t logitrack-configserver:local .
docker run --rm --name configserver-local -p 127.0.0.1:8085:8085 logitrack-configserver:local
```

Las pruebas de contexto usan exclusivamente un backend local de fixture; la integración Git se comprueba por separado con la imagen real. La imagen final contiene JRE 25, aplicación y sonda Java de salud; ejecuta como UID/GID 10001, sin curl ni herramientas de compilación. El contexto excluye Git, pruebas, target, archivos de entorno y claves. No se deben guardar secretos en archivos de configuración ni pasarlos como argumentos de build.

## Configuración de arranque

| Variable | Valor predeterminado | Uso |
| --- | --- | --- |
| CONFIG_GIT_URI | https://github.com/LogiTrack-Atom/configserver-ms.git | Repositorio de propiedades |
| CONFIG_GIT_DEFAULT_LABEL | develop | Rama consultada si no se indica label |

`clone-on-start=true` hace que un repositorio inaccesible impida arrancar correctamente. El clon necesita salida HTTPS y escritura temporal; el usuario sin privilegios puede escribir en `/tmp`. No se configura refresco dinámico: los clientes reciben configuración al iniciar y deben reiniciarse para adoptar otra revisión.

## Verificar el contrato remoto

Elegir un commit o tag existente del repositorio de propiedades:

```bash
CONFIG_LABEL=b40bd4d0eecd5eff6e5ef0a7b23d0a106a39ba13
for app in shipment-service courier-service security-service; do
  for perfil in dev prod; do
    curl --fail "http://localhost:8085/$app/$perfil/$CONFIG_LABEL"
  done
done
curl --fail http://localhost:8085/actuator/health
```

Cada respuesta debe contener `version` igual a la revisión elegida, `propertySources` del archivo base y del perfil y el marcador `logitrack.config.marker` correspondiente. Los placeholders sensibles permanecen sin resolver: se resuelven en el entorno de cada cliente. `/actuator/env` y `/actuator/configprops` no se exponen. Config Server sirve propiedades; por eso este repositorio y su backend deben contener únicamente configuración no sensible.

El servidor rechaza configuraciones vacías (`accept-empty=false`); su sonda comprueba
shipment-service/dev para verificar una configuración que existe. Un label inexistente debe fallar; una respuesta vacía o sin marcador no acredita una configuración válida. El Compose general publica este puerto en loopback y conecta los clientes mediante la red interna. No se publica directamente en Internet.

## Integración y entrega

El Compose y el procedimiento de claves, bases e imágenes se mantienen en [shipment-service](https://github.com/LogiTrack-Atom/shipment-service/blob/develop/docs/sprint02/Compose.md). `configserver-ms` es un repositorio Git y no constituye un quinto contenedor de aplicación. Las ramas parten de `develop`, se vinculan a su issue y se integran mediante PR hacia `develop`.
