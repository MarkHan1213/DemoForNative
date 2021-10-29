# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("VR_demo")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

# build native_app_glue as a static lib

set(jnilibs "${CMAKE_SOURCE_DIR}/../jniLib")

add_definitions(-DXR_USE_PLATFORM_ANDROID)
add_definitions(-DXR_USE_GRAPHICS_API_OPENGL_ES)

include_directories(
        util
        render
        sample
        glm
        inc
        inc/assimp
        inc/freetype_2_9_1
        inc/openxr_loader
        model
        opencv_3_0_0
        egl
        looper
        xr
)

file(GLOB src-files
        ${CMAKE_SOURCE_DIR}/*.cpp
        ${CMAKE_SOURCE_DIR}/render/*.cpp
        ${CMAKE_SOURCE_DIR}/util/*.cpp
        ${CMAKE_SOURCE_DIR}/sample/*.cpp
        ${CMAKE_SOURCE_DIR}/egl/*.cpp
        ${CMAKE_SOURCE_DIR}/xr/*.cpp
        ${CMAKE_SOURCE_DIR}/looper/*.cpp)

set(${CMAKE_C_FLAGS}, "${CMAKE_C_FLAGS}")
add_library(native_app_glue STATIC
        ${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c)

# now build app's shared lib
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11 -Wall -Werror -Wno-unused-variable -Wno-unneeded-internal-declaration -Wno-unused-function")

# Export ANativeActivity_onCreate(),
# Refer to: https://github.com/android-ndk/ndk/issues/381.
set(CMAKE_SHARED_LINKER_FLAGS
        "${CMAKE_SHARED_LINKER_FLAGS} -u ANativeActivity_onCreate")

add_library(VR_demo
        SHARED
        ${src-files}
        ${CMAKE_SOURCE_DIR}/inc/openxr_loader/common/gfxwrapper_opengl.c)

target_include_directories(VR_demo PRIVATE
        ${ANDROID_NDK}/sources/android/native_app_glue)

add_library(assimp SHARED IMPORTED )
set_target_properties(assimp PROPERTIES IMPORTED_LOCATION ${jnilibs}/${ANDROID_ABI}/libassimp.so)
#
add_library(opencv_java3 SHARED IMPORTED )
set_target_properties(opencv_java3 PROPERTIES IMPORTED_LOCATION ${jnilibs}/${ANDROID_ABI}/libopencv_java3.so)
#
add_library(freetype STATIC IMPORTED )
set_target_properties(freetype PROPERTIES IMPORTED_LOCATION ${jnilibs}/${ANDROID_ABI}/libfreetype.a)
#
add_library(openxr_loader SHARED IMPORTED )
set_target_properties(openxr_loader PROPERTIES IMPORTED_LOCATION ${jnilibs}/${ANDROID_ABI}/libopenxr_loader.so)


set(third-party-libs
        openxr_loader
        freetype
        assimp
        opencv_java3
        )

# add lib dependencies
target_link_libraries(VR_demo
        android
        native_app_glue
        ${log-lib}
        ${third-party-libs}
        EGL
        GLESv3
        log)
if(TARGET openxr-gfxwrapper)
    target_link_libraries(VR_demo openxr-gfxwrapper)
endif()
