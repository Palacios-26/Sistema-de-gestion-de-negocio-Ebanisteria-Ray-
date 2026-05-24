# 🍓 Optimización para Raspberry Pi - Sistema Ebanistería Ray

> **Objetivo**: Ejecutar el sistema completo en una Raspberry Pi con bajo consumo de recursos

---

## 📋 Especificaciones Recomendadas

### Hardware

| Componente | Recomendado | Mínimo |
|-----------|-----------|--------|
| Placa | Raspberry Pi 4 | Pi 3B+ |
| RAM | 4GB | 2GB |
| Almacenamiento | SSD 256GB | SD 32GB |
| Poder | 27W | 15W |
| Refrigeración | Disipador | Ventilador |

### Red

```
┌─────────────────────────────────────┐
│   Raspberry Pi (192.168.1.100)      │
│   ├─ PostgreSQL (5432)              │
│   ├─ Backend Spring Boot (8080)     │
│   └─ Frontend Nginx (80)            │
└─────────────────────────────────────┘
         ↓
┌────────────────────────────────┐
│  LAN (tu casa/oficina)         │
│  Acceso desde:                 │
│  - PC: http://192.168.1.100    │
│  - Mobile: http://192.168.1.100│
└────────────────────────────────┘
```

---

## 🔧 Preparación del SO

### Paso 1: Instalar Raspberry Pi OS Lite

**Herramienta**: Raspberry Pi Imager

```bash
# Descargar de:
https://www.raspberrypi.com/software/

# Opción recomendada:
- Raspberry Pi OS (64-bit) Lite
- Habilitar SSH en configuración avanzada
- Configurar WiFi
```

### Paso 2: Actualizar Sistema

```bash
ssh pi@192.168.1.100  # o la IP que obtengas

# Dentro de la Pi:
sudo apt-get update
sudo apt-get upgrade -y
sudo apt-get install -y curl git
```

### Paso 3: Aumentar Swap (importante para compilaciones)

```bash
# Ver swap actual
free -h

# Aumentar a 2GB
sudo dphys-swapfile swapoff
sudo sed -i 's/^CONF_SWAPSIZE=.*/CONF_SWAPSIZE=2048/' /etc/dphys-swapfile
sudo dphys-swapfile swapon

# Verificar
free -h
```

---

## 🐳 Instalar Docker y Docker Compose

### Paso 1: Instalar Docker

```bash
# Script oficial de Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Agregar usuario 'pi' al grupo docker
sudo usermod -aG docker pi

# Logout y login nuevamente para aplicar cambios
exit
ssh pi@192.168.1.100
```

### Paso 2: Instalar Docker Compose

```bash
# Opción 1: Desde pip (más rápido)
sudo apt-get install -y python3-pip
pip3 install docker-compose

# Opción 2: Descargarlo precompilado (alternativa)
# Seguir documentación oficial
```

### Paso 3: Verificar Instalación

```bash
docker --version
docker-compose --version
```

---

## 📦 Optimizar Imágenes Docker

### Backend: Dockerfile Optimizado

Crear `backend/Dockerfile`:

```dockerfile
# Multi-stage build para reducir tamaño
FROM eclipse-temurin:17-jre-alpine as builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven && \
    mvn clean package -DskipTests

# Imagen final (pequeña)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar JAR compilado
COPY --from=builder /build/target/*.jar app.jar

# Crear usuario no-root
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser
USER appuser

# Configuración JVM optimizada para Raspberry Pi
ENV JAVA_OPTS="-Xmx384m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD java -cp /app:/ -c "import java.net.*; new URL(\"http://localhost:8080/actuator/health\").openConnection().getResponseCode();" 2>/dev/null || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Frontend: Dockerfile Optimizado

Crear `frontend/Dockerfile`:

```dockerfile
# Build
FROM node:18-alpine as builder
WORKDIR /build
COPY package*.json .
RUN npm ci && npm cache clean --force
COPY . .
RUN npm run build

# Servidor Nginx ligero
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Copiar archivos compilados
COPY --from=builder /build/dist .

# Configuración Nginx optimizada
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

CMD ["nginx", "-g", "daemon off;"]
```

### Nginx Configuration

Crear `frontend/nginx.conf`:

```nginx
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log warn;
pid /var/run/nginx.pid;

events {
    worker_connections 512;
    use epoll;
}

