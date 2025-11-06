# CLAUDE.md - GeoIP2 Java API

This document contains guidance for Claude (and other AI assistants) when working with the GeoIP2-java codebase. It captures architectural patterns, conventions, and lessons learned to help maintain consistency and quality.

## Project Overview

**GeoIP2-java** is MaxMind's official Java client library for:
- **GeoIP2/GeoLite2 Web Services**: Country, City, and Insights endpoints
- **GeoIP2/GeoLite2 Databases**: Local MMDB file reading for various database types (City, Country, ASN, Anonymous IP, ISP, etc.)

The library provides both web service clients and database readers that return strongly-typed model objects containing geographic, ISP, anonymizer, and other IP-related data.

**Key Technologies:**
- Java 17+ (using modern Java features like records)
- Jackson for JSON serialization/deserialization
- MaxMind DB reader for binary database files
- Maven for build management
- JUnit 5 for testing
- WireMock for web service testing

## Code Architecture

### Package Structure

```
com.maxmind.geoip2/
├── model/              # Response models (CityResponse, InsightsResponse, etc.)
├── record/             # Data records (City, Location, Traits, Anonymizer, etc.)
├── exception/          # Custom exceptions for error handling
├── DatabaseReader      # Local MMDB file reader
├── WebServiceClient    # HTTP client for MaxMind web services
└── DatabaseProvider/WebServiceProvider interfaces
```

### Key Design Patterns

#### 1. **Java Records for Immutable Data Models**
All model and record classes use Java records for immutability and conciseness:

```java
public record Anonymizer(
    @JsonProperty("confidence")
    Integer confidence,

    @JsonProperty("is_anonymous")
    boolean isAnonymous,

    // ... more fields
) implements JsonSerializable {
    // Compact canonical constructor for defaults
    public Anonymizer {
        // Set defaults for null values
    }
}
```

**Key Points:**
- Records provide automatic `equals()`, `hashCode()`, `toString()`, and accessor methods
- Use `@JsonProperty` for JSON field mapping
- Use `@MaxMindDbParameter` for database field mapping
- Implement compact canonical constructors to set defaults for null values

#### 2. **Alphabetical Parameter Ordering**
Record parameters are **always** ordered alphabetically by field name. This maintains consistency across the codebase:

```java
public record InsightsResponse(
    Anonymizer anonymizer,  // A comes first
    City city,              // C comes next
    Continent continent,    // C (alphabetically after "city")
    // ... etc.
)
```

#### 3. **Deprecation Strategy**

When deprecating fields:

**For record parameters** (preferred for new deprecations):
```java
public record Traits(
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("is_anonymous")
    boolean isAnonymous,
    // ...
)
```

This automatically marks the accessor method (`isAnonymous()`) as deprecated.

**For JavaBeans-style getters** (legacy code only):
```java
@Deprecated(since = "5.0.0", forRemoval = true)
public String getUserType() {
    return userType();
}
```

**Do NOT add deprecated getters for new fields** - they're only needed for backward compatibility with existing fields that had JavaBeans-style getters before the record migration.

#### 4. **Default Constructors for Record Classes**

All record classes in `src/main/java/com/maxmind/geoip2/record/` should provide a no-arg constructor that sets sensible defaults:

```java
public Anonymizer() {
    this(null, false, false, false, false, false, false, null, null);
}
```

- Nullable fields → `null`
- Boolean fields → `false`

**Note:** Model classes in `src/main/java/com/maxmind/geoip2/model/` do not require default constructors as they are typically constructed from API responses.

#### 5. **Web Service Only vs Database Records**

Some record classes are only used by web services and do **not** need MaxMind DB support:

**Web Service Only Records** (no `@MaxMindDbParameter` or `@MaxMindDbConstructor`):
- Records that are exclusive to web service responses (e.g., `Anonymizer` for Insights API)
- Only need `@JsonProperty` annotations for JSON deserialization
- Simpler implementation without database parsing logic

**Database-Supported Records** (need `@MaxMindDbParameter` and often `@MaxMindDbConstructor`):
- Records used by both web services and database files (e.g., `Traits`, `Location`, `City`)
- Need both `@JsonProperty` and `@MaxMindDbParameter` annotations
- May need `@MaxMindDbConstructor` for date parsing or other database-specific conversion

**How to Determine:**
- Check the JavaDoc - does it say "This is only available from the X web service"?
- Look at existing similar records in the `record/` package
- If in doubt, ask - adding unnecessary database support adds complexity

