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

---

# Refactorización Liskov Substitution Principle (LSP)

## Problema Original

El código original en `lsp/original/` violaba el principio LSP al definir comportamiento en la clase base `Animal` que no todas las subclases pueden cumplir:

```java
public class Animal {
  public void makeSound() { }
  public void walk() {
    System.out.println("Animal is walking.");
  }
}

public class Dog extends Animal {
  @Override
  public void makeSound() {
    System.out.println("Dog barks.");
  }
}

public class Fish extends Animal {
  @Override
  public void walk() {
    throw new UnsupportedOperationException("Fish can't walk.");
  }
}
```

**Problema:** `Fish` es una subclase de `Animal`, pero no puede cumplir el contrato de `walk()` sin lanzar una excepción. Esto viola LSP porque un objeto `Animal` (que podría ser un `Fish`) no puede ser substituido por su subclase sin romper el comportamiento esperado.

## Estructura Refactorizada

```
lsp/modified/
  Main.java
  Animal.java           # Clase base solo con comportamiento común
  Dog.java              # Implementa IWalkingAnimal
  Fish.java             # Solo extiende Animal (sin walk)
  IWalkingAnimal.java   # Interfaz para comportamiento de caminar
```

## Cambios Realizados

### 1. Extracción de la Interfaz IWalkingAnimal

**Antes:** El método `walk()` estaba en la clase base `Animal`.

**Después:** Se creó una interfaz `IWalkingAnimal` que solo define el comportamiento de caminar:

```java
public interface IWalkingAnimal {
  public void walk();
}
```

**Mejora:** Solo las clases que pueden caminar implementan esta interfaz. `Fish` ya no necesita lanzar excepciones.

### 2. Eliminación de walk() de Animal

**Antes:** `Animal` tenía un método `walk()` por defecto.

**Después:** `Animal` solo contiene el comportamiento común a todos los animales (`makeSound()`).

**Mejora:** La clase base ahora representa correctamente el denominador común de todos los animales.

### 3. Implementación de IWalkingAnimal en Dog

**Antes:** `Dog` heredaba `walk()` de `Animal` sin problemas, pero la jerarquía era incorrecta.

**Después:** `Dog` implementa `IWalkingAnimal` explícitamente:

```java
public class Dog extends Animal implements IWalkingAnimal {
  @Override
  public void walk() {
    System.out.println("Dog walks.");
  }
}
```

**Mejora:** `Dog` puede ser substituido por `IWalkingAnimal` sin problemas.

### 4. Simplificación de Fish

**Antes:** `Fish` lanzaba `UnsupportedOperationException` en `walk()`.

**Después:** `Fish` simplemente extiende `Animal` sin sobrescribir ningún método:

```java
public class Fish extends Animal {
}
```

**Mejora:** `Fish` ya no viola el principio. No necesita proporcionar una implementación vacía o lanzar excepciones.

## Principio Aplicado

> Los objetos de una superclase deben poder ser substituidos por objetos de sus subclases sin afectar el comportamiento del programa.

La refactorización cumple con LSP porque:
- **Sustitución válida:** Un `Animal` puede ser cualquier subclase (`Dog` o `Fish`) sin que `walk()` lance excepciones
- **Contrato respetado:** `IWalkingAnimal` define un contrato claro que solo las clases que pueden caminar implementan
- **Sin excepciones en tiempo de ejecución:** Ya no hay `UnsupportedOperationException` cuando se llama `walk()` en un objeto esperado como `IWalkingAnimal`

## Puntos de Mejora Fuera del Principio

### 1. Uso de cast en Main.java

**Código actual:**
```java
Animal dog = new Dog();
dog.makeSound();
((Dog) dog).walk();  // Cast necesario para llamar walk()
```

**Problema:** Si `dog` está declarado como `Animal`, se necesita un cast para acceder a `walk()`. Esto indica que la declaración de tipo no refleja completamente el comportamiento del objeto.

**Mejora sugerida:** Usar la interfaz directamente cuando se necesite comportamiento específico:
```java
IWalkingAnimal dog = new Dog();
dog.walk();
```

O mantener ambas referencias según el contexto:
```java
Animal dog = new Dog();
dog.makeSound();

IWalkingAnimal walkingDog = new Dog();
walkingDog.walk();
```

### 2. Decisión de diseño sobre la referencia Animal

La existencia de `Animal dog = new Dog()` y `Animal fish = new Fish()` en `Main.java` sugiere que se desea tratar a ambos polimórficamente. Sin embargo, si el objetivo es usar `walk()` en `Dog`, la referencia debería ser `IWalkingAnimal`. Se mantuvo este patrón deliberadamente para demostrar que:

- **LSP se cumple:** Ambos pueden ser `Animal` sin violar el contrato de `makeSound()`
- **El cast muestra la limitación:** Cuando se necesita comportamiento específico, la jerarquía actual requiere decidir entre flexibilidad (usar `Animal`) o tipo específico (usar `IWalkingAnimal`)

### Nota sobre la Decisión de Diseño

