plugins {
    alias(libs.plugins.android.application)
    id ("com.google.protobuf") version ("0.9.2")

}

android {
    namespace = "com.example.grpcclient"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.grpcclient"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }



}



dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("io.grpc:grpc-okhttp:1.49.0") // Para comunicação HTTP/2
    implementation ("io.grpc:grpc-protobuf:1.49.0") // Para suporte a Protobuf
    implementation ("io.grpc:grpc-stub:1.49.0") // Para criação de stubs
    implementation ("javax.annotation:javax.annotation-api:1.3.2")
    implementation("com.redmadrobot:input-mask-android:7.2.6")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.5"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.49.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            //task.dependsOn("downloadProto")
            task.builtins {
                create("java") {
                }
            }
            task.plugins {
                create("grpc")
            }
        }
    }
}

//tasks.register<Exec>("downloadProto") {
//    commandLine("curl", "-o", "$projectDir/src/main/proto/contract.proto", "http://localhost:8081/contract.proto")
//}
