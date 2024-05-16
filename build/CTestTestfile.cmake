# CMake generated Testfile for 
# Source directory: /Users/ethan/Documents/GitHub/GameOfLife
# Build directory: /Users/ethan/Documents/GitHub/GameOfLife/build
# 
# This file includes the relevant testing commands required for 
# testing this directory and lists subdirectories to be tested as well.
add_test([=[TestGameOfLife]=] "/Users/ethan/Library/Java/JavaVirtualMachines/openjdk-21.0.2/Contents/Home/bin/java" "-cp" "/Users/ethan/Documents/GitHub/GameOfLife/build/GameOfLife.jar" "GameOfLife")
set_tests_properties([=[TestGameOfLife]=] PROPERTIES  _BACKTRACE_TRIPLES "/Users/ethan/Documents/GitHub/GameOfLife/CMakeLists.txt;20;add_test;/Users/ethan/Documents/GitHub/GameOfLife/CMakeLists.txt;0;")
