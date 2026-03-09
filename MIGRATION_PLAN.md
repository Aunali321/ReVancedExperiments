# Patcher v21 -> v22 Migration Plan for ReVancedExperiments

## Build System Changes

### 1. Gradle Wrapper (gradle/wrapper/gradle-wrapper.properties)
- `gradle-8.9-bin.zip` -> `gradle-9.3.1-bin.zip`
- Update SHA256 checksum

### 2. gradle/libs.versions.toml
- `revanced-patcher = "21.0.0"` -> `"22.0.0"`
- `smali = "3.0.5"` -> `"3.0.8"` 
- Remove `smali` version (if unused directly)
- `android-library` plugin: remove `version.ref = "agp"` (managed by settings plugin now)
- Add new library entries if needed: `okhttp`, `appcompat`, `retrofit`, `guava`, `apksig`

### 3. settings.gradle.kts
- Plugin version: `1.0.0-dev.7` -> `1.0.0-dev.10`
- Add `mavenLocal()` to pluginManagement repositories
- Change credentials to `credentials(PasswordCredentials::class)`
- Change maven URL to match new registry
- Add `dependencyResolutionManagement { repositories { mavenLocal() } }`
- Fix proguardFiles path: use `rootProject.projectDir.resolve("extensions/proguard-rules.pro").toString()`

### 4. Root build.gradle.kts (NEW FILE)
```kotlin
plugins {
    alias(libs.plugins.android.library) apply false
}
```

### 5. patches/build.gradle.kts
- Kotlin compiler flags: `-Xcontext-receivers` -> `-Xcontext-parameters` + `-Xexplicit-backing-fields`
- Change `freeCompilerArgs = listOf(...)` to `freeCompilerArgs.addAll(...)`

### 6. extensions/shared/library/build.gradle.kts
- `id("com.android.library")` -> `alias(libs.plugins.android.library)`

### 7. extensions/instagram/build.gradle.kts
- Remove `extension { name = ... }` block
- Remove `android { namespace = ... }` block
- Simplify to just dependencies

### 8. extensions/instagram/stub/build.gradle.kts
- `id(libs.plugins.android.library.get().pluginId)` -> `alias(libs.plugins.android.library)`

### 9. gradle.properties
- Add `org.gradle.jvmargs = -Xms512M -Xmx2048M`
- Add `android.uniquePackageNames = false`

---

## Code Migration Changes

### Import Changes (apply globally)

| Old (v21) | New (v22) |
|-----------|-----------|
| `app.revanced.patcher.extensions.InstructionExtensions.addInstruction` | `app.revanced.patcher.extensions.addInstruction` |
| `app.revanced.patcher.extensions.InstructionExtensions.addInstructions` | `app.revanced.patcher.extensions.addInstructions` |
| `app.revanced.patcher.extensions.InstructionExtensions.addInstructionsWithLabels` | `app.revanced.patcher.extensions.addInstructionsWithLabels` |
| `app.revanced.patcher.extensions.InstructionExtensions.getInstruction` | `app.revanced.patcher.extensions.getInstruction` |
| `app.revanced.patcher.extensions.InstructionExtensions.instructions` | `app.revanced.patcher.extensions.instructions` |
| `app.revanced.patcher.extensions.InstructionExtensions.removeInstruction` | `app.revanced.patcher.extensions.removeInstruction` |
| `app.revanced.patcher.util.proxy.mutableTypes.MutableClass` | `app.revanced.com.android.tools.smali.dexlib2.mutable.MutableClassDef` |
| `app.revanced.patcher.util.proxy.mutableTypes.MutableField` | `app.revanced.com.android.tools.smali.dexlib2.mutable.MutableField` |
| `app.revanced.patcher.util.proxy.mutableTypes.MutableField.Companion.toMutable` | `app.revanced.com.android.tools.smali.dexlib2.mutable.MutableField.Companion.toMutable` |
| `app.revanced.patcher.util.proxy.mutableTypes.MutableMethod` | `app.revanced.com.android.tools.smali.dexlib2.mutable.MutableMethod` |
| `app.revanced.patcher.util.smali.ExternalLabel` | `app.revanced.patcher.ExternalLabel` |
| `app.revanced.patcher.FingerprintBuilder` | (removed, fingerprints work differently now) |
| `app.revanced.patcher.fingerprint` | `app.revanced.patcher.gettingFirstMethodDeclaratively` / `app.revanced.patcher.firstMethodDeclaratively` |

### Type Renames
- `MutableClass` -> `MutableClassDef` everywhere

### Context Parameter Syntax
- `context(BytecodePatchContext)` -> `context(context: BytecodePatchContext)` (or `context(_: BytecodePatchContext)`)

### Fingerprint Migration
Old pattern (v21):
```kotlin
internal val someFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC)
    returns("Z")
    parameters("L")
    strings("someString")
    custom { method, _ -> ... }
}
// Usage: someFingerprint.method
```

New pattern (v22):
```kotlin
internal val BytecodePatchContext.someMethod by gettingFirstMethodDeclaratively("someString") {
    accessFlags(AccessFlags.PUBLIC)
    returnType("Z")
    parameterTypes("L")
}
// Usage: someMethod (already a MutableMethod)
```

Key differences:
- `returns(...)` -> `returnType(...)`
- `parameters(...)` -> `parameterTypes(...)`
- `strings(...)` -> moved to constructor argument or `instructions(...)` block
- `custom { method, _ -> }` -> different approach, use `instructions()` matchers
- Result is a delegated property on `BytecodePatchContext`
- No longer need `.method` / `.originalMethod` / `.classDef` - the delegate IS the method

### BytecodePatchContext API Changes
- `proxy(classDef).mutableClass` -> use `firstClassDef()` or the method directly
- `classBy { predicate }` -> `firstClassDefOrNull(predicate)`
- `classes` -> `classDefs`
- `navigate(this).to(index).stop()` -> `context.navigate(this).to(index).stop()` (explicit context)

### BytecodeUtils.kt Overhaul
The `returnEarly`/`returnLate` methods are completely rewritten to use `EncodedValue` types from smali instead of manual string-based smali construction. The new version also supports boxed return types.

### forEachLiteralValueInstruction -> forEachInstructionAsSequence
Old v21 workaround code with `proxy(classDef).mutableClass.findMutableMethodOf(method)` is replaced with a cleaner `forEachInstructionAsSequence` that uses `firstMethod()`.

---

## Files to Modify

### Build files (8 files):
1. `gradle/wrapper/gradle-wrapper.properties`
2. `gradle/libs.versions.toml`
3. `settings.gradle.kts`
4. `build.gradle.kts` (NEW)
5. `patches/build.gradle.kts`
6. `extensions/shared/library/build.gradle.kts`
7. `extensions/instagram/build.gradle.kts`
8. `extensions/instagram/stub/build.gradle.kts`
9. `gradle.properties`

### Source files (~64 .kt files):
All files under `patches/src/main/kotlin/` need import updates and API migration.
Key files requiring significant changes:
- `li/auna/util/BytecodeUtils.kt` (1237 lines - major rewrite)
- `li/auna/util/ResourceUtils.kt`
- All `Fingerprints.kt` files (complete rewrite of fingerprint definitions)
- All patch files (import updates, fingerprint usage updates)
- Shared infrastructure patches (GmsCoreSupportPatch, AddResourcesPatch, ResourceMappingPatch, etc.)