Esta implementación demuestra el principio LSP correctamente: cualquier subclase de `Animal` puede substituir a `Animal` sin romper el programa. Sin embargo, en un diseño real, si muchos animales necesitan comportamientos específicos como `walk()`, podría considerarse si `Animal` es la abstracción correcta o si debería definirse desde el inicio como una interfaz más granular.

---

# Refactorización Interface Segregation Principle (ISP)

## Problema Original

El código original en `isp/original/` violaba el principio ISP al definir una interfaz `Device` con métodos que no todas las implementaciones necesitaban:

```java
interface Device {
  void turnOn();
  void turnOff();
  void charge();
}

class DisposableCamera implements Device {
  @Override
  public void turnOn() { }
  @Override
  public void turnOff() { }
  @Override
  public void charge() {
    throw new UnsupportedOperationException("Disposable cameras cannot be charged.");
  }
}
```

**Problema:** `DisposableCamera` se veía forzada a implementar `charge()` aunque su hardware no lo permitía, violando ISP. Esto resulta en métodos vacíos o que lanzan excepciones.

## Estructura Refactorizada

```
isp/modified/
  Main.java
  Phone.java               # Implementa Device y Chargeable
  DisposableCamera.java    # Solo implementa Device
  interfaces/
    Device.java            # Interfaz con solo turnOn/turnOff
    Chargeable.java         # Interfaz separada para charge
```

## Cambios Realizados

### 1. Separación de la Interfaz Device

**Antes:** Una única interfaz `Device` con tres métodos.

**Después:** Dos interfaces especializadas:
- `Device` - solo `turnOn()` y `turnOff()`
- `Chargeable` - solo `charge()`

**Mejora:** Cada interfaz representa un comportamiento específico y cohesivo.

### 2. Implementación de Chargeable en Phone

**Antes:** `Phone` implementaba `Device` que incluía `charge()`.

**Después:** `Phone` implementa ambas interfaces:

```java
public class Phone implements Device, Chargeable {
  @Override
  public void turnOn() { }
  @Override
  public void turnOff() { }
  @Override
  public void charge() {
    System.out.println("Phone is charging.");
  }
}
```

**Mejora:** `Phone` declara explícitamente que puede cargarse.

### 3. Simplificación de DisposableCamera

**Antes:** `DisposableCamera` implementaba `Device` forzadamente y lanzaba excepción en `charge()`.

**Después:** `DisposableCamera` solo implementa `Device`:

```java
class DisposableCamera implements Device {
  @Override
  public void turnOn() {
    System.out.println("Disposable camera is turning on.");
  }
  @Override
  public void turnOff() {
    System.out.println("Disposable camera is turning off.");
  }
}
```

**Mejora:** Ya no necesita implementar métodos que no puede cumplir.

### 4. Actualización de Main.java

**Antes:**
```java
Device phone = new Phone();
Device camera = new DisposableCamera();
phone.charge(); // Funciona, pero no hay forma de saber en tiempo de compilación si un Device es chargeable
camera.charge(); // Lanza UnsupportedOperationException en tiempo de ejecución
```

**Después:**
```java
Device phone = new Phone();
Device camera = new DisposableCamera();
phone.turnOn();
((Chargeable) phone).charge();
camera.turnOn();
// camera.charge() no es accesible, el compilador lo impide
```

**Mejora:** El código que intenta cargar un dispositivo que no es `Chargeable` falla en tiempo de compilación, no en ejecución.

## Principio Aplicado

> Los clientes no deben verse forzados a depender de interfaces que no utilizan.

La refactorización cumple con ISP porque:
- **Interfaces granulares:** `Device` y `Chargeable` son interfaces pequeñas y enfocadas
- **Sin Implementaciones Vacías:** `DisposableCamera` no implementa métodos que no necesita
- **Type Safety:** El compilador previene errores de intentar cargar dispositivos no cargables

## Puntos de Mejora Fuera del Principio

### Cast en Main.java

**Código actual:**
```java
((Chargeable) phone).charge();
```

**Problema:** El uso de cast indica que `phone` declarado como `Device` no expone el método `charge()`. Esto fuerza al programador a conocer el tipo concreto.

**Mejora sugerida:** Si se necesita cargar dispositivos, considerar una función que recibe `Chargeable` directamente:
```java
private static void chargeDevice(Chargeable device) {
    device.charge();
}
```

O declarar la variable con el tipo más específico desde el inicio:
```java
Phone phone = new Phone();
phone.charge();
```

### Consideración de Diseño

La existencia de `((Chargeable) phone).charge()` en `Main.java` demuestra que a veces se necesita trabajar con interfaces específicas. Si el código requiere frecuentemente cargar dispositivos, podría tener sentido que `Chargeable` sea la interfaz principal y `Device` solo un extra. Sin embargo, el diseño actual es válido para ISP y demuestra correctamente el principio.

---

# Refactorización Dependency Inversion Principle (DIP)

## Problema Original

El código original en `dip/original/PaymentProcessor.java` violaba el principio DIP al depender directamente de una implementación concreta:

