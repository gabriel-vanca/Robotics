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
include Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/depend.make

# Include the progress variables for this target.
include Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/progress.make

# Include the compile flags for this target's objects.
include Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/flags.make

Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o: Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/flags.make
Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o: Examples/Devices/Memory/SD_Minimal.c
	$(CMAKE_COMMAND) -E cmake_progress_report "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/CMakeFiles" $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/Devices/Memory" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o   -c "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/Devices/Memory/SD_Minimal.c"

Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/SD_Minimal.dir/SD_Minimal.c.i"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/Devices/Memory" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/Devices/Memory/SD_Minimal.c" > CMakeFiles/SD_Minimal.dir/SD_Minimal.c.i

Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/SD_Minimal.dir/SD_Minimal.c.s"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/Devices/Memory" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/Devices/Memory/SD_Minimal.c" -o CMakeFiles/SD_Minimal.dir/SD_Minimal.c.s

Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o.requires:
.PHONY : Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o.requires

Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o.provides: Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o.requires
	$(MAKE) -f Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/build.make Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o.provides.build
.PHONY : Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o.provides

Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o.provides.build: Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o

# Object files for target SD_Minimal
SD_Minimal_OBJECTS = \
"CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o"

# External object files for target SD_Minimal
SD_Minimal_EXTERNAL_OBJECTS =

Examples/Devices/Memory/SD_Minimal: Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o
Examples/Devices/Memory/SD_Minimal: Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/build.make
Examples/Devices/Memory/SD_Minimal: src/libclient.a
Examples/Devices/Memory/SD_Minimal: Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX executable SD_Minimal"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/Devices/Memory" && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/SD_Minimal.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/build: Examples/Devices/Memory/SD_Minimal
.PHONY : Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/build

Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/requires: Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/SD_Minimal.c.o.requires
.PHONY : Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/requires

Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/clean:
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/Devices/Memory" && $(CMAKE_COMMAND) -P CMakeFiles/SD_Minimal.dir/cmake_clean.cmake
.PHONY : Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/clean

Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/depend:
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/Devices/Memory" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/Devices/Memory" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/DependInfo.cmake" --color=$(COLOR)
.PHONY : Examples/Devices/Memory/CMakeFiles/SD_Minimal.dir/depend
