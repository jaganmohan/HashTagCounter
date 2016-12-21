#
#Define java compiler and compiler flags
#

JCFLAGS = -g
JC = javac

#
#SRC - source directory for .java files
#DEST - destination directory for compiled .class filess
#
SRC = src/adsproject
DEST = bin

#
#All targets that need to be executed sequentially
#
all: clean .java.class .jar

#
#clearing any default targets for building .class files from .java files
#that make may have
#
.SUFFIXES: 
	.java .class

#
#Compiles .java files from SRC directory to .class files and places them in DEST directory
#
.java.class: clean
	$(JC) $(JCFLAGS) -d $(DEST)  $(SRC)/NodeFibHeap.java\
		$(SRC)/FibonacciHeap.java\
		$(SRC)/HashTagCounter.java

#
#creates an executable jar from the classes
#
.jar: .java.class
	jar cvfm hashtagcounter manifest -C bin .
	chmod a+x hashtagcounter

#
#outputs version of the java compiler this makefile is using
#
VERSION:
	$(JC) -version

#CLASSES = \
	$(SRC)/NodeFibHeap.java\
	$(SRC)/FibonacciHeap.java\
	$(SRC)/HashTagCounter.java

#default: classes

#classes: $(CLASSES:.java=.class)

clean: 
	$(RM) -r $(DEST)
	-mkdir $(DEST)

