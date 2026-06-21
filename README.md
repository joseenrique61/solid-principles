# Refactorización Single Responsibility Principle (SRP)

## Problema Original

El código original en `srp/original/UserManager.java` violaba el principio SRP al tener múltiples responsabilidades en una sola clase:

- Validación de email
- Validación de contraseña
- Persistencia en base de datos
- Envío de emails de bienvenida
- Lógica de orquestación (coordinar todas las operaciones)

```java
class UserManager {
  public void addUser(String email, String password) {
    if (isValidEmail(email) && isValidPassword(password)) {
      saveToDatabase(email, password);   // Persistencia
      sendWelcomeEmail(email);           // Envío de email
    }
  }
  // ... métodos de validación, persistencia y envío mezclados
}
```

## Estructura Refactorizada

```
srp/modified/
  Main.java
  UserController.java      # Orquestación de la lógica de usuarios
  UserValidator.java       # Combina validaciones de email y contraseña
  DatabaseManager.java     # Acceso y persistencia en base de datos
  EmailSender.java         # Envío de emails
  models/
    User.java              # Modelo de dominio
  validators/
    EmailValidator.java    # Validación de email
    PasswordValidator.java # Validación de contraseña
```

## Cambios Realizados

### 1. Separación de Validadores

**Antes:** Los métodos `isValidEmail` e `isValidPassword` estaban en `UserManager`.

**Después:** Se crearon clases independientes:
- `validators/EmailValidator.java` - Validación de formato de email
- `validators/PasswordValidator.java` - Validación de longitud de contraseña

**Mejora:** Cada validador tiene una única responsabilidad. Son reutilizables en otros contextos y fáciles de probar por separado.

### 2. Extracción de DatabaseManager

**Antes:** El método `saveToDatabase` estaba en `UserManager`.

**Después:** `DatabaseManager.java` maneja toda la interacción con la base de datos.

**Mejora:** Si en el futuro cambia la implementación de persistencia (ej: de MySQL a PostgreSQL), solo se modifica `DatabaseManager`.

### 3. Extracción de EmailSender

**Antes:** El método `sendWelcomeEmail` estaba en `UserManager`.

**Después:** `EmailSender.java` encapsula el envío de emails.

**Mejora:** El envío de emails puede cambiar independientemente. Facilita mocking en pruebas.

### 4. Modelo User

**Antes:** Se usaban strings sueltos (email, password).

**Después:** Se creó `models/User.java` con encapsulamiento y getters/setters.

**Mejora:** Representa correctamente el concepto de dominio "Usuario". Permite extensión futura (agregar id, nombre, etc.) sin cambiar firmas de métodos.

### 5. UserValidator Combinado

**Antes:** La lógica de validación estaba dispersa.

**Después:** `UserValidator.java` combina ambas validaciones, manteniendo las reglas de negocio en un solo lugar.

**Mejora:** Centraliza la lógica de validación de usuario. Si cambian los requisitos, solo se modifica esta clase.

### 6. UserController con Inyección de Dependencias

**Antes:** `UserManager` instanciaba directamente sus dependencias.

**Después:** `UserController` recibe sus dependencias via constructor.

**Mejora:**
- Facilita la sustitución por mocks en pruebas
- Las dependencias son explícitas
- Reduce acoplamiento

## Principio Aplicado

> Una clase debe tener una única razón para cambiar.

Cada clase del código refactorizado tiene una única responsabilidad:
- `EmailValidator` -> Solo valida emails
- `PasswordValidator` -> Solo valida contraseñas
- `DatabaseManager` -> Solo maneja la base de datos
- `EmailSender` -> Solo envía emails
- `UserValidator` -> Solo valida usuarios
- `UserController` -> Solo orquesta el proceso de registro

---

# Refactorización Open/Closed Principle (OCP)

## Problema Original

El código original en `ocp/original/NotificationService.java` violaba el principio OCP al utilizar condicionales para determinar el tipo de notificación a enviar:

```java
public void sendNotification(String type, String message) {
  if (type.equals("Email")) {
    System.out.println("Sending Email: " + message);
  } else if (type.equals("SMS")) {
    System.out.println("Sending SMS: " + message);
  } else if (type.equals("Push")) {
    System.out.println("Sending Push Notification: " + message);
  } else {
    System.out.println("Invalid notification type!");
  }
}
```

**Problema:** Cada vez que se necesitaba agregar un nuevo tipo de notificación (Fax, WhatsApp, etc.), era necesario modificar este método, violando el principio de abierto para extensión, cerrado para modificación.

## Estructura Refactorizada

```
ocp/modified/
  Main.java
  NotificationProcessor.java  # Procesa notificaciones usando la interfaz
  NotificationService.java    # Interfaz que define el contrato
  notificationServices/
    EmailService.java          # Implementación para Email
    SmsService.java           # Implementación para SMS
    PushService.java          # Implementación para Push
    FaxService.java           # Implementación para Fax
    WhatsappService.java      # Implementación para WhatsApp
```

## Cambios Realizados

### 1. Creación de la Interfaz NotificationService

**Antes:** Un único método con lógica condicional para todos los tipos.

**Después:** Una interfaz simple que define el contrato:

```java
public interface NotificationService {
  void sendNotification(String message);
}
```

**Mejora:** Cualquier nuevo tipo de notificación solo necesita implementar esta interfaz.

### 2. Extracción de Notificaciones a Clases Concretas

**Antes:** `EmailService`, `SmsService`, etc. no existían.

**Después:** Cada tipo de notificación tiene su propia clase:

```java
public class EmailService implements NotificationService {
  public void sendNotification(String message) {
    System.out.println("Sending Email: " + message);
  }
}
```

**Mejora:** Cada clase tiene una única responsabilidad. Agregar un nuevo tipo no requiere modificar las existentes.

### 3. Introducción de NotificationProcessor

**Antes:** La selección del tipo de notificación se hacía mediante strings ("Email", "SMS").

**Después:** `NotificationProcessor` recibe cualquier implementación de `NotificationService`:

```java
public class NotificationProcessor {
  public void processNotification(NotificationService notificationService, String message) {
    notificationService.sendNotification(message);
  }
}
```

**Mejora:**
- Elimina el acoplamiento a tipos específicos
- Permite agregar nuevos tipos sin modificar este procesador
- Facilita las pruebas con mocks

### 4. Polimorfismo en Lugar de Condicionales

**Antes:**
```java
service.sendNotification("Email", "Hello!");
service.sendNotification("SMS", "Hello!");
```

**Después:**
```java
notificationProcessor.processNotification(new EmailService(), "Hello!");
notificationProcessor.processNotification(new SmsService(), "Hello!");
```

**Mejora:** La selección del tipo ocurre en tiempo de compilación (o mediante configuración), no en runtime con condicionales.

## Principio Aplicado

> Las entidades de software deben estar abiertas para extensión pero cerradas para modificación.

La refactorización cumple con OCP porque:
- **Abierto para extensión:** Para agregar `TelegramService` o `SlackService`, solo se crea una nueva clase que implemente `NotificationService`
- **Cerrado para modificación:** No es necesario modificar `NotificationProcessor` ni `NotificationService` para agregar nuevos tipos
