cmake_minimum_required(VERSION 2.8)

project(face C CXX)

#set(CMAKE_BUILD_TYPE Debug)
#set(CMAKE_BUILD_TYPE Release)
#set(CMAKE_BUILD_TYPE RelWithDebInfo)


find_package(OpenCV)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -D_GLIBCXX_USE_CXX11_ABI=0")

set(LIB ${PROJECT_NAME})
set(APP main-${LIB})

set(SRC_FILES 
  bitmap_utils.cpp
  com_example_camera_FaceDetection.cpp   
)

add_library(${LIB} SHARED ${SRC_FILES})
#add_executable(${APP} ${SRC_FILES} Main.cpp)


include_directories(${OpenCV_INCLUDE_DIRS} ${PROJECT_SOURCE_DIR})

target_link_libraries(${LIB} ${OpenCV_LIBS} jnigraphics)
#target_link_libraries(${APP} ${OpenCV_LIBS})

set_target_properties(${LIB} PROPERTIES
  LINK_FLAGS "-Wl,-z,defs,--as-needed,--version-script=${PROJECT_SOURCE_DIR}/api.map"
)

set(CMAKE_INSTALL_PREFIX ${PROJECT_BINARY_DIR}/install)
install(TARGETS ${LIB} DESTINATION lib)

