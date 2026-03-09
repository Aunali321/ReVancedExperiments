# Patcher v21 -> v22 Migration Work Log

## Reference Repository
- **Location**: `/home/aun/Programming/Projects/ReVanced/revanced-patches`
- Official revanced-patches repo on patcher v22. USE IT AS REFERENCE for v22 API patterns.

## Status: COMPLETE ✓
Build passes successfully. All files migrated to v22 API.

---

## Session 1: Core Migration

### 1. Build System Files
All build files updated:
- `gradle/wrapper/gradle-wrapper.properties` - gradle 8.9 -> 9.3.1
- `gradle/libs.versions.toml` - patcher 21->22, smali 3.0.5->3.0.8
- `settings.gradle.kts` - plugin 1.0.0-dev.10, `mavenLocal()`, `PasswordCredentials`, `proguardFiles` absolute path, credential name `githubPackages` (lowercase)
- `build.gradle.kts` (root, NEW) - `alias(libs.plugins.android.library) apply false`
- `patches/build.gradle.kts` - `-Xcontext-parameters`, `-Xexplicit-backing-fields`, `freeCompilerArgs.addAll`
- `extensions/shared/library/build.gradle.kts` - `alias(libs.plugins.android.library)`
- `extensions/shared/build.gradle.kts` - added `android { defaultConfig { minSdk = 26 } }`
- `extensions/instagram/build.gradle.kts` - simplified + added `android { defaultConfig { minSdk = 26 } }`
- `extensions/instagram/stub/build.gradle.kts` - `alias(libs.plugins.android.library)`
- `gradle.properties` - added jvmargs, `android.uniquePackageNames = false`

### 2. All Fingerprints.kt Files (18 files)
Converted from v21 `fingerprint {}` to v22 delegated properties.

**Pattern mapping:**
| v21 | v22 |
|-----|-----|
| `fingerprint { ... }` | `BytecodePatchContext.xMethod by gettingFirstMethodDeclaratively(...)` |
| `returns("V")` | `returnType("V")` |
| `parameters("L")` | `parameterTypes("L")` |
| `strings("x", "y")` | First args: `gettingFirstMethodDeclaratively("x", "y")` |
| `custom { m, c -> ... }` | `name("foo"); definingClass("Bar;")` |
| Used as `.classDef` | `gettingFirstClassDefDeclaratively(...)` |
| Used as `.methodOrNull` | `gettingFirstMethodDeclarativelyOrNull(...)` |
| Opcodes needing match indices | `composingFirstMethod { ... }` |

### 3. All Patch Files (~24 files)
**Pattern mapping:**
| v21 | v22 |
|-----|-----|
| `execute {}` | `apply {}` |
| `finalize {}` | `afterDependents {}` |
| `someFingerprint.method` | Direct property access |
| `proxy(it).mutableClass` | `firstClassDef(it.type)` |
| `classes.forEach` | `classDefs.forEach` |
| `Patch<*>` | `Patch` |

### 4. BytecodeUtils.kt
Already migrated. Has extra `findFreeRegister` + `InstructionUtils` not in reference (custom utilities, not v21 remnants).

### 5. ResourceUtils.kt & Utils.kt
Matched to reference:
- Added `Node.removeFromParent()`
- Added directory creation + error handling in `copyResources`
- Added `findPlayStoreServicesVersion()`
- Added `Class<*>.allAssignableTypes()`

---

## Session 2: Build Fixes & Reference Alignment

