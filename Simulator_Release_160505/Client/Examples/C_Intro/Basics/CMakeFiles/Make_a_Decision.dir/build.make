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
include Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/depend.make

# Include the progress variables for this target.
include Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/progress.make

# Include the compile flags for this target's objects.
include Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/flags.make

Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o: Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/flags.make
Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o: Examples/C_Intro/Basics/Make_a_Decision.c
	$(CMAKE_COMMAND) -E cmake_progress_report "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/CMakeFiles" $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o   -c "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics/Make_a_Decision.c"

Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.i"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics/Make_a_Decision.c" > CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.i

Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.s"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics/Make_a_Decision.c" -o CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.s

Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o.requires:
.PHONY : Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o.requires

Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o.provides: Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o.requires
	$(MAKE) -f Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/build.make Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o.provides.build
.PHONY : Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o.provides

Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o.provides.build: Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o

# Object files for target Make_a_Decision
Make_a_Decision_OBJECTS = \
"CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o"

# External object files for target Make_a_Decision
Make_a_Decision_EXTERNAL_OBJECTS =

Examples/C_Intro/Basics/Make_a_Decision: Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o
Examples/C_Intro/Basics/Make_a_Decision: Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/build.make
Examples/C_Intro/Basics/Make_a_Decision: src/libclient.a
Examples/C_Intro/Basics/Make_a_Decision: Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX executable Make_a_Decision"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Make_a_Decision.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/build: Examples/C_Intro/Basics/Make_a_Decision
.PHONY : Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/build

Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/requires: Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/Make_a_Decision.c.o.requires
.PHONY : Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/requires

Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/clean:
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" && $(CMAKE_COMMAND) -P CMakeFiles/Make_a_Decision.dir/cmake_clean.cmake
.PHONY : Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/clean

Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/depend:
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/DependInfo.cmake" --color=$(COLOR)
.PHONY : Examples/C_Intro/Basics/CMakeFiles/Make_a_Decision.dir/depend

