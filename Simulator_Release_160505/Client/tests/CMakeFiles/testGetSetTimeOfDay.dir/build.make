# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.2

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client"

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client"

# Include any dependencies generated for this target.
include tests/CMakeFiles/testGetSetTimeOfDay.dir/depend.make

# Include the progress variables for this target.
include tests/CMakeFiles/testGetSetTimeOfDay.dir/progress.make

# Include the compile flags for this target's objects.
include tests/CMakeFiles/testGetSetTimeOfDay.dir/flags.make

tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o: tests/CMakeFiles/testGetSetTimeOfDay.dir/flags.make
tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o: tests/testGetSetTimeOfDay.c
	$(CMAKE_COMMAND) -E cmake_progress_report "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/CMakeFiles" $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/tests" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o   -c "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/tests/testGetSetTimeOfDay.c"

tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.i"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/tests" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/tests/testGetSetTimeOfDay.c" > CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.i

tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.s"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/tests" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/tests/testGetSetTimeOfDay.c" -o CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.s

tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o.requires:
.PHONY : tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o.requires

tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o.provides: tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o.requires
	$(MAKE) -f tests/CMakeFiles/testGetSetTimeOfDay.dir/build.make tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o.provides.build
.PHONY : tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o.provides

tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o.provides.build: tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o

# Object files for target testGetSetTimeOfDay
testGetSetTimeOfDay_OBJECTS = \
"CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o"

# External object files for target testGetSetTimeOfDay
testGetSetTimeOfDay_EXTERNAL_OBJECTS =

tests/testGetSetTimeOfDay: tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o
tests/testGetSetTimeOfDay: tests/CMakeFiles/testGetSetTimeOfDay.dir/build.make
tests/testGetSetTimeOfDay: src/libclient.a
tests/testGetSetTimeOfDay: tests/CMakeFiles/testGetSetTimeOfDay.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX executable testGetSetTimeOfDay"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/tests" && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/testGetSetTimeOfDay.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
tests/CMakeFiles/testGetSetTimeOfDay.dir/build: tests/testGetSetTimeOfDay
.PHONY : tests/CMakeFiles/testGetSetTimeOfDay.dir/build

tests/CMakeFiles/testGetSetTimeOfDay.dir/requires: tests/CMakeFiles/testGetSetTimeOfDay.dir/testGetSetTimeOfDay.c.o.requires
.PHONY : tests/CMakeFiles/testGetSetTimeOfDay.dir/requires

tests/CMakeFiles/testGetSetTimeOfDay.dir/clean:
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/tests" && $(CMAKE_COMMAND) -P CMakeFiles/testGetSetTimeOfDay.dir/cmake_clean.cmake
.PHONY : tests/CMakeFiles/testGetSetTimeOfDay.dir/clean

tests/CMakeFiles/testGetSetTimeOfDay.dir/depend:
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/tests" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/tests" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/tests/CMakeFiles/testGetSetTimeOfDay.dir/DependInfo.cmake" --color=$(COLOR)
.PHONY : tests/CMakeFiles/testGetSetTimeOfDay.dir/depend

