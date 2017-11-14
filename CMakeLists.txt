
cmake_minimum_required(VERSION 3.0 FATAL_ERROR)

project(TestIO)

# find ROOT
list(APPEND CMAKE_PREFIX_PATH $ENV{ROOTSYS})
find_package(ROOT)
include_directories(${ROOT_INCLUDE_DIRS})
add_definitions(${ROOT_CXX_FLAGS})

# test ROOT's vars
message ("ROOT_INCLUDE_DIRS = ${ROOT_INCLUDE_DIRS}")
message ("ROOT_LIBRARIES = ${ROOT_LIBRARIES}")

# c++11
set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

file (GLOB EXECUTABLES ${PROJECT_SOURCE_DIR}/src/*.cpp)
foreach (EXEC_FILE ${EXECUTABLES})
    string(REPLACE ".cpp" "" EXEC_NAME ${EXEC_FILE})
    string(REPLACE "${PROJECT_SOURCE_DIR}/src/" "" EXEC_NAME ${EXEC_NAME})
    message ("Adding Executable: ${EXEC_NAME}")
    add_executable(${EXEC_NAME} ${EXEC_FILE})
    target_link_libraries(${EXEC_NAME} ${ROOT_LIBRARIES})
endforeach(EXEC_FILE)