## Testing Conventions

### Test Structure

Tests are organized by model/class:
- `src/test/java/com/maxmind/geoip2/model/` - Response model tests
- `src/test/resources/test-data/` - JSON fixtures for tests

### JSON Test Fixtures

When adding new fields to responses:
1. Update the JSON fixture files in `src/test/resources/test-data/`
2. Update the corresponding test methods in `*Test.java` files
3. Update `JsonTest.java` to include the new fields in round-trip tests

Example: Adding `anonymizer` to `InsightsResponse`:
```json
{
  "anonymizer": {
    "confidence": 99,
    "is_anonymous": true,
    "network_last_seen": "2024-12-31",
    "provider_name": "NordVPN"
  },
  // ... other fields
}
```

### WireMock for Web Service Tests

Web service tests use WireMock to stub HTTP responses:
```java
wireMock.stubFor(get(urlEqualTo("/geoip/v2.1/insights/1.1.1.1"))
    .willReturn(aResponse()
        .withStatus(200)
        .withBody(readJsonFile("insights0"))));
```

## Working with This Codebase

### Adding New Fields to Existing Records

1. **Determine alphabetical position** for the new field
2. **Add the field** with proper annotations:
   ```java
   @JsonProperty("field_name")
   @MaxMindDbParameter(name = "field_name")
   Type fieldName,
   ```
3. **Update the default constructor** (if in `record/` package) to include the new parameter
4. **For minor version releases**: Add a deprecated constructor matching the old signature to avoid breaking changes (see "Avoiding Breaking Changes in Minor Versions" section)
5. **Add JavaDoc** describing the field
6. **Update test fixtures** with example data
7. **Add test assertions** to verify the field is properly deserialized

### Adding New Records

When creating a new record class in `src/main/java/com/maxmind/geoip2/record/`:

1. **Determine if web service only or database-supported** (see "Web Service Only vs Database Records" section)
2. **Follow the pattern** from existing similar records (e.g., `Location`, `Traits`, or `Anonymizer`)
3. **Alphabetize parameters** by field name
4. **Add appropriate annotations**:
   - All records: `@JsonProperty`
   - Database-supported only: `@MaxMindDbParameter` and possibly `@MaxMindDbConstructor`
5. **Implement `JsonSerializable`** interface
6. **Add a no-arg default constructor** (see section on Default Constructors)
7. **Don't add deprecated getters** - the record accessors are sufficient
8. **Provide comprehensive JavaDoc** for all parameters

### Deprecation Guidelines

When deprecating fields in favor of new structures:

1. **Use `@Deprecated` on record parameters** (not explicit methods)
2. **Include helpful deprecation messages** in JavaDoc pointing to alternatives
3. **Mark as `forRemoval = true`** with appropriate version
4. **Keep deprecated fields functional** - don't break existing code
5. **Update CHANGELOG.md** with deprecation notices

Example deprecation message:
```java
* @param isAnonymous This is true if the IP address belongs to any sort of anonymous network.
 *                    This field is deprecated. Please use the anonymizer object from the
 *                    Insights response.
```

### CHANGELOG.md Format

Always update `CHANGELOG.md` for user-facing changes:

```markdown
## 5.0.0 (unreleased)

* A new `Anonymizer` record has been added...
* A new `ipRiskSnapshot` field has been added...
* The anonymous IP flags have been deprecated...
* **BREAKING:** Description of breaking changes...
```

### Avoiding Breaking Changes in Minor Versions

When adding a new field to an existing record class during a **minor version release** (e.g., 4.x.0 → 4.y.0), you must maintain backward compatibility for users who may be programmatically constructing these records.

**The Problem:** Adding a field to a record changes the signature of the canonical constructor, which is a breaking change for existing code that constructs the record directly.

**The Solution:** Add a deprecated constructor that matches the old signature:

```java
public record Traits(
    // ... existing fields ...
    String domain,

    // NEW FIELD added in minor version (inserted in alphabetical position)
    Double ipRiskSnapshot,

    String organization
) {
    // Updated default constructor with new field
    public Traits() {
        this(null, null, null);
    }

    // Deprecated constructor maintaining old signature for backward compatibility
    @Deprecated(since = "4.5.0", forRemoval = true)
    public Traits(
        String domain,
        String organization
        // Note: ipRiskSnapshot is NOT in this constructor
    ) {
        this(domain, null, organization);  // New field defaults to null (in alphabetical position)
    }
}
```