### Build Compilation Fixes
1. **ResourceMappingPatch.kt** - Complete rewrite to match reference: added `ResourceType` enum with `IndexedMatcherPredicate`, `operator fun invoke()`, `operator fun get()`, `fromValue()`, simplified resource loading
2. **MaxMediaQualityPatch.kt** - Added `import app.revanced.patcher.*`
3. **DownloadBoostPatch.kt** - Added `import app.revanced.patcher.*`
4. **DeobfuscateSmobPatch.kt** - Added `import app.revanced.patcher.*`
5. **Instagram bypass integrity Fingerprints.kt** - `gettingFirstClassDefDeclaratively` with strings was wrong API. Changed to `gettingFirstMethodDeclaratively` for string matching + `firstClassDef(method.definingClass)` in patch. Added `context(_: BytecodePatchContext)` for `getIsValidSignatureMethod()`.
6. **Instagram bypass integrity BypassIntegrityPatch.kt** - Updated to use `firstClassDef(isValidSignatureClassMethod.definingClass)`
7. **settings.gradle.kts** - Fixed credential name `GitHubPackages` -> `githubPackages`
8. **extensions/shared/build.gradle.kts** - Added `android { defaultConfig { minSdk = 26 } }`
9. **extensions/instagram/build.gradle.kts** - Added `android { defaultConfig { minSdk = 26 } }`

### Code Style Alignment with Reference
1. **Import style**: All files now use `import app.revanced.patcher.*` wildcard (reference pattern) instead of specific imports like `import app.revanced.patcher.firstClassDef`. Fixed in: DeobfuscateSmobPatch, MaxMediaQualityPatch, DownloadBoostPatch, BypassIntegrityPatch, checks/Fingerprints.kt
2. **GmsCoreSupportPatch.kt** - **Complete rewrite** from reference:
   - Replaced old `PERMISSIONS`/`ACTIONS`/`AUTHORITIES` constants with `GMS_PERMISSIONS`, `GMS_AUTHORITIES`, `APP_PERMISSIONS` (mutable), `APP_AUTHORITIES` (mutable)
   - Added `EXTENSION_CLASS_DESCRIPTOR` const
   - Changed `mainActivityOnCreateMethod` param to `getMainActivityOnCreateMethodToGetInsertIndex: Pair<...>`
   - Resource patch now parses AndroidManifest.xml DOM at runtime instead of string replacement
   - Added `prefixOrReplace()` utility, `URI.create()` for content URL parsing
   - Added `originalPackageNameExtensionMethod.returnEarly()` and `getGmsCoreVendorGroupIdMethod.returnEarly()`
3. **GMS Fingerprints.kt** - Added two new fingerprints from reference: `getGmsCoreVendorGroupIdMethod`, `originalPackageNameExtensionMethod`
4. **SharedExtensionPatch.kt** - Updated `activityOnCreateExtensionHook` to include reference comment about `parameterTypes`
5. **AddResourcesPatch.kt** - Fixed `document(targetFile.path)` -> `document("res/$value/...")` to match reference

### Files NOT Updated (intentional)
- **ChangePackageNamePatch.kt** - Reference has new features (`updatePermissions`, `updateProviders`, incompatible apps list). These are feature additions our project doesn't need yet.
- **SettingsPatch.kt** - Reference has theme colors, brand license, icon preferences, search. Feature additions not needed.

---

## v22 API Quick Reference

### Import Pattern
```kotlin
import app.revanced.patcher.*                    // Always use wildcard for main patcher
import app.revanced.patcher.extensions.*         // Or specific: extensions.addInstruction
import app.revanced.patcher.patch.bytecodePatch  // Specific for patch types
```

### Fingerprint Patterns
```kotlin
// Method by strings
val BytecodePatchContext.fooMethod by gettingFirstMethodDeclaratively("string1", "string2") {
    returnType("V")
}

// ClassDef by type
val BytecodePatchContext.fooClassDef by gettingFirstClassDefDeclaratively("Lfull/class/Type;")

// Method with opcode matching (need indices)
val BytecodePatchContext.fooMatch by composingFirstMethod {
    opcodes(Opcode.X, Opcode.Y)
}
// Usage: fooMatch.method, fooMatch[-1]

// Nullable method
val BytecodePatchContext.fooMethod by gettingFirstMethodDeclarativelyOrNull(...)

// Method on a ClassDef (needs context)
context(_: BytecodePatchContext)
fun ClassDef.getFoo() = firstMethodDeclaratively { ... }
```

### Context Parameters
```kotlin
context(context: BytecodePatchContext)  // When you need to use context
context(_: BytecodePatchContext)        // When you just need the context in scope
```
