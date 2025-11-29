# Film Information API

REST API сервіс для пошуку детальної інформації про фільми з використанням OMDb API та функціоналу надсилання результатів пошуку на електронну пошту. Застосунок надає вичерпні дані про фільми включаючи рейтинги, акторів, режисерів, нагороди та касові збори.

## Технологічний стек

- **Java 17+** — основна мова програмування
- **Spring Boot 3.x** — фреймворк для побудови застосунку
- **Spring Web** — для створення RESTful API
- **Spring Mail** — для надсилання електронних листів
- **RestClient** — для інтеграції з OMDb API
- **Thymeleaf** — для генерації HTML шаблонів email
- **Lombok** — для зменшення boilerplate коду
- **Jackson** — для обробки JSON даних

## Функціональні можливості

Застосунок забезпечує наступний функціонал:

- Пошук детальної інформації про фільми за назвою
- Отримання даних з OMDb API включаючи постери, рейтинги, сюжет та технічні деталі
- Надсилання інформації про фільм на електронну пошту з оформленим HTML шаблоном
- Централізована обробка помилок з інформативними повідомленнями
- Підтримка CORS для інтеграції з фронтенд застосунками
- Валідація вхідних даних та обробка відсутніх результатів

## API Endpoints

### Пошук фільму за назвою

**GET** `/api/v1/films/{title}`

Повертає детальну інформацію про фільм, що найбільш відповідає вказаній назві.

**Параметри шляху:**
- `title` — назва фільму (наприклад, "Inception", "The Matrix")

**Приклад запиту:**
```http
GET /api/v1/films/Inception
```

**Приклад успішної відповіді (200 OK):**
```json
{
  "Title": "Inception",
  "Year": "2010",
  "Rated": "PG-13",
  "Released": "16 Jul 2010",
  "Runtime": "148 min",
  "Genre": "Action, Sci-Fi, Thriller",
  "Director": "Christopher Nolan",
  "Writer": "Christopher Nolan",
  "Actors": "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
  "Plot": "A thief who steals corporate secrets through the use of dream-sharing technology...",
  "Language": "English, Japanese, French",
  "Country": "United States, United Kingdom",
  "Awards": "Won 4 Oscars. 157 wins & 220 nominations total",
  "Poster": "https://m.media-amazon.com/images/M/MV5BMjAxMzY3...",
  "Ratings": [
    {
      "Source": "Internet Movie Database",
      "Value": "8.8/10"
    },
    {
      "Source": "Rotten Tomatoes",
      "Value": "87%"
    }
  ],
  "imdbRating": "8.8",
  "imdbVotes": "2,500,000",
  "BoxOffice": "$292,587,330"
}
```

**Коди відповідей:**
- `200 OK` — фільм успішно знайдено
- `404 Not Found` — фільм не знайдено в базі OMDb
- `503 Service Unavailable` — проблеми з доступом до зовнішнього API

### Надсилання інформації про фільм на email

**POST** `/api/v1/films/send/{recipientEmail}`

Надсилає детальну інформацію про фільм на вказану електронну адресу у форматі оформленого HTML листа.

**Параметри шляху:**
- `recipientEmail` — електронна адреса отримувача

**Тіло запиту:**
```json
{
  "subject": "Інформація про фільм Inception",
  "film": {
    "Title": "Inception",
    "Year": "2010",
    "Genre": "Action, Sci-Fi, Thriller",
    ...
  }
}
```

**Приклад запиту:**
```http
POST /api/v1/films/send/user@example.com
Content-Type: application/json

{
  "subject": "Ваш запит про фільм",
  "film": { ... }
}
```

**Код відповіді:**
- `200 OK` — лист успішно надіслано

## Архітектура застосунку

Проєкт організовано за трирівневою архітектурою з розділенням відповідальності:

**Controller Layer** — FilmController обробляє HTTP запити, валідує вхідні дані та делегує виконання бізнес-логіки сервісному шару. Контролер відповідає за формування коректних HTTP відповідей із відповідними статус-кодами.

**Service Layer** — містить два основних сервіси. FilmService виконує інтеграцію з OMDb API, обробляє отримані дані та валідує результати пошуку. EmailService відповідає за генерацію HTML контенту на основі Thymeleaf шаблонів та відправку листів через SMTP протокол.

**Model Layer** — Film представляє повну структуру даних про фільм з маппінгом JSON полів, Rating містить інформацію про рейтинги від різних джерел, EmailFilmContext інкапсулює дані для відправки листа включаючи тему та об'єкт фільму.

**Exception Handling** — централізований механізм обробки винятків через ExceptionControllerAdvice забезпечує уніфіковані відповіді при виникненні помилок різних типів.

## Інтеграція з OMDb API

Застосунок використовує RestClient для взаємодії з OMDb API. Сервіс виконує GET запити до зовнішнього API, передаючи назву фільму та API ключ для автентифікації.

OMDb API повертає детальну інформацію у форматі JSON, яка десеріалізується у модель Film за допомогою Jackson. Модель використовує анотації JsonProperty для коректного маппінгу полів, оскільки OMDb використовує заголовковий регістр для назв полів.

