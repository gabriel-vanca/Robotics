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
include Examples/ActivityBot/CMakeFiles/Test_Ping.dir/depend.make

# Include the progress variables for this target.
include Examples/ActivityBot/CMakeFiles/Test_Ping.dir/progress.make

# Include the compile flags for this target's objects.
include Examples/ActivityBot/CMakeFiles/Test_Ping.dir/flags.make

Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o: Examples/ActivityBot/CMakeFiles/Test_Ping.dir/flags.make
Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o: Examples/ActivityBot/Test_Ping.c
	$(CMAKE_COMMAND) -E cmake_progress_report "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/CMakeFiles" $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/Test_Ping.dir/Test_Ping.c.o   -c "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot/Test_Ping.c"

Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/Test_Ping.dir/Test_Ping.c.i"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot/Test_Ping.c" > CMakeFiles/Test_Ping.dir/Test_Ping.c.i

Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/Test_Ping.dir/Test_Ping.c.s"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot/Test_Ping.c" -o CMakeFiles/Test_Ping.dir/Test_Ping.c.s

Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o.requires:
.PHONY : Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o.requires

Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o.provides: Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o.requires
	$(MAKE) -f Examples/ActivityBot/CMakeFiles/Test_Ping.dir/build.make Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o.provides.build
.PHONY : Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o.provides

Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o.provides.build: Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o

# Object files for target Test_Ping
Test_Ping_OBJECTS = \
"CMakeFiles/Test_Ping.dir/Test_Ping.c.o"

# External object files for target Test_Ping
Test_Ping_EXTERNAL_OBJECTS =

Examples/ActivityBot/Test_Ping: Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o
Examples/ActivityBot/Test_Ping: Examples/ActivityBot/CMakeFiles/Test_Ping.dir/build.make
Examples/ActivityBot/Test_Ping: src/libclient.a
Examples/ActivityBot/Test_Ping: Examples/ActivityBot/CMakeFiles/Test_Ping.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX executable Test_Ping"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Test_Ping.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
Examples/ActivityBot/CMakeFiles/Test_Ping.dir/build: Examples/ActivityBot/Test_Ping
.PHONY : Examples/ActivityBot/CMakeFiles/Test_Ping.dir/build

Examples/ActivityBot/CMakeFiles/Test_Ping.dir/requires: Examples/ActivityBot/CMakeFiles/Test_Ping.dir/Test_Ping.c.o.requires
.PHONY : Examples/ActivityBot/CMakeFiles/Test_Ping.dir/requires

Examples/ActivityBot/CMakeFiles/Test_Ping.dir/clean:
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" && $(CMAKE_COMMAND) -P CMakeFiles/Test_Ping.dir/cmake_clean.cmake
.PHONY : Examples/ActivityBot/CMakeFiles/Test_Ping.dir/clean

Examples/ActivityBot/CMakeFiles/Test_Ping.dir/depend:
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot/CMakeFiles/Test_Ping.dir/DependInfo.cmake" --color=$(COLOR)
.PHONY : Examples/ActivityBot/CMakeFiles/Test_Ping.dir/depend
