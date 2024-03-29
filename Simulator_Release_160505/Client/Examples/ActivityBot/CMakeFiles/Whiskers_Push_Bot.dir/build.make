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
include Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/depend.make

# Include the progress variables for this target.
include Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/progress.make

# Include the compile flags for this target's objects.
include Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/flags.make

Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o: Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/flags.make
Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o: Examples/ActivityBot/Whiskers_Push_Bot.c
	$(CMAKE_COMMAND) -E cmake_progress_report "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/CMakeFiles" $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o   -c "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot/Whiskers_Push_Bot.c"

Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.i"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot/Whiskers_Push_Bot.c" > CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.i

Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.s"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot/Whiskers_Push_Bot.c" -o CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.s

Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o.requires:
.PHONY : Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o.requires

Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o.provides: Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o.requires
	$(MAKE) -f Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/build.make Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o.provides.build
.PHONY : Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o.provides

Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o.provides.build: Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o

# Object files for target Whiskers_Push_Bot
Whiskers_Push_Bot_OBJECTS = \
"CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o"

# External object files for target Whiskers_Push_Bot
Whiskers_Push_Bot_EXTERNAL_OBJECTS =

Examples/ActivityBot/Whiskers_Push_Bot: Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o
Examples/ActivityBot/Whiskers_Push_Bot: Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/build.make
Examples/ActivityBot/Whiskers_Push_Bot: src/libclient.a
Examples/ActivityBot/Whiskers_Push_Bot: Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX executable Whiskers_Push_Bot"
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Whiskers_Push_Bot.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/build: Examples/ActivityBot/Whiskers_Push_Bot
.PHONY : Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/build

Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/requires: Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/Whiskers_Push_Bot.c.o.requires
.PHONY : Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/requires

Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/clean:
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" && $(CMAKE_COMMAND) -P CMakeFiles/Whiskers_Push_Bot.dir/cmake_clean.cmake
.PHONY : Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/clean

Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/depend:
	cd "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot" "/media/gabriel/SSD Storage/OneDrive/University/Year One/COMP105P - Robotics Programming/Robotics/Simulator_Release_160505/Client/Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/DependInfo.cmake" --color=$(COLOR)
.PHONY : Examples/ActivityBot/CMakeFiles/Whiskers_Push_Bot.dir/depend

