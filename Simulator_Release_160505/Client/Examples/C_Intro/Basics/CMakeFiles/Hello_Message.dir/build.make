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
include Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/depend.make

# Include the progress variables for this target.
include Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/progress.make

# Include the compile flags for this target's objects.
include Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/flags.make

Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o: Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/flags.make
Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o: Examples/C_Intro/Basics/Hello_Message.c
	$(CMAKE_COMMAND) -E cmake_progress_report "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/CMakeFiles" $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/Hello_Message.dir/Hello_Message.c.o   -c "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics/Hello_Message.c"

Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/Hello_Message.dir/Hello_Message.c.i"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics/Hello_Message.c" > CMakeFiles/Hello_Message.dir/Hello_Message.c.i

Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/Hello_Message.dir/Hello_Message.c.s"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics/Hello_Message.c" -o CMakeFiles/Hello_Message.dir/Hello_Message.c.s

Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o.requires:
.PHONY : Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o.requires

Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o.provides: Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o.requires
	$(MAKE) -f Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/build.make Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o.provides.build
.PHONY : Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o.provides

Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o.provides.build: Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o

# Object files for target Hello_Message
Hello_Message_OBJECTS = \
"CMakeFiles/Hello_Message.dir/Hello_Message.c.o"

# External object files for target Hello_Message
Hello_Message_EXTERNAL_OBJECTS =

Examples/C_Intro/Basics/Hello_Message: Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o
Examples/C_Intro/Basics/Hello_Message: Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/build.make
Examples/C_Intro/Basics/Hello_Message: src/libclient.a
Examples/C_Intro/Basics/Hello_Message: Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX executable Hello_Message"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Hello_Message.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/build: Examples/C_Intro/Basics/Hello_Message
.PHONY : Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/build

Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/requires: Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/Hello_Message.c.o.requires
.PHONY : Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/requires

Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/clean:
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" && $(CMAKE_COMMAND) -P CMakeFiles/Hello_Message.dir/cmake_clean.cmake
.PHONY : Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/clean

Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/depend:
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/DependInfo.cmake" --color=$(COLOR)
.PHONY : Examples/C_Intro/Basics/CMakeFiles/Hello_Message.dir/depend

