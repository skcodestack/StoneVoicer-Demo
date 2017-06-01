

cmake_minimum_required(VERSION 3.4.1)

set(distribution_dir ../../../../libs )




find_library(   log-lib
                log )


add_library(    fmod
                SHARED
                 IMPORTED )


set_target_properties(  fmod
                        PROPERTIES IMPORTED_LOCATION
                        ${distribution_dir}/${ANDROID_ABI}/libfmod.so)

add_library(    fmodL
                SHARED
                IMPORTED )

set_target_properties(  fmodL
                        PROPERTIES IMPORTED_LOCATION
                        ${distribution_dir}/${ANDROID_ABI}/libfmodL.so)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")




add_library(    stonevoicer
                SHARED
                src/main/cpp/StoneVoicer.cpp )

include_directories(src/main/cpp/inc)

target_link_libraries(  stonevoicer
                        fmod
                        fmodL
                        ${log-lib} )
