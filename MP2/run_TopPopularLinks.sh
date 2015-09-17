INPUT=TopPopularLinks
OUTPUT=E-output
hadoop fs -rm -r -f /mp2/$OUTPUT
rm -rf ./build ./$INPUT.jar ./output;
mkdir build; mkdir output;
export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar;
hadoop com.sun.tools.javac.Main $INPUT.java -d build;
jar -cvf $INPUT.jar -C build/ ./;
hadoop jar $INPUT.jar $INPUT -D N=5 /mp2/links /mp2/$OUTPUT
hadoop fs -get /mp2/$OUTPUT/part* ./output;