```java
public class PaymentProcessor {
  private CreditCardPayment payment;

  public PaymentProcessor() {
    this.payment = new CreditCardPayment();
  }

  public void makePayment(double amount) {
    payment.processPayment(amount);
  }
}
```

**Problema:** `PaymentProcessor` depende directamente de `CreditCardPayment`. Si se quisiera usar otro método de pago (PayPal, Crypto), sería necesario modificar `PaymentProcessor`, violando el principio.

## Estructura Refactorizada

```
dip/modified/
  Main.java
  PaymentMethod.java      # Interfaz que define el contrato
  PaymentProcessor.java  # Depende de la abstracción
  paymentMethods/
    CreditCardPayment.java
    PayPalPayment.java
    CryptoPayment.java
```

## Cambios Realizados

### 1. Creación de la Interfaz PaymentMethod

**Antes:** `PaymentProcessor` dependía de la clase concreta `CreditCardPayment`.

**Después:** Se creó una interfaz `PaymentMethod` que define el contrato:

```java
public interface PaymentMethod {
  void processPayment(double amount);
}
```

**Mejora:** Cualquier método de pago ahora puede implementar esta interfaz sin acoplar `PaymentProcessor` a una implementación específica.

### 2. Modificación de PaymentProcessor para Depender de la Abstracción

**Antes:**
```java
private CreditCardPayment payment;
```

**Después:**
```java
private PaymentMethod paymentMethod;
```

**Mejora:** `PaymentProcessor` ahora depende de una abstracción, no de una implementación concreta.

### 3. Inyección de Dependencias via Constructor

**Antes:**
```java
public PaymentProcessor() {
  this.payment = new CreditCardPayment();
}
```

**Después:**
```java
public PaymentProcessor(PaymentMethod paymentMethod) {
  this.paymentMethod = paymentMethod;
}
```

**Mejora:**
- `PaymentProcessor` ya no crea sus dependencias, las recibe
- Fácil de probar con mocks
- Las dependencias son explícitas

### 4. Extracción de Clases Concretas

**Antes:** Solo existía `CreditCardPayment` y estaba acoplada a `PaymentProcessor`.

**Después:** Cada método de pago es una clase independiente:

```java
public class CreditCardPayment implements PaymentMethod {
  @Override
  public void processPayment(double amount) {
    System.out.println("Processing credit card payment of $" + amount);
  }
}

public class PayPalPayment implements PaymentMethod {
  @Override
  public void processPayment(double amount) {
    System.out.println("Processing PayPal payment of $" + amount);
  }
}

public class CryptoPayment implements PaymentMethod {
  @Override
  public void processPayment(double amount) {
    System.out.println("Processing crypto payment of $" + amount);
  }
}
```

**Mejora:** Cada clase tiene una única responsabilidad. Agregar un nuevo método de pago no requiere modificar los existentes ni `PaymentProcessor`.

### 5. Actualización de Main.java

**Antes:**
```java
PaymentProcessor processor = new PaymentProcessor();
processor.makePayment(150.0);
```

**Después:**
```java
PaymentMethod creditCardPayment = new CreditCardPayment();
PaymentProcessor creditCardProcessor = new PaymentProcessor(creditCardPayment);
creditCardProcessor.makePayment(150.0);

PaymentMethod payPalPayment = new PayPalPayment();
PaymentProcessor payPalProcessor = new PaymentProcessor(payPalPayment);
payPalProcessor.makePayment(300.0);
```

**Mejora:** El cliente decide qué implementación usar y la inyecta en `PaymentProcessor`.

## Principio Aplicado

> Las entidades de software deben depender de abstracciones, no de concreciones.

La refactorización cumple con DIP porque:
- **Módulo de alto nivel independientes:** `PaymentProcessor` no depende de concreciones
- **Abstracciones correctas:** `PaymentMethod` define el contrato entre módulos
- **Módulos de bajo nivel dependen de abstracciones:** `CreditCardPayment`, `PayPalPayment`, `CryptoPayment` implementan `PaymentMethod`

## Puntos de Mejora Fuera del Principio

### Instanciación en Main.java

**Código actual:**
```java
PaymentMethod creditCardPayment = new CreditCardPayment();
```

**Problema:** `Main.java` todavía instancia las clases concretas directamente. En un diseño más avanzado, se podría usar un contenedor de inyección de dependencias o un Factory pattern para desacoplar completamente el código cliente de las implementaciones.

**Mejora sugerida:** Usar un contenedor DI como Spring o Guice:
```java
public static void main(String[] args) {
  PaymentProcessor processor = container.get(PaymentProcessor.class);
  processor.makePayment(150.0);
}
```

O un Factory simple:
```java
PaymentProcessor processor = PaymentProcessorFactory.create("creditcard");
```

### Consideración de Diseño

La inyección de dependencias via constructor es correcta y demuestra el principio DIP. Sin embargo, en aplicaciones reales, el uso de un contenedor DI o framework (Spring, Guice, Dagger) facilita la gestión de dependencias en código a mayor escala.
