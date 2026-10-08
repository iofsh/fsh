cmake_minimum_required(VERSION 3.22.1)
project(fsh_native C CXX)

add_library(fsh_native SHARED
    cpp/src/placeholder.c
)

target_compile_options(fsh_native PRIVATE -Wall -Wextra)
