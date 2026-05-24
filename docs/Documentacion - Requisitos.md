# Sistema de Gestión para Ebanistería Ray

## Descripción

Este proyecto tiene como objetivo desarrollar un sistema de gestión para la empresa Ebanistería Ray, permitiendo administrar clientes, trabajos, cotizaciones, inventario y finanzas mediante una aplicación desarrollada en Java y SQL Server.

El sistema busca optimizar los procesos administrativos y mejorar el control de información dentro del negocio.


## Objetivo General

Desarrollar un sistema de gestión integral que permita administrar los procesos operativos y administrativos de la ebanistería.


## Objetivos Específicos

- Gestionar clientes.
- Registrar trabajos realizados.
- Generar cotizaciones.
- Controlar inventario de materiales.
- Registrar ingresos y egresos.
- Generar historial de trabajos por cliente.



# Requisitos Funcionales del Sistema

## 1. GESTIÓN DE CLIENTES

### Atributos de Cliente
- **Nombre** (obligatorio)
- **Teléfono** (obligatorio)
- **Email**
- **Dirección** (obligatorio)
- **Tipo de Cliente** (ej: Residencial, Comercial, etc.)
- **Fecha de Registro** (automática)
- **Observaciones** (notas generales)

### Funcionalidades
- ✅ Crear cliente
- ✅ Actualizar información del cliente
- ✅ Ver historial de trabajos del cliente
- ✅ Ver historial de cotizaciones del cliente
- ✅ Listar todos los clientes
- ✅ Buscar cliente por nombre/teléfono

---

## 2. GESTIÓN DE TRABAJOS

### Atributos de Trabajo
- **Cliente** (relación obligatoria)
- **Descripción** (obligatorio)
- **Tipo de Trabajo** (ej: Mueble, Reparación, etc.)
- **Estado** (obligatorio):
  - Cotizado
  - En Proceso
  - Terminado
  - Cancelado
- **Fecha de Inicio** (obligatorio)
- **Fecha de Entrega** (obligatorio)
- **Cotización Asociada** (opcional)
- **Valor Total del Trabajo**

### Funcionalidades
- ✅ Crear trabajo
- ✅ Actualizar estado del trabajo
- ✅ Actualizar información del trabajo
- ✅ Listar trabajos por estado
- ✅ Ver trabajos pendientes (En proceso, Cotizado)
- ✅ Asociar/desasociar cotización a trabajo

---

## 3. GESTIÓN DE COTIZACIONES

### Atributos de Cotización
- **Cliente** (relación obligatoria)
- **Trabajo** (relación opcional - se asigna después)
- **Descripción del Trabajo**
- **Costo de Materiales** (obligatorio)
- **Costo de Mano de Obra** (obligatorio)
- **Costo de Transporte**
- **Costo Total** (calculado: materiales + mano de obra + transporte)
- **Estado** (obligatorio):
  - Pendiente
  - Aceptada
  - Rechazada
- **Fecha de Creación** (automática)
- **Fecha de Actualización** (automática)
- **Observaciones**
- **Historial de Cambios** (registrar cada actualización)

### Funcionalidades
- ✅ Crear cotización
- ✅ Actualizar cotización (y registrar cambios)
- ✅ Cambiar estado de cotización
- ✅ Listar cotizaciones por estado
- ✅ Ver cotizaciones de un cliente específico
- ✅ Convertir cotización aceptada en trabajo

---

## 4. GESTIÓN DE FINANZAS

### Ingresos
- **Ingresos por Trabajos**:
  - Valor total del trabajo
  - Desglose: Materiales + Mano de Obra + Transporte
  - Estado de pago (Pagado, Pendiente, Parcial)
  - Fecha de pago

### Egresos
- **Gastos Operacionales**:
  - Costo de materiales comprados
  - Arriendo
  - Energía
  - Otros gastos
- Fecha de egreso
- Descripción

### Reportes Financieros
- ✅ Ingresos totales (período)
- ✅ Egresos totales (período)
- ✅ Ganancia neta (período)
- ✅ Ganancia por trabajo
- ✅ Desglose de ingresos por tipo
- ✅ Desglose de egresos por categoría

---

## 5. GESTIÓN DE INVENTARIO

### Atributos de Material
- **Nombre del Material** (obligatorio)
- **Categoría** (ej: Madera, Herrajes, Pintura, etc.)
- **Cantidad en Stock** (obligatorio)
- **Unidad de Medida** (ej: kg, metros, unidades, etc.)
- **Cantidad Mínima** (para alertas)
- **Costo Unitario**
- **Proveedores** (información de contacto)
- **Observaciones**

### Funcionalidades
- ✅ Crear material
- ✅ Actualizar stock
- ✅ Registrar entrada/salida de material
- ✅ Listar materiales con stock bajo
- ✅ **Alerta de Stock Bajo**: Papá recibe notificación cuando stock < cantidad mínima
- ✅ Ver historial de movimientos por material
- ✅ Buscar material por nombre/categoría

---

## 6. GESTIÓN DE USUARIOS Y ACCESO

### Usuarios del Sistema
1. **Papá** - Administrador
   - Acceso a todo
   - Recibe alertas de stock bajo
   - Autorización de operaciones críticas
   
2. **Mamá** - Usuario Regular
   - Acceso a todo
   - Solo lectura en finanzas críticas
   
3. **Hermano** - Usuario Regular
   - Acceso a todo
   
4. **Desarrollador** - Usuario Regular
   - Acceso a todo

### Funcionalidades
- ✅ Autenticación con usuario/contraseña
- ✅ Sesiones seguras (JWT)
- ✅ Roles y permisos
- ✅ Log de auditoría (quién cambió qué y cuándo)

---

## 7. NOTIFICACIONES Y ALERTAS

### Alertas del Sistema
- 📢 **Stock Bajo**: Cuando material < cantidad mínima
- 📢 **Trabajos Próximos a Vencer**: Trabajos en proceso con fecha cercana
- 📢 **Pagos Pendientes**: Trabajos terminados sin pago

### Canales de Notificación
- Dashboard del sistema (pop-ups)
- Email (opcional - Fase 2)
- WhatsApp (opcional - Fase 2)

---

## 8. REQUISITOS NO FUNCIONALES

- **Seguridad**: Contraseñas hasheadas, JWT para sesiones
- **Rendimiento**: Respuestas en < 500ms
- **Confiabilidad**: Backups automáticos
- **Usabilidad**: Interfaz intuitiva para no-técnicos
- **Escalabilidad**: Preparado para crecer

---

## 📊 Priorización

### MVP - Fase 1 (Alto)
1. Gestión de Clientes
2. Gestión de Trabajos
3. Gestión de Cotizaciones

### Fase 2 (Medio)
4. Gestión de Finanzas
5. Alertas de Stock

### Fase 3 (Bajo)
6. Reportes avanzados
7. Notificaciones por email/WhatsApp
8. PDF


# Tecnologías Utilizadas

- Java
- Swing
- SQL Server
- JDBC
- Arquitectura MVC


# Módulos del Sistema

- Clientes
- Trabajos
- Cotizaciones
- Inventario
- Finanzas
- Usuarios


# Relaciones del Sistema

- Un cliente puede tener múltiples trabajos.
- Un trabajo pertenece a un cliente.
- Una cotización puede convertirse en trabajo.
- Un trabajo puede tener múltiples pagos.


# Estados de Trabajo

- Cotizado
- Pendiente
- En proceso
- Terminado
- Entregado
- Cancelado
