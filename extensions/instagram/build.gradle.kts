extension {
    name = "extensions/extension.rve"
}

android {
    namespace = "app.revanced.extension"
}

dependencies {
    compileOnly(project(":extensions:shared:library"))
}