http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    # Logging
    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for"';

    access_log /var/log/nginx/access.log main;

    # Optimización
    sendfile on;
    tcp_nopush on;
    tcp_nodelay on;
    keepalive_timeout 65;
    types_hash_max_size 2048;
    client_max_body_size 20M;

    # Compresión GZIP
    gzip on;
    gzip_vary on;
    gzip_comp_level 6;
    gzip_types text/plain text/css text/xml text/javascript 
               application/json application/javascript application/xml+rss 
               application/rss+xml application/atom+xml image/svg+xml;

    # Cache
    map $sent_http_content_type $expires {
        default off;
        text/html epoch;
        text/css max;
        application/javascript max;
        ~image/ max;
    }

    server {
        listen 80;
        server_name _;

        expires $expires;

        location / {
            root /usr/share/nginx/html;
            try_files $uri $uri/ /index.html;
        }

        location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
            expires 7d;
            add_header Cache-Control "public, immutable";
        }
    }
}
```

---

## 🗄️ PostgreSQL en Raspberry Pi

### docker-compose.yml Optimizado

```yaml
version: '3.8'

services:
  # PostgreSQL ligero
  postgres:
    image: postgres:15-alpine
    container_name: ebanisteria-db
    restart: unless-stopped
    environment:
      POSTGRES_USER: ebanisteria
      POSTGRES_PASSWORD: ebanisteria123
      POSTGRES_DB: ebanisteria_db
      POSTGRES_INITDB_ARGS: "-c shared_buffers=128MB -c max_connections=50"
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - ebanisteria-net
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ebanisteria -d ebanisteria_db"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Backend
  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    container_name: ebanisteria-api
    restart: unless-stopped
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/ebanisteria_db
      SPRING_DATASOURCE_USERNAME: ebanisteria
      SPRING_DATASOURCE_PASSWORD: ebanisteria123
      SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE: 10
      SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE: 2
      JAVA_OPTS: "-Xmx384m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
    depends_on:
      postgres:
        condition: service_healthy
    networks:
      - ebanisteria-net
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/api/actuator/health"]
      interval: 30s
      timeout: 5s
      retries: 3

  # Frontend
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    container_name: ebanisteria-web
    restart: unless-stopped
    ports:
      - "80:80"
      - "443:443"  # Si configuras HTTPS
    depends_on:
      - backend
    networks:
      - ebanisteria-net
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost/"]
      interval: 30s
      timeout: 5s
      retries: 3

volumes:
  postgres_data:
    driver: local

networks:
  ebanisteria-net:
    driver: bridge
```

---

## 🚀 Desplegar en Raspberry Pi

### Paso 1: Preparar Codigo

```bash
# En tu máquina local, preparar proyecto
git add .
git commit -m "Preparar para deployment en RPi"
git push origin develop
```

### Paso 2: Clonar en Raspberry Pi

```bash
# SSH a la Pi
ssh pi@192.168.1.100

# Clonar repositorio
git clone https://github.com/Palacios-26/Sistema-de-gestion-de-negocio-Ebanisteria-Ray-.git
cd Sistema-de-gestion-de-negocio-Ebanisteria-Ray-

# Checkout de rama (puede estar en develop)
git checkout develop
```

### Paso 3: Build y Deploy

```bash
# Construir imágenes Docker (tomará tiempo, ~30 min)
docker-compose build

# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f

# Esperar a que todo esté listo...
```

### Paso 4: Verificar que Funciona

```bash
# En otra terminal:
curl http://localhost/  # Frontend
curl http://localhost:8080/api  # Backend
docker exec ebanisteria-db psql -U ebanisteria -d ebanisteria_db -c "SELECT 1;"  # DB
```

---

## 📱 Acceso desde Móvil / PC

### En la LAN

```
1. Descubre IP de Raspberry Pi:
   En router: 192.168.1.100 (ejemplo)
   O: ssh pi@192.168.1.100
      hostname -I

2. En cualquier dispositivo de la misma red:
   http://192.168.1.100  (Frontend)
   http://192.168.1.100:8080  (Backend API)
```

### IP Estática (Recomendado)

```bash
# En Raspberry Pi
sudo nano /etc/dhcpcd.conf

# Agregar al final:
interface wlan0
static ip_address=192.168.1.100/24
static routers=192.168.1.1
static domain_name_servers=8.8.8.8 8.8.4.4

# Guardar: Ctrl+O, Enter, Ctrl+X
# Reiniciar
sudo reboot
```

---

## 💾 Backups Automáticos

### Script de Backup

Crear `backup.sh` en Raspberry Pi:

```bash
#!/bin/bash

BACKUP_DIR="/home/pi/backups"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
DB_NAME="ebanisteria_db"
DB_USER="ebanisteria"

# Crear directorio si no existe
mkdir -p $BACKUP_DIR

# Backup de Base de Datos
docker exec ebanisteria-db pg_dump -U $DB_USER $DB_NAME | \
    gzip > "$BACKUP_DIR/db_backup_$TIMESTAMP.sql.gz"

