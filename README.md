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

> Una clase debe tener una única razón para cambiar. - Robert C. Martin

Cada clase del código refactorizado tiene una única responsabilidad:
- `EmailValidator` -> Solo valida emails
- `PasswordValidator` -> Solo valida contraseñas
- `DatabaseManager` -> Solo maneja la base de datos
- `EmailSender` -> Solo envía emails
- `UserValidator` -> Solo valida usuarios
- `UserController` -> Solo orquesta el proceso de registro