**Key Points:**
- The deprecated constructor **matches the signature before the new field was added**
- It calls the new canonical constructor with `null` (or appropriate default) for the new field
- Mark it `@Deprecated` with `forRemoval = true` for the next major version
- Document this in CHANGELOG.md as a new feature, not a breaking change

**For Major Versions:** You do NOT need to add the deprecated constructor - breaking changes are expected in major version bumps (e.g., 4.x.0 → 5.0.0).

### Multi-threaded Safety

Both `DatabaseReader` and `WebServiceClient` are **thread-safe** and should be reused across requests:
- Create once, share across threads
- Reusing clients enables connection pooling and improves performance
- Document thread-safety in JavaDoc for all client classes

## Common Pitfalls and Solutions

### Problem: Breaking Changes in Minor Versions
Adding a new field to a record changes the canonical constructor signature, breaking existing code.

**Solution**: For minor version releases, add a deprecated constructor that maintains the old signature. See "Avoiding Breaking Changes in Minor Versions" section for details.

### Problem: Record Constructor Ambiguity
When you have two constructors with similar signatures (e.g., both ending with `String`), you may get "ambiguous constructor" errors.

**Solution**: Cast `null` parameters to their specific type:
```java
this(null, false, null);  // Cast if needed: (TypeName) null
```

### Problem: Test Failures After Adding New Fields
After adding new fields to a response model, tests fail with deserialization errors.

**Solution**: Update **all** related test fixtures:
1. Test JSON files (e.g., `insights0.json`, `insights1.json`)
2. In-line JSON in `JsonTest.java`
3. Test assertions in `*ResponseTest.java` files

## Development Workflow

### Running Tests
```bash
mvn clean test                    # Run all tests
mvn test -Dtest=JsonTest          # Run specific test class
mvn test -Dtest=InsightsResponseTest,JsonTest  # Multiple tests
```

### Code Style
- **Checkstyle** enforces code style (see `checkstyle.xml`)
- Run `mvn checkstyle:check` to verify compliance
- Tests must pass checkstyle to merge

### Version Requirements
- **Java 17+** required
- Uses modern Java features (records, sealed classes potential)
- Target compatibility should match current LTS Java versions

## Useful Patterns

### Pattern: Compact Canonical Constructor
Use compact canonical constructors to set defaults and validate:

```java
public record InsightsResponse(
    Anonymizer anonymizer,
    City city,
    // ...
) {
    public InsightsResponse {
        anonymizer = anonymizer != null ? anonymizer : new Anonymizer();
        city = city != null ? city : new City();
        // ...
    }
}
```

### Pattern: Empty Object Defaults
Return empty objects instead of null for better API ergonomics:

```java
public City city() {
    return city;  // Never null due to compact constructor
}
```

Users can safely call `response.city().name()` even if city data is absent.

### Pattern: JsonSerializable Interface
All models implement `JsonSerializable` for consistent JSON output:

```java
public interface JsonSerializable {
    default String toJson() throws IOException {
        JsonMapper mapper = JsonMapper.builder()
            .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .addModule(new JavaTimeModule())
            .addModule(new InetAddressModule())
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();
        return mapper.writeValueAsString(this);
    }
}
```

## Database vs Web Service Architecture

### Database Reader
- Reads binary MMDB files using `maxmind-db` library
- Methods return `Optional<T>` or throw `AddressNotFoundException`
- Support for multiple database types: City, Country, ASN, Anonymous IP, etc.
- Thread-safe, should be reused

### Web Service Client
- Uses Java 11+ `HttpClient` for HTTP requests
- Methods throw `GeoIp2Exception` or subclasses on errors
- Supports custom timeouts, locales, and proxy configuration
- Thread-safe, connection pooling via reuse

## Key Dependencies

- **maxmind-db**: Binary MMDB database reader
- **jackson-databind**: JSON serialization/deserialization
- **jackson-datatype-jsr310**: Java 8+ date/time support
- **wiremock**: HTTP mocking for tests
- **junit-jupiter**: JUnit 5 testing framework

## Additional Resources

- [API Documentation](https://maxmind.github.io/GeoIP2-java/)
- [GeoIP2 Web Services Docs](https://dev.maxmind.com/geoip/docs/web-services)
- [MaxMind DB Format](https://maxmind.github.io/MaxMind-DB/)
- GitHub Issues: https://github.com/maxmind/GeoIP2-java/issues

---

*Last Updated: 2024-11-06*
