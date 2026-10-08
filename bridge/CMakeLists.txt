cmake_minimum_required(VERSION 3.22.1)
project(fsh_bridge C CXX)

add_library(fsh_bridge SHARED
    cpp/src/placeholder.c
)

target_compile_options(fsh_bridge PRIVATE -Wall -Wextra)
