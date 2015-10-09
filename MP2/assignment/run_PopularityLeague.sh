INPUT=PopularityLeague
OUTPUT=F-output
hadoop fs -rm -r -f /mp2/$OUTPUT
rm -rf ./build ./*.jar ./output;
mkdir build; mkdir output;
export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar;
hadoop com.sun.tools.javac.Main $INPUT.java -d build;
jar -cvf $INPUT.jar -C build/ ./;
hadoop jar $INPUT.jar $INPUT -D league=/mp2/misc/league.txt /mp2/links /mp2/$OUTPUT
hadoop fs -get /mp2/$OUTPUT/part* ./output;