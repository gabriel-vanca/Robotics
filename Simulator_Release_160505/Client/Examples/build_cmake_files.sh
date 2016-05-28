for i in `gfind . -name '*.c' -printf '%f '`;do echo DECLARE_APP\(`echo $i | sed 's/\.c//'` $i\) >> CMakeLists.txt;done