# Backup de datos persistentes (si hay)
tar -czf "$BACKUP_DIR/data_backup_$TIMESTAMP.tar.gz" \
    -C /var/lib/docker/volumes ebanisteria_postgres_data

# Mantener últimos 7 backups
find $BACKUP_DIR -name "*.gz" -mtime +7 -delete

echo "Backup completado: $TIMESTAMP"
```

### Programar con Cron

```bash
# Editar crontab
crontab -e

# Agregar línea para ejecutar diariamente a las 2:00 AM
0 2 * * * /home/pi/backup.sh >> /var/log/ebanisteria-backup.log 2>&1
```

---

## 🔐 Configurar HTTPS (Opcional pero recomendado)

### Con Let's Encrypt (si tienes dominio)

```bash
# Instalar Certbot
sudo apt-get install certbot python3-certbot-nginx

# Obtener certificado
sudo certbot certonly --standalone -d tu-dominio.com

# Configurar Nginx para usar certificado
# (modificar nginx.conf para redirigir HTTP a HTTPS)
```

### Para desarrollo local (sin dominio)

```bash
# Generar certificado auto-firmado
openssl req -x509 -newkey rsa:4096 -nodes -out cert.pem -keyout key.pem -days 365

# Copiar a Pi y configurar en Nginx
```

---

## 📊 Monitoreo

### Ver uso de recursos

```bash
# En vivo
docker stats

# Histórico
docker logs -f ebanisteria-api
docker logs -f ebanisteria-web
```

### Alertas de Memoria

```bash
# Si la Pi se queda sin RAM:
# 1. Aumentar swap (ya hecho arriba)
# 2. Reducir -Xmx de JVM (en docker-compose.yml)
# 3. Optimizar BD (indices)
```

---

## 🔄 Actualizar Sistema

### Actualizar código

```bash
ssh pi@192.168.1.100
cd Sistema-de-gestion-de-negocio-Ebanisteria-Ray-
git pull origin develop
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

### Rollback si algo falla

```bash
# Mantener última versión de imagen
docker images | grep ebanisteria

# Usar versión anterior
docker-compose down
git checkout HEAD~1
docker-compose up -d
```

---

## 🐛 Troubleshooting

### "Out of memory"

```bash
# Ver memoria actual
free -h

# Reducir JVM memory en docker-compose.yml
# De: -Xmx384m
# A: -Xmx256m

# Reiniciar
docker-compose down
docker-compose up -d
```

### "Disk space full"

```bash
# Ver espacio
df -h

# Limpiar imágenes viejas
docker image prune -a

# Limpiar contenedores parados
docker container prune

# Limpiar volúmenes no usados
docker volume prune
```

### Backend no levanta

```bash
# Ver logs
docker logs ebanisteria-api -f

# Verificar conexión a BD
docker exec ebanisteria-db psql -U ebanisteria -d ebanisteria_db -c "SELECT 1;"

# Si la BD no responde, reiniciar
docker-compose restart postgres
```

### Frontend no carga estilos

```bash
# Limpiar cache navegador (Ctrl+Shift+Delete)
# O:
docker-compose restart frontend
```

---

## 📈 Performance Tips

| Acción | Beneficio |
|--------|-----------|
| Usar SSD en lugar de SD | 10x más rápido |
| Agregar RAM a 4GB | Evita swap lento |
| Indexar campos en BD | 100x queries más rápidas |
| Caché en frontend (PWA) | Funciona offline |
| Compresión GZIP | 70% menos ancho |
| Lazy loading en tablas | Carga inicial rápida |

---

## ✅ Checklist Final

- [ ] Docker instalado y funcionando
- [ ] docker-compose.yml optimizado
- [ ] Dockerfiles compilados sin errores
- [ ] Base de datos persiste datos
- [ ] Backend accesible en puerto 8080
- [ ] Frontend accesible en puerto 80
- [ ] Acceso desde móvil en misma LAN
- [ ] Backups configurados
- [ ] Monitoreo de recursos activado
- [ ] Documentación de acceso compartida con familia

---

## 📚 Referencias

- [Raspberry Pi Docs](https://www.raspberrypi.com/documentation/)
- [Docker para Raspberry Pi](https://docs.docker.com/engine/install/raspberry-pi-os/)
- [PostgreSQL Performance](https://wiki.postgresql.org/wiki/Performance_Optimization)
- [Nginx Tuning](https://nginx.org/en/docs/http/ngx_http_gzip_module.html)

---

**Última actualización**: Mayo 2026

¡Tu sistema está listo para ser usado en producción! 🎉