Після отримання відповіді сервіс валідує наявність обов'язкових полів для підтвердження успішного знаходження фільму. У разі відсутності результатів або проблем з мережею генеруються відповідні винятки ResourceNotFoundException або NetworkErrorException.

## Система надсилання Email

EmailService інтегровано з Spring Mail для відправки HTML листів через SMTP протокол. Система використовує Thymeleaf шаблонізатор для генерації професійно оформленого контенту.

HTML шаблон film-email-template містить структуровану розмітку для відображення всієї інформації про фільм включаючи постер, основні відомості, сюжет, рейтинги та технічні деталі. Thymeleaf контекст наповнюється даними про фільм, після чого шаблон обробляється у фінальний HTML контент.

Сервіс створює MIME повідомлення з підтримкою HTML форматування, встановлює необхідні заголовки включаючи відправника, отримувача та тему листа, та виконує відправку через налаштований SMTP сервер Gmail.

## Налаштування та запуск

### Вимоги

- Java 17 або новіша версія
- Maven 3.6+ або Gradle
- API ключ від OMDb (отримати на http://www.omdbapi.com/apikey.aspx)
- Gmail акаунт з налаштованим App Password для SMTP

### Змінні оточення

Створіть файл `.env` або встановіть змінні оточення:

```properties
BASE_API_URL=https://www.omdbapi.com
API_KEY=your_omdb_api_key
ALLOWED_ORIGIN=http://localhost:3000
MAIL_USERNAME=your_gmail@gmail.com
MAIL_PASSWORD=your_gmail_app_password
```

### Конфігурація application.yml

```yaml
api:
  base-url: ${BASE_API_URL}
  key: ${API_KEY}
  origin: ${ALLOWED_ORIGIN}
  mail:
    from: ${MAIL_USERNAME}

spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          connectiontimeout: 5000
          timeout: 5000
          writetimeout: 5000
```

### Налаштування Gmail SMTP

Для використання Gmail як SMTP сервера необхідно:

1. Увімкнути двофакторну автентифікацію у вашому Google акаунті
2. Згенерувати App Password у налаштуваннях безпеки Google
3. Використати згенерований пароль як значення MAIL_PASSWORD

### Запуск застосунку

**За допомогою Maven:**
```bash
mvn clean install
mvn spring-boot:run
```

**За допомогою Gradle:**
```bash
gradle clean build
gradle bootRun
```

Застосунок буде доступний за адресою: `http://localhost:8080`

### Тестування API

**Пошук фільму:**
```bash
curl http://localhost:8080/api/v1/films/Inception
```

**Надсилання на email:**
```bash
curl -X POST http://localhost:8080/api/v1/films/send/recipient@example.com \
  -H "Content-Type: application/json" \
  -d '{
    "subject": "Film Information",
    "film": {
      "Title": "Inception",
      "Year": "2010",
      "Genre": "Action, Sci-Fi"
    }
  }'
```

## Обробка помилок

Застосунок повертає структуровані відповіді при виникненні помилок:

**404 Not Found** — коли фільм не знайдено:
```json
{
  "timestamp": "2024-12-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Film not found",
  "path": "/api/v1/films/NonExistentMovie"
}
```

**503 Service Unavailable** — при проблемах з доступом до OMDb API:
```json
{
  "timestamp": "2024-12-15T10:30:00",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Server error, try again",
  "path": "/api/v1/films/SomeMovie"
}
```

## CORS конфігурація

Застосунок налаштовано для підтримки Cross-Origin запитів від фронтенд застосунків. Конфігурація дозволяє:

- Запити з вказаного origin (налаштовується через ALLOWED_ORIGIN)
- Всі основні HTTP методи (GET, POST, PUT, DELETE)
- Передачу credentials для автентифікованих запитів
- Кастомні заголовки для розширеної функціональності

## Шаблон Email

HTML шаблон film-email-template розміщено у директорії resources/templates та містить професійне оформлення для відображення інформації про фільм. Шаблон включає секції для постера, основних відомостей, сюжету, рейтингів та додаткових деталей.

Thymeleaf вираз у шаблоні дозволяють динамічно підставляти дані про фільм, забезпечуючи персоналізований вміст для кожного листа.

## Розширення функціоналу

Застосунок можна розширити наступними можливостями:

- Пошук фільмів за різними критеріями (рік, жанр, актори)
- Кешування результатів для зменшення навантаження на OMDb API
- Збереження історії пошуків у базу даних
- Створення списків обраних фільмів для користувачів
- Підтримка пакетної відправки декількох фільмів
- Інтеграція з іншими кінематографічними базами даних
- Додавання рекомендаційної системи на основі уподобань

## Безпека

Застосунок використовує змінні оточення для зберігання чутливих даних включаючи API ключі та SMTP credentials. Ніколи не комітьте файли з реальними значеннями у публічні репозиторії.

Для production середовища рекомендується:

- Використання секретів менеджерів для зберігання credentials
- Впровадження rate limiting для захисту від зловживань
- Додавання автентифікації та авторизації користувачів
- Валідація та санітизація всіх вхідних даних
- Використання HTTPS для всіх комунікацій

## Ліцензія

Цей проєкт створено в освітніх цілях.

## Автори

Розроблено як практична робота з курсу веб-розробки